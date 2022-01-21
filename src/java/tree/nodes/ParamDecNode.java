package tree.nodes;

import tree.leaves.LeafID;

public class ParamDecNode {
    public String name = "ParamDecNode";
    public Boolean out;
    public TypeNode type;
    public LeafID leafID;

    public ParamDecNode(Boolean out, TypeNode type, LeafID leafID) {
        this.out = out;
        this.type = type;
        this.leafID = leafID;
    }

}