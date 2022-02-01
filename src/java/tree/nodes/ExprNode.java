package tree.nodes;

import symbol_table.ValueType;
import visitor.CodeGen_Int_Visitable;
import visitor.CodeGen_Int_Visitor;
import visitor.Semantic_Int_Visitable;
import visitor.Semantic_Int_Visitor;

public class ExprNode implements Semantic_Int_Visitable, CodeGen_Int_Visitable {
    // Attributi
    public String name = "ExprNode";
    public String op;
    public Object val_One;
    public Object val_Two;

    // Controllo semantico
    public ValueType type = null;

    // Costruttori
    public ExprNode(String op, Object val_One) {
        this.op = op;
        this.val_One = val_One;
    }

    public ExprNode(String op, Object val_One, Object val_Two) {
        this.op = op;
        this.val_One = val_One;
        this.val_Two = val_Two;
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