package visitor;

import symbol_table.SymbolTable;
import symbol_table.SymbolTableEntry;
import symbol_table.Type;
import symbol_table.ValueType;
import tree.leaves.*;
import tree.nodes.*;
import java.util.Stack;

public class Semantic_Visitor implements Semantic_Int_Visitor {

    public Stack<SymbolTable> stack = new Stack<>();

    @Override
    public void visit(ProgramNode programNode) {
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.symbolTableName = "Global";
        stack.push(symbolTable);
        if (programNode.varDecList != null) {
            for (VarDeclNode varDeclNode : programNode.varDecList) {
                varDeclNode.accept(this);
            }
        }
        if (programNode.funList != null) {
            for (FunNode funNode : programNode.funList) {
                funNode.accept(this);
            }
        }
        programNode.main.accept(this);
    }

    @Override
    public void visit(VarDeclNode varDeclNode) {
        if (varDeclNode.idInitList != null) {
            for (IdInitNode idInitNode : varDeclNode.idInitList) {
                try {
                    SymbolTable picked = stack.peek();
                    picked.createEntry_variable(idInitNode.leafID.value, varDeclNode.type);
                } catch (Exception e) {
                    System.out.println(("[ERRORE SEMANTICO] variabile " + idInitNode.leafID.value + " già dichiarata"));
                    System.exit(1);
                }
                idInitNode.type = varDeclNode.type;
                idInitNode.accept(this);
            }
        } else if (varDeclNode.IdListInitObbl != null) {
            /*
             * VAR necessita inferenza di tipo
             * for (IdInitObblNode idInitObblNode : varDeclNode.IdListInitObbl) {
             * try {
             * SymbolTable picked = stack.peek();
             * //inferenza di tipo
             * picked.createEntry_variable(idInitObblNode.leafID.value,
             * varDeclNode.type.type);
             * } catch (Exception e) {
             * e.printStackTrace();
             * }
             * idInitObblNode.accept(this);
             * idInitObblNode.setType(varDeclNode.type.type);
             * }
             */
        }
    }

    @Override
    public void visit(IdInitNode idInitNode) {
        idInitNode.leafID.type = idInitNode.type;
        idInitNode.leafID.accept(this);
        if (idInitNode.exprNode != null) {
            idInitNode.exprNode.accept(this);
            System.out.println("[DEBUG] " + idInitNode.leafID.value + ":" + idInitNode.type + " assegno "
                    + idInitNode.exprNode.type);
            if (!checkAssignmentType(idInitNode.type, idInitNode.exprNode.type)) {
                System.err.println(
                        "[ERRORE SEMANTICO] inizializzazione sbagliata per variabile " + idInitNode.leafID.value);
                System.exit(1);
            }
        }
    }

    @Override
    public void visit(ExprNode exprNode) {
        if (exprNode.val_One != null && exprNode.val_Two != null) {
            ((ExprNode) exprNode.val_One).accept(this);
            ((ExprNode) exprNode.val_Two).accept(this);
            System.out.println("[DEBUG] " + (((ExprNode) exprNode.val_One).type) + " " + exprNode.op + " "
                    + (((ExprNode) exprNode.val_Two).type));
            if (exprNode.op.equalsIgnoreCase("PLUS") ||
                    exprNode.op.equalsIgnoreCase("MINUS") ||
                    exprNode.op.equalsIgnoreCase("TIMES") ||
                    exprNode.op.equalsIgnoreCase("DIV")) {
                ValueType resultType = getType_Operations(
                        ((ExprNode) exprNode.val_One).type,
                        ((ExprNode) exprNode.val_Two).type);
                if (resultType == null) {
                    System.err.println("[ERRORE SEMANTICO] tipi op PLUS/MINUS/TIMES/DIV sbagliato");
                    System.exit(1);
                } else
                    exprNode.type = resultType;
            } else if (exprNode.op.equalsIgnoreCase("AND") || exprNode.op.equalsIgnoreCase("OR")) {
                ValueType resultType = getType_AndOr(
                        ((ExprNode) exprNode.val_One).type,
                        ((ExprNode) exprNode.val_Two).type);
                if (resultType == null) {
                    System.err.println("[ERRORE SEMANTICO] tipi op AND/OR sbagliato");
                    System.exit(1);
                } else
                    exprNode.type = resultType;
            } else {
                ValueType resultType = getType_Boolean(
                        ((ExprNode) exprNode.val_One).type,
                        ((ExprNode) exprNode.val_Two).type);
                if (resultType == null) {
                    System.err.println("[ERRORE SEMANTICO] tipi op LT/GT/ecc. sbagliato");
                    System.exit(1);
                } else
                    exprNode.type = resultType;
            }
        } else if (exprNode.val_One != null) {
            if (exprNode.val_One instanceof LeafIntegerConst) {
                ((LeafIntegerConst) exprNode.val_One).accept(this);
                exprNode.type = ((LeafIntegerConst) exprNode.val_One).type;
            } else if (exprNode.val_One instanceof LeafRealConst) {
                ((LeafRealConst) exprNode.val_One).accept(this);
                exprNode.type = ((LeafRealConst) exprNode.val_One).type;
            } else if (exprNode.val_One instanceof LeafStringConst) {
                ((LeafStringConst) exprNode.val_One).accept(this);
                exprNode.type = ((LeafStringConst) exprNode.val_One).type;
            } else if (exprNode.val_One instanceof LeafBool) {
                ((LeafBool) exprNode.val_One).accept(this);
                exprNode.type = ((LeafBool) exprNode.val_One).type;
            } else if (exprNode.val_One instanceof LeafID) {
                ((LeafID) exprNode.val_One).accept(this);
                exprNode.type = ((LeafID) exprNode.val_One).type;
            }

            /*
             * // CallProc
             * else if (exprNode.val_One instanceof CallProcNode) {
             * ((CallProcNode) exprNode.value1).accept(this);
             * exprNode.setTypes(((CallProcNode) exprNode.value1).types);
             * }
             * // Uminus
             * else if (exprNode.name.equalsIgnoreCase("UminusOp")) {
             * ((ExprNode) exprNode.value1).accept(this);
             * if (((ExprNode) exprNode.value1).types.get(0) == ValueType.Integer ||
             * ((ExprNode) exprNode.value1).types.get(0) == ValueType.Float)
             * exprNode.setType(((ExprNode) exprNode.value1).types.get(0));
             * else {
             * System.err.
             * println("Semantic error: type mismatch for 'Uminus' operation. Required type: Integer or Float, provided: "
             * + ((ExprNode) exprNode.value1).types.get(0));
             * System.exit(0);
             * }
             * }
             * // NOT
             * else if (exprNode.name.equalsIgnoreCase("NotOp")) {
             * ((ExprNode) exprNode.value1).accept(this);
             * if (((ExprNode) exprNode.value1).types.get(0) == ValueType.Boolean)
             * exprNode.setType(((ExprNode) exprNode.value1).types.get(0));
             * else {
             * System.err.
             * println("Semantic error: type mismatch for 'NOT' operation. Required type: Boolean, provided: "
             * + ((ExprNode) exprNode.value1).types.get(0));
             * System.exit(0);
             * }
             * }
             */

        }
    }

    @Override
    public void visit(MainNode mainNode) {
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.symbolTableName = "Main";
        symbolTable.setFatherSymTab(stack.firstElement());
        stack.push(symbolTable);
        if (mainNode.varDeclList != null) {
            for (VarDeclNode varDeclNode : mainNode.varDeclList) {
                varDeclNode.accept(this);
            }
        }
    }

    @Override
    public void visit(StatNode statNode) {

    }

    @Override
    public void visit(IfStatNode ifStatNode) {

    }

    @Override
    public void visit(FunNode funNode) {

    }

    @Override
    public void visit(AssignStatNode assignStatNode) {

    }

    @Override
    public void visit(CallFunNode callFunNode) {

    }

    @Override
    public void visit(WhileStatNode whileStatNode) {

    }

    @Override
    public void visit(WriteStatNode writeStatNode) {

    }

    @Override
    public void visit(ReadStatNode readStatNode) {

    }

    @Override
    public void visit(ParamDecNode paramDecNode) {

    }

    @Override
    public void visit(ElseNode elseNode) {

    }

    @Override
    public void visit(IdInitObblNode idInitObblNode) {

    }

    @Override
    public void visit(ConstNode constNode) {

    }

    @Override
    public void visit(ReturnNode resultNode) {
    }

    @Override
    public void visit(LeafIntegerConst leafIntegerConst) {
        leafIntegerConst.setType("integer");
    }

    @Override
    public void visit(LeafRealConst leafRealConst) {
        leafRealConst.setType("real");
    }

    @Override
    public void visit(LeafBool leafBool) {
        leafBool.setType("bool");
    }

    @Override
    public void visit(LeafStringConst leafStringConst) {
        leafStringConst.setType("string");
    }

    @Override
    public void visit(LeafID leafID) {
        SymbolTable symbolTable = stack.peek();
        try {
            SymbolTableEntry symbolTableEntry = symbolTable.containsEntry(leafID.value);
            leafID.type = symbolTableEntry.valueType;
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static boolean checkAssignmentType(ValueType variable, ValueType assign) {
        if (variable == ValueType.integer && assign == ValueType.integer)
            return true;
        if (variable == ValueType.real && assign == ValueType.real)
            return true;
        if (variable == ValueType.bool && assign == ValueType.bool)
            return true;
        if (variable == ValueType.string && assign == ValueType.string)
            return true;
        else
            return false;
    }

    public static ValueType getType_AndOr(ValueType type1, ValueType type2) {
        if (type1 == ValueType.bool && type2 == ValueType.bool)
            return ValueType.bool;
        return null;
    }

    public static ValueType getType_Operations(ValueType type1, ValueType type2) {
        if (type1 == ValueType.integer && type2 == ValueType.integer)
            return ValueType.integer;
        if (type1 == ValueType.integer && type2 == ValueType.real)
            return ValueType.real;
        if (type1 == ValueType.real && type2 == ValueType.integer)
            return ValueType.real;
        if (type1 == ValueType.real && type2 == ValueType.integer)
            return ValueType.real;
        if (type1 == ValueType.real && type2 == ValueType.real)
            return ValueType.real;
        return null;
    }

    public static ValueType getType_Boolean(ValueType type1, ValueType type2) {
        if (type1 == ValueType.bool && type2 == ValueType.bool)
            return ValueType.bool;
        if (type1 == ValueType.integer && type2 == ValueType.integer)
            return ValueType.bool;
        if (type1 == ValueType.integer && type2 == ValueType.real)
            return ValueType.bool;
        if (type1 == ValueType.real && type2 == ValueType.integer)
            return ValueType.bool;
        if (type1 == ValueType.real && type2 == ValueType.real)
            return ValueType.bool;
        if (type1 == ValueType.string && type2 == ValueType.string)
            return ValueType.bool;
        return null;
    }

}
