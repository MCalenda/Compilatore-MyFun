package tree.nodes;

import java.util.ArrayList;

import tree.leaves.LeafID;

public class ReadStatNode {
    public String nome = "ReadStatNode";
    public ArrayList<LeafID> IdList;
    public ExprNode expr;

    public ReadStatNode(ArrayList<LeafID> IdList, ExprNode expr) {
        this.IdList = IdList;
        this.expr = expr;
    }

    public ReadStatNode(ArrayList<LeafID> IdList) {
        this.IdList = IdList;
    }
}