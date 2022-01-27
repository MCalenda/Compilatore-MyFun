package tree.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

import visitor.Semantic_Int_Visitable;
import visitor.Semantic_Int_Visitor;
import visitor.Syntax_Int_Visitable;
import visitor.Syntax_Int_Visitor;

public class StatNode extends DefaultMutableTreeNode implements Syntax_Int_Visitable, Semantic_Int_Visitable {
    public String name = "StatNode";
    public IfStatNode ifStatNode = null;
    public WhileStatNode whileStatNode = null;
    public ReadStatNode readStatNode = null;
    public WriteStatNode writeStatNode = null;
    public AssignStatNode assignStatNode = null;
    public CallFunNode callFunNode = null;
    public ReturnNode resultNode = null;

    public StatNode(IfStatNode ifStatNode) {
        super("StatNode");
        this.ifStatNode = ifStatNode;
    }

    public StatNode(WhileStatNode whileStatNode) {
        super("StatNode");
        this.whileStatNode = whileStatNode;
    }

    public StatNode(ReadStatNode readStatNode) {
        super("StatNode");
        this.readStatNode = readStatNode;
    }

    public StatNode(WriteStatNode writeStatNode) {
        super("StatNode");
        this.writeStatNode = writeStatNode;
    }

    public StatNode(AssignStatNode assignStatNode) {
        super("StatNode");
        this.assignStatNode = assignStatNode;
    }

    public StatNode(CallFunNode callFunNode) {
        super("StatNode");
        this.callFunNode = callFunNode;
    }

    public StatNode(ReturnNode resultNode) {
        super("StatNode");
        this.resultNode = resultNode;
    }

    @Override
    public DefaultMutableTreeNode accept(Syntax_Int_Visitor v) {
        return v.visit(this);
    }

    @Override
    public void accept(Semantic_Int_Visitor v) {
        v.visit(this);
    }
}