package visitor;

import java.util.ArrayList;
import java.util.Stack;

import symbol_table.*;
import tree.leaves.*;
import tree.nodes.*;

public class Semantic_Visitor implements Semantic_Int_Visitor {
    // Stack contente le tabelle dei simboli
    public Stack<SymbolTable> stack = new Stack<>();
    // Flag di attivazione per i log di Debug
    public boolean debugTab = false;
    public boolean debugVar = false;

    // Metodi polimorfi per l'implementazione del visitor
    /* ---------------------------------------------------------- */
    /* ---------------------------------------------------------- */
    @Override
    public void visit(ProgramNode programNode) {
        // Creo la ST globale e la pusho nello stack
        SymbolTable symbolTable = new SymbolTable("Global");
        stack.push(symbolTable);

        // Controllo la lista di dichiarazioni di variabili
        if (programNode.varDecList != null)
            for (VarDeclNode varDeclNode : programNode.varDecList)
                varDeclNode.accept(this);

        // Controllo la lista di dichiarazioni di funzioni
        if (programNode.funList != null)
            for (FunNode funNode : programNode.funList)
                funNode.accept(this);

        // Controllo il main
        programNode.main.accept(this);

        // DEBUG
        if (debugTab) {
            System.out.println("Tabella " + stack.peek().symbolTableName + " |");
            for (String key : stack.peek().keySet())
                System.out.println(key + ": " + stack.peek().get(key).toString());
            System.out.println("-----------------------------------------------------");
        }

        // Rimuovo la ST dallo stack, in quanto non serve più
        stack.pop();
    }

    @Override
    public void visit(VarDeclNode varDeclNode) {
        // Prendo la tabella al top dello stack
        SymbolTable picked = stack.peek();

        // Controllo la lista di ID della dichiarazione (inizializzazione possibile)
        if (varDeclNode.idInitList != null) {
            for (IdInitNode idInitNode : varDeclNode.idInitList) {
                // Passo il tipo ad ogni elemento della lista di ID
                idInitNode.type = varDeclNode.type;
                // Creo la variabile in tabella
                if (!picked.createEntry_variable(idInitNode.leafID.value, varDeclNode.type)) {
                    System.out.println("[ERRORE SEMANTICO] variabile " + idInitNode.leafID.value + " già dichiarata nel T.E. " + stack.peek().symbolTableName);
                    System.exit(1);
                }
                // Controllo idInitNode
                idInitNode.accept(this);
            }

            // Controllo la lista di ID della dichiarazione (inizializzazione obbligata)
        } else if (varDeclNode.IdListInitObbl != null) {
            for (IdInitObblNode idInitObblNode : varDeclNode.IdListInitObbl) {
                // Inferisco il tipo del valore
                idInitObblNode.value.accept(this);
                // Passo il tipo ad ogni elemento della lista di ID
                idInitObblNode.type = idInitObblNode.value.type;
                // Creo la variabile in tabella
                if (!picked.createEntry_variable(idInitObblNode.leafID.value, idInitObblNode.type)) {
                    System.out.println("[ERRORE SEMANTICO] variabile " + idInitObblNode.leafID.value + " già dichiarata nel T.E. " + stack.peek().symbolTableName);
                    System.exit(1);
                }
                // Controllo idInitObblNode
                idInitObblNode.accept(this);
            }
        }
    }

    @Override
    public void visit(IdInitNode idInitNode) {
        // Controllo il tipo della leafID
        idInitNode.leafID.accept(this);

        // Nel caso ci sia un inizializzazione
        if (idInitNode.exprNode != null) {
            idInitNode.exprNode.accept(this);
            // Controllo se il tipo dell'inizializzazione è compatibile con la variabile
            if (!checkAssignmentType(idInitNode.type, idInitNode.exprNode.type)) {
                System.err.println("[ERRORE SEMANTICO] init errato variabile " + idInitNode.leafID.value + " atteso: [" + idInitNode.type + "] assegnato: [" + idInitNode.exprNode.type + "]");
                System.exit(1);
            }

            // DEBUG
            if (debugVar)
                System.out.println("[DEBUG] " + idInitNode.leafID.value + ":" + idInitNode.type + " assegno " + idInitNode.exprNode.type);
        } else if (debugVar)
            System.out.println("[DEBUG] variabile " + idInitNode.leafID.value + ":" + idInitNode.type + " dichiarato");
    }

    @Override
    public void visit(IdInitObblNode idInitObblNode) {
        // Controllo il tipo della leafID
        idInitObblNode.leafID.accept(this);

        // DEBUG
        if (debugVar)
            System.out.println("[DEBUG] variabile " + idInitObblNode.leafID.value + ":var assegno " + idInitObblNode.value.type);
    }

    @Override
    public void visit(FunNode funNode) {
        // Creo una nuova ST per la funzione
        SymbolTable symbolTable = new SymbolTable(funNode.leafID.value);

        // Setto come padre la tabella Global e la inserisco nello stack
        symbolTable.setFatherSymTab(stack.firstElement());
        stack.push(symbolTable);

        // Lista di tipi dei parametri
        ArrayList<ValueType> params = new ArrayList<>();
        // Lista di booleani, se è un OUT/@
        ArrayList<Boolean> isOut = new ArrayList<>();

        // Controllo la lista di parametri
        if (funNode.paramDecList != null)
            for (ParamDecNode parDecNode : funNode.paramDecList) {
                parDecNode.accept(this);
                // Inserisco il tipo del paramentro nella lista
                params.add(parDecNode.type);
                // Inserisco se il paramentro è di tipo OUT/@ o meno, nella lista
                if (parDecNode.out)
                    isOut.add(true);
                else
                    isOut.add(false);
            }

        // Creo la funzione nella tabella ROOT (firstElement)
        if (!stack.firstElement().createEntry_function(funNode.leafID.value, funNode.type, params, isOut)) {
            System.err.println("[ERRORE SEMANTICO] funzione " + funNode.leafID.value + " gia dichiarata nel T.E. " + stack.peek().symbolTableName);
            System.exit(1);
        }

        // Controllo la lista di dichiarazioni di variabili
        if (funNode.varDecList != null)
            for (VarDeclNode varDeclNode : funNode.varDecList)
                varDeclNode.accept(this);

        // Controllo la lista di statements
        if (funNode.statList != null) {
            for (StatNode statNode : funNode.statList)
                statNode.accept(this);
        }

        // DEBUG
        if (debugTab) {
            System.out.println("Tabella " + stack.peek().symbolTableName + " | padre: " + stack.peek().fatherSymbolTable.symbolTableName + " |");
            for (String key : stack.peek().keySet())
                System.out.println(key + ": " + stack.peek().get(key).toString());
            System.out.println("-----------------------------------------------------");
        }

        // Rimuovo la ST dallo stack, in quanto non serve più
        stack.pop();
    }

    @Override
    public void visit(ParamDecNode paramDecNode) {
        // Prendo la tabella al top (sarà sempre quella della funzione essendo in
        // creazione)
        SymbolTable picked = stack.peek();

        // Creo la variabile in tabella
        if (!picked.createEntry_variable(paramDecNode.leafID.value, paramDecNode.type)) {
            System.out.println("ERRORE SEMANTICO] parametro " + paramDecNode.leafID.value + " già dichiarato nel T.E. " + stack.peek().symbolTableName);
            System.exit(1);
        }
    }

    @Override
    public void visit(MainNode mainNode) {
        // Creo una nuova ST per il main
        SymbolTable symbolTable = new SymbolTable("Main");

        // Setto come padre la tabella Global e la inserisco nello stack
        symbolTable.setFatherSymTab(stack.firstElement());
        stack.push(symbolTable);

        // Controllo la lista di dichiarazioni di variabili
        if (mainNode.varDeclList != null)
            for (VarDeclNode varDeclNode : mainNode.varDeclList)
                varDeclNode.accept(this);

        // Controllo la lista di dichiarazioni di funzioni
        if (mainNode.statList != null)
            for (StatNode statNode : mainNode.statList)
                statNode.accept(this);

        // DEBUG
        if (debugTab) {
            System.out.println("| Tabella " + stack.peek().symbolTableName + " | padre: " + stack.peek().fatherSymbolTable.symbolTableName + " |");
            for (String key : stack.peek().keySet())
                System.out.println(key + ": " + stack.peek().get(key).toString());
            System.out.println("-----------------------------------------------------");
        }

        // Rimuovo la ST dallo stack, in quanto non serve più
        stack.pop();
    }

    // Statement
    /* ---------------------------------------------------------- */
    @Override
    public void visit(StatNode statNode) {
        // Controllo in vase a quella attiva
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
        // Controllo che il tipo dell'espressione dell'IF, sia un booleano
        ifStatNode.expr.accept(this);
        if (ifStatNode.expr.type != ValueType.bool) {
            System.err.println("[ERRORE SEMANTICO] espressione dello statement IF di tipo non bool");
            System.exit(1);
        }

        // Creo una nuova tabella e setto il padre come il top dello stack (Il
        // chiamante) e inserisco
        SymbolTable symbolTable = new SymbolTable(ifStatNode.name);
        symbolTable.setFatherSymTab(stack.peek());
        stack.push(symbolTable);

        // Controllo la lista di dichiarazioni di variabili
        for (VarDeclNode varDeclNode : ifStatNode.varDeclList)
            varDeclNode.accept(this);

        // Controllo la lista di statements
        for (StatNode statNode : ifStatNode.statList)
            statNode.accept(this);

        // DEBUG
        if (debugTab) {
            System.out.println("Tabella " + stack.peek().symbolTableName + " | padre: " + stack.peek().fatherSymbolTable.symbolTableName + " |");
            for (String key : stack.peek().keySet())
                System.out.println(key + ": " + stack.peek().get(key).toString());
            System.out.println("-----------------------------------------------------");
        }

        // Rimuovo la ST dallo stack, in quanto non serve più
        stack.pop();

        // Controllo, se c'è, il nodo ELSE
        if (ifStatNode.elseNode != null) {
            ifStatNode.elseNode.accept(this);
        }
    }

    @Override
    public void visit(ElseNode elseNode) {
        // Creo una nuova tabella e setto il padre come il top dello stack (Il
        // chiamante) e inserisco
        SymbolTable symbolTable = new SymbolTable(elseNode.name);
        symbolTable.setFatherSymTab(stack.peek());
        stack.push(symbolTable);

        // Controllo la lista di dichiarazioni di variabili
        for (VarDeclNode varDeclNode : elseNode.varDeclList)
            varDeclNode.accept(this);

        // Controllo la lista di statements
        for (StatNode statNode : elseNode.statList)
            statNode.accept(this);

        // DEBUG
        if (debugTab) {
            System.out.println("Tabella " + stack.peek().symbolTableName + " | padre: " + stack.peek().fatherSymbolTable.symbolTableName + " |");
            for (String key : stack.peek().keySet())
                System.out.println(key + ": " + stack.peek().get(key).toString());
            System.out.println("-----------------------------------------------------");
        }

        // Rimuovo la ST dallo stack, in quanto non serve più
        stack.pop();
    }

    @Override
    public void visit(WhileStatNode whileStatNode) {
        // Controllo che il tipo dell'espressione del WHILE, sia un booleano
        whileStatNode.expr.accept(this);
        if (whileStatNode.expr.type != ValueType.bool) {
            System.err.println("[ERRORE SEMANTICO] espressione dello statement WHILE di tipo non bool");
            System.exit(1);
        }

        // Creo una nuova tabella e setto il padre come il top dello stack (Il
        // chiamante) e inserisco
        SymbolTable symbolTable = new SymbolTable(whileStatNode.name);
        symbolTable.setFatherSymTab(stack.peek());
        stack.push(symbolTable);

        // Controllo la lista di dichiarazioni di variabili
        for (VarDeclNode varDeclNode : whileStatNode.varDeclList)
            varDeclNode.accept(this);

        // Controllo la lista di statements
        for (StatNode statNode : whileStatNode.statList)
            statNode.accept(this);

        // DEBUG
        if (debugTab) {
            System.out.println("Tabella " + stack.peek().symbolTableName + " | padre: " + stack.peek().fatherSymbolTable.symbolTableName + " |");
            for (String key : stack.peek().keySet())
                System.out.println(key + ": " + stack.peek().get(key).toString());
            System.out.println("-----------------------------------------------------");
        }

        // Rimuovo la ST dallo stack, in quanto non serve più
        stack.pop();
    }

    @Override
    public void visit(ReadStatNode readStatNode) {
        // Controllo il tipo dell'espressione della READ, se c'è
        if (readStatNode.expr != null) {
            readStatNode.expr.accept(this);
            // L'espressione deve essere di tipo string
            if (readStatNode.expr.type != ValueType.string) {
                System.err.println("[SEMANTIC ERROR] tipo expr op READ errato atteso: [string] assegnato: [" + readStatNode.expr.type + "]");
            }
        }

        // Controllo gli ID
        for (LeafID leafID : readStatNode.IdList)
            leafID.accept(this);
    }

    @Override
    public void visit(WriteStatNode writeStatNode) {
        // Controllo l'espressione
        writeStatNode.expr.accept(this);
    }

    @Override
    public void visit(AssignStatNode assignStatNode) {
        // Controllo l'ID
        assignStatNode.leafID.accept(this);

        // Controllo il tipo dell'espressione
        assignStatNode.expr.accept(this);

        // Controllo che i tipi siano compatibili per un assegnamento
        if (!checkAssignmentType(assignStatNode.leafID.type, assignStatNode.expr.type)) {
            System.err.println("[SEMANTIC ERROR] assegnamento sbagliato per variabile " + assignStatNode.leafID.value + " atteso: [" + assignStatNode.leafID.type + "] assegnato: [" + assignStatNode.expr.type + "]");
            System.exit(1);
        }

        // DEBUG
        if (debugVar)
            System.out.println("[DEBUG] " + assignStatNode.leafID.value + ":" + assignStatNode.leafID.type + " assegno " + assignStatNode.expr.type);
    }

    @Override
    public void visit(CallFunNode callFunNode) {
        // Trovo la definizione di funzione nella tabella Global partendo dalla ROOT
        SymbolTable symbolTable = stack.firstElement();
        SymbolTableEntry functionDef = symbolTable.containsFunctionEntry(callFunNode.leafID.value);

        // Controllo se la funzione è dichiarata nella tabella Global
        if (functionDef == null) {
            System.err.println("[ERRORE SEMANTICO] funzione " + callFunNode.leafID.value + " non dichiarata");
            System.exit(1);
        }

        // Controllo che i parametri passati siano giusti in numero
        if (functionDef.params.size() != callFunNode.exprList.size()) {
            System.err.println("[ERRORE SEMANTICO] i parametri della funzione " + callFunNode.leafID.value + " non corrispondono con la dichiarazione atteso: " + functionDef.params);
            System.exit(0);

        } else {
            // Controllo i singoli parametri
            for (int i = 0; i < callFunNode.exprList.size(); i++) {
                callFunNode.exprList.get(i).accept(this);

                // Controllo che il parametro sia giusto in tipo
                if (callFunNode.exprList.get(i).type != functionDef.params.get(i)) {
                    System.err.println("[ERRORE SEMANTICO] i parametri della funzione " + callFunNode.leafID.value + " non corrispondono con la dichiarazione atteso: " + functionDef.params);
                    System.exit(1);
                }

                // Controllo che il parametro sia giusto in modalità outpar
                if ((callFunNode.exprList.get(i).op.equalsIgnoreCase("OUTPAR")) != functionDef.isOut.get(i)) {
                    System.err.println("[ERRORE SEMANTICO] i parametri della funzione " + callFunNode.leafID.value + " non corrispondono con la dichiarazione atteso: " + functionDef.params);
                    System.exit(1);
                }
            }
        }

        // Setto il tipo della callFun come il tipo di ritorno della funzione
        callFunNode.type = functionDef.valueType;
    }

    @Override
    public void visit(ReturnNode returnNode) {
        // Controllo il tipo dell'espressione del return
        returnNode.expr.accept(this);

        // Controllo se esiste una funzione in cui il return si trova nella tabella
        // corrente
        SymbolTable symbolTable = stack.peek();
        SymbolTableEntry functionDef = symbolTable.containsFunctionEntry(symbolTable.symbolTableName);

        // Risalgo fino alla tabella Global
        while (functionDef == null){
            symbolTable = symbolTable.fatherSymbolTable;
            functionDef = symbolTable.containsFunctionEntry(symbolTable.symbolTableName);
        }

        // Se il tipo di ritorno della funzione non è compatibile con l'espressione del
        // return
        if (functionDef.valueType != returnNode.expr.type) {
            System.err.println("[ERRORE SEMANTICO] valore di ritorno della funzione " + symbolTable.symbolTableName + " errato atteso: [" + functionDef.valueType + "] assegnato: [" + returnNode.expr.type + "]");
            System.exit(1);
        }
    }

    // ExprNode e Costanti
    /* ---------------------------------------------------------- */
    @Override
    public void visit(ExprNode exprNode) {
        // Se è un'operazione con doppio argomento
        if (exprNode.val_One != null && exprNode.val_Two != null) {
            // Controllo il tipo delle due espressioni
            ((ExprNode) exprNode.val_One).accept(this);
            ((ExprNode) exprNode.val_Two).accept(this);

            // DEBUG
            if (debugVar)
                System.out.println("[DEBUG] " + (((ExprNode) exprNode.val_One).type) + " " + exprNode.op + " " + (((ExprNode) exprNode.val_Two).type));

            // Se è un operazione aritmetica
            if (exprNode.op.equalsIgnoreCase("PLUS") || exprNode.op.equalsIgnoreCase("MINUS") || exprNode.op.equalsIgnoreCase("TIMES") || exprNode.op.equalsIgnoreCase("DIV") || exprNode.op.equalsIgnoreCase("POW")) {
                // Controllo che i tipi delle due espressioni siano compatibili per questa operazione
                ValueType resultType = getType_Operations(((ExprNode) exprNode.val_One).type, ((ExprNode) exprNode.val_Two).type);
                if (resultType == null) {
                    System.err.println("[ERRORE SEMANTICO] tipi per op " + exprNode.op + " errati");
                    System.exit(1);
                } else
                    // Assegno il tipo risultante all'espressione
                    exprNode.type = resultType;

                // Se è una operazione di divisione per intero
            } else if (exprNode.op.equalsIgnoreCase("DIVINT")) {
                // Controllo che i tipi delle due espressioni siano compatibili per questa operazione
                ValueType resultType = getTypeDivInt(((ExprNode) exprNode.val_One).type, ((ExprNode) exprNode.val_Two).type);
                if (resultType == null) {
                    System.err.println("[ERRORE SEMANTICO] tipi per op " + exprNode.op + " errati");
                    System.exit(1);
                } else
                    // Assegno il tipo risultante all'espressione
                    exprNode.type = resultType;

                // Se è un AND o un OR
            } else if (exprNode.op.equalsIgnoreCase("AND") || exprNode.op.equalsIgnoreCase("OR")) {
                // Controllo che i tipi delle due espressioni siano compatibili per questa operazione
                ValueType resultType = getType_AndOr(((ExprNode) exprNode.val_One).type, ((ExprNode) exprNode.val_Two).type);
                if (resultType == null) {
                    System.err.println("[ERRORE SEMANTICO] tipi per op " + exprNode.op + " errati");
                    System.exit(1);
                } else
                    // Assegno il tipo risultante all'espressione
                    exprNode.type = resultType;

                // Se è una concatenzazione di stringhe
            } else if (exprNode.op.equalsIgnoreCase("STR_CONCAT")) {
                // Controllo che i tipi delle due espressioni siano compatibili per questa operazione
                ValueType resultType = getType_StrConc(((ExprNode) exprNode.val_One).type, ((ExprNode) exprNode.val_Two).type);
                if (resultType == null) {
                    System.err.println("[ERRORE SEMANTICO] tipi per op " + exprNode.op + " errati");
                    System.exit(1);
                } else
                    // Assegno il tipo risultante all'espressione
                    exprNode.type = resultType;

                // Se è una operazione EQ o NE (=, !=)
            } else if (exprNode.op.equalsIgnoreCase("EQ") || exprNode.op.equalsIgnoreCase("NE")) {
                // Controllo che i tipi delle due espressioni siano compatibili per questa operazione
                ValueType resultType = getTypeEQNE(((ExprNode) exprNode.val_One).type, ((ExprNode) exprNode.val_Two).type);
                if (resultType == null) {
                    System.err.println("[ERRORE SEMANTICO] tipi per op " + exprNode.op + " errati");
                    System.exit(1);
                } else
                    // Assegno il tipo risultante all'espressione
                    exprNode.type = resultType;

                // Se è una qualsiasi altra operazione a doppio argomento (<, <=, >, >=)
            } else {
                // Controllo che i tipi delle due espressioni siano compatibili per questa operazione
                ValueType resultType = getType_Boolean(((ExprNode) exprNode.val_One).type, ((ExprNode) exprNode.val_Two).type);
                if (resultType == null) {
                    System.err.println("[ERRORE SEMANTICO] tipi per op " + exprNode.op + " errati");
                    System.exit(1);
                } else
                    // Assegno il tipo risultante all'espressione
                    exprNode.type = resultType;
            }

            // Se è un'operazione a singolo argomento
        } else if (exprNode.val_One != null) {
            // Se è una costante intera
            if (exprNode.val_One instanceof LeafIntegerConst) {
                // Converto, controllo e salvo il tipo
                ((LeafIntegerConst) exprNode.val_One).accept(this);
                exprNode.type = ((LeafIntegerConst) exprNode.val_One).type;

                // Se è una costante reale
            } else if (exprNode.val_One instanceof LeafRealConst) {
                // Converto, controllo e salvo il tipo
                ((LeafRealConst) exprNode.val_One).accept(this);
                exprNode.type = ((LeafRealConst) exprNode.val_One).type;

                // Se è una costante stringa
            } else if (exprNode.val_One instanceof LeafStringConst) {
                // Converto, controllo e salvo il tipo
                ((LeafStringConst) exprNode.val_One).accept(this);
                exprNode.type = ((LeafStringConst) exprNode.val_One).type;

                // Se è una costante booleana
            } else if (exprNode.val_One instanceof LeafBool) {
                // Converto, controllo e salvo il tipo
                ((LeafBool) exprNode.val_One).accept(this);
                exprNode.type = ((LeafBool) exprNode.val_One).type;

                // Se è una leafID
            } else if (exprNode.val_One instanceof LeafID) {
                // Converto, controllo e salvo il tipo
                ((LeafID) exprNode.val_One).accept(this);
                exprNode.type = ((LeafID) exprNode.val_One).type;

                // Se è un'operazione di meno unario
            } else if (exprNode.op.equalsIgnoreCase("UMINUS")) {
                // Converto e controllo
                ((ExprNode) exprNode.val_One).accept(this);
                // Controllo che il tipo dell'argomento sia compatibile per questa operazione (Intero o Reale)
                if (((ExprNode) exprNode.val_One).type == ValueType.integer || ((ExprNode) exprNode.val_One).type == ValueType.real)
                    exprNode.type = (((ExprNode) exprNode.val_One).type);
                else {
                    System.err.println("[ERRORE SEMANTICO] tipo per op " + exprNode.op + " errato");
                    System.exit(1);
                }
                // DEBUG
                if (debugVar)
                    System.out.println("[DEBUG] " + exprNode.op + " " + (((ExprNode) exprNode.val_One).type));

                // Se è un operazione di not
            } else if (exprNode.op.equalsIgnoreCase("NOT")) {
                // Converto e controllo
                ((ExprNode) exprNode.val_One).accept(this);
                // Controllo che il tipo dell'argomento sia compatibile per questa operazione (Boolean)
                if (((ExprNode) exprNode.val_One).type == ValueType.bool)
                    exprNode.type = (((ExprNode) exprNode.val_One).type);
                else {
                    System.err.println("[ERRORE SEMANTICO] tipo per op " + exprNode.op + " errato");
                    System.exit(1);
                }
                if (debugVar)
                    System.out.println("[DEBUG] " + exprNode.op + " " + (((ExprNode) exprNode.val_One).type));

                // Se è una chiamata a funzione
            } else if (exprNode.val_One instanceof CallFunNode) {
                // Converto, controllo e salvo il tipo
                ((CallFunNode) exprNode.val_One).accept(this);
                exprNode.type = ((CallFunNode) exprNode.val_One).type;
            }
        }
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

    // Visite delle foglie
    /* ---------------------------------------------------------- */
    @Override
    public void visit(LeafID leafID) {
        // Prendo la tabella sul top dello stack
        SymbolTable symbolTable = stack.peek();
        SymbolTableEntry symbolTableEntry = symbolTable.containsEntry(leafID.value);

        // Controllo se la variabile è stata dichiarata e ne salvo il tipo
        if (symbolTableEntry != null)
            leafID.type = symbolTableEntry.valueType;
        else {
            System.err.println("[ERRORE SEMANTICO] variabile " + leafID.value + " non dichiarata nel T.E. " + stack.peek().symbolTableName);
            System.exit(1);
        }
    }

    @Override
    public void visit(LeafIntegerConst leafIntegerConst) {
        // Passo il mio tipo al padre
        leafIntegerConst.type = ValueType.integer;
    }

    @Override
    public void visit(LeafBool leafBool) {
        // Passo il mio tipo al padre
        leafBool.type = ValueType.bool;
    }

    @Override
    public void visit(LeafRealConst leafRealConst) {
        // Passo il mio tipo al padre
        leafRealConst.type = ValueType.real;
    }

    @Override
    public void visit(LeafStringConst leafStringConst) {
        // Passo il mio tipo al padre
        leafStringConst.type = ValueType.string;
    }

    // Metodi per il Type Checking
    /* ---------------------------------------------------------- */
    /* ---------------------------------------------------------- */
    public static boolean checkAssignmentType(ValueType variable, ValueType assign) {
        // Controlli sulle operazioni di assegnamento
        if (variable == ValueType.integer && assign == ValueType.integer)
            return true;
        if (variable == ValueType.real && assign == ValueType.real)
            return true;
        if (variable == ValueType.integer && assign == ValueType.real)
            return true;
        if (variable == ValueType.real && assign == ValueType.integer)
            return true;
        if (variable == ValueType.bool && assign == ValueType.bool)
            return true;
        if (variable == ValueType.string && assign == ValueType.string)
            return true;
        else
            return false;
    }

    public static ValueType getType_Operations(ValueType type1, ValueType type2) {
        // Controlli sulle operazioni
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
        // Controlli sulle operazioni di concatenzazione
        if (type1 == ValueType.string && type2 == ValueType.string)
            return ValueType.string;
        if (type1 == ValueType.string && type2 == ValueType.integer)
            return ValueType.string;
        if (type1 == ValueType.string && type2 == ValueType.real)
            return ValueType.string;
        if (type1 == ValueType.string && type2 == ValueType.bool)
            return ValueType.string;
        return null;
    }

    public static ValueType getType_AndOr(ValueType type1, ValueType type2) {
        // Controlli sulle operazioni di AND e OR
        if (type1 == ValueType.bool && type2 == ValueType.bool)
            return ValueType.bool;
        return null;
    }

    public static ValueType getTypeEQNE(ValueType type1, ValueType type2) {
        // Controlli sulle operazioni di EQ e NE
        if (type1 == ValueType.integer && type2 == ValueType.integer)
            return ValueType.bool;
        if (type1 == ValueType.real && type2 == ValueType.real)
            return ValueType.bool;
        if (type1 == ValueType.integer && type2 == ValueType.real)
            return ValueType.bool;
        if (type1 == ValueType.real && type2 == ValueType.integer)
            return ValueType.bool;
        if (type1 == ValueType.string && type2 == ValueType.string)
            return ValueType.bool;
        if (type1 == ValueType.bool && type2 == ValueType.bool)
            return ValueType.bool;
        return null;
    }

    public static ValueType getType_Boolean(ValueType type1, ValueType type2) {
        // Controlli sulle operazioni booleane
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

    public static ValueType getTypeDivInt(ValueType type1, ValueType type2) {
        // Controlli sulle operazioni di DIVINT
        if (type1 == ValueType.integer && type2 == ValueType.integer)
            return ValueType.integer;
        return null;
    }
}
