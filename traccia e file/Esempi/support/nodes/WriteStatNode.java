package support.nodes;

import org.w3c.dom.Element;
import visitor.*;

import java.util.ArrayList;

public class WriteStatNode implements StatNode, ISyntaxVisitable, ISemanticVisitable, ICVisitable {

    public String name = null;
    public ArrayList<ExprNode> exprList;

    public WriteStatNode(String name, ArrayList<ExprNode> exprList) {
        this.name = name;
        this.exprList = exprList;
    }

    @Override
    public String toString() {
        return "WriteStateNode{" +
                "exprList=" + exprList +
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
