package tree.nodes;

import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import visitor.Syntax_Visitable;
import visitor.Syntax_Int_Visitor;

public class ElseNode extends DefaultMutableTreeNode implements Syntax_Visitable{
    public String name = "ElseNode";
    public ArrayList<VarDeclNode> varDeclList;
    public ArrayList<StatNode> statList;

    public ElseNode(ArrayList<VarDeclNode> varDeclList, ArrayList<StatNode> statList) {
        super("ElseNode");
        this.varDeclList = varDeclList;
        this.statList = statList;
    }

    @Override
    public DefaultMutableTreeNode accept(Syntax_Int_Visitor v) {
        return v.visit(this);
    }
}