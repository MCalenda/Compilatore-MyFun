package tree.nodes;

import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import visitor.Syntax_Visitable;
import visitor.Syntax_Int_Visitor;

public class MainNode extends DefaultMutableTreeNode implements Syntax_Visitable {
    public String name = "MainNode";
    public ArrayList<VarDeclNode> varDeclList;
    public ArrayList<StatNode> statList;


    public MainNode(ArrayList<VarDeclNode> varDeclList, ArrayList<StatNode> statList) {
        super("MainNode");
        this.varDeclList = varDeclList;
        this.statList = statList;
    }


    @Override
    public DefaultMutableTreeNode accept(Syntax_Int_Visitor v) {
        return v.visit(this);
    }
}
