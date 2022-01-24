package tree.leaves;

public class LeafBool {
    public String name = "LeafBool";
    public Boolean value;

    public LeafBool(Boolean value) {
        this.value = value;
    }
    
    public String toString(){
        return value.toString();
    }
}