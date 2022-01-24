package support.nodes;

import org.w3c.dom.Element;
import visitor.*;

import java.util.ArrayList;

public class ProgramNode implements ISyntaxVisitable, ISemanticVisitable, ICVisitable {

    public String name = null;
    public ArrayList<VarDeclNode> nodeArrayList;
    public ArrayList<ProcNode> procNodeArrayList;

    public ProgramNode(String name, ArrayList<VarDeclNode> nodeArrayList, ArrayList<ProcNode> procNodeArrayList) {
        this.name = name;
        this.nodeArrayList = nodeArrayList;
        this.procNodeArrayList=procNodeArrayList;
    }

    @Override
    public String toString() {
        return "ProgramNode{" +
                "VarDeclList=" +nodeArrayList +
                ", ProcList=" + procNodeArrayList +
                ", name='" + name + '\'' +
                '}';
    }

    public ArrayList<VarDeclNode> getNodeArrayList() {
        return nodeArrayList;
    }

    public void setNodeArrayList(ArrayList<VarDeclNode> nodeArrayList) {
        this.nodeArrayList = nodeArrayList;
    }

    public ArrayList<ProcNode> getProcNodeArrayList() {
        return procNodeArrayList;
    }

    public void setProcNodeArrayList(ArrayList<ProcNode> procNodeArrayList) {
        this.procNodeArrayList = procNodeArrayList;
    }

    @Override
    public Element accept(ISyntaxVisitor v) {
        return v.visit(this);
    }

    @Override
    public void accept(ISemanticVisitor v) { v.visit(this); }

    @Override
    public void accept(ICVisitor v) { v.visit(this); }
}
