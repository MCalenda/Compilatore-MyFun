package tree.leaves;

import symbol_table.ValueType;
import visitor.CodeGen_Int_Visitable;
import visitor.CodeGen_Int_Visitor;
import visitor.Semantic_Int_Visitable;
import visitor.Semantic_Int_Visitor;

public class LeafID implements Semantic_Int_Visitable, CodeGen_Int_Visitable{
    public String name = "LeafID";
    public String value;
    
    // Controllo semantico
    public ValueType type = null;

    public LeafID(String value) {
        this.value = value;
    }
    
    public String toString(){
        return value;
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
