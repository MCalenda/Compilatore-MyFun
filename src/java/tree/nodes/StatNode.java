package tree.nodes;

public class StatNode {
    public String name = "StatNode";
    public IfStatNode ifStatNode = null;
    public WhileStatNode whileStatNode = null;
    public ReadStatNode readStatNode = null;
    public WriteStatNode writeStatNode = null;
    public AssignStatNode assignStatNode = null;
    public CallFunNode callFunNode = null;
    public ExprNode expr = null;

    public StatNode(IfStatNode ifStatNode) {
        this.ifStatNode = ifStatNode;
    }

    public StatNode(WhileStatNode whileStatNode) {
        this.whileStatNode = whileStatNode;
    }

    public StatNode(ReadStatNode readStatNode) {
        this.readStatNode = readStatNode;
    }

    public StatNode(WriteStatNode writeStatNode) {
        this.writeStatNode = writeStatNode;
    }

    public StatNode(AssignStatNode assignStatNode) {
        this.assignStatNode = assignStatNode;
    }

    public StatNode(CallFunNode callFunNode) {
        this.callFunNode = callFunNode;
    }

    public StatNode(ExprNode expr) {
        this.expr = expr;
    }
}