package tree.nodes;

import symbol_table.ValueType;
import tree.leaves.LeafID;
import visitor.CodeGen_Int_Visitable;
import visitor.CodeGen_Int_Visitor;
import visitor.Semantic_Int_Visitable;
import visitor.Semantic_Int_Visitor;

public class ParamDecNode implements Semantic_Int_Visitable, CodeGen_Int_Visitable {
    // Attributi
    public String name = "ParamDecNode";
    public Boolean out;
    public LeafID leafID;

    // Controllo semantico
    public ValueType type = null;

    // Costruttore
    public ParamDecNode(Boolean out, ValueType type, LeafID leafID) {
        this.out = out;
        this.type = type;
        this.leafID = leafID;
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