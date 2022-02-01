package visitor;

import java.io.*;
import java.security.cert.TrustAnchor;
import java.util.ArrayList;

import org.apache.commons.lang3.ObjectUtils.Null;

import java_cup.internal_error;
import symbol_table.ValueType;
import tree.leaves.*;
import tree.nodes.*;

public class CodeGen_Visitor implements CodeGen_Int_Visitor {

    private PrintWriter wr;
    private String lastID = null;
    private Boolean isConcat = false;
    private ArrayList<String> isOutParam = null;

    public CodeGen_Visitor(String name) throws IOException {
        this.isOutParam = new ArrayList<String>();

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

        if (programNode.varDecList != null) {
            wr.println("\n// Dichiarazione delle variabili locali");
            for (VarDeclNode varDeclNode : programNode.varDecList) {
                varDeclNode.accept(this);
            }
        }

        if (programNode.funList != null) {
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

        idInitObblNode.leafID.accept(this);

        if (idInitObblNode.type == ValueType.string) {
            wr.print("[512]");
        }

        wr.print(" = ");

        idInitObblNode.value.accept(this);

        wr.print(";");
    }

    @Override
    public void visit(FunNode funNode) {
        if (funNode.type != null) {
            wr.print(convert_type(funNode.type));
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

        if (paramDecNode.out || paramDecNode.type == ValueType.string) {
            this.isOutParam.add(paramDecNode.leafID.value);
        }

        paramDecNode.leafID.accept(this);
    }

    @Override
    public void visit(StatNode statNode) {
        if (statNode.ifStatNode != null)
            statNode.ifStatNode.accept(this);
        if (statNode.whileStatNode != null)
            statNode.whileStatNode.accept(this);
        if (statNode.readStatNode != null)
            statNode.readStatNode.accept(this);
        if (statNode.writeStatNode != null)
            statNode.writeStatNode.accept(this);
        if (statNode.assignStatNode != null)
            statNode.assignStatNode.accept(this);
        if (statNode.callFunNode != null)
            statNode.callFunNode.accept(this);
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
                wr.print("scanf(\"%d\", ");
                break;
            case string:
                wr.print("scanf(\"%s\", ");
                break;
            case real:
                wr.print("scanf(\"%f\", ");
                break;
            }
            leafID.accept(this);
            wr.print(");");
        }

    }

    @Override
    public void visit(WriteStatNode writeStatNode) {

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

        // Accetto l'espressione che viene stampata
        writeStatNode.expr.accept(this);
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
        assignStatNode.leafID.accept(this);
        wr.print(" = ");
        assignStatNode.expr.accept(this);
        if (!assignStatNode.expr.op.equalsIgnoreCase("CALLFUN")) {
            wr.print(";");
        }
    }

    @Override
    public void visit(CallFunNode callFunNode) {
        if (this.isConcat) {
            wr.print("strcat(" + this.lastID + ", ");
            this.isConcat = false;
            callFunNode.leafID.accept(this);
            this.isConcat = true;
        } else {
            callFunNode.leafID.accept(this);
        }

        wr.print("(");
        if (callFunNode.exprList.size() != 0) {
            ExprNode lastExprNode = callFunNode.exprList.get(callFunNode.exprList.size() - 1);
            for (ExprNode exprNode : callFunNode.exprList) {
                if (exprNode.op.equalsIgnoreCase("OUTPAR") && exprNode.type != ValueType.string)
                    wr.print("&");
                exprNode.accept(this);
                if (lastExprNode != exprNode)
                    wr.print(", ");
            }
        }
        
        wr.print(")");
    
        if (this.isConcat) {
            wr.print("); ");
        }
    }

    @Override
    public void visit(ReturnNode returnNode) {
        wr.print("return ");
        returnNode.expr.accept(this);
        wr.print("; ");
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
            }
            wr.print("; ");
        }
    }

    private String convert_type(ValueType type) {
        switch (type) {
        case integer:
            return "int";
        case string:
            return "char";
        case real:
            return "double";
        case bool:
            return "bool";
        }
        return "null";
    }

    @Override
    public void visit(LeafID leafID) {
        if (this.isOutParam.contains(leafID.value)) {
            wr.print("*");
        }

        if (this.isConcat) {
            wr.print("strcat(" + this.lastID + ", ");
            wr.print(leafID.value);
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
                wr.print(" pow(");
                ((ExprNode) exprNode.val_One).accept(this);
                wr.print(", ");
                ((ExprNode) exprNode.val_Two).accept(this);
                wr.print(")");
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
                    wr.print(") == 1");
                } else if (((ExprNode) exprNode.val_Two).type == ValueType.string) {
                    wr.print("strcmp(");
                    ((ExprNode) exprNode.val_One).accept(this);
                    wr.print(", ");
                    ((ExprNode) exprNode.val_Two).accept(this);
                    wr.print(") == 1");
                } else if (exprNode.val_One instanceof LeafStringConst || exprNode.val_Two instanceof LeafStringConst) {
                    wr.print("strcmp(");
                    ((ExprNode) exprNode.val_One).accept(this);
                    wr.print(", ");
                    ((ExprNode) exprNode.val_Two).accept(this);
                    wr.print(") == 1");
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
}
