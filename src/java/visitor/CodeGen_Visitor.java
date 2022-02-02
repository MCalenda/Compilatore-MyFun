package visitor;

import java.io.*;
import java.util.ArrayList;

import symbol_table.ValueType;
import tree.leaves.*;
import tree.nodes.*;

public class CodeGen_Visitor implements CodeGen_Int_Visitor {
    // Attributi
    private PrintWriter wr;

    private String lastID = null;
    private int concatParamId = 0;

    private Boolean isConcat = false;
    private Boolean isInFunCall = false;

    private ArrayList<String> isOutParam = null;
    private ArrayList<String> tempList = null;
    private ArrayList<String> concatParam = null;

    // Costruttore
    public CodeGen_Visitor(String name) throws IOException {
        this.isOutParam = new ArrayList<String>();
        this.tempList = new ArrayList<String>();
        this.concatParam = new ArrayList<String>();

        File file = new File("src/test_files/C_Code/" + name.substring(0, name.length() - 6).split("/")[2] + ".c");
        if (!file.exists()) {
            file.createNewFile();
            System.out.print("File " + file.getName() + " creato !!!");
        } else {
            file.delete();
            file.createNewFile();
            System.out.print("File " + file.getName() + " creato !!!");
        }

        wr = new PrintWriter(file);
    }

    @Override
    public int visit(ProgramNode programNode) {
        wr.println("#include <stdio.h>");
        wr.println("#include <stdlib.h>");
        wr.println("#include <stdbool.h>");
        wr.println("#include <string.h>");
        wr.println("#include <math.h>");

        // Funzioni per la conversione di tipo
        wr.println("\n// Funzioni di conversione");
        wr.print("char *int_to_string(int x) { ");
        wr.print("char *str = malloc(512 * sizeof(char)); ");
        wr.print("sprintf(str, \"%d\", x); ");
        wr.print("return str; } ");

        wr.print("char *double_to_string(double x) { ");
        wr.print("char *str = malloc(512 * sizeof(char)); ");
        wr.print("sprintf(str, \"%f\", x); ");
        wr.print("return str; } ");

        if (programNode.varDecList.size() != 0) {
            wr.println("\n// Dichiarazione delle variabili locali");
            for (VarDeclNode varDeclNode : programNode.varDecList) {
                varDeclNode.accept(this);
            }
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
                wr.print(convert_type(varDeclNode.type) + " ");
                idInitNode.accept(this);
            }
        } else if (varDeclNode.IdListInitObbl != null) {
            for (IdInitObblNode idInitObblNode : varDeclNode.IdListInitObbl) {
                idInitObblNode.accept(this);
            }
        }
    }

    @Override
    public void visit(IdInitObblNode idInitObblNode) {
        wr.print(convert_type(idInitObblNode.type) + " ");

        if (idInitObblNode.type == ValueType.string) {
            wr.print("*");
            idInitObblNode.leafID.accept(this);
            wr.print("= malloc(512 * sizeof(char));");
        } else {
            idInitObblNode.leafID.accept(this);
            wr.print(" = ");
        }

        if (idInitObblNode.type == ValueType.string) {
            wr.print("strcpy(");
            idInitObblNode.leafID.accept(this);
            wr.print(", ");
            idInitObblNode.value.accept(this);
            wr.print(");");
        } else {
            idInitObblNode.value.accept(this);
            wr.print(";");
        }
    }

    @Override
    public void visit(FunNode funNode) {
        if (funNode.type != null) {
            wr.print(convert_type(funNode.type) + " ");
            if (funNode.type == ValueType.string) {
                wr.print(" *");
            }
        } else {
            wr.print("void ");
        }

        funNode.leafID.accept(this);
        wr.print("(");

        if (funNode.paramDecList.size() != 0) {
            ParamDecNode lastParDeclNode = funNode.paramDecList.get(funNode.paramDecList.size() - 1);
            for (ParamDecNode paramDecNode : funNode.paramDecList) {
                paramDecNode.accept(this);

                if (lastParDeclNode != paramDecNode)
                    wr.print(", ");
            }
        }

        wr.print(") {");

        if (funNode.varDecList != null) {
            for (VarDeclNode varDeclNode : funNode.varDecList) {
                wr.print("\t");
                varDeclNode.accept(this);
            }
        }

        if (funNode.statList != null) {
            for (StatNode statNode : funNode.statList) {
                wr.print("\t");
                statNode.accept(this);
            }
        }

        wr.print("}");

        this.isOutParam = new ArrayList<String>();
    }

    @Override
    public void visit(ParamDecNode paramDecNode) {
        wr.print(convert_type(paramDecNode.type) + " ");

        if (paramDecNode.out && paramDecNode.type != ValueType.string) {
            this.isOutParam.add(paramDecNode.leafID.value);
        } else {
            if (paramDecNode.type == ValueType.string) {
                wr.print("*");
            }
        }

        paramDecNode.leafID.accept(this);
    }

    @Override
    public void visit(StatNode statNode) {
        // Nel caso in cui sia un if
        if (statNode.ifStatNode != null) {
            if (statNode.ifStatNode.expr.op.equalsIgnoreCase("CALLFUN")) {
                this.isInFunCall = true;
                this.concact_for_callfun(((CallFunNode) statNode.ifStatNode.expr.val_One).exprList);
            }
            statNode.ifStatNode.accept(this);
            this.isInFunCall = false;
        }

        // Nel caso in cui sia un while
        if (statNode.whileStatNode != null) {
            if (statNode.whileStatNode.expr.op.equalsIgnoreCase("CALLFUN")) {
                this.isInFunCall = true;
                this.concact_for_callfun(((CallFunNode) statNode.whileStatNode.expr.val_One).exprList);
            }
            statNode.whileStatNode.accept(this);
            this.isInFunCall = false;
        }

        // Nel caso in cui sia una read
        if (statNode.readStatNode != null) {
            // Nel caso in cui sia una read con messaggio
            if (statNode.readStatNode.expr != null) {
                if (statNode.readStatNode.expr.op.equalsIgnoreCase("CALLFUN")) {
                    this.isInFunCall = true;
                    this.concact_for_callfun(((CallFunNode) statNode.readStatNode.expr.val_One).exprList);
                }
            }
            statNode.readStatNode.accept(this);
            this.isInFunCall = false;
        }

        // Nel caso in cui sia una chiamata a una stampa
        if (statNode.writeStatNode != null) {
            if (statNode.writeStatNode.expr.op.equalsIgnoreCase("CALLFUN")) {
                this.isInFunCall = true;
                this.concact_for_callfun(((CallFunNode) statNode.writeStatNode.expr.val_One).exprList);
            }
            statNode.writeStatNode.accept(this);
            this.isInFunCall = false;
        }

        // Nel caso in cui sia un assegnamento
        if (statNode.assignStatNode != null) {
            if (statNode.assignStatNode.expr.op.equalsIgnoreCase("CALLFUN")) {
                this.isInFunCall = true;
                this.concact_for_callfun(((CallFunNode) statNode.assignStatNode.expr.val_One).exprList);
            }
            statNode.assignStatNode.accept(this);
            this.isInFunCall = false;
        }

        // Nel caso in cui sia una chiamata di funzione
        if (statNode.callFunNode != null) {
            this.concact_for_callfun(statNode.callFunNode.exprList);
            statNode.callFunNode.accept(this);
        }

        // Nel caso in cui sia un statement di return
        if (statNode.returnNode != null)
            statNode.returnNode.accept(this);
    }

    @Override
    public void visit(IfStatNode ifStatNode) {
        wr.print("if (");

        ifStatNode.expr.accept(this);

        wr.print(") {");

        for (VarDeclNode varDeclNode : ifStatNode.varDeclList) {
            varDeclNode.accept(this);
        }

        for (StatNode statNode : ifStatNode.statList) {
            statNode.accept(this);
        }

        wr.print("}");

        if (ifStatNode.elseNode != null) {
            ifStatNode.elseNode.accept(this);
        }

    }

    @Override
    public void visit(WhileStatNode whileStatNode) {
        wr.print("while (");

        whileStatNode.expr.accept(this);

        wr.print(") {");

        for (VarDeclNode varDeclNode : whileStatNode.varDeclList) {
            varDeclNode.accept(this);
        }

        for (StatNode statNode : whileStatNode.statList) {
            statNode.accept(this);
        }

        wr.print("}");
    }

    @Override
    public void visit(ReadStatNode readStatNode) {
        if (readStatNode.expr != null) {
            wr.print("printf(");
            readStatNode.expr.accept(this);
            wr.print(");");
        }

        for (LeafID leafID : readStatNode.IdList) {
            // Dal tipo dell'espressione carpisco il tipo di valore da stampare
            switch (leafID.type) {
            case integer, bool:
                wr.print("scanf(\"%d\", &");
                break;
            case string:
                wr.print("scanf(\"%s\", ");
                break;
            case real:
                wr.print("scanf(\"%f\", &");
                break;
            }
            leafID.accept(this);
            wr.print(");");
        }

    }

    @Override
    public void visit(WriteStatNode writeStatNode) {
        if (writeStatNode.expr.op.equalsIgnoreCase("STR_CONCAT")) {
            wr.print("char *");
            wr.print(this.create_temp());
            wr.print("= malloc(512 * sizeof(char));");

            wr.print("strcpy(");
            wr.print(this.tempList.get(this.get_current_temp()));
            wr.print(", \"\");");

            this.lastID = this.tempList.get(this.get_current_temp());
            this.isConcat = true;
            writeStatNode.expr.accept(this);
            this.isConcat = false;
        }

        // Dal tipo dell'espressione carpisco il tipo di valore da stampare
        switch (writeStatNode.expr.type) {
        case integer, bool:
            wr.print("printf(\"%d\", ");
            break;
        case string:
            wr.print("printf(\"%s\", ");
            break;
        case real:
            wr.print("printf(\"%f\", ");
            break;
        }

        if (!writeStatNode.expr.op.equalsIgnoreCase("STR_CONCAT")) {
            // Accetto l'espressione che viene stampata
            writeStatNode.expr.accept(this);
        } else {
            wr.print(this.lastID);
        }
        wr.print("); ");

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
        if (!assignStatNode.expr.op.equalsIgnoreCase("STR_CONCAT")) {
            assignStatNode.leafID.accept(this);
            wr.print(" = ");
            assignStatNode.expr.accept(this);
            wr.print(";");
        } else {
            this.lastID = assignStatNode.leafID.value;
            this.isConcat = true;
            assignStatNode.expr.accept(this);
            this.isConcat = false;
        }
    }

    @Override
    public void visit(CallFunNode callFunNode) {
        boolean itsWasConcat = false;
        boolean conversion = false;

        // La stai chiamando in una concatenenazione
        if (this.isConcat) {
            wr.print("strcat(" + this.lastID + ", ");
            this.isConcat = false;
            itsWasConcat = true;
            switch (callFunNode.type) {
            case integer, bool:
                conversion = true;
                wr.print("int_to_string(");
                callFunNode.leafID.accept(this);
                break;
            case real:
                conversion = true;
                wr.print("double_to_string(");
                callFunNode.leafID.accept(this);
                break;
            case string:
                callFunNode.leafID.accept(this);
            }
        } else {
            callFunNode.leafID.accept(this);
        }

        wr.print("(");
        if (callFunNode.exprList.size() != 0) {
            ExprNode lastExprNode = callFunNode.exprList.get(callFunNode.exprList.size() - 1);
            for (ExprNode exprNode : callFunNode.exprList) {
                if (exprNode.op.equalsIgnoreCase("STR_CONCAT")) {
                    wr.print(this.concatParam.get(this.concatParamId));
                    this.concatParamId++;
                } else {
                    if (exprNode.op.equalsIgnoreCase("OUTPAR") && exprNode.type != ValueType.string)
                        wr.print("&");

                    exprNode.accept(this);
                }
                if (lastExprNode != exprNode)
                    wr.print(", ");
            }
        }

        if (itsWasConcat) {
            wr.print(") ");
            itsWasConcat = false;
        }

        if (conversion) {
            wr.print(") ");
            conversion = false;
        }

        // BUG DA RISOLVERE
        if (this.isInFunCall) {
            wr.print(")");
        } else {
            wr.print(");");
        }
    }

    @Override
    public void visit(ReturnNode returnNode) {
        if (!returnNode.expr.op.equalsIgnoreCase("STR_CONCAT")) {
            wr.print("return ");
            this.isInFunCall = true;
            returnNode.expr.accept(this);
            wr.print("; ");
            this.isInFunCall = false;
        } else {
            wr.print("char *");
            wr.print(this.create_temp());
            wr.print("= malloc(512 * sizeof(char));");

            wr.print("strcpy(");
            wr.print(this.tempList.get(this.get_current_temp()));
            wr.print(", \"\");");

            this.lastID = this.tempList.get(this.get_current_temp());
            this.isConcat = true;
            returnNode.expr.accept(this);
            this.isConcat = false;

            wr.print("return ");
            wr.print(this.tempList.get(this.get_current_temp()));
            wr.print("; ");
        }
    }

    @Override
    public void visit(MainNode mainNode) {
        wr.print("int main() {");
        for (VarDeclNode varDeclNode : mainNode.varDeclList) {
            varDeclNode.accept(this);
        }
        for (StatNode statNode : mainNode.statList) {
            statNode.accept(this);
        }
        wr.print("return 0; ");
        wr.print("}");
    }

    @Override
    public void visit(IdInitNode idInitNode) {
        // Se sto inizializzando una stringa
        if (idInitNode.type == ValueType.string) {
            wr.print("*");
            idInitNode.leafID.accept(this);
            wr.print("= malloc(512 * sizeof(char));");

            if (idInitNode.exprNode != null) {
                if (!idInitNode.exprNode.op.equals("STR_CONCAT")) {
                    wr.print("strcpy(");
                    idInitNode.leafID.accept(this);
                    wr.print(", ");
                    idInitNode.exprNode.accept(this);
                    wr.print("); ");
                } else {
                    this.lastID = idInitNode.leafID.value;
                    wr.print("strcpy(");
                    idInitNode.leafID.accept(this);
                    wr.print(", \"\");");
                    this.isConcat = true;
                    idInitNode.exprNode.accept(this);
                    this.isConcat = false;
                }
            } else {
                wr.print("strcpy(");
                idInitNode.leafID.accept(this);
                wr.print(", \"\");");
            }

        } else {
            // Se sto inizializzando qualsiasi altra cosa
            idInitNode.leafID.accept(this);

            if (idInitNode.exprNode != null) {
                wr.print(" = ");

                // Conservo l'ultimo ID
                this.lastID = idInitNode.leafID.value;

                idInitNode.exprNode.accept(this);

                if (!idInitNode.exprNode.op.equalsIgnoreCase("CALLFUN")) {
                    wr.print("; ");
                }
            } else {
                wr.print("; ");
            }
        }
    }

    @Override
    public void visit(LeafID leafID) {
        if (this.isOutParam.contains(leafID.value)) {
            wr.print("*");
        }

        if (this.isConcat) {
            wr.print("strcat(" + this.lastID + ", ");
            switch (leafID.type) {
            case integer, bool:
                wr.print("int_to_string(");
                wr.print(leafID.value);
                wr.print(")");
                break;
            case real:
                wr.print("double_to_string(");
                wr.print(leafID.value);
                wr.print(")");
                break;
            case string:
                wr.print(leafID.value);
                break;
            }
            wr.print("); ");
        } else {
            wr.print(leafID.value);
        }
    }

    @Override
    public void visit(ExprNode exprNode) {
        if (exprNode.val_One != null && exprNode.val_Two != null) {
            switch (exprNode.op) {
            case "PLUS":
                if (!this.isConcat) {
                    ((ExprNode) exprNode.val_One).accept(this);
                    wr.print(" + ");
                    ((ExprNode) exprNode.val_Two).accept(this);
                } else {
                    this.concact_convertion(exprNode, "+");
                }
                break;
            case "MINUS":
                if (!this.isConcat) {
                    ((ExprNode) exprNode.val_One).accept(this);
                    wr.print(" - ");
                    ((ExprNode) exprNode.val_Two).accept(this);
                } else {
                    this.concact_convertion(exprNode, "-");
                }
                break;
            case "TIMES":
                if (!this.isConcat) {
                    ((ExprNode) exprNode.val_One).accept(this);
                    wr.print(" * ");
                    ((ExprNode) exprNode.val_Two).accept(this);
                } else {
                    this.concact_convertion(exprNode, "*");
                }
                break;
            case "DIV":
                if (!this.isConcat) {
                    ((ExprNode) exprNode.val_One).accept(this);
                    wr.print(" / ");
                    ((ExprNode) exprNode.val_Two).accept(this);
                } else {
                    this.concact_convertion(exprNode, "/");
                }
                break;
            case "DIVINT":
                if (!this.isConcat) {
                    ((ExprNode) exprNode.val_One).accept(this);
                    wr.print(" / ");
                    ((ExprNode) exprNode.val_Two).accept(this);
                } else {
                    this.concact_convertion(exprNode, "/");
                }
                break;
            case "AND":
                ((ExprNode) exprNode.val_One).accept(this);
                wr.print(" && ");
                ((ExprNode) exprNode.val_Two).accept(this);
                break;
            case "POW":
                if (this.isConcat) {
                    wr.print("strcat(");
                    wr.print(this.lastID);
                    wr.print(", ");

                    this.isConcat = false;
                    wr.print("double_to_string(");
                    wr.print(" pow(");
                    ((ExprNode) exprNode.val_One).accept(this);
                    wr.print(", ");
                    ((ExprNode) exprNode.val_Two).accept(this);
                    wr.print(")");
                    this.isConcat = true;

                    wr.print("));");
                } else {
                    wr.print(" pow(");
                    ((ExprNode) exprNode.val_One).accept(this);
                    wr.print(", ");
                    ((ExprNode) exprNode.val_Two).accept(this);
                    wr.print(")");
                }
                break;
            case "STR_CONCAT":
                ((ExprNode) exprNode.val_One).accept(this);
                ((ExprNode) exprNode.val_Two).accept(this);
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
                if (((ExprNode) exprNode.val_One).type == ValueType.string) {
                    wr.print("strcmp(");
                    ((ExprNode) exprNode.val_One).accept(this);
                    wr.print(", ");
                    ((ExprNode) exprNode.val_Two).accept(this);
                    wr.print(") == 0");
                } else if (((ExprNode) exprNode.val_Two).type == ValueType.string) {
                    wr.print("strcmp(");
                    ((ExprNode) exprNode.val_One).accept(this);
                    wr.print(", ");
                    ((ExprNode) exprNode.val_Two).accept(this);
                    wr.print(") == 0");
                } else if (exprNode.val_One instanceof LeafStringConst || exprNode.val_Two instanceof LeafStringConst) {
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
                ((ExprNode) exprNode.val_One).accept(this);
                wr.print(" != ");
                ((ExprNode) exprNode.val_Two).accept(this);
                break;
            }
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
    public void visit(LeafIntegerConst leafIntegerConst) {
        if (this.isConcat) {
            wr.print("strcat(" + this.lastID + ", ");
            wr.print("\"" + leafIntegerConst.value.toString() + "\"");
            wr.print("); ");
        } else {
            wr.print(leafIntegerConst.value.toString());
        }
    }

    @Override
    public void visit(LeafBool leafBool) {
        if (this.isConcat) {
            wr.print("strcat(" + this.lastID + ", ");
            wr.print("\"" + leafBool.value.toString() + "\"");
            wr.print("); ");
        } else {
            wr.print(leafBool.value.toString());
        }
    }

    @Override
    public void visit(LeafRealConst leafRealConst) {
        if (this.isConcat) {
            wr.print("strcat(" + this.lastID + ", ");
            wr.print("\"" + leafRealConst.value.toString() + "\"");
            wr.print("); ");
        } else {
            wr.print(leafRealConst.value.toString());
        }
    }

    @Override
    public void visit(LeafStringConst leafStringConst) {
        if (leafStringConst.value.length() != 0) {
            if (this.isConcat) {
                wr.print("strcat(" + this.lastID + ", ");
                wr.print("\"" + leafStringConst.value + "\"");
                wr.print("); ");
            } else {
                wr.print("\"" + leafStringConst.value + "\"");
            }
        } else
            wr.print("\"\"");
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

    @Override
    public void visit(ElseNode elseNode) {
        wr.print("else {");

        for (VarDeclNode varDeclNode : elseNode.varDeclList) {
            varDeclNode.accept(this);
        }

        for (StatNode statNode : elseNode.statList) {
            statNode.accept(this);
        }
        wr.print("} ");
    }

    private String convert_type(ValueType type) {
        switch (type) {
        case integer:
            return "int";
        case string:
            return "char";
        case real:
            return "float";
        case bool:
            return "bool";
        }
        return "null";
    }

    private String create_temp() {
        this.lastID = "temp_" + this.tempList.size();
        this.tempList.add("temp_" + this.tempList.size());

        return this.tempList.get(this.tempList.size() - 1);
    }

    private Integer get_current_temp() {
        return this.tempList.size() - 1;
    }

    private void concact_for_callfun(ArrayList<ExprNode> exprList) {
        for (ExprNode exprNode : exprList) {
            if (exprNode.op.equalsIgnoreCase("STR_CONCAT")) {
                wr.print("char *");
                wr.print(this.create_temp());
                wr.print("= malloc(512 * sizeof(char));");

                wr.print("strcpy(");
                wr.print(this.tempList.get(this.get_current_temp()));
                wr.print(", \"\");");

                this.concatParam.add(this.tempList.get(this.get_current_temp()));
                this.lastID = this.tempList.get(this.get_current_temp());
                this.isConcat = true;
                exprNode.accept(this);
                this.isConcat = false;
            }
        }
    }

    private void concact_convertion(ExprNode expr, String op) {
        wr.print("strcat(");
        wr.print(this.lastID);
        wr.print(", ");
        switch (expr.type) {
        case integer:
            wr.print("int_to_string(");
            this.isConcat = false;
            ((ExprNode) expr.val_One).accept(this);
            wr.print(" " + op + " ");
            ((ExprNode) expr.val_Two).accept(this);
            this.isConcat = true;
            wr.print(")");
            break;
        case real:
            wr.print("double_to_string(");
            this.isConcat = false;
            ((ExprNode) expr.val_One).accept(this);
            wr.print(" " + op + " ");
            ((ExprNode) expr.val_Two).accept(this);
            this.isConcat = true;
            wr.print(")");
            break;
        default:
            break;
        }
        wr.print(");");
    }
}
