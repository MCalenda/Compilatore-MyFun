package visitor;

import symbol_table.SymbolTable;
import symbol_table.SymbolTableEntry;
import symbol_table.ValueType;
import tree.leaves.*;
import tree.nodes.*;

import java.util.ArrayList;
import java.util.Stack;

import cup.sym;
import java_cup.shift_action;

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
        for (SymbolTable s : stack){
            System.out.println("Tabella " + s.symbolTableName);
            for (String key: s.keySet()) {
                System.out.println(key + ": " + s.get(key).toString());
            }
            System.out.println("--------------");
        }
    }

    @Override
    public void visit(VarDeclNode varDeclNode) {
        if (varDeclNode.idInitList != null) {
            for (IdInitNode idInitNode : varDeclNode.idInitList) {
                try {
                    SymbolTable picked = stack.peek();
                    picked.createEntry_variable(idInitNode.leafID.value, varDeclNode.type);
                } catch (Exception e) {
                    System.out.println(("[SEMANTIC ERROR] variabile " + idInitNode.leafID.value + " già dichiarata"));
                    System.exit(1);
                }
                // Passo il tipo dell'inizializzazione ad ogni elemento della lista di ID
                idInitNode.type = varDeclNode.type;
                idInitNode.accept(this);
            }
        } else if (varDeclNode.IdListInitObbl != null) {
            for (IdInitObblNode idInitObblNode : varDeclNode.IdListInitObbl) {
                try {
                    idInitObblNode.value.accept(this);
                    idInitObblNode.type = idInitObblNode.value.type;
                    SymbolTable picked = stack.peek();
                    picked.createEntry_variable(idInitObblNode.leafID.value, idInitObblNode.type);
                } catch (Exception e) {
                    System.out.println(("[SEMANTIC ERROR] variabile " + idInitObblNode.leafID.value + " già dichiarata"));
                    System.exit(1);
                }
                idInitObblNode.accept(this);
            }
        }
    }

    @Override
    public void visit(IdInitNode idInitNode) {
        // Aggiorno il tipo di leafID
        idInitNode.leafID.accept(this);

        // Nel caso ci sia un operazione di assegnamento
        if (idInitNode.exprNode != null) {
            idInitNode.exprNode.accept(this);
            System.out.println("[DEBUG] " + idInitNode.leafID.value + ":" + idInitNode.type + " assegno " + idInitNode.exprNode.type);
            if (!checkAssignmentType(idInitNode.type, idInitNode.exprNode.type)) {
                System.err.println("[SEMANTIC ERROR] inizializzazione sbagliata per variabile " + idInitNode.leafID.value);
                System.exit(1);
            }
        } else {
            System.out.println("[DEBUG] " + idInitNode.leafID.value + ":" + idInitNode.type + " dichiarato");
        }
    }

    @Override
    public void visit(ExprNode exprNode) {
        // Se è un espressione con doppio argomento
        if (exprNode.val_One != null && exprNode.val_Two != null) {
            ((ExprNode) exprNode.val_One).accept(this);
            ((ExprNode) exprNode.val_Two).accept(this);

            System.out.println("[DEBUG] " + (((ExprNode) exprNode.val_One).type) + " " + exprNode.op + " " + (((ExprNode) exprNode.val_Two).type));

            // Se è un operazione matematica
            if (exprNode.op.equalsIgnoreCase("PLUS") || exprNode.op.equalsIgnoreCase("MINUS") || exprNode.op.equalsIgnoreCase("TIMES") || exprNode.op.equalsIgnoreCase("DIV")) {
                ValueType resultType = getType_Operations(((ExprNode) exprNode.val_One).type, ((ExprNode) exprNode.val_Two).type);
                if (resultType == null) {
                    System.err.println("[SEMANTIC ERROR] tipi op PLUS/MINUS/TIMES/DIV sbagliato");
                    System.exit(1);
                } else
                    exprNode.type = resultType;

                // Se è un AND o un OR
            } else if (exprNode.op.equalsIgnoreCase("AND") || exprNode.op.equalsIgnoreCase("OR")) {
                ValueType resultType = getType_AndOr(((ExprNode) exprNode.val_One).type, ((ExprNode) exprNode.val_Two).type);
                if (resultType == null) {
                    System.err.println("[SEMANTIC ERROR] tipi op AND/OR sbagliato");
                    System.exit(1);
                } else
                    exprNode.type = resultType;

                // Se è una qualsiasi altra operazione Booleana (<, <=, >, >=, =, !=)
            } else {
                ValueType resultType = getType_Boolean(((ExprNode) exprNode.val_One).type, ((ExprNode) exprNode.val_Two).type);
                if (resultType == null) {
                    System.err.println("[SEMANTIC ERROR] tipi op LT/GT/ecc. sbagliato");
                    System.exit(1);
                } else
                    exprNode.type = resultType;
            }

            // Se è un espressione con argomento singolo
        } else if (exprNode.val_One != null) {

            // Se è un assegnamento di qualche costante o ID
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

                // Se è un operazione unaria come UMINUS o NOT
            } else if (exprNode.op.equalsIgnoreCase("UMINUS")) {
                ((ExprNode) exprNode.val_One).accept(this);
                System.out.println("[DEBUG] " + exprNode.op + " " + (((ExprNode) exprNode.val_One).type));
                if (((ExprNode) exprNode.val_One).type == ValueType.integer || ((ExprNode) exprNode.val_One).type == ValueType.real)
                    exprNode.type = (((ExprNode) exprNode.val_One).type);
                else {
                    System.err.println("[SEMANTIC ERROR] tipi op UMINUS sbagliato");
                    System.exit(1);
                }
            } else if (exprNode.op.equalsIgnoreCase("NOT")) {
                ((ExprNode) exprNode.val_One).accept(this);
                System.out.println("[DEBUG] " + exprNode.op + " " + (((ExprNode) exprNode.val_One).type));
                if (((ExprNode) exprNode.val_One).type == ValueType.bool)
                    exprNode.type = (((ExprNode) exprNode.val_One).type);
                else {
                    System.err.println("[SEMANTIC ERROR] tipo op NOT sbagliato");
                    System.exit(1);
                }
                // Call fun
            } else if (exprNode.val_One instanceof CallFunNode) {
                ((CallFunNode) exprNode.val_One).accept(this);
                // una funzione puo restituire piu tipi
                // exprNode.type = ((CallFunNode) exprNode.val_One).type;
            }
        }
    }

    @Override
    public void visit(MainNode mainNode) {
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.symbolTableName = "Main";
        // Setta come padre Global
        symbolTable.setFatherSymTab(stack.firstElement());
        stack.push(symbolTable);

        // Se il main ha delle inizializzazioni
        if (mainNode.varDeclList != null) {
            for (VarDeclNode varDeclNode : mainNode.varDeclList) {
                varDeclNode.accept(this);
            }
        }
        if (mainNode.statList != null) {
            for (StatNode statNode : mainNode.statList) {
                statNode.accept(this);
            }
        }
        stack.pop();
    }

    @Override
    public void visit(StatNode statNode) {
        if (statNode.ifStatNode != null) {
            statNode.ifStatNode.accept(this);
        } else if (statNode.whileStatNode != null) {
            statNode.whileStatNode.accept(this);
        } else if (statNode.readStatNode != null) {
            statNode.readStatNode.accept(this);
        } else if (statNode.writeStatNode != null) {
            statNode.writeStatNode.accept(this);
        } else if (statNode.assignStatNode != null) {
            statNode.assignStatNode.accept(this);
        } else if (statNode.callFunNode != null) {
            statNode.callFunNode.accept(this);
        } else if (statNode.returnNode != null) {
            statNode.returnNode.accept(this);
        }     
    }

    @Override
    public void visit(IfStatNode ifStatNode) {

    }

    @Override
    public void visit(FunNode funNode) {
        try {
            ArrayList<ValueType> params = new ArrayList<>();
            if (funNode.paramDecList != null) {
                for (ParamDecNode parDecNode : funNode.paramDecList) {
                    params.add(parDecNode.type);
                }
            }
            stack.firstElement().createEntry_function(funNode.leafID.value, funNode.type, params);
        } catch (Exception e) {
            System.err.println("Semantic Error");
            System.exit(1);
        }

        SymbolTable symbolTable = new SymbolTable();
        symbolTable.symbolTableName = funNode.leafID.value;
        symbolTable.setFatherSymTab(stack.firstElement());
        stack.push(symbolTable);

        if (funNode.paramDecList != null) {
            for (ParamDecNode parDecNode : funNode.paramDecList)
                parDecNode.accept(this);
        }

        if (funNode.statList != null) {
            for (StatNode statNode : funNode.statList)
                statNode.accept(this);
        }
        stack.pop();
    }

    @Override
    public void visit(AssignStatNode assignStatNode) {

    }

    @Override
    public void visit(CallFunNode callFunNode) {
        SymbolTableEntry functionDef = null;
        
        // Controllo se il nome della funzione è nel Type Environment
        try {
            SymbolTable symbolTable = stack.peek();
            functionDef = symbolTable.containsFunctionEntry(callFunNode.leafID.value);
        } catch (Exception e) {
            System.err.println("Errore semantico");
            System.exit(0);
        }

        // Controllo dei parametri della funzione
        if (functionDef.params != null) {
            if (functionDef.params.size() != callFunNode.exprList.size()) {
                System.err.println("[SEMANTIC ERROR] 1");
                System.exit(0);
            } else {
                for (int i = 0; i < callFunNode.exprList.size(); i++) {
                    callFunNode.exprList.get(i).accept(this);
                    if (callFunNode.exprList.get(i).type != functionDef.params.get(i)) {
                        System.err.println("[SEMANTIC ERROR] 2");
                        System.exit(0);
                    }
                }
            }
        }
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
        try {
            SymbolTable picked = stack.peek();
            picked.createEntry_variable(paramDecNode.leafID.value, paramDecNode.type);
        } catch (Exception e) {
            System.out.println(("[SEMANTIC ERROR] parametro " + paramDecNode.leafID.value + " già dichiarato"));
            System.exit(1);
        }
    }

    @Override
    public void visit(ElseNode elseNode) {

    }

    @Override
    public void visit(IdInitObblNode idInitObblNode) {
        idInitObblNode.leafID.type = idInitObblNode.type;
        idInitObblNode.leafID.accept(this);
        System.out.println("[DEBUG] " + idInitObblNode.leafID.value + ":var assegno " + idInitObblNode.value.type);
    }

    @Override
    public void visit(ConstNode constNode) {
        if (constNode.value instanceof LeafIntegerConst) {
            constNode.type = ValueType.integer;
        } else if (constNode.value instanceof LeafRealConst) {
            constNode.type = ValueType.real;
        } else if (constNode.value instanceof LeafBool) {
            constNode.type = ValueType.bool;
        } else if (constNode.value instanceof LeafStringConst) {
            constNode.type = ValueType.string;
        }
    }

    @Override
    public void visit(ReturnNode resultNode) {
    }

    // Visite delle foglie
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

    @Override
    public void visit(LeafIntegerConst leafIntegerConst) {
        leafIntegerConst.type = ValueType.integer;
    }

    @Override
    public void visit(LeafRealConst leafRealConst) {
        leafRealConst.type = ValueType.real;
    }

    @Override
    public void visit(LeafBool leafBool) {
        leafBool.type = ValueType.bool;
    }

    @Override
    public void visit(LeafStringConst leafStringConst) {
        leafStringConst.type = ValueType.string;
    }

    // Metodi per il type checking
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

    public static ValueType getType_Operations(ValueType type1, ValueType type2) {
        if (type1 == ValueType.integer && type2 == ValueType.integer)
            return ValueType.integer;
        if (type1 == ValueType.integer && type2 == ValueType.real)
            return ValueType.real;
        if (type1 == ValueType.real && type2 == ValueType.integer)
            return ValueType.real;
        if (type1 == ValueType.real && type2 == ValueType.real)
            return ValueType.real;
        return null;
    }

    public static ValueType getType_StrConc(ValueType type1, ValueType type2) {
        if (type1 == ValueType.string && type2 == ValueType.string)
            return ValueType.string;
        return null;
    }

    public static ValueType getType_AndOr(ValueType type1, ValueType type2) {
        if (type1 == ValueType.bool && type2 == ValueType.bool)
            return ValueType.bool;
        return null;
    }

    public static ValueType getType_Boolean(ValueType type1, ValueType type2) {
        if (type1 == ValueType.integer && type2 == ValueType.integer)
            return ValueType.bool;
        if (type1 == ValueType.real && type2 == ValueType.real)
            return ValueType.bool;
        if (type1 == ValueType.integer && type2 == ValueType.real)
            return ValueType.bool;
        if (type1 == ValueType.real && type2 == ValueType.integer)
            return ValueType.bool;
        return null;
    }
}