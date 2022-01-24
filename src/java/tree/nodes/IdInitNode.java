package tree.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

import tree.leaves.LeafID;
import visitor.Syntax_Int_Visitable;
import visitor.Syntax_Int_Visitor;

public class IdInitNode extends DefaultMutableTreeNode implements Syntax_Int_Visitable {
    public String name = "IdInitNode";
    public LeafID leafID = null;
    public ExprNode exprNode = null;

    public IdInitNode(LeafID leafID) {
        super("IdInitNode");
        this.leafID = leafID;
    }

    public IdInitNode(LeafID leafID, ExprNode exprNode) {
        super("IdInitNode");
        this.leafID = leafID;
        this.exprNode = exprNode;
    }

    @Override
    public DefaultMutableTreeNode accept(Syntax_Int_Visitor v) {
        return v.visit(this);
    }
}