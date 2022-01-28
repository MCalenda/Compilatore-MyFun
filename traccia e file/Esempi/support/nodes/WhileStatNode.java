package support.nodes;

import org.w3c.dom.Element;
import visitor.*;

public class WhileStatNode implements StatNode, ISyntaxVisitable, ISemanticVisitable, ICVisitable {

    public String name = null;
    public StatListNode preCondList;
    public StatListNode afterCondList;
    public ExprNode condition;

    public WhileStatNode(String name, StatListNode preCondList, StatListNode afterCondList, ExprNode condition) {
        this.name = name;
        this.preCondList = preCondList;
        this.afterCondList = afterCondList;
        this.condition = condition;
    }

    public WhileStatNode(String name, StatListNode afterCondList, ExprNode condition) {
        this.name = name;
        this.afterCondList = afterCondList;
        this.condition = condition;
        this.preCondList = null;
    }

    @Override
    public String toString() {
        return "WhileStatNode{" +
                "preCondList=" + preCondList +
                ", afterCondList=" + afterCondList +
                ", condition=" + condition +
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
