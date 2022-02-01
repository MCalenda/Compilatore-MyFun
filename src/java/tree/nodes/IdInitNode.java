package tree.nodes;

import symbol_table.ValueType;
import tree.leaves.LeafID;
import visitor.CodeGen_Int_Visitable;
import visitor.CodeGen_Int_Visitor;
import visitor.Semantic_Int_Visitable;
import visitor.Semantic_Int_Visitor;

public class IdInitNode implements Semantic_Int_Visitable, CodeGen_Int_Visitable {
    // Attributi
    public String name = "IdInitNode";
    public LeafID leafID = null;
    public ExprNode exprNode = null;

    // Controllo semantico
    public ValueType type = null;

    // Costruttori
    public IdInitNode(LeafID leafID) {
        this.leafID = leafID;
    }

    public IdInitNode(LeafID leafID, ExprNode exprNode) {
        this.leafID = leafID;
        this.exprNode = exprNode;
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