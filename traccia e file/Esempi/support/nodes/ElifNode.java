package support.nodes;

import org.w3c.dom.Element;
import visitor.*;

public class ElifNode implements ISyntaxVisitable, ISemanticVisitable, ICVisitable {

    public String name = null;
    public StatListNode statListNode;
    public ExprNode exprNode;

    public ElifNode(String name, ExprNode exprNode, StatListNode statListNode) {
        this.name=name;
        this.exprNode=exprNode;
        this.statListNode=statListNode;
    }

    @Override
    public Element accept(ISyntaxVisitor v) {
        return v.visit(this);
    }

    @Override
    public void accept(ISemanticVisitor v) { v.visit(this); }

    @Override
    public void accept(ICVisitor v) { v.visit(this); }

    @Override
    public String toString() {
        return "ElifNode{" +
                "statListNode=" + statListNode +
                ", exprNode=" + exprNode +
                ", name='" + name + '\'' +
                '}';
    }
}
