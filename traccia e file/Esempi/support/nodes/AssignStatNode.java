package support.nodes;

import org.w3c.dom.Element;
import support.leafs.LeafID;
import visitor.*;

import java.util.ArrayList;

public class AssignStatNode implements StatNode, ISyntaxVisitable, ISemanticVisitable, ICVisitable {

    public String name = null;
    public ArrayList<LeafID> idList = null;
    public ArrayList<ExprNode> exprList = null;

    public AssignStatNode(String name, ArrayList<LeafID> idList, ArrayList<ExprNode> exprList) {
        this.name = name;
        this.idList = idList;
        this.exprList = exprList;
    }

    @Override
    public String toString() {
        return "AssignStatNode{" +
                "idList=" + idList +
                ", exprList=" + exprList +
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
