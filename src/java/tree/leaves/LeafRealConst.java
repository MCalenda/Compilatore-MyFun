package tree.leaves;

import symbol_table.SymbolTable;
import symbol_table.ValueType;
import visitor.Semantic_Int_Visitable;
import visitor.Semantic_Int_Visitor;

public class LeafRealConst implements Semantic_Int_Visitable{
    public String name = "LeafRealConst";
    public Double value;
    
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

    public LeafRealConst(Double value) {
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