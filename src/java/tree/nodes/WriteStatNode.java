package tree.nodes;

import symbol_table.ValueType;
import visitor.CodeGen_Int_Visitable;
import visitor.CodeGen_Int_Visitor;
import visitor.Semantic_Int_Visitable;
import visitor.Semantic_Int_Visitor;

public class WriteStatNode implements Semantic_Int_Visitable, CodeGen_Int_Visitable {
    // Attributi
    public String name = "WriteStatNode";
    public ExprNode expr;
    public String op;

    // Controllo semantico
    public ValueType type = null;

    // Costruttore
    public WriteStatNode(String op, ExprNode expr) {
        this.op = op;
        this.expr = expr;
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