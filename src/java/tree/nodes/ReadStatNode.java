package tree.nodes;

import java.util.ArrayList;

import tree.leaves.LeafID;
import visitor.CodeGen_Int_Visitable;
import visitor.CodeGen_Int_Visitor;
import visitor.Semantic_Int_Visitable;
import visitor.Semantic_Int_Visitor;

public class ReadStatNode implements Semantic_Int_Visitable, CodeGen_Int_Visitable {
    // Attributi
    public String nome = "ReadStatNode";
    public ArrayList<LeafID> IdList;
    public ExprNode expr;

    // Costruttori
    public ReadStatNode(ArrayList<LeafID> IdList, ExprNode expr) {
        this.IdList = IdList;
        this.expr = expr;
    }

    public ReadStatNode(ArrayList<LeafID> IdList) {
        this.IdList = IdList;
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