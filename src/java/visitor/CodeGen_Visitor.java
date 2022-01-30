package visitor;

import java.io.*;

import symbol_table.ValueType;
import tree.leaves.*;
import tree.nodes.*;

public class CodeGen_Visitor implements CodeGen_Int_Visitor {

    private PrintWriter wr;
    private Integer tempCounter;

    public CodeGen_Visitor(String name) throws IOException {
        tempCounter = 0;
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
            wr.print(convert_type(funNode.type) + " ");
        } else {
            wr.print("void ");
        }

        funNode.leafID.accept(this);
        wr.print("(");

        if (funNode.paramDecList != null) {
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
    }

    @Override
    public void visit(ParamDecNode paramDecNode) {
        wr.print(convert_type(paramDecNode.type) + " ");

        if (paramDecNode.out) {
            wr.print("*");
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
            wr.print("\t");
            varDeclNode.accept(this);
        }

        for (StatNode statNode : ifStatNode.statList) {
            statNode.accept(this);
        }

        wr.print("\t}");
    }

    @Override
    public void visit(WhileStatNode whileStatNode) {
        wr.print("while (");

        whileStatNode.expr.accept(this);

        wr.print(") {");

        for (VarDeclNode varDeclNode : whileStatNode.varDeclList) {
            wr.print("\t");
            varDeclNode.accept(this);
        }

        for (StatNode statNode : whileStatNode.statList) {
            statNode.accept(this);
        }

        wr.print("\t}");
    }

    @Override
    public void visit(ReadStatNode readStatNode) {
        // TODO Auto-generated method stub

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
        wr.print("; ");
    }

    @Override
    public void visit(CallFunNode callFunNode) {
        callFunNode.leafID.accept(this);

        wr.print("(");
        ExprNode lastExprNode = callFunNode.exprList.get(callFunNode.exprList.size() - 1);
        for (ExprNode exprNode : callFunNode.exprList) {
            exprNode.accept(this);
            if (lastExprNode != exprNode)
                wr.print(", ");
        }
        wr.print(")");
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
        if (idInitNode.type == ValueType.string) {
            wr.print("*");
        }
        
        idInitNode.leafID.accept(this);

        if (idInitNode.exprNode != null) {
            wr.print(" = ");
            idInitNode.exprNode.accept(this);
        }

        wr.print(";");
    }

    // DA VEDERE SE TERNELO O MENO
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

    // DA VEDERE SE TENERLO O MENO
    private Integer get_temp() {
        tempCounter++;
        return tempCounter;
    }


    @Override
    public void visit(LeafID leafID) {
        wr.print(leafID.value);
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
                ((ExprNode) exprNode.val_Two).accept(this);
                wr.print(")");
                break;

            case "STR_CONCAT":
                wr.print("char temp_" + get_temp() + "[512] = ");
                ((ExprNode) exprNode.val_One).accept(this);
                wr.print("; ");

                wr.print("char temp_" + get_temp() + "[512] = ");
                ((ExprNode) exprNode.val_Two).accept(this);
                wr.print("; ");

                wr.print("char *temp_" + get_temp() + " = ");
                wr.print("strcat(temp_" + (get_temp() - 1) + ", temp_" + (get_temp() - 2) +")");
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
                ((ExprNode) exprNode.val_One).accept(this);
                wr.print(" == ");
                ((ExprNode) exprNode.val_Two).accept(this);
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
        if (leafStringConst.value.length() != 0)
            wr.print("\"" + leafStringConst.value + "\"");
        else
            wr.print(leafStringConst.value);
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
}
