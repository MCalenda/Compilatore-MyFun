package tree.leaves;

import symbol_table.ValueType;
import visitor.CodeGen_Int_Visitable;
import visitor.CodeGen_Int_Visitor;
import visitor.Semantic_Int_Visitable;
import visitor.Semantic_Int_Visitor;

public class LeafStringConst implements Semantic_Int_Visitable, CodeGen_Int_Visitable{
    public String name = "LeafStringConst";
    public String value;

    // Controllo semantico
    public ValueType type = null;

    public LeafStringConst(String value) {
        this.value = value;
    }
    public String toString() {
        return value.toString();
    }

    @Override
    public void accept(Semantic_Int_Visitor v) {
        v.visit(this);
        
    }
    @Override
    public void accept(CodeGen_Int_Visitor v) {
        v.visit(this);
    }
}