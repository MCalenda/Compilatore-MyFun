package tree.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

import visitor.Syntax_Visitable;
import visitor.Syntax_Int_Visitor;

public class ReturnNode extends DefaultMutableTreeNode implements Syntax_Visitable {
    public String name = "ReturnNode";
    public ExprNode expr;

    public ReturnNode(ExprNode expr) {
        super("ReturnNode");
        this.expr = expr;
    }

    @Override
    public DefaultMutableTreeNode accept(Syntax_Int_Visitor v) {
        return v.visit(this);
    }
}