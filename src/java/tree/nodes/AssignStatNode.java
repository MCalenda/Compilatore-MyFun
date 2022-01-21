package tree.nodes;

import tree.leaves.LeafID;

public class AssignStatNode {
    public String name = "AssignStatNode";
    public LeafID leafID;
    public ExprNode expr;

    public AssignStatNode(LeafID id, ExprNode expr) {
        this.leafID = id;
        this.expr = expr;
    }
}