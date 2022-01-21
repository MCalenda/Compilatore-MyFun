package tree.nodes;

public class ExprNode {
    public String name = "ExprNode";
    public String op;
    public Object val_One;
    public Object val_Two;


    public ExprNode(String op, Object val_One) {
        this.op = op;
        this.val_One = val_One;
    }

    public ExprNode(String op, Object val_One, Object val_Two) {
        this.op = op;
        this.val_One = val_One;
        this.val_Two = val_Two;
    }    

}