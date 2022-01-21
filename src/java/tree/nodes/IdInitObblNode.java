package tree.nodes;

import tree.leaves.LeafID;

public class IdInitObblNode {
    public String name = "IdInitObblNode";
    public LeafID leafID;
    public Object value;

    public IdInitObblNode(LeafID leafID, Object value) {
        this.leafID = leafID;
        this.value = value;
    }
}