package visitor;

import support.leafs.*;
import support.nodes.*;

public interface ISemanticVisitor {

    public void visit(ProgramNode node);
    public void visit(VarDeclNode node);
    public void visit(TypeNode node);
    public void visit(IdInitNode node);
    public void visit(ProcNode node);
    public void visit(ProcBodyNode node);
    public void visit(ParDeclNode node);
    public void visit(ResultTypeNode node);
    public void visit(IfStatNode node);
    public void visit(StatListNode nodeList);
    public void visit(WhileStatNode node);
    public void visit(ReadLnStatNode node);
    public void visit(WriteStatNode node);
    public void visit(AssignStatNode node);
    public void visit(CallProcNode node);
    public void visit(ElifNode node);
    public void visit(ExprNode node);
    public void visit(LeafBool leaf);
    public void visit(LeafFloatConst leaf);
    public void visit(LeafID leaf);
    public void visit(LeafIntConst leaf);
    public void visit(LeafNull leaf);
    public void visit(LeafStringConst leaf);
}
