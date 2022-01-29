package tree.nodes;

import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import visitor.CodeGen_Int_Visitable;
import visitor.CodeGen_Int_Visitor;
import visitor.Semantic_Int_Visitable;
import visitor.Semantic_Int_Visitor;
import visitor.Syntax_Int_Visitable;
import visitor.Syntax_Int_Visitor;

public class IfStatNode extends DefaultMutableTreeNode implements Syntax_Int_Visitable, Semantic_Int_Visitable, CodeGen_Int_Visitable {
    public String name = "IfStatNode";
    public ExprNode expr;
    public ArrayList<VarDeclNode> varDeclList;
    public ArrayList<StatNode> statList;
    public ElseNode elseNode;

    public IfStatNode(ExprNode expr, ArrayList<VarDeclNode> listDecl, ArrayList<StatNode> statList, ElseNode elseNode) {
        super("IfStatNode");
        this.expr = expr;
        this.varDeclList = listDecl;
        this.statList = statList;
        this.elseNode = elseNode;
    }

    @Override
    public DefaultMutableTreeNode accept(Syntax_Int_Visitor v) {
        return v.visit(this);
    }

    @Override
    public void accept(Semantic_Int_Visitor v) {
        v.visit(this);
    }

    @Override
    public void accept(CodeGen_Int_Visitor v) {
        v.visit(this);
    }
}