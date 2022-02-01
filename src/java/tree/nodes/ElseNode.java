package tree.nodes;

import java.util.ArrayList;

import visitor.CodeGen_Int_Visitable;
import visitor.CodeGen_Int_Visitor;
import visitor.Semantic_Int_Visitable;
import visitor.Semantic_Int_Visitor;

public class ElseNode implements Semantic_Int_Visitable, CodeGen_Int_Visitable {
    // Attributi
    public String name = "ElseNode";
    public ArrayList<VarDeclNode> varDeclList;
    public ArrayList<StatNode> statList;

    // Costruttore
    public ElseNode(ArrayList<VarDeclNode> varDeclList, ArrayList<StatNode> statList) {
        this.varDeclList = varDeclList;
        this.statList = statList;
    }

    // Metodi polimorfi per i visitor
    @Override
    public void accept(Semantic_Int_Visitor v) {
        v.visit(this);
    }

    @Override
    public void accept(CodeGen_Int_Visitor v) {
        v.visit(this);
    }
}