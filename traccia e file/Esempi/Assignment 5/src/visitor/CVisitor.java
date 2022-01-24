package visitor;

import support.leafs.*;
import support.nodes.*;
import utils.SymbolTable;
import utils.ValueType;

import java.io.*;

public class CVisitor implements ICVisitor {

    private PrintWriter writer;
    private String actualProcName = "";
    private static int callProcIndex = 0;
    private static int argumentsMain = 0;
    private static int strcpyIndex = 0;

    public CVisitor(String name) throws IOException {
        File file = new File(name.substring(0, name.length() - 4).split("/")[1] + ".c");
        if (file.createNewFile()) {
            // File non esistente, lo crea
            System.out.println("File " + file.getName() + " created");
        } else {
            // File esistente
            file.delete();
            file.createNewFile();
            System.out.println("File " + file.getName() + " created");
        }

        writer = new PrintWriter(file);
    }

    @Override
    public int visit(ProgramNode node) {
        writer.println("#include <stdio.h>");
        writer.println("#include <stdlib.h>");
        writer.println("#include <stdbool.h>");
        writer.println("#include <string.h>\n");

        // ArrayList<VarDeclNode>
        if (node.nodeArrayList != null) {
            if (node.nodeArrayList.size() != 0)
                writer.println("// Dichiarazione variabili globali");
            for (VarDeclNode declNodo : node.nodeArrayList) {
                declNodo.accept(this);
            }
        }

        // ArrayList<ProcNode>
        for (ProcNode procNode : node.procNodeArrayList) {
            procNode.accept(this);
        }
        writer.close();
        return argumentsMain;
    }

    @Override
    public void visit(VarDeclNode node) {

        // ArrayList<IdInitNode>
        for (IdInitNode idInitNode : node.identifiers) {

            // TypeNode
            node.type.accept(this);

            // IdInitNode
            idInitNode.accept(this);

        }

    }

    @Override
    public void visit(TypeNode node) {
        try {
            if (SymbolTable.StringToType(node.type) == ValueType.Integer) {
                writer.print("int ");
            } else if (SymbolTable.StringToType(node.type) == ValueType.Float) {
                writer.print("float ");
            } else if (SymbolTable.StringToType(node.type) == ValueType.String) {
                writer.print("char ");
            } else if (SymbolTable.StringToType(node.type) == ValueType.Boolean) {
                writer.print("bool ");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    @Override
    public void visit(IdInitNode node) {
        // LeafID
        node.varName.accept(this);

        if (node.type == ValueType.String)
            writer.print("[512]");

        // ExprNode
        if (node.initValue != null) {
            writer.print(" = ");
            node.initValue.accept(this);
        }

        writer.println(";");
    }

    @Override
    public void visit(ProcNode node) {
        writer.println("\n// Proc " + node.id.value);
        this.actualProcName = node.id.value;
        // Se la funzione ha tipo di ritorno void non devo creare la struttura
        if (node.resultTypeList.get(0).isVoid) {
            writer.print("void " + node.id.value);
            if (node.paramDeclList == null) {
                writer.println("() {");
            } else {
                writer.print("(");
                ParDeclNode lastParDeclNode = node.paramDeclList.get(node.paramDeclList.size() - 1);
                for (ParDeclNode parDeclNode : node.paramDeclList) {
                    parDeclNode.accept(this);
                    if (lastParDeclNode != parDeclNode)
                        writer.print(",");
                }
                writer.println(") {");
            }
        }
        // Stessa cosa per le funzioni con un singolo tipo di ritorno
        else if (node.resultTypeList.size() == 1) {
            // Type
            node.resultTypeList.get(0).typeNode.accept(this);

            // Controllo se è una stringa per inserire il puntatore (*)
            if (node.resultTypeList.get(0).typeNode.type.equalsIgnoreCase("String")) {
                writer.print("* ");
            }

            // Nome della proc
            writer.print(node.id.value);

            // Parametri
            if (node.paramDeclList == null) {
                writer.println("() {");
            } else {
                writer.print("(");
                if (node.id.value.equals("main")) {
                    writer.println("int argc, char*argv[]) {");
                    int i = 1;
                    for (ParDeclNode parDeclNode : node.paramDeclList) {
                        for (LeafID leafID : parDeclNode.identifiers) {
                            parDeclNode.typeNode.accept(this);

                            writer.print(leafID.value);
                            if (parDeclNode.typeNode.type.equalsIgnoreCase("string")) {
                                writer.println("[512];");
                                writer.print("strcpy(" + leafID.value + ", argv[" + i + "]);");
                            } else {
                                writer.print(" = ");
                            }

                            switch (parDeclNode.typeNode.type) {
                                case "INT":
                                    writer.println("atoi(argv[" + i + "]);");
                                    break;

                                case "FLOAT":
                                    writer.println("atof(argv[" + i + "]);");
                                    break;

                                case "BOOL":
                                    writer.println("strcmp(\"true\",argv[" + i + "])==0;");
                                    break;
                            }
                            i++;
                        }
                    }
                    CVisitor.argumentsMain = i - 1;
                } else {
                    for (ParDeclNode parDeclNode : node.paramDeclList) {
                        parDeclNode.accept(this);
                        if (node.paramDeclList.get(node.paramDeclList.size() - 1) != parDeclNode) {
                            writer.print(",");
                        }
                    }
                    writer.println(") {");
                }
            }
        }
        // Se la funzione ha più tipi di ritorno devo creare la struttura
        else {
            // Creo la struttura con i parametri di ritorno
            writer.println("struct struct_" + node.id.value + " {");
            int i = 0;
            for (ResultTypeNode resultTypeNode : node.resultTypeList) {
                resultTypeNode.typeNode.accept(this);
                writer.print("variable" + i);
                if (resultTypeNode.typeNode.type.equalsIgnoreCase("STRING"))
                    writer.print("[512]");
                writer.println(";");
                i++;
            }
            writer.println("};");
            // :Creo la struttura con i parametri di ritorno

            // Creo il corpo della funzione
            writer.print("\nstruct struct_" + node.id.value + " " + node.id.value); // tipoDiRitorno nomeFunzione(parDeclList) {procBody...}
            if (node.paramDeclList == null)
                writer.println("() {");
            else {
                writer.print("(");
                for (ParDeclNode parDeclNode : node.paramDeclList) {
                    parDeclNode.accept(this);
                    if (node.paramDeclList.get(node.paramDeclList.size() - 1) != parDeclNode) {
                        writer.print(",");
                    }
                }
                writer.println(") {");
            }
        }
        node.procBody.accept(this);
        writer.println("}\n");
    }

    @Override
    public void visit(ProcBodyNode node) {
        for (VarDeclNode varDeclNode : node.varDeclList) {
            varDeclNode.accept(this);
        }

        // StatListNode
        if (node.statListNode != null) {
            node.statListNode.accept(this);
        }

        // Se la funzione resituisce più di un valore devo creare la struttura e metterci dentro i parametri da restituire
        if (node.returnExprs.size() > 1) {
            writer.println("\n// Struttura di ritorno");
            String nomeStruct = "struct_" + actualProcName;
            String nomeVariabile = "return_" + actualProcName;

            writer.println("struct " + nomeStruct + " " + nomeVariabile + ";");

            for (int i = 0; i < node.returnExprs.size(); i++) {
                ExprNode exprNode = node.returnExprs.get(i);
                if (exprNode.types.get(0) == ValueType.String) {
                    writer.print("strcpy(" + nomeVariabile + ".variable" + i + ", ");
                    exprNode.accept(this);
                    writer.print(");");
                } else {
                    writer.print(nomeVariabile + ".variable" + i + " = ");
                    exprNode.accept(this);
                    writer.println(";");
                }
            }

            writer.println("\nreturn " + nomeVariabile + ";");
        }
        // Se la funzione resituisce un solo valore non devo creare la struttura
        else if (node.returnExprs.size() == 1) {
            writer.print("\nreturn ");
            node.returnExprs.get(0).accept(this);
            writer.println("; ");
        }
    }

    @Override
    public void visit(ParDeclNode node) {

        for (LeafID nodeID : node.identifiers) {
            node.typeNode.accept(this);
            if(node.typeNode.type.equalsIgnoreCase("String"))
                writer.print("* ");

            nodeID.accept(this);
            if (node.identifiers.get(node.identifiers.size() - 1) != nodeID) {
                writer.print(",");
            }
        }
    }

    @Override
    public void visit(ResultTypeNode node) {
        writer.println(node.typeNode.type);
    }

    @Override
    public void visit(IfStatNode node) {
        writer.print("if (");
        node.condition.accept(this);
        writer.println(") {");
        node.ifBody.accept(this);
        writer.println("}");

        for (ElifNode elifNode : node.elifList) {
            elifNode.accept(this);
        }

        if (node.elseBody != null) {
            writer.println("else {");
            node.elseBody.accept(this);
            writer.println("}");
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
                    writer.println(";");
                    break;
            }
        }
    }

    @Override
    public void visit(WhileStatNode node) {

        if (node.preCondList != null) {
            node.preCondList.accept(this);
        }

        writer.print("while (");
        // List of conditions

        node.condition.accept(this);
        writer.println(") {");

        // StatListNode (afterCondizione)
        node.afterCondList.accept(this);

        if (node.preCondList != null) {
            node.preCondList.accept(this);
        }

        writer.println("}");
    }

    @Override
    public void visit(ReadLnStatNode node) {
        for (LeafID leafId : node.idList) {
            if (leafId.getType() == ValueType.String) {
                writer.print("scanf(\"%s\", ");
                leafId.accept(this);
                writer.println(");");
            } else if (leafId.getType() == ValueType.Boolean || leafId.getType() == ValueType.Integer) {
                writer.print("scanf(\"%d\", &");
                leafId.accept(this);
                writer.println(");");
            } else if (leafId.getType() == ValueType.Float) {
                writer.print("scanf(\"%f\", &");
                leafId.accept(this);
                writer.println(");");
            }
        }
    }

    @Override
    public void visit(WriteStatNode node) {
        for (ExprNode exprNode : node.exprList) {
            switch (exprNode.name) {
                case "INT_CONST":
                    writer.print("printf(\"%d\", ");
                    break;

                case "FLOAT_CONST":
                    writer.print("printf(\"%f\", ");
                    break;

                case "ID":
                    writer.print("printf(");
                    switch (exprNode.types.get(0).name()) {
                        case "String":
                            writer.print("\"%s\", ");
                            break;

                        case "Boolean", "Integer":
                            writer.print("\"%d\", ");
                            break;

                        case "Float":
                            writer.print("\"%f\", ");
                            break;
                    }
                    break;

                case "CallProcOp":
                    CallProcNode callProcNode = (CallProcNode) exprNode.value1;
                    if (callProcNode != null) {
                        if (callProcNode.types.size() > 1) {
                            String nomeStruct = "struct_" + callProcNode.leafID.value;
                            String nomeVariabile = callProcNode.leafID.value + "_return" + callProcIndex;
                            writer.print("struct " + nomeStruct + " " + nomeVariabile + " = ");
                            callProcNode.accept(this);
                            writer.println(";");
                            callProcIndex++;
                            for (int i = 0; i < callProcNode.types.size(); i++) {
                                writer.print("printf(");
                                String variabile = nomeVariabile + ".variable" + i;
                                switch (callProcNode.types.get(i).name()) {
                                    case "String":
                                        writer.print("\"%s\\n\", ");
                                        break;

                                    case "Boolean", "Integer":
                                        writer.print("\"%d\\n\", ");
                                        break;

                                    case "Float":
                                        writer.print("\"%f\\n\", ");
                                        break;
                                }
                                writer.println(variabile + ");");
                            }
                        } else {
                            for (ValueType valueType : callProcNode.types) {
                                writer.print("printf(");
                                switch (valueType.name()) {
                                    case "String":
                                        writer.print("\"%s\", ");
                                        break;

                                    case "Boolean", "Integer":
                                        writer.print("\"%d\", ");
                                        break;

                                    case "Float":
                                        writer.print("\"%f\", ");
                                        break;
                                }
                                callProcNode.accept(this);
                                writer.println(");");
                            }
                        }
                    }
                    break;

                case "AddOp":
                    switch (SemanticVisitor.getType_Operations(((ExprNode) exprNode.value1).types.get(0), ((ExprNode) exprNode.value2).types.get(0))) {
                        case String:
                            writer.print("printf(\"%s\", ");
                            exprNode.accept(this);
                            writer.print(");");
                            break;
                        case Integer, Boolean:
                            writer.print("printf(\"%d\", ");
                            exprNode.accept(this);
                            writer.print(");");
                            break;
                        case Float:
                            writer.print("printf(\"%f\", ");
                            exprNode.accept(this);
                            writer.print(");");
                            break;
                    }
                    break;

                default:
                    writer.print("printf(");
                    break;
            }

            if (!(exprNode.value1 instanceof CallProcNode) && !exprNode.name.equalsIgnoreCase("AddOp")) {
                exprNode.accept(this);
                writer.println(");");
            }
        }
    }

    @Override
    public void visit(AssignStatNode node) {

        // ciclo sulle variabili a cui assegnare i valori
        if (node.idList.size() == 1) {
            if (node.idList.get(0).type == ValueType.String) {
                writer.print("strcpy(");
                node.idList.get(0).accept(this);
                writer.print(",");
                node.exprList.get(0).accept(this);
                writer.println(");");
            } else {
                node.idList.get(0).accept(this);
                writer.print(" = ");
                node.exprList.get(0).accept(this);
                writer.println(";");
            }
        } else {
            // Variabile temporanea per prendere l'expression giusta (cioè la parte destra dell'assegnazione)
            int localIndex = 0;

            for (int i = 0; i < node.idList.size(); i++) {
                ExprNode expression = node.exprList.get(localIndex);

                // se è una proc
                if (expression.name.equalsIgnoreCase("CallProcOp")) {
                    CallProcNode callProcNode = (CallProcNode) expression.value1;

                    // Se ha un solo valore di ritorno
                    if (callProcNode.types.size() == 1) {
                        node.idList.get(i).accept(this);
                        writer.print(" = ");
                        expression.accept(this);
                        writer.println(";");
                    }
                    // Se ha più valori di ritorno devo creare la struttura
                    else {
                        // Creo la variabile struct
                        writer.println("\n// Struttura con i valori di ritorno di " + callProcNode.leafID.value);
                        writer.print("struct struct_");
                        callProcNode.leafID.accept(this);

                        writer.print(" ");
                        callProcNode.leafID.accept(this);
                        writer.print("_return" + callProcIndex);

                        writer.print(" = ");
                        callProcNode.accept(this);
                        writer.println(";");

                        //ciclo sui tipi di ritorno
                        for (int j = 0; j < callProcNode.types.size(); j++) {

                            if (callProcNode.types.get(j) == ValueType.String) {
                                writer.print("strcpy(");
                                node.idList.get(i).accept(this);
                                writer.print(", ");
                                callProcNode.leafID.accept(this);
                                writer.println("_return" + callProcIndex + ".variable" + j + ");");

                            } else {
                                node.idList.get(i).accept(this);
                                writer.print(" = ");
                                callProcNode.leafID.accept(this);
                                writer.println("_return" + callProcIndex + ".variable" + j + ";");
                            }

                            //sommo la size dei tipi di ritorno all'indice del primo ciclo
                            if (j != callProcNode.types.size() - 1)
                                i++;
                        }

                        localIndex++;
                        callProcIndex++;

                        //Lascio un po' di spazio
                        writer.println();
                    }
                }
                // Non è una callProc
                else {
                    if (node.idList.get(i).type == ValueType.String) {
                        writer.print("strcpy(");
                        node.idList.get(i).accept(this);
                        writer.print(",");
                        node.exprList.get(i).accept(this);
                        writer.println(");");
                    } else {
                        node.idList.get(i).accept(this);
                        writer.print(" = ");
                        expression.accept(this);
                        writer.println(";");
                    }
                    localIndex++;
                }
            }
        }
    }

    @Override
    public void visit(CallProcNode node) {
        node.leafID.accept(this);

        if (node.exprList != null) {
            writer.print("(");
            ExprNode lastExprNode = node.exprList.get(node.exprList.size() - 1);
            for (ExprNode exprNode : node.exprList) {
                exprNode.accept(this);
                if (exprNode != lastExprNode)
                    writer.print(", ");
            }
            writer.println(")");
        } else {
            writer.println("()");
        }
    }

    @Override
    public void visit(ElifNode node) {
        writer.print("else if (");
        node.exprNode.accept(this);
        writer.println(") {");
        node.statListNode.accept(this);
        writer.println("}");
    }

    @Override
    public void visit(ExprNode exprNode) {
        if (exprNode.value1 != null && exprNode.value2 != null) {
            //((ExprNode) exprNode.value1).accept(this);

            switch (exprNode.name) {
                case "AddOp":
                    ((ExprNode) exprNode.value1).accept(this);
                    writer.print(" + ");
                    ((ExprNode) exprNode.value2).accept(this);
                    break;
                case "DiffOp":
                    ((ExprNode) exprNode.value1).accept(this);
                    writer.print(" - ");
                    ((ExprNode) exprNode.value2).accept(this);
                    break;
                case "DivOp":
                    ((ExprNode) exprNode.value1).accept(this);
                    writer.print(" / ");
                    ((ExprNode) exprNode.value2).accept(this);
                    break;
                case "MulOp":
                    ((ExprNode) exprNode.value1).accept(this);
                    writer.print(" * ");
                    ((ExprNode) exprNode.value2).accept(this);
                    break;
                case "AndOp":
                    ((ExprNode) exprNode.value1).accept(this);
                    writer.print(" && ");
                    ((ExprNode) exprNode.value2).accept(this);
                    break;
                case "OrOp":
                    ((ExprNode) exprNode.value1).accept(this);
                    writer.print(" || ");
                    ((ExprNode) exprNode.value2).accept(this);
                    break;
                case "GTOp":
                    if (((ExprNode) exprNode.value1).types.get(0) == ValueType.String) {
                        writer.print("strcmp(");
                        ((ExprNode) exprNode.value1).accept(this);
                        writer.print(", ");
                        ((ExprNode) exprNode.value2).accept(this);
                        writer.print(") > 0");
                    } else {
                        ((ExprNode) exprNode.value1).accept(this);
                        writer.print(" > ");
                        ((ExprNode) exprNode.value2).accept(this);
                    }
                    break;
                case "GEOp":
                    if (((ExprNode) exprNode.value1).types.get(0) == ValueType.String) {
                        writer.print("strcmp(");
                        ((ExprNode) exprNode.value1).accept(this);
                        writer.print(", ");
                        ((ExprNode) exprNode.value2).accept(this);
                        writer.print(") >= 0");
                    } else {
                        ((ExprNode) exprNode.value1).accept(this);
                        writer.print(" >= ");
                        ((ExprNode) exprNode.value2).accept(this);
                    }
                    break;
                case "LTOp":
                    if (((ExprNode) exprNode.value1).types.get(0) == ValueType.String) {
                        writer.print("strcmp(");
                        ((ExprNode) exprNode.value1).accept(this);
                        writer.print(", ");
                        ((ExprNode) exprNode.value2).accept(this);
                        writer.print(") < 0");
                    } else {
                        ((ExprNode) exprNode.value1).accept(this);
                        writer.print(" < ");
                        ((ExprNode) exprNode.value2).accept(this);
                    }
                    break;
                case "LEOp":
                    if (((ExprNode) exprNode.value1).types.get(0) == ValueType.String) {
                        writer.print("strcmp(");
                        ((ExprNode) exprNode.value1).accept(this);
                        writer.print(", ");
                        ((ExprNode) exprNode.value2).accept(this);
                        writer.print(") <= 0");
                    } else {
                        ((ExprNode) exprNode.value1).accept(this);
                        writer.print(" <= ");
                        ((ExprNode) exprNode.value2).accept(this);
                    }
                    break;
                case "EQOp":
                    if (((ExprNode) exprNode.value1).types.get(0) == ValueType.String) {
                        writer.print("strcmp(");
                        ((ExprNode) exprNode.value1).accept(this);
                        writer.print(", ");
                        ((ExprNode) exprNode.value2).accept(this);
                        writer.print(") == 0");
                    } else {
                        ((ExprNode) exprNode.value1).accept(this);
                        writer.print(" == ");
                        ((ExprNode) exprNode.value2).accept(this);
                    }
                    break;
                case "NEOp":
                    if (((ExprNode) exprNode.value1).types.get(0) == ValueType.String) {
                        writer.print("strcmp(");
                        ((ExprNode) exprNode.value1).accept(this);
                        writer.print(", ");
                        ((ExprNode) exprNode.value2).accept(this);
                        writer.print(") != 0");
                    } else {
                        ((ExprNode) exprNode.value1).accept(this);
                        writer.print(" != ");
                        ((ExprNode) exprNode.value2).accept(this);
                    }
                    break;
            }

            //((ExprNode) exprNode.value2).accept(this);


        } else if (exprNode.value1 != null) {
            if (exprNode.name.equalsIgnoreCase("UminusOp")) {
                writer.print("-");
                ((ExprNode) exprNode.value1).accept(this);
            } else if (exprNode.name.equalsIgnoreCase("NotOp")) {
                writer.print("!");
                ((ExprNode) exprNode.value1).accept(this);
            } else if (exprNode.value1 instanceof LeafNull)
                ((LeafNull) exprNode.value1).accept(this);
            else if (exprNode.value1 instanceof LeafIntConst)
                ((LeafIntConst) exprNode.value1).accept(this);
            else if (exprNode.value1 instanceof LeafFloatConst)
                ((LeafFloatConst) exprNode.value1).accept(this);
            else if (exprNode.value1 instanceof LeafStringConst)
                ((LeafStringConst) exprNode.value1).accept(this);
            else if (exprNode.value1 instanceof LeafBool)
                ((LeafBool) exprNode.value1).accept(this);
            else if (exprNode.value1 instanceof LeafID)
                ((LeafID) exprNode.value1).accept(this);
            else if (exprNode.value1 instanceof CallProcNode)
                ((CallProcNode) exprNode.value1).accept(this);
            else if (exprNode.value1 instanceof ExprNode)
                ((ExprNode) exprNode.value1).accept(this);
        }
    }

    @Override
    public void visit(LeafBool leaf) {
        writer.print(leaf.value);
    }

    @Override
    public void visit(LeafFloatConst leaf) {
        writer.print(leaf.value);
    }

    @Override
    public void visit(LeafID leaf) {
        writer.print(leaf.value);
    }

    @Override
    public void visit(LeafIntConst leaf) {
        writer.print(leaf.value);
    }

    @Override
    public void visit(LeafNull leaf) {
        writer.print("NULL");
    }

    @Override
    public void visit(LeafStringConst leaf) {
        writer.print("\"" + leaf.value + "\"");
    }
}
