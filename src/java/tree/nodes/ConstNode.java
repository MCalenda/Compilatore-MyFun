package tree.nodes;

import symbol_table.ValueType;
import visitor.CodeGen_Int_Visitable;
import visitor.CodeGen_Int_Visitor;
import visitor.Semantic_Int_Visitable;
import visitor.Semantic_Int_Visitor;

public class ConstNode implements Semantic_Int_Visitable, CodeGen_Int_Visitable {
    // Attributi
    public String name = "ConstNode";
    public Object value;

    // Controllo semantico
    public ValueType type = null;

    // Costruttore
    public ConstNode(Object val_One) {
        this.value = val_One;
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