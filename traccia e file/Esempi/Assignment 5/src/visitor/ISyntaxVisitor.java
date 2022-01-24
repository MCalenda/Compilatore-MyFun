package visitor;

import org.w3c.dom.Element;
import support.leafs.*;
import support.nodes.*;

public interface ISyntaxVisitor {

    public Element visit(ProgramNode node);
    public Element visit(VarDeclNode node);
    public Element visit(TypeNode node);
    public Element visit(IdInitNode node);
    public Element visit(ProcNode node);
    public Element visit(ProcBodyNode node);
    public Element visit(ParDeclNode node);
    public Element visit(ResultTypeNode node);
    public Element visit(IfStatNode node);
    public Element visit(StatListNode nodeList);
    public Element visit(WhileStatNode node);
    public Element visit(ReadLnStatNode node);
    public Element visit(WriteStatNode node);
    public Element visit(AssignStatNode node);
    public Element visit(CallProcNode node);
    public Element visit(ElifNode node);
    public Element visit(ExprNode node);
    public Element visit(LeafBool leaf);
    public Element visit(LeafFloatConst leaf);
    public Element visit(LeafID leaf);
    public Element visit(LeafIntConst leaf);
    public Element visit(LeafNull leaf);
    public Element visit(LeafStringConst leaf);
}
