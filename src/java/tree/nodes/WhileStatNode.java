package tree.nodes;

import java.util.ArrayList;

public class WhileStatNode {
    public String name = "WhileStatNode";
    public ExprNode expr;
    public ArrayList<VarDeclNode> varDeclList;
    public ArrayList<StatNode> statList;

    public WhileStatNode(ExprNode expr, ArrayList<VarDeclNode> listDecl, ArrayList<StatNode> statList) {
        this.expr = expr;
        this.varDeclList = listDecl;
        this.statList = statList;
    }

}