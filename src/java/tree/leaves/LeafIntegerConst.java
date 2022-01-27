package tree.leaves;

import symbol_table.ValueType;
import visitor.Semantic_Int_Visitable;
import visitor.Semantic_Int_Visitor;

public class LeafIntegerConst implements Semantic_Int_Visitable{
    public String name = "LeafIntegerConst";
    public Integer value;

    // Controllo semantico
    public ValueType type = null;

    public LeafIntegerConst(Integer value) {
        this.value = value;
    }

    public String toString() {
        return value.toString();
    }

    @Override
    public void accept(Semantic_Int_Visitor v) {
        v.visit(this);
        
    }
}