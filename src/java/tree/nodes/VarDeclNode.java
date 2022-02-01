package tree.nodes;

import java.util.ArrayList;

import visitor.CodeGen_Int_Visitable;
import visitor.CodeGen_Int_Visitor;
import visitor.Semantic_Int_Visitable;
import visitor.Semantic_Int_Visitor;
import symbol_table.ValueType;

public class VarDeclNode implements Semantic_Int_Visitable, CodeGen_Int_Visitable {
    // Attributi
    public String name = "VarDeclNode";
    public ArrayList<IdInitNode> idInitList = null;
    public ArrayList<IdInitObblNode> IdListInitObbl = null;

    // Controllo semantico
    public ValueType type = null;

    // Costruttori
    public VarDeclNode(ValueType type, ArrayList<IdInitNode> idInitList) {
        this.type = type;
        this.idInitList = idInitList;
    }

    public VarDeclNode(ArrayList<IdInitObblNode> IdListInitObbl) {
        this.IdListInitObbl = IdListInitObbl;
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