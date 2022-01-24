package tree.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

import tree.leaves.LeafID;
import visitor.Syntax_Int_Visitable;
import visitor.Syntax_Int_Visitor;

public class IdInitObblNode extends DefaultMutableTreeNode implements Syntax_Int_Visitable {
    public String name = "IdInitObblNode";
    public LeafID leafID;
    public ConstNode value;

    public IdInitObblNode(LeafID leafID, ConstNode value) {
        super("IdInitObblNode");
        this.leafID = leafID;
        this.value = value;
    }

    @Override
    public DefaultMutableTreeNode accept(Syntax_Int_Visitor v) {
        return v.visit(this);
    }
}