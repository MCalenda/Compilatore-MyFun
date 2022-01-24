package visitor;

import support.leafs.*;
import support.nodes.*;
import utils.SymbolTable;
import utils.SymbolTableEntry;
import utils.Type;
import utils.ValueType;

import java.util.ArrayList;
import java.util.Stack;

public class SemanticVisitor implements ISemanticVisitor {

    public Stack<SymbolTable> stack = new Stack<>();

    @Override
    public void visit(ProgramNode programNode) {
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.symbolTableName = "Global Scope";

        stack.push(symbolTable);

        // ArrayList<VarDeclNode>
        if (programNode.nodeArrayList != null) {
            for (VarDeclNode varDeclNode : programNode.nodeArrayList) {
                varDeclNode.accept(this);
            }
        }

        // ArrayList<ProcNode>
        for (ProcNode procNodo : programNode.procNodeArrayList) {
            procNodo.accept(this);
        }
    }

    @Override
    public void visit(VarDeclNode varDeclNode) {
        // TypeNode
        varDeclNode.type.accept(this);

        // ArrayList<IdInitNode>
        for (IdInitNode idInitNode : varDeclNode.identifiers) {
            try {
                SymbolTable picked = stack.peek();
                if (picked.getFatherSymTab() != null) {
                    SymbolTableEntry symbolTableEntry = picked.getFatherSymTab().get(idInitNode.varName.value);
                    if (symbolTableEntry != null && symbolTableEntry.type == Type.Function) {
                        System.err.println("Semantic error: Cannot declare a variable with ID: " + idInitNode.varName.value + ". There is a function with same ID.");
                        System.exit(0);
                    } else {
                        picked.createEntry_variable(idInitNode.varName.value, varDeclNode.type.type);
                    }
                } else {
                    picked.createEntry_variable(idInitNode.varName.value, varDeclNode.type.type);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }

            idInitNode.accept(this);
            idInitNode.setType(varDeclNode.type.type);
        }
    }

    @Override
    public void visit(TypeNode node) {
    }

    @Override
    public void visit(IdInitNode idInitNode) {
        // LeafID
        idInitNode.varName.accept(this);

        // ExprNode
        if (idInitNode.initValue != null) {
            idInitNode.initValue.accept(this);
            if (idInitNode.initValue.types.size() > 1) {
                System.err.println("Semantic error: multiple value to initialize a variable");
                System.exit(0);
            }

            if (!checkAssignmentType(idInitNode.varName.type, idInitNode.initValue.types.get(0))) {
                System.err.println("Semantic error: wrong initialization for variable " + idInitNode.varName.value);
                System.exit(0);
            }
        }
    }

    @Override
    public void visit(ProcNode procNode) {
        // Controllo se la funzione dichiarata esiste già nel type environment
        try {
            ArrayList<ValueType> in, out;
            in = new ArrayList<>();
            out = new ArrayList<>();

            // ArrayList<ParDeclNode>
            if (procNode.paramDeclList != null) {
                for (ParDeclNode parDeclNode : procNode.paramDeclList) {
                    ValueType valueType = SymbolTable.StringToType(parDeclNode.typeNode.type);
                    for (int i = 0; i < parDeclNode.identifiers.size(); i++) in.add(valueType);
                }
            }

            // ArrayList<ResultTypeNode>
            for (ResultTypeNode resultTypeNode : procNode.resultTypeList) {
                ValueType vtype;
                if (!resultTypeNode.isVoid) {
                    vtype = SymbolTable.StringToType(resultTypeNode.typeNode.type);
                    out.add(vtype);
                }
                resultTypeNode.accept(this);
            }

            stack.firstElement().createEntry_function(procNode.id.value, in, out);
            if (procNode.id.value.equals("main")) {
                if (out.size() > 1 || out.size() < 1) {
                    throw new Exception("main function must have only one integer as return type");
                } else if (out.size() == 1) {
                    if (out.get(0) != ValueType.Integer) {
                        throw new Exception("main function must have one integer as return type");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

        // Se non c'è posso continuare
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.symbolTableName = "Function " + procNode.id.value + " scope";
        symbolTable.setFatherSymTab(stack.firstElement());
        stack.push(symbolTable);

        // ArrayList<ParDeclNode>
        if (procNode.paramDeclList != null) {
            for (ParDeclNode pdn : procNode.paramDeclList)
                pdn.accept(this);
        }

        // ProcBody
        procNode.procBody.accept(this);

        // Semantic check
        // Controllo se il numero di tipi di ritorno della proc è minore di 1 (Inutile? Lo fa già l'analisi sintattica)
        if (procNode.resultTypeList.size() < 1) {
            System.err.println("Semantic error: wrong declaration of result types");
            System.exit(0);
        } else {
            for (int i = 0; i < procNode.resultTypeList.size(); i++) {
                ResultTypeNode resultTypeNode = procNode.resultTypeList.get(i);

                // Check se un tipo di ritorno è void
                if (resultTypeNode.isVoid) {
                    if (procNode.procBody.returnExprs.size() != 0) {
                        System.err.println("Semantic error: function " + procNode.id.value + " must be void only or some other type.");
                        System.exit(0);
                    }
                }
                // : if (resultTypeNode.isVoid)
                else {
                    if (procNode.procBody.returnExprs.size() != procNode.resultTypeList.size()) {
                        System.err.println("Semantic error: the number of returned values in proc " + procNode.id.value + " is different from the one defined. Required: " + procNode.resultTypeList.size() + ", provided: " + procNode.procBody.returnExprs.size());
                        System.exit(0);
                    } else {
                        ExprNode exprNode = procNode.procBody.returnExprs.get(i);
                        try {
                            if (!checkAssignmentType(SymbolTable.StringToType(resultTypeNode.typeNode.type), exprNode.types.get(0))) {
                                System.err.println("Semantic error: the return type of " + exprNode.value1 + " in proc " + procNode.id.value + " is different from the one requested. Required: " + resultTypeNode.typeNode.type + ", provided: " + exprNode.types.get(0));
                                System.exit(0);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.exit(0);
                        }
                    }
                }
                // :else
            }
            // :for
        }

        stack.pop();
    }

    @Override
    public void visit(ProcBodyNode node) {
        // ArrayList<VarDecl>
        for (VarDeclNode varDeclNode : node.varDeclList) {
            varDeclNode.accept(this);
        }

        // StatListNode
        if (node.statListNode != null) {
            node.statListNode.accept(this);
        }

        // ArrayList<ExprNode>
        for (ExprNode exprNode : node.returnExprs) {
            exprNode.accept(this);
        }
    }

    @Override
    public void visit(ParDeclNode node) {
        node.typeNode.accept(this);

        for (LeafID nodeID : node.identifiers) {
            try {
                SymbolTable picked = stack.peek();
                SymbolTableEntry symbolTableEntry = picked.getFatherSymTab().get(nodeID.value);
                if (symbolTableEntry != null && symbolTableEntry.type == Type.Function) {
                    System.err.println("Semantic error: Cannot declare a variable with ID: " + nodeID.value + ". There is a function with same ID.");
                    System.exit(0);
                } else {
                    picked.createEntry_variable(nodeID.value, node.typeNode.type);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }
            nodeID.accept(this);
        }
    }

    @Override
    public void visit(ResultTypeNode node) {
        if (!node.isVoid) {
            node.typeNode.accept(this);
        }
    }

    @Override
    public void visit(IfStatNode node) {
        node.condition.accept(this);
        node.ifBody.accept(this);

        for (ElifNode elifNode : node.elifList) {
            elifNode.accept(this);
        }

        if (node.elseBody != null) {
            node.elseBody.accept(this);
        }
    }

    @Override
    public void visit(StatListNode nodeList) {
        for (StatNode statNode : nodeList.statList) {
            switch (statNode.getClass().getSimpleName()) {
                case "IfStatNode":
                    IfStatNode ifStat = (IfStatNode) statNode;
                    ifStat.accept(this);
                    break;
                case "WhileStatNode":
                    WhileStatNode whileStatNode = (WhileStatNode) statNode;
                    whileStatNode.accept(this);
                    break;
                case "ReadLnStatNode":
                    ReadLnStatNode readStatNode = (ReadLnStatNode) statNode;
                    readStatNode.accept(this);
                    break;
                case "WriteStatNode":
                    WriteStatNode writeStatNode = (WriteStatNode) statNode;
                    writeStatNode.accept(this);
                    break;
                case "AssignStatNode":
                    AssignStatNode assignStatNode = (AssignStatNode) statNode;
                    assignStatNode.accept(this);
                    break;
                case "CallProcNode":
                    CallProcNode callProcNode = (CallProcNode) statNode;
                    callProcNode.accept(this);
                    break;
            }
        }
    }

    @Override
    public void visit(WhileStatNode node) {
        // StatListNode (precondizione)
        if (node.preCondList != null) {
            node.preCondList.accept(this);
        }

        // StatListNode (afterCondizione)
        node.afterCondList.accept(this);

        // ExprNode
        node.condition.accept(this);
        // Controllo i tipi delle condizioni
        if (node.condition.types.size() > 1) {
            System.err.println("Semantic error: condition type not allowed in while");
            System.exit(0);
        } else if (node.condition.types.get(0) != ValueType.Boolean) {
            System.err.println("Semantic error: condition type not allowed in while");
            System.exit(0);
        }
    }

    @Override
    public void visit(ReadLnStatNode node) {
        // Semantic check
        try {
            SymbolTable picked = stack.peek();
            SymbolTableEntry symbolTableEntry = null;
            for (LeafID leafID : node.idList) {
                if (picked.containsKey(leafID.value)) {
                    symbolTableEntry = picked.containsEntry(leafID.value);
                    if (!symbolTableEntry.isVariable()) {
                        System.err.println("Semantic error: impossible to call a proc in " + node.name);
                        System.exit(0);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

        for (LeafID leafId : node.idList) {
            leafId.accept(this);
        }
    }

    @Override
    public void visit(WriteStatNode node) {
        for (ExprNode exprNode : node.exprList) {
            exprNode.accept(this);
        }
    }

    @Override
    public void visit(AssignStatNode node) {
        // Array temporaneo per il controllo dei tipi
        ArrayList<ValueType> valueTypesTMP = new ArrayList<>();

        for (LeafID leafId : node.idList) {
            leafId.accept(this);
        }

        for (ExprNode exprNode : node.exprList) {
            exprNode.accept(this);
            // Aggiungo i valueType di exprNode all'array temporaneo
            for (ValueType valueType : exprNode.types) valueTypesTMP.add(valueType);
        }


        if (valueTypesTMP.size() != node.idList.size()) {
            System.err.println("Semantic Error: ID does not match with assign values in assign stat");
            System.exit(0);
        }

        for (int i = 0; i < valueTypesTMP.size(); i++) {
            if (!checkAssignmentType(node.idList.get(i).type, valueTypesTMP.get(i))) {
                //if (valueTypesTMP.get(i) != node.idList.get(i).type) {
                System.err.println("Semantic Error: ID type does not match" +
                        " with Assign Value type in assign stat for id: " + node.idList.get(i).value);
                System.exit(0);
            }
        }
    }

    @Override
    public void visit(CallProcNode node) {
        // Controllo se la proc chiamata è nel type environment
        SymbolTable symbolTable = stack.peek();
        SymbolTableEntry symbolTableEntry = null;
        try {
            if (symbolTable.containsKey(node.leafID.value)) {
                symbolTableEntry = symbolTable.containsFunctionEntry(node.leafID.value);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

        // Controllo dei parametri con la symbol table
        // Funzioni non void
        if (node.exprList != null) {
            if (symbolTableEntry.inputParams.size() != node.exprList.size()) {
                System.err.println("Semantic error: number of params doesn't match in function " + node.leafID.value + " call. Required: " + symbolTableEntry.inputParams.size() + ", provided: " + node.exprList.size());
                System.exit(0);
            } else {
                for (int i = 0; i < symbolTableEntry.inputParams.size(); i++) {
                    node.exprList.get(i).accept(this);
                    //if (node.exprList.get(i).types.get(0) != symbolTableEntry.inputParams.get(i)) {
                    if (getType_Boolean(node.exprList.get(i).types.get(0), symbolTableEntry.inputParams.get(i)) == null) {
                        System.err.println("Semantic error: type mismatch for call proc " + node.leafID.value + ". Required: " + symbolTableEntry.inputParams + ", provided: '" + node.exprList.get(i).types.get(0) + "' in position " + i);
                        System.exit(0);
                    }
                }
            }
        }
        // Funzioni void
        else {
            if (symbolTableEntry.inputParams.size() != 0) {
                System.err.println("Semantic error: type mismatch for input params of call proc " + node.leafID.value + ". Required: " + symbolTableEntry.inputParams);
                System.exit(0);
            }
        }

        // Assegno i tipi alla lista di parametri di output
        node.setTypes(symbolTableEntry.outputParams);

        // Controllo sul tipo void
        if (symbolTableEntry.outputParams.size() == 0) {
            node.setType("void");
        }
    }

    @Override
    public void visit(ElifNode node) {
        node.exprNode.accept(this);
        node.statListNode.accept(this);
    }

    @Override
    public void visit(ExprNode exprNode) {
        if (exprNode.value1 != null && exprNode.value2 != null) {
            // 2 exprs
            ((ExprNode) exprNode.value1).accept(this);
            ((ExprNode) exprNode.value2).accept(this);

            //Addizione, Sottrazione, Moltiplicazione, Divisione
            if (
                    exprNode.name.equalsIgnoreCase("AddOp") ||
                            exprNode.name.equalsIgnoreCase("DiffOp") ||
                            exprNode.name.equalsIgnoreCase("MulOp") ||
                            exprNode.name.equalsIgnoreCase("DivOp")
            ) {
                // Se takenType == null -> Errore per tipi non compatibili
                ValueType takenType = getType_Operations(
                        ((ExprNode) exprNode.value1).types.get(0),
                        ((ExprNode) exprNode.value2).types.get(0)
                );

                if (takenType == null) {
                    System.err.println("Semantic error: type not compatible with operation (" + exprNode.name + "). First type: " + ((ExprNode) exprNode.value1).types.get(0) + ", second type: " + ((ExprNode) exprNode.value2).types.get(0));
                    System.exit(0);
                } else exprNode.setType(takenType);

            }
            //AND, OR
            else if (exprNode.name.equalsIgnoreCase("AndOp") || exprNode.name.equalsIgnoreCase("OrOp")) {
                // Se takenType == null -> Errore per tipi non compatibili
                ValueType takenType = getType_AndOr(
                        ((ExprNode) exprNode.value1).types.get(0),
                        ((ExprNode) exprNode.value2).types.get(0)
                );

                if (takenType == null) {
                    System.err.println("Semantic error: type not compatible with operation (" + exprNode.name + "). Required: [Boolean, Boolean], provided: [" + ((ExprNode) exprNode.value1).types.get(0) + ", " + ((ExprNode) exprNode.value2).types.get(0) + "]");
                    System.exit(0);
                } else exprNode.setType(takenType);

            }
            //LT, GT, LE...
            else {
                if (((ExprNode) exprNode.value1).types.size() > 1 || ((ExprNode) exprNode.value2).types.size() > 1) {
                    System.err.println("Semantic error: callProc returns multiple values in logical operation (" + exprNode.name + ")");
                    System.exit(0);
                }
                // Se takenType == null -> Errore per tipi non compatibili
                ValueType takenType = getType_Boolean(
                        ((ExprNode) exprNode.value1).types.get(0),
                        ((ExprNode) exprNode.value2).types.get(0)
                );

                if (takenType == null) {
                    System.err.println("Semantic error: type not compatible with logical operation (" + exprNode.name + "). First type: " + ((ExprNode) exprNode.value1).types.get(0) + ", second type: " + ((ExprNode) exprNode.value2).types.get(0));
                    System.exit(0);
                } else exprNode.setType(takenType);
            }

        } else if (exprNode.value1 != null) {
            // 1 expr
            // Leaf Null
            if (exprNode.value1 instanceof LeafNull) {
                ((LeafNull) exprNode.value1).accept(this);
                exprNode.setType(((LeafNull) exprNode.value1).type);
            }
            // LeafIntConst
            else if (exprNode.value1 instanceof LeafIntConst) {
                ((LeafIntConst) exprNode.value1).accept(this);
                exprNode.setType(((LeafIntConst) exprNode.value1).type);
            }
            // LeafFloatConst
            else if (exprNode.value1 instanceof LeafFloatConst) {
                ((LeafFloatConst) exprNode.value1).accept(this);
                exprNode.setType(((LeafFloatConst) exprNode.value1).type);
            }
            // LeafStringConst
            else if (exprNode.value1 instanceof LeafStringConst) {
                ((LeafStringConst) exprNode.value1).accept(this);
                exprNode.setType(((LeafStringConst) exprNode.value1).type);
            }
            // LeafBool
            else if (exprNode.value1 instanceof LeafBool) {
                ((LeafBool) exprNode.value1).accept(this);
                exprNode.setType(((LeafBool) exprNode.value1).type);
            }
            // LeafID
            else if (exprNode.value1 instanceof LeafID) {
                ((LeafID) exprNode.value1).accept(this);
                exprNode.setType(((LeafID) exprNode.value1).type);
            }
            // CallProc
            else if (exprNode.value1 instanceof CallProcNode) {
                ((CallProcNode) exprNode.value1).accept(this);
                exprNode.setTypes(((CallProcNode) exprNode.value1).types);
            }
            // Uminus
            else if (exprNode.name.equalsIgnoreCase("UminusOp")) {
                ((ExprNode) exprNode.value1).accept(this);
                if (((ExprNode) exprNode.value1).types.get(0) == ValueType.Integer || ((ExprNode) exprNode.value1).types.get(0) == ValueType.Float)
                    exprNode.setType(((ExprNode) exprNode.value1).types.get(0));
                else {
                    System.err.println("Semantic error: type mismatch for 'Uminus' operation. Required type: Integer or Float, provided: " + ((ExprNode) exprNode.value1).types.get(0));
                    System.exit(0);
                }
            }
            // NOT
            else if (exprNode.name.equalsIgnoreCase("NotOp")) {
                ((ExprNode) exprNode.value1).accept(this);
                if (((ExprNode) exprNode.value1).types.get(0) == ValueType.Boolean)
                    exprNode.setType(((ExprNode) exprNode.value1).types.get(0));
                else {
                    System.err.println("Semantic error: type mismatch for 'NOT' operation. Required type: Boolean, provided: " + ((ExprNode) exprNode.value1).types.get(0));
                    System.exit(0);
                }
            }
        }
    }

    @Override
    public void visit(LeafID leaf) {
        SymbolTable symbolTable = stack.peek();
        try {
            if (symbolTable.containsKey(leaf.value)) {
                SymbolTableEntry symbolTableEntry = null;
                try {
                    symbolTableEntry = symbolTable.containsEntry(leaf.value);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(0);
                }

                // Se richiamo l'id di una proc senza gli argomenti
                if (symbolTableEntry.valueType == null) {
                    System.err.println("Semantic error: variable " + leaf.value + " is not declared");
                    System.exit(0);
                }

                leaf.type = symbolTableEntry.valueType;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }


    }

    @Override
    public void visit(LeafBool leaf) {
        leaf.setType("bool");
    }

    @Override
    public void visit(LeafFloatConst leaf) {
        leaf.setType("float");
    }

    @Override
    public void visit(LeafIntConst leaf) {
        leaf.setType("int");
    }

    @Override
    public void visit(LeafNull leaf) {
        leaf.setType("null");
    }

    @Override
    public void visit(LeafStringConst leaf) {
        leaf.setType("string");
    }

    public static boolean checkAssignmentType(ValueType variable, ValueType assign) {
        if (assign == ValueType.Null)
            return true;
        if (variable == ValueType.Integer && assign == ValueType.Integer)
            return true;
        if (variable == ValueType.Float && assign == ValueType.Float)
            return true;
        if (variable == ValueType.Float && assign == ValueType.Integer)
            return true;
        if (variable == ValueType.Boolean && assign == ValueType.Boolean)
            return true;
        if (variable == ValueType.String && assign == ValueType.String)
            return true;
        else
            return false;
    }

    public static ValueType getType_AndOr(ValueType type1, ValueType type2) {
        if (type1 == ValueType.Boolean && type2 == ValueType.Boolean)
            return ValueType.Boolean;

        return null;
    }

    public static ValueType getType_Operations(ValueType type1, ValueType type2) {
        if (type1 == ValueType.Integer && type2 == ValueType.Integer)
            return ValueType.Integer;
        if (type1 == ValueType.Integer && type2 == ValueType.Float)
            return ValueType.Float;
        if (type1 == ValueType.Float && type2 == ValueType.Integer)
            return ValueType.Float;
        if (type1 == ValueType.Float && type2 == ValueType.Float)
            return ValueType.Float;

        return null;
    }

    public static ValueType getType_Boolean(ValueType type1, ValueType type2) {
        if (type1 == ValueType.Boolean && type2 == ValueType.Boolean)
            return ValueType.Boolean;
        if (type1 == ValueType.Integer && type2 == ValueType.Integer)
            return ValueType.Boolean;
        if (type1 == ValueType.Integer && type2 == ValueType.Float)
            return ValueType.Boolean;
        if (type1 == ValueType.Float && type2 == ValueType.Integer)
            return ValueType.Boolean;
        if (type1 == ValueType.Float && type2 == ValueType.Float)
            return ValueType.Boolean;
        if (type1 == ValueType.String && type2 == ValueType.String)
            return ValueType.Boolean;

        return null;
    }
}
