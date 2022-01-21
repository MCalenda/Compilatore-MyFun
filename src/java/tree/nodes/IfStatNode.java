package tree.nodes;

import java.util.ArrayList;

public class IfStatNode {
    public String name = "IfStatNode";
    public ExprNode expr;
    public ArrayList<VarDeclNode> varDeclList;
    public ArrayList<StatNode> statList;
    public ElseNode elseNode;

    public IfStatNode(ExprNode expr, ArrayList<VarDeclNode> listDecl, ArrayList<StatNode> statList, ElseNode elseNode) {
        this.expr = expr;
        this.varDeclList = listDecl;
        this.statList = statList;
        this.elseNode = elseNode;
    }
}