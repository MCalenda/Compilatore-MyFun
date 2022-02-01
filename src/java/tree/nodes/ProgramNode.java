package tree.nodes;

import java.util.ArrayList;

import visitor.CodeGen_Int_Visitable;
import visitor.CodeGen_Int_Visitor;

public class ProgramNode implements CodeGen_Int_Visitable {
    // Attributi
    public String name = "ProgramNode";
    public ArrayList<VarDeclNode> varDecList;
    public ArrayList<FunNode> funList;
    public MainNode main;

    // Costruttore
    public ProgramNode(ArrayList<VarDeclNode> varDecList, ArrayList<FunNode> funList, MainNode main) {
        this.varDecList = varDecList;
        this.funList = funList;
        this.main = main;
    }

    // Metodo polimorfi per i visitor
    @Override
    public void accept(CodeGen_Int_Visitor v) {
        v.visit(this);
    }
}