package visitor;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import support.leafs.*;
import support.nodes.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class SyntaxVisitor implements ISyntaxVisitor {

    DocumentBuilderFactory dbFactory;
    DocumentBuilder dBuilder;
    Document doc;

    public SyntaxVisitor() throws ParserConfigurationException {
        dbFactory = DocumentBuilderFactory.newInstance();
        dBuilder = dbFactory.newDocumentBuilder();
        doc = dBuilder.newDocument();
    }

    @Override
    public Element visit(ProgramNode programNode) {
        // root element
        Element programElement = doc.createElement(programNode.name);
        doc.appendChild(programElement);

        // ArrayList<VarDeclNode> 🟢
        if(programNode.nodeArrayList != null){
            Element varDeclElement = doc.createElement("VarDeclList");
            for(VarDeclNode declNodo: programNode.nodeArrayList) {
                varDeclElement.appendChild(declNodo.accept(this));
            }
            programElement.appendChild(varDeclElement);
        }

        // ArrayList<ProcNode> 🟢
        Element procListElement = doc.createElement("ProcList");
        for(ProcNode procNodo: programNode.procNodeArrayList) {
            procListElement.appendChild(procNodo.accept(this));
        }
        programElement.appendChild(procListElement);

        return programElement;
    }

    @Override
    public Element visit(VarDeclNode varDeclnode) {
        Element varDeclElement = doc.createElement(varDeclnode.name);

        // TypeNode 🟢
        varDeclElement.appendChild(varDeclnode.type.accept(this));

        // ArrayList<IdInitNode> 🟢
        for(IdInitNode idInitNode: varDeclnode.identifiers) {
            varDeclElement.appendChild(idInitNode.accept(this));
        }

        return varDeclElement;
    }

    @Override
    public Element visit(TypeNode typeNode) {
        Element typeNodeElement = doc.createElement(typeNode.name);
        typeNodeElement.setAttribute("type", typeNode.type);

        return typeNodeElement;
    }

    @Override
    public Element visit(IdInitNode idInitNode) {
        Element idInitElement = doc.createElement(idInitNode.name);

        // LeafID 🟢
        idInitElement.appendChild(idInitNode.varName.accept(this));

        // ExprNode 🟢
        if(idInitNode.initValue != null) {
            idInitElement.appendChild(idInitNode.initValue.accept(this));
        }

        return idInitElement;
    }

    @Override
    public Element visit(ProcNode procNode) {
        Element procElement = doc.createElement(procNode.name);

        // LeafID 🟢
        procElement.appendChild(procNode.id.accept(this));

        // ArrayList<ParDeclNode> 🟢
        if(procNode.paramDeclList != null) {
            Element paramDeclElement = doc.createElement("ParamsList");
            for(ParDeclNode parDeclNode: procNode.paramDeclList){
                paramDeclElement.appendChild(parDeclNode.accept(this));
            }
            procElement.appendChild(paramDeclElement);
        }

        // ArrayList<ResultTypeNode> 🟢
        Element resultTypeElement = doc.createElement("ResultTypeList");
        for(ResultTypeNode resultTypeNode: procNode.resultTypeList){
            resultTypeElement.appendChild(resultTypeNode.accept(this));
        }
        procElement.appendChild(resultTypeElement);

        // ProcBodyNode 🟢
        procElement.appendChild(procNode.procBody.accept(this));

        return procElement;
    }

    @Override
    public Element visit(ProcBodyNode procBodyNode) {
        Element procBodyElement = doc.createElement(procBodyNode.name);

        // ArrayList<VarDecl> 🟢
        Element varDeclElement = doc.createElement("VarDeclList");
        procBodyElement.appendChild(varDeclElement);
        for(VarDeclNode varDeclNode: procBodyNode.varDeclList) {
            varDeclElement.appendChild(varDeclNode.accept(this));
        }

        // StatListNode 🟢
        if(procBodyNode.statListNode != null) {
            procBodyElement.appendChild(procBodyNode.statListNode.accept(this));
        }

        // ArrayList<ExprNode> 🟢
        Element returnExprsElement = doc.createElement("ReturnExprsList");
        procBodyElement.appendChild(returnExprsElement);
        for(ExprNode exprNode: procBodyNode.returnExprs) {
            returnExprsElement.appendChild(exprNode.accept(this));
        }

        return procBodyElement;
    }

    @Override
    public Element visit(ParDeclNode parDeclNode) {
        Element procElement = doc.createElement(parDeclNode.name);

        // TypeNode 🟢
        procElement.appendChild(parDeclNode.typeNode.accept(this));

        // ArrayList<LeafID> 🟢
        Element identifiersElement = doc.createElement("Identifiers");
        procElement.appendChild(identifiersElement);
        for(LeafID leafIdNode: parDeclNode.identifiers){
            identifiersElement.appendChild(leafIdNode.accept(this));
        }

        return procElement;
    }

    @Override
    public Element visit(ResultTypeNode resultTypeNode) {
        Element procElement = doc.createElement(resultTypeNode.name);

        // TypeNode 🟢
        if(resultTypeNode.typeNode != null) {
            procElement.appendChild(resultTypeNode.typeNode.accept(this));
        }
        else
        // boolean
        {
            procElement.setAttribute("isVoid", "" + resultTypeNode.isVoid);
        }

        return procElement;
    }

    @Override
    public Element visit(IfStatNode node) {
        Element ifStatElement = doc.createElement(node.name);

        Element conditionNode = doc.createElement("Condition");
        ifStatElement.appendChild(conditionNode);

        // ExprNode 🟢
        conditionNode.appendChild(node.condition.accept(this));

        // StatListNode 🟢
        Element bodyElement = doc.createElement("Body");
        ifStatElement.appendChild(bodyElement);
        bodyElement.appendChild(node.ifBody.accept(this));

        // ArrayList<ElifNode> 🟢
        Element elifListElement = doc.createElement("ElifList");
        ifStatElement.appendChild(elifListElement);
        for(ElifNode elifNode: node.elifList) {
            elifListElement.appendChild(elifNode.accept(this));
        }

        // StatListNode 🟢
        if(node.elseBody != null) {
            Element elseElement = doc.createElement("ElseBody");
            ifStatElement.appendChild(elseElement);
            elseElement.appendChild(node.elseBody.accept(this));
        }

        return ifStatElement;
    }

    @Override
    public Element visit(StatListNode nodeList) {
        Element statListElement = doc.createElement(nodeList.name);

        // ArrayList<StatNode> 🟢
        for(StatNode statNode: nodeList.statList) {
            switch (statNode.getClass().getSimpleName()) {
                case "IfStatNode":
                    IfStatNode ifStat = (IfStatNode) statNode;
                    statListElement.appendChild(ifStat.accept(this));
                    break;
                case "WhileStatNode":
                    WhileStatNode whileStatNode = (WhileStatNode) statNode;
                    statListElement.appendChild(whileStatNode.accept(this));
                    break;
                case "ReadLnStatNode":
                    ReadLnStatNode readStatNode = (ReadLnStatNode) statNode;
                    statListElement.appendChild(readStatNode.accept(this));
                    break;
                case "WriteStatNode":
                    WriteStatNode writeStatNode = (WriteStatNode) statNode;
                    statListElement.appendChild(writeStatNode.accept(this));
                    break;
                case "AssignStatNode":
                    AssignStatNode assignStatNode = (AssignStatNode) statNode;
                    statListElement.appendChild(assignStatNode.accept(this));
                    break;
                case "CallProcNode":
                    CallProcNode callProcNode = (CallProcNode) statNode;
                    statListElement.appendChild(callProcNode.accept(this));
                    break;
            }
        }

        return statListElement;
    }

    @Override
    public Element visit(WhileStatNode node) {
        Element whileStatElement = doc.createElement(node.name);

        // StatListNode (precondizione) 🟢
        if(node.preCondList != null) {
            Element preCondElement = doc.createElement("PreStats");
            whileStatElement.appendChild(preCondElement);
            preCondElement.appendChild(node.preCondList.accept(this));
        }

        // StatListNode (afterCondizione) 🟢
        Element afterConditionsElement = doc.createElement("DoBodyStats");
        whileStatElement.appendChild(afterConditionsElement);
        afterConditionsElement.appendChild(node.afterCondList.accept(this));

        // ExprNode 🟢
        Element conditionElement = doc.createElement("Condition");
        whileStatElement.appendChild(conditionElement);
        conditionElement.appendChild(node.condition.accept(this));

        return whileStatElement;
    }

    @Override
    public Element visit(ReadLnStatNode node) {
        Element readLnStatElement = doc.createElement(node.name);

        // ArrayList<LeafID> 🟢
        Element listElement = doc.createElement("idList");
        readLnStatElement.appendChild(listElement);
        for(LeafID leafId: node.idList) {
            listElement.appendChild(leafId.accept(this));
        }

        return readLnStatElement;
    }

    @Override
    public Element visit(WriteStatNode node) {
        Element writeLnStatElement = doc.createElement(node.name);

        // ArrayList<ExprNode> 🟢
        Element listNode = doc.createElement("ExprList");
        for(ExprNode exprNode: node.exprList) {
            listNode.appendChild(exprNode.accept(this));
        }
        writeLnStatElement.appendChild(listNode);

        return writeLnStatElement;
    }

    @Override
    public Element visit(AssignStatNode node) {
        Element assignStatElement = doc.createElement(node.name);

        // ArrayList<LeafID> 🟢
        Element idListElement = doc.createElement("IdList");
        for(LeafID leafId: node.idList) {
            idListElement.appendChild(leafId.accept(this));
        }
        assignStatElement.appendChild(idListElement);

        // ArrayList<ExprNode> 🟢
        Element exprListElement = doc.createElement("ValueList");
        for(ExprNode exprNode: node.exprList) {
            exprListElement.appendChild(exprNode.accept(this));
        }
        assignStatElement.appendChild(exprListElement);

        return assignStatElement;
    }

    @Override
    public Element visit(CallProcNode node) {
        Element callProcElement = doc.createElement(node.name);

        // LeafID 🟢
        callProcElement.appendChild(node.leafID.accept(this));

        // ArrayList<ExprNode> 🟢
        if(node.exprList != null) {
            Element exprElement = doc.createElement("ParamsList");
            callProcElement.appendChild(exprElement);
            for(ExprNode exprNode: node.exprList) {
                exprElement.appendChild(exprNode.accept(this));
            }
        }

        return callProcElement;
    }

    @Override
    public Element visit(ElifNode node) {
        Element elifElement = doc.createElement(node.name);

        Element conditionNode = doc.createElement("Condition");
        elifElement.appendChild(conditionNode);

        // ExprNode 🟢
        conditionNode.appendChild(node.exprNode.accept(this));

        // StatListNode 🟢
        Element bodyElement = doc.createElement("Body");
        elifElement.appendChild(bodyElement);
        bodyElement.appendChild(node.statListNode.accept(this));

        return elifElement;
    }

    @Override
    public Element visit(ExprNode exprNode) {
        Element node = doc.createElement(exprNode.name);

        if (exprNode.value1 != null && exprNode.value2 != null) {
            node.appendChild(((ExprNode)exprNode.value1).accept(this));
            node.appendChild(((ExprNode)exprNode.value2).accept(this));
        }
        else if (exprNode.value1 != null) {
            if (exprNode.value1 instanceof LeafNull)
                node.appendChild(((LeafNull)exprNode.value1).accept(this));
            if (exprNode.value1 instanceof LeafIntConst)
                node.appendChild(((LeafIntConst)exprNode.value1).accept(this));
            if (exprNode.value1 instanceof LeafFloatConst)
                node.appendChild(((LeafFloatConst)exprNode.value1).accept(this));
            if (exprNode.value1 instanceof LeafStringConst)
                node.appendChild(((LeafStringConst)exprNode.value1).accept(this));
            if (exprNode.value1 instanceof LeafBool)
                node.appendChild(((LeafBool)exprNode.value1).accept(this));
            if (exprNode.value1 instanceof LeafID)
                node.appendChild(((LeafID)exprNode.value1).accept(this));
            if (exprNode.value1 instanceof CallProcNode)
                node.appendChild(((CallProcNode)exprNode.value1).accept(this));
            if (exprNode.value1 instanceof ExprNode)
                node.appendChild(((ExprNode)exprNode.value1).accept(this));
        }
        return node;
    }

    @Override
    public Element visit(LeafBool leaf) {
        Element leafElement = doc.createElement(leaf.name);
        leafElement.setAttribute("value", String.valueOf(leaf.value));

        return leafElement;
    }

    @Override
    public Element visit(LeafFloatConst leaf) {
        Element leafElement = doc.createElement(leaf.name);
        leafElement.setAttribute("value", leaf.value.toString());

        return leafElement;
    }

    @Override
    public Element visit(LeafID leafIdNode) {
        Element programElement = doc.createElement(leafIdNode.name);
        programElement.setAttribute("value", leafIdNode.value);

        return programElement;
    }

    @Override
    public Element visit(LeafIntConst leaf) {
        Element leafElement = doc.createElement(leaf.name);
        leafElement.setAttribute("value", leaf.value.toString());

        return leafElement;
    }

    @Override
    public Element visit(LeafNull leaf) {
        Element leafElement = doc.createElement(leaf.name);

        return leafElement;
    }

    @Override
    public Element visit(LeafStringConst leaf) {
        Element leafElement = doc.createElement(leaf.name);
        leafElement.setAttribute("value", leaf.value);

        return leafElement;
    }

    public void saveXML(String name) throws TransformerException {
        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        DOMSource source = new DOMSource(doc);

        StreamResult result = new StreamResult(new File(name.substring(0, name.length()-4).split("/")[1]+ ".xml"));
        transformer.transform(source, result);
    }
}
