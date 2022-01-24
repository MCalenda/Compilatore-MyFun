package tree.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

import visitor.Syntax_Visitable;
import visitor.Syntax_Int_Visitor;

public class WriteStatNode extends DefaultMutableTreeNode implements Syntax_Visitable  {
    public String name = "WriteStatNode";
    public ExprNode expr;
    public String op;

    public WriteStatNode(String op, ExprNode expr) {
        super("WriteStatNode");
        this.op = op;
        this.expr = expr;
    }

    @Override
    public DefaultMutableTreeNode accept(Syntax_Int_Visitor v) {
        return v.visit(this);
    }
}