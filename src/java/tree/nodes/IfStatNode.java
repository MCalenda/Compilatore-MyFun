package tree.nodes;

import java.util.ArrayList;

import visitor.CodeGen_Int_Visitable;
import visitor.CodeGen_Int_Visitor;
import visitor.Semantic_Int_Visitable;
import visitor.Semantic_Int_Visitor;

public class IfStatNode implements Semantic_Int_Visitable, CodeGen_Int_Visitable {
    // Attributi
    public String name = "IfStatNode";
    public ExprNode expr;
    public ArrayList<VarDeclNode> varDeclList;
    public ArrayList<StatNode> statList;
    public ElseNode elseNode;

    // Costruttore
    public IfStatNode(ExprNode expr, ArrayList<VarDeclNode> listDecl, ArrayList<StatNode> statList, ElseNode elseNode) {
        this.expr = expr;
        this.varDeclList = listDecl;
        this.statList = statList;
        this.elseNode = elseNode;
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