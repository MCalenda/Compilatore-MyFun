package tree.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

import tree.leaves.LeafID;
import visitor.Syntax_Visitable;
import visitor.Syntax_Int_Visitor;

public class AssignStatNode extends DefaultMutableTreeNode implements Syntax_Visitable {
    public String name = "AssignStatNode";
    public LeafID leafID;
    public ExprNode expr;

    public AssignStatNode(LeafID id, ExprNode expr) {
        super("AssignStatNode");
        this.leafID = id;
        this.expr = expr;
    }

    @Override
    public DefaultMutableTreeNode accept(Syntax_Int_Visitor v) {
        return v.visit(this);
    }
}