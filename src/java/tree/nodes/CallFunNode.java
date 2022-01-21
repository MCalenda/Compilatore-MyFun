package tree.nodes;

import java.util.ArrayList;

import tree.leaves.LeafID;

public class CallFunNode {
    public String name = "CallFunNode";
    public LeafID leafID;
    public ArrayList<ExprNode> exprList;


    public CallFunNode(LeafID leafID, ArrayList<ExprNode> exprList) {
        this.leafID = leafID;
        this.exprList = exprList;
    }


    public CallFunNode(LeafID leafID) {
        this.leafID = leafID;
    }
}