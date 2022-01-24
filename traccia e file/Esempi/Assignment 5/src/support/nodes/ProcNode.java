package support.nodes;

import org.w3c.dom.Element;
import support.leafs.LeafID;
import visitor.*;

import java.util.ArrayList;

public class ProcNode implements ISyntaxVisitable, ISemanticVisitable, ICVisitable {

    public String name = null;
    public LeafID id = null;
    public ArrayList<ParDeclNode> paramDeclList = null;
    public ArrayList<ResultTypeNode> resultTypeList = null;
    public ProcBodyNode procBody = null;

    public ProcNode(String name, LeafID id, ArrayList<ParDeclNode> paramDeclList,
                    ArrayList<ResultTypeNode> resultTypeList, ProcBodyNode procBody){
        this.name = name;
        this.id = id;
        this.paramDeclList = paramDeclList;
        this.resultTypeList = resultTypeList;
        this.procBody = procBody;
    }

    public ProcNode(String name, LeafID id, ArrayList<ResultTypeNode> resultTypeList,
                    ProcBodyNode procBody){
        this.name = name;
        this.id = id;
        this.resultTypeList = resultTypeList;
        this.procBody = procBody;
    }

    @Override
    public String toString() {
        return "ProcNode{" +
                "id=" + id +
                ", paramDeclList=" + paramDeclList +
                ", resultTypeList=" + resultTypeList +
                ", procBody=" + procBody +
                ", name='" + name + '\'' +
                '}';
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
