package tree.nodes;

import java.util.ArrayList;

import symbol_table.ValueType;
import tree.leaves.LeafID;
import visitor.CodeGen_Int_Visitable;
import visitor.CodeGen_Int_Visitor;
import visitor.Semantic_Int_Visitable;
import visitor.Semantic_Int_Visitor;

public class CallFunNode implements Semantic_Int_Visitable, CodeGen_Int_Visitable {
    // Attributi
    public String name = "CallFunNode";
    public LeafID leafID;
    public ArrayList<ExprNode> exprList;

    // Controllo semantico
    public ValueType type = null;

    // Costruttori
    public CallFunNode(LeafID leafID, ArrayList<ExprNode> exprList) {
        this.leafID = leafID;
        this.exprList = exprList;
    }

    public CallFunNode(LeafID leafID) {
        this.leafID = leafID;
        this.exprList = new ArrayList<ExprNode>();
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