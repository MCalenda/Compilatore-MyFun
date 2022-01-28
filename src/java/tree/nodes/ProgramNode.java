package tree.nodes;

import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import visitor.CodeGen_Int_Visitable;
import visitor.CodeGen_Int_Visitor;
import visitor.Syntax_Int_Visitable;
import visitor.Syntax_Int_Visitor;

public class ProgramNode extends DefaultMutableTreeNode implements Syntax_Int_Visitable, CodeGen_Int_Visitable{
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

    @Override
    public void accept(CodeGen_Int_Visitor v) {
        v.visit(this);
    }
}