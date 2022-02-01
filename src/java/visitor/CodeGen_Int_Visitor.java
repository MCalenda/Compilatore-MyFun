package visitor;

import tree.leaves.LeafBool;
import tree.leaves.LeafID;
import tree.leaves.LeafIntegerConst;
import tree.leaves.LeafRealConst;
import tree.leaves.LeafStringConst;
import tree.nodes.*;

// Interfaccia per l'implementazione dei vari metodi polimorfi
public interface CodeGen_Int_Visitor {
    public int visit(ProgramNode node);
    public void visit(VarDeclNode varDeclNode);
    public void visit(FunNode funNode);
    public void visit(MainNode mainNode);
    public void visit(IdInitNode idInitNode);
    public void visit(LeafID leafID);
    public void visit(ExprNode exprNode);
    public void visit(LeafIntegerConst leafIntegerConst);
    public void visit(LeafBool leafBool);
    public void visit(LeafRealConst leafRealConst);
    public void visit(LeafStringConst leafStringConst);
    public void visit(IdInitObblNode idInitObblNode);
    public void visit(ConstNode constNode);
    public void visit(ParamDecNode paramDecNode);
    public void visit(StatNode statNode);
    public void visit(IfStatNode ifStatNode);
    public void visit(WhileStatNode whileStatNode);
    public void visit(ReadStatNode readStatNode);
    public void visit(WriteStatNode writeStatNode);
    public void visit(AssignStatNode assignStatNode);
    public void visit(CallFunNode callFunNode);
    public void visit(ReturnNode returnNode);
    public void visit(ElseNode elseNode);
}