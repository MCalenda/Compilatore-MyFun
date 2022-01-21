package tree.nodes;

import java.util.ArrayList;

import tree.leaves.LeafID;

public class FunNode {
    public String name = "FunNode";
    public LeafID leafID;
    public ArrayList<ParamDecNode> paramDecList;
    public TypeNode type;
    public ArrayList<VarDeclNode> varDecList;
    public ArrayList<StatNode> statList;

    public FunNode(LeafID leafID, ArrayList<ParamDecNode> paramDecList, TypeNode type,
            ArrayList<VarDeclNode> varDecList, ArrayList<StatNode> statList) {
        this.leafID = leafID;
        this.paramDecList = paramDecList;
        this.type = type;
        this.varDecList = varDecList;
        this.statList = statList;
    }

    public FunNode(LeafID leafID, ArrayList<ParamDecNode> paramDecList, ArrayList<VarDeclNode> varDecList,
            ArrayList<StatNode> statList) {
        this.leafID = leafID;
        this.paramDecList = paramDecList;
        this.varDecList = varDecList;
        this.statList = statList;
    }
}