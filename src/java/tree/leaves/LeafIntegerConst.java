package tree.leaves;

import symbol_table.ValueType;
import visitor.CodeGen_Int_Visitable;
import visitor.CodeGen_Int_Visitor;
import visitor.Semantic_Int_Visitable;
import visitor.Semantic_Int_Visitor;

public class LeafIntegerConst implements Semantic_Int_Visitable, CodeGen_Int_Visitable {
    // Attributi
    public String name = "LeafIntegerConst";
    public Integer value;

    // Controllo semantico
    public ValueType type = null;

    // Costruttore
    public LeafIntegerConst(Integer value) {
        this.value = value;
    }

    public String toString() {
        return value.toString();
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