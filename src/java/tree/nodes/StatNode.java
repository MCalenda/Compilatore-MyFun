package tree.nodes;

import visitor.CodeGen_Int_Visitable;
import visitor.CodeGen_Int_Visitor;
import visitor.Semantic_Int_Visitable;
import visitor.Semantic_Int_Visitor;

public class StatNode implements Semantic_Int_Visitable, CodeGen_Int_Visitable {
    // Attributi
    public String name = "StatNode";
    public IfStatNode ifStatNode = null;
    public WhileStatNode whileStatNode = null;
    public ReadStatNode readStatNode = null;
    public WriteStatNode writeStatNode = null;
    public AssignStatNode assignStatNode = null;
    public CallFunNode callFunNode = null;
    public ReturnNode returnNode = null;

    // Costruttori
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

    public StatNode(ReturnNode returnNode) {
        this.returnNode = returnNode;
    }

    // Metodi polimorfi per i visitor
    @Override
    public void accept(Semantic_Int_Visitor v) {
        v.visit(this);
    }

    @Override
    public void accept(CodeGen_Int_Visitor v) {
        v.visit(this);
    }
}