package tree.leaves;

public class LeafRealConst {
    public String name = "LeafRealConst";
    public Double value;

    public LeafRealConst(Double value) {
        this.value = value;
    }

    public String toString() {
        return value.toString();
    }
}