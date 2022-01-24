package visitor;

import javax.swing.tree.DefaultMutableTreeNode;

import tree.nodes.*;

public interface Syntax_Int_Visitor {
    public DefaultMutableTreeNode visit(ElseNode elseNode);
    public DefaultMutableTreeNode visit(FunNode funNode);
    public DefaultMutableTreeNode visit(VarDeclNode varDeclNode);
    public DefaultMutableTreeNode visit(TypeNode typeNode);
    public DefaultMutableTreeNode visit(IdInitNode idInitNode);
    public DefaultMutableTreeNode visit(ExprNode exprNode);
    public DefaultMutableTreeNode visit(MainNode mainNode);
    public DefaultMutableTreeNode visit(StatNode statNode);
    public DefaultMutableTreeNode visit(IfStatNode ifStatNode);
    public DefaultMutableTreeNode visit(AssignStatNode assignStatNode);
    public DefaultMutableTreeNode visit(CallFunNode callFunNode);
    public DefaultMutableTreeNode visit(WhileStatNode whileStatNode);
    public DefaultMutableTreeNode visit(WriteStatNode writeStatNode);
    public DefaultMutableTreeNode visit(ReadStatNode readStatNode);
    public DefaultMutableTreeNode visit(ParamDecNode paramDecNode);
    public DefaultMutableTreeNode visit(ProgramNode programNode);
    public DefaultMutableTreeNode visit(IdInitObblNode idInitObblNode);
    public DefaultMutableTreeNode visit(ConstNode constNode);
    public DefaultMutableTreeNode visit(ReturnNode resultNode);

}
