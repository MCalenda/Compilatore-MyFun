package tree.nodes;

import tree.leaves.LeafID;

public class IdInitNode {
    public String name = "IdInitNode";
    public LeafID leafID = null;
    public ExprNode exprNode = null;

    public IdInitNode(LeafID leafID) {
        this.leafID = leafID;
    }

    public IdInitNode(LeafID leafID, ExprNode exprNode) {
        this.leafID = leafID;
        this.exprNode = exprNode;
    }
}