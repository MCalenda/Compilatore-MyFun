package tree.nodes;

import java.util.ArrayList;

import symbol_table.ValueType;
import tree.leaves.LeafID;
import visitor.CodeGen_Int_Visitable;
import visitor.CodeGen_Int_Visitor;
import visitor.Semantic_Int_Visitable;
import visitor.Semantic_Int_Visitor;

public class FunNode implements Semantic_Int_Visitable, CodeGen_Int_Visitable {
    // Attributi
    public String name = "FunNode";
    public LeafID leafID;
    public ArrayList<ParamDecNode> paramDecList;
    public ArrayList<VarDeclNode> varDecList;
    public ArrayList<StatNode> statList;

    // Controllo semantico
    public ValueType type = null;

    // Costruttori
    public FunNode(LeafID leafID, ArrayList<ParamDecNode> paramDecList, ValueType type, ArrayList<VarDeclNode> varDecList, ArrayList<StatNode> statList) {
        this.leafID = leafID;
        this.paramDecList = paramDecList;
        this.type = type;
        this.varDecList = varDecList;
        this.statList = statList;
    }

    public FunNode(LeafID leafID, ArrayList<ParamDecNode> paramDecList, ArrayList<VarDeclNode> varDecList, ArrayList<StatNode> statList) {
        this.leafID = leafID;
        this.paramDecList = paramDecList;
        this.varDecList = varDecList;
        this.statList = statList;
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