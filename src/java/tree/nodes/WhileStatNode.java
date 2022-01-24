package tree.nodes;

import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import visitor.Syntax_Visitable;
import visitor.Syntax_Int_Visitor;

public class WhileStatNode extends DefaultMutableTreeNode implements Syntax_Visitable {
    public String name = "WhileStatNode";
    public ExprNode expr;
    public ArrayList<VarDeclNode> varDeclList;
    public ArrayList<StatNode> statList;

    public WhileStatNode(ExprNode expr, ArrayList<VarDeclNode> listDecl, ArrayList<StatNode> statList) {
        super("WhileStatNode");
        this.expr = expr;
        this.varDeclList = listDecl;
        this.statList = statList;
    }

    @Override
    public DefaultMutableTreeNode accept(Syntax_Int_Visitor v) {
        return v.visit(this);
    }

}