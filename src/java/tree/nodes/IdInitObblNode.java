package tree.nodes;

import symbol_table.ValueType;
import tree.leaves.LeafID;
import visitor.CodeGen_Int_Visitable;
import visitor.CodeGen_Int_Visitor;
import visitor.Semantic_Int_Visitable;
import visitor.Semantic_Int_Visitor;

public class IdInitObblNode implements Semantic_Int_Visitable, CodeGen_Int_Visitable {
    // Attributi
    public String name = "IdInitObblNode";
    public LeafID leafID;
    public ConstNode value;

    // Controllo semantico
    public ValueType type = null;

    // Costruttore
    public IdInitObblNode(LeafID leafID, ConstNode value) {
        this.leafID = leafID;
        this.value = value;
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