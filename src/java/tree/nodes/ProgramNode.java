package tree.nodes;

import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import visitor.Syntax_Visitable;
import visitor.Syntax_Int_Visitor;

public class ProgramNode extends DefaultMutableTreeNode implements Syntax_Visitable {
    public String name = "ProgramNode";
    public ArrayList<VarDeclNode> varDecList;
    public ArrayList<FunNode> funList;
    public MainNode main;

    public ProgramNode(ArrayList<VarDeclNode> varDecList, ArrayList<FunNode> funList, MainNode main) {
        super("ProgramNode");
        this.varDecList = varDecList;
        this.funList = funList;
        this.main = main;
    }

    @Override
    public DefaultMutableTreeNode accept(Syntax_Int_Visitor v) {
        return v.visit(this);
    }
}