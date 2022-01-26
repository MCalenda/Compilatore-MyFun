package tree.leaves;

import symbol_table.SymbolTable;
import symbol_table.ValueType;
import visitor.Semantic_Int_Visitable;
import visitor.Semantic_Int_Visitor;

public class LeafBool implements Semantic_Int_Visitable{
    public String name = "LeafBool";
    public Boolean value;

    // Controllo semantico
    public ValueType type = null;

    public void setType(String t) {
        try {
            this.type = SymbolTable.StringToValueType(t);
        } catch (Exception e) {
            System.exit(0);
            e.printStackTrace();
        }
    }

    public LeafBool(Boolean value) {
        this.value = value;
    }
    
    public String toString(){
        return value.toString();
    }

    @Override
    public void accept(Semantic_Int_Visitor v) {
        v.vist(this);        
    }
}