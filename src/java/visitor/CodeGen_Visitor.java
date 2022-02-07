package visitor;

import java.io.*;
import java.util.ArrayList;

import symbol_table.ValueType;
import tree.leaves.*;
import tree.nodes.*;

public class CodeGen_Visitor implements CodeGen_Int_Visitor {
    // Scrive all'interno del file finale
    private final PrintWriter wr;
    // Lista di parametri dichiarato con (OUT/@)
    private ArrayList<String> isOutParam = null;

    // Nel caso sia una dichiarazione globale
    private boolean isDeclVar = false;

    // Costruttore
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public CodeGen_Visitor(String name) throws IOException {
        File file = new File("src/test_files/c_out/" + name.substring(0, name.length() - 4).split("/")[2] + ".c");
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        System.out.print("File " + file.getName() + " creato nella cartella \"src/test_files/c_out\" !!!");

        wr = new PrintWriter(file);
    }

    // Metodi polimorfi per l'implementazione del visitor
    /* ---------------------------------------------------------- */
    /* ---------------------------------------------------------- */
    @Override
    public int visit(ProgramNode programNode) {
        wr.println("#include <stdio.h>");
        wr.println("#include <stdlib.h>");
        wr.println("#include <stdbool.h>");
        wr.println("#include <string.h>");
        wr.println("#include <math.h>");

        // Funzioni per la concatenazione
        wr.println("\n// Funzioni di concatenazione");
        wr.print("char *concatInt(char *string, int toConcat) {\n");
        wr.print("int length = snprintf(NULL, 0,\"%d\", toConcat);\n");
        wr.print("char *converted = (char *) malloc(length + 1);\n");
        wr.print("sprintf(converted, \"%d\", toConcat);\n");
        wr.print("char *concat = (char *) malloc(1 + strlen(string)+ strlen(converted));\n");
        wr.print("strcpy(concat, string);\n");
        wr.print("strcat(concat, converted);\n");
        wr.print("return concat;\n}\n");

        wr.print("char *concatReal(char *string, float toConcat) {\n");
        wr.print("int length = snprintf(NULL, 0,\"%f\", toConcat);\n");
        wr.print("char *converted = (char *) malloc(length + 1);\n");
        wr.print("sprintf(converted, \"%f\", toConcat);\n");
        wr.print("char *concat = (char *) malloc(1 + strlen(string)+ strlen(converted));\n");
        wr.print("strcpy(concat, string);\n");
        wr.print("strcat(concat, converted);\n");
        wr.print("return concat;\n}\n");

        wr.print("char *concatBool(char *string, int toConcat) {\n");
        wr.print("char *converted = (char *) malloc(6);\n");
        wr.print("if(toConcat == 1) strcpy(converted, \"true\");\n");
        wr.print("else strcpy(converted, \"false\");\n");
        wr.print("char *concat = (char *) malloc(1 + strlen(string)+ strlen(converted));\n");
        wr.print("strcpy(concat, string);\n");
        wr.print("strcat(concat, converted);\n");
        wr.print("return concat;\n}\n");

        wr.print("char *concatString(char *string, char *toConcat) {\n");
        wr.print("char *concat = (char *) malloc(1 + strlen(string)+ strlen(toConcat));\n");
        wr.print("strcpy(concat, string);\n");
        wr.print("strcat(concat, toConcat);\n");
        wr.print("return concat;\n}\n");

        // Corpo del ProgramNode
        if (programNode.varDecList.size() != 0) {
            // È una dichiarazione globale
            this.isDeclVar = true;
            wr.println("\n\n// Dichiarazione delle variabili globali");
            for (VarDeclNode varDeclNode : programNode.varDecList) {
                varDeclNode.accept(this);
            }
            this.isDeclVar = false;
        }

        if (programNode.funList.size() != 0) {
            wr.println("\n\n// Dichiarazione delle funzioni");
            for (FunNode funNode : programNode.funList) {
                funNode.accept(this);
            }
        }

        wr.println("\n\n// Funzione Main");
        programNode.main.accept(this);

        wr.close();
        return 0;
    }

    @Override
    public void visit(VarDeclNode varDeclNode) {
        if (varDeclNode.idInitList != null) {
            for (IdInitNode idInitNode : varDeclNode.idInitList) {
                // Stampa il tipo della dichiarazione
                wr.print(convert_type(varDeclNode.type) + " ");
                idInitNode.accept(this);
            }
        } else if (varDeclNode.IdListInitObbl != null) {
            for (IdInitObblNode idInitObblNode : varDeclNode.IdListInitObbl) {
                // Stampa il tipo della dichiarazione
                wr.print(convert_type(idInitObblNode.type) + " ");
                idInitObblNode.accept(this);
            }
        }
    }

    @Override
    public void visit(IdInitNode idInitNode) {
        // Se è una dichiarazione globale
        if (this.isDeclVar) {
            idInitNode.leafID.accept(this);
            if (idInitNode.type == ValueType.string)
                wr.print("[512]");

            // Se sto effettuando un inizializzazione
            if (idInitNode.exprNode != null) {
                if (idInitNode.type == ValueType.string) {
                    // Inizializzazione della variabile stringa
                    wr.print(" = ");
                    idInitNode.exprNode.accept(this);

                } else {
                    // Inizializzazione di un tipo non stringa
                    wr.print(" = ");
                    idInitNode.exprNode.accept(this);
                }
            }
        } else {
            // Se sto dichiarando una stringa
            if (idInitNode.type == ValueType.string)
                wr.print("*");

            idInitNode.leafID.accept(this);
            if (idInitNode.type == ValueType.string)
                wr.print(" = malloc(512 * sizeof(char))");

            // Se sto effettuando un inizializzazione
            if (idInitNode.exprNode != null) {
                if (idInitNode.type == ValueType.string) {
                    // Inizializzazione della variabile stringa
                    wr.print(";\n");
                    wr.print("strcpy(");
                    idInitNode.leafID.accept(this);
                    wr.print(", ");
                    idInitNode.exprNode.accept(this);
                    wr.print(")");
                } else {
                    // Inizializzazione di un tipo non stringa
                    wr.print(" = ");
                    idInitNode.exprNode.accept(this);
                }
            }
        }

        wr.print(";\n");
    }

    @Override
    public void visit(IdInitObblNode idInitObblNode) {
        // Se sto dichiarando una stringa
        if (idInitObblNode.type == ValueType.string)
            wr.print("*");

        idInitObblNode.leafID.accept(this);

        // In questo nodo le inizializzazioni sono obbligatorie
        if (idInitObblNode.type == ValueType.string) {
            // Inizializzazione della variabile stringa
            wr.print(" = malloc(512 * sizeof(char));\n");
            wr.print("strcpy(");
            idInitObblNode.leafID.accept(this);
            wr.print(", ");
            idInitObblNode.value.accept(this);
            wr.print(");\n");
        } else {
            // Inizializzazione di un tipo non stringa
            wr.print(" = ");
            idInitObblNode.value.accept(this);
            wr.print(";\n");
        }
    }

    @Override
    public void visit(FunNode funNode) {
        // Lista dei parametri (OUT/@)
        this.isOutParam = new ArrayList<>();

        // Se la funzione ha un tipo di ritorno
        if (funNode.type != null) {
            wr.print(convert_type(funNode.type) + " ");

            // Se questo tipo di ritorno è una stringa
            if (funNode.type == ValueType.string)
                wr.print("*");
        } else
            wr.print("void ");

        funNode.leafID.accept(this);
        wr.print("(");

        // Se la funzione ha dei parametri
        if (funNode.paramDecList.size() != 0) {
            for (int i = 0; i < funNode.paramDecList.size(); i++) {
                ParamDecNode paramDecNode = funNode.paramDecList.get(i);
                paramDecNode.accept(this);
                if (i != funNode.paramDecList.size() - 1)
                    wr.print(", ");
            }
        }
        wr.print(") {\n");

        // Gestione delle Dichiarazioni e Statement
        if (funNode.varDecList != null) {
            for (VarDeclNode varDeclNode : funNode.varDecList) {
                varDeclNode.accept(this);
            }
        }
        if (funNode.statList != null) {
            for (StatNode statNode : funNode.statList) {
                statNode.accept(this);
            }
        }
        wr.print("\n}\n");

        this.isOutParam = new ArrayList<String>();
    }

    @Override
    public void visit(ParamDecNode paramDecNode) {
        // Stampa il tipo di parametro
        wr.print(convert_type(paramDecNode.type) + " ");

        // Se è di tipo out
        if (paramDecNode.out)
            this.isOutParam.add(paramDecNode.leafID.value);

        // Se è un tipo stringa
        if (paramDecNode.type == ValueType.string)
            wr.print("*");

        paramDecNode.leafID.accept(this);
    }

    @Override
    public void visit(MainNode mainNode) {
        wr.print("int main() {\n");

        for (VarDeclNode varDeclNode : mainNode.varDeclList) {
            varDeclNode.accept(this);
        }
        for (StatNode statNode : mainNode.statList) {
            statNode.accept(this);
        }

        wr.print("return 0; ");
        wr.print("\n}\n");
    }

    // Statement
    /* ---------------------------------------------------------- */
    @Override
    public void visit(StatNode statNode) {
        if (statNode.ifStatNode != null) {
            statNode.ifStatNode.accept(this);
        }

        if (statNode.whileStatNode != null) {
            statNode.whileStatNode.accept(this);
        }

        if (statNode.readStatNode != null) {
            statNode.readStatNode.accept(this);
        }

        if (statNode.writeStatNode != null) {
            statNode.writeStatNode.accept(this);
        }

        if (statNode.assignStatNode != null) {
            statNode.assignStatNode.accept(this);
        }

        if (statNode.callFunNode != null) {
            statNode.callFunNode.accept(this);
            wr.print(";\n");
        }

        if (statNode.returnNode != null) {
            statNode.returnNode.accept(this);
        }
    }

    @Override
    public void visit(IfStatNode ifStatNode) {
        wr.print("if(");
        ifStatNode.expr.accept(this);
        wr.print(") {\n");

        for (VarDeclNode varDeclNode : ifStatNode.varDeclList) {
            varDeclNode.accept(this);
        }
        for (StatNode statNode : ifStatNode.statList) {
            statNode.accept(this);
        }

        wr.print("\n}\n");

        if (ifStatNode.elseNode != null) {
            ifStatNode.elseNode.accept(this);
        }
    }

    @Override
    public void visit(ElseNode elseNode) {
        wr.print("else {\n");

        for (VarDeclNode varDeclNode : elseNode.varDeclList) {
            varDeclNode.accept(this);
        }
        for (StatNode statNode : elseNode.statList) {
            statNode.accept(this);
        }

        wr.print("} ");
    }

    @Override
    public void visit(WhileStatNode whileStatNode) {
        wr.print("while(");
        whileStatNode.expr.accept(this);
        wr.print(") {\n");

        for (VarDeclNode varDeclNode : whileStatNode.varDeclList) {
            varDeclNode.accept(this);
        }

        for (StatNode statNode : whileStatNode.statList) {
            statNode.accept(this);
        }

        wr.print("\n}\n");
    }

    @Override
    public void visit(ReadStatNode readStatNode) {
        if (readStatNode.expr != null) {
            wr.print("printf(\"%s\", ");
            readStatNode.expr.accept(this);
            wr.print(");\n");
        }

        // Per ogni ID nello statement di lettura
        for (LeafID leafID : readStatNode.IdList) {
            // Dal tipo dell'espressione carpisco il tipo di valore da stampare
            switch (leafID.type) {
            case integer, bool -> wr.print("scanf(\"%d\", &");
            case string -> wr.print("scanf(\"%s\", ");
            case real -> wr.print("scanf(\"%f\", &");
            }
            leafID.accept(this);
            wr.print(");\n");
        }
    }

    @Override
    public void visit(WriteStatNode writeStatNode) {
        // Dal tipo dell'espressione carpisco il tipo di valore da stampare
        switch (writeStatNode.expr.type) {
        case integer, bool -> wr.print("printf(\"%d\", ");
        case string -> wr.print("printf(\"%s\", ");
        case real -> wr.print("printf(\"%f\", ");
        }

        writeStatNode.expr.accept(this);
        wr.print(");\n");

        // Aggiungo eventuali extra post stampa
        switch (writeStatNode.op) {
        // La stampa ? (solo per chiarezza)
        case "WRITE":
            break;
        // La stampa ?.
        case "WRITELN":
            wr.print("printf(\"\\r\\n\"); ");
            break;
        // La stampa ?:
        case "WRITET":
            wr.print("printf(\"\\t\"); ");
            break;
        // La stampa ?,
        case "WRITEB":
            wr.print("printf(\" \"); ");
            break;
        }
    }

    @Override
    public void visit(AssignStatNode assignStatNode) {
        assignStatNode.leafID.accept(this);
        wr.print(" = ");
        assignStatNode.expr.accept(this);
        wr.print(";\n");
    }

    @Override
    public void visit(CallFunNode callFunNode) {
        callFunNode.leafID.accept(this);

        wr.print("(");
        // Se la chiamata ha dei parametri
        if (callFunNode.exprList.size() != 0) {
            for (int i = 0; i < callFunNode.exprList.size(); i++) {
                ExprNode exprNode = callFunNode.exprList.get(i);
                // Se il parametro è di tipo (OUT/@)
                if (exprNode.op.equalsIgnoreCase("OUTPAR"))
                    wr.print("&");
                exprNode.accept(this);
                if (i != callFunNode.exprList.size() - 1)
                    wr.print(", ");
            }
        }
        wr.print(")");
    }

    @Override
    public void visit(ReturnNode returnNode) {
        wr.print("return ");
        returnNode.expr.accept(this);
        wr.print(";\n");
    }

    // ExprNode e Costanti
    /* ---------------------------------------------------------- */
    @Override
    public void visit(ExprNode exprNode) {
        // Se si tratta di un espresisone a due valori
        if (exprNode.val_One != null && exprNode.val_Two != null) {
            switch (exprNode.op) {
            case "PLUS":
                ((ExprNode) exprNode.val_One).accept(this);
                wr.print(" + ");
                ((ExprNode) exprNode.val_Two).accept(this);
                break;

            case "MINUS":
                ((ExprNode) exprNode.val_One).accept(this);
                wr.print(" - ");
                ((ExprNode) exprNode.val_Two).accept(this);
                break;

            case "TIMES":
                ((ExprNode) exprNode.val_One).accept(this);
                wr.print(" * ");
                ((ExprNode) exprNode.val_Two).accept(this);
                break;

            case "DIV":
                ((ExprNode) exprNode.val_One).accept(this);
                wr.print(" / ");
                ((ExprNode) exprNode.val_Two).accept(this);
                break;

            case "DIVINT":
                ((ExprNode) exprNode.val_One).accept(this);
                wr.print(" / ");
                ((ExprNode) exprNode.val_Two).accept(this);
                break;

            case "AND":
                ((ExprNode) exprNode.val_One).accept(this);
                wr.print(" && ");
                ((ExprNode) exprNode.val_Two).accept(this);
                break;

            case "POW":
                wr.print("pow(");
                ((ExprNode) exprNode.val_One).accept(this);
                wr.print(", ");
                ((ExprNode) exprNode.val_Two).accept(this);
                wr.print(")");
                break;

            case "STR_CONCAT":
                // Se è una concatenazione di tipo Stringa & Intero
                if (((ExprNode) exprNode.val_Two).type == ValueType.integer) {
                    wr.print("concatInt( ");
                    ((ExprNode) exprNode.val_One).accept(this);
                    wr.print(", ");
                    ((ExprNode) exprNode.val_Two).accept(this);
                    wr.print(")");
                }
                // Se è una concatenazione di tipo Stringa & Reale
                if (((ExprNode) exprNode.val_Two).type == ValueType.real) {
                    wr.print("concatReal( ");
                    ((ExprNode) exprNode.val_One).accept(this);
                    wr.print(", ");
                    ((ExprNode) exprNode.val_Two).accept(this);
                    wr.print(")");
                }
                // Se è una concatenazione di tipo Stringa & Booleano
                if (((ExprNode) exprNode.val_Two).type == ValueType.bool) {
                    wr.print("concatBool( ");
                    ((ExprNode) exprNode.val_One).accept(this);
                    wr.print(", ");
                    ((ExprNode) exprNode.val_Two).accept(this);
                    wr.print(")");
                }
                // Se è una concatenazione di tipo Stringa & Stringa
                if (((ExprNode) exprNode.val_Two).type == ValueType.string) {
                    wr.print("concatString( ");
                    ((ExprNode) exprNode.val_One).accept(this);
                    wr.print(", ");
                    ((ExprNode) exprNode.val_Two).accept(this);
                    wr.print(")");
                }
                break;

            case "OR":
                ((ExprNode) exprNode.val_One).accept(this);
                wr.print(" || ");
                ((ExprNode) exprNode.val_Two).accept(this);
                break;

            case "GT":
                ((ExprNode) exprNode.val_One).accept(this);
                wr.print(" > ");
                ((ExprNode) exprNode.val_Two).accept(this);
                break;

            case "GE":
                ((ExprNode) exprNode.val_One).accept(this);
                wr.print(" >= ");
                ((ExprNode) exprNode.val_Two).accept(this);
                break;

            case "LT":
                ((ExprNode) exprNode.val_One).accept(this);
                wr.print(" < ");
                ((ExprNode) exprNode.val_Two).accept(this);
                break;

            case "LE":
                ((ExprNode) exprNode.val_One).accept(this);
                wr.print(" <= ");
                ((ExprNode) exprNode.val_Two).accept(this);
                break;

            case "EQ":
                // Se il primo valore è una stringa lo sono entrambi
                if (((ExprNode) exprNode.val_One).type == ValueType.string) {
                    wr.print("strcmp(");
                    ((ExprNode) exprNode.val_One).accept(this);
                    wr.print(", ");
                    ((ExprNode) exprNode.val_Two).accept(this);
                    wr.print(") == 0");
                } else {
                    ((ExprNode) exprNode.val_One).accept(this);
                    wr.print(" == ");
                    ((ExprNode) exprNode.val_Two).accept(this);
                }
                break;

            case "NE":
                // Se il primo valore è una stringa lo sono entrambi
                if (((ExprNode) exprNode.val_One).type == ValueType.string) {
                    wr.print("strcmp(");
                    ((ExprNode) exprNode.val_One).accept(this);
                    wr.print(", ");
                    ((ExprNode) exprNode.val_Two).accept(this);
                    wr.print(") != 0");
                } else {
                    ((ExprNode) exprNode.val_One).accept(this);
                    wr.print(" != ");
                    ((ExprNode) exprNode.val_Two).accept(this);
                }
                break;
            }
            // Se si tratta di un espresisone a singolo valore
        } else if (exprNode.val_One != null) {
            if (exprNode.val_One instanceof LeafIntegerConst) {
                ((LeafIntegerConst) exprNode.val_One).accept(this);
            } else if (exprNode.val_One instanceof LeafBool) {
                ((LeafBool) exprNode.val_One).accept(this);
            } else if (exprNode.val_One instanceof LeafID) {
                ((LeafID) exprNode.val_One).accept(this);
            } else if (exprNode.val_One instanceof LeafRealConst) {
                ((LeafRealConst) exprNode.val_One).accept(this);
            } else if (exprNode.val_One instanceof LeafStringConst) {
                ((LeafStringConst) exprNode.val_One).accept(this);
            } else if (exprNode.op.equalsIgnoreCase("UMINUS")) {
                wr.print("-");
                ((ExprNode) exprNode.val_One).accept(this);
            } else if (exprNode.op.equalsIgnoreCase("NOT")) {
                wr.print("!");
                ((ExprNode) exprNode.val_One).accept(this);
            } else if (exprNode.op.equalsIgnoreCase("CALLFUN")) {
                ((CallFunNode) exprNode.val_One).accept(this);
            } else if (exprNode.val_One instanceof ExprNode) {
                ((ExprNode) exprNode.val_One).accept(this);
            }
        }
    }

    @Override
    public void visit(ConstNode constNode) {
        if (constNode.value instanceof LeafIntegerConst) {
            ((LeafIntegerConst) constNode.value).accept(this);
        } else if (constNode.value instanceof LeafRealConst) {
            ((LeafRealConst) constNode.value).accept(this);
        } else if (constNode.value instanceof LeafBool) {
            ((LeafBool) constNode.value).accept(this);
        } else if (constNode.value instanceof LeafStringConst) {
            ((LeafStringConst) constNode.value).accept(this);
        }
    }

    // Visite delle foglie
    /* ---------------------------------------------------------- */
    @Override
    public void visit(LeafID leafID) {
        // Se è un ID di tipo (OUT/@) contenuto nella
        if (this.isOutParam != null)
            if (this.isOutParam.contains(leafID.value)) {
                wr.print("*");
            }

        wr.print(leafID.value);
    }

    @Override
    public void visit(LeafIntegerConst leafIntegerConst) {
        wr.print(leafIntegerConst.value.toString());
    }

    @Override
    public void visit(LeafBool leafBool) {
        wr.print(leafBool.value.toString());
    }

    @Override
    public void visit(LeafRealConst leafRealConst) {
        wr.print(leafRealConst.value.toString());
    }

    @Override
    public void visit(LeafStringConst leafStringConst) {
        // Se non è una stringa vuota
        if (leafStringConst.value.length() != 0)
            wr.print("\"" + leafStringConst.value + "\"");
        else
            wr.print("\"\"");
    }

    // Metodi di supporto all'implementazione del visitor
    /* ---------------------------------------------------------- */
    /* ---------------------------------------------------------- */
    private String convert_type(ValueType type) {
        return switch (type) {
        case integer -> "int";
        case string -> "char";
        case real -> "float";
        case bool -> "bool";
        };
    }
}
