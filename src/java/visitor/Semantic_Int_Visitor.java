package visitor;

import tree.nodes.*;

public interface Semantic_Int_Visitor {
    
    public void visit(ElseNode elseNode);
    public void visit(FunNode funNode);
    public void visit(VarDeclNode varDeclNode);
    public void visit(TypeNode typeNode);
    public void visit(IdInitNode idInitNode);
    public void visit(ExprNode exprNode);
    public void visit(MainNode mainNode);
    public void visit(StatNode statNode);
    public void visit(IfStatNode ifStatNode);
    public void visit(AssignStatNode assignStatNode);
    public void visit(CallFunNode callFunNode);
    public void visit(WhileStatNode whileStatNode);
    public void visit(WriteStatNode writeStatNode);
    public void visit(ReadStatNode readStatNode);
    public void visit(ParamDecNode paramDecNode);
    public void visit(ProgramNode programNode);
    public void visit(IdInitObblNode idInitObblNode);
    public void visit(ConstNode constNode);
    public void visit(ReturnNode resultNode);

}
