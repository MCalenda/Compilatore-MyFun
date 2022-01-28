package tree.leaves;

import symbol_table.ValueType;
import visitor.CodeGen_Int_Visitable;
import visitor.CodeGen_Int_Visitor;
import visitor.Semantic_Int_Visitable;
import visitor.Semantic_Int_Visitor;

public class LeafBool implements Semantic_Int_Visitable, CodeGen_Int_Visitable{
    public String name = "LeafBool";
    public Boolean value;

    // Controllo semantico
    public ValueType type = null;

    public LeafBool(Boolean value) {
        this.value = value;
    }
    
    public String toString(){
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