package tree.nodes;

public class WriteStatNode {
    public String name = "WriteStatNode";
    public ExprNode expr;
    public String op;

    public WriteStatNode(String op, ExprNode expr) {
        this.op = op;
        this.expr = expr;
    }
}