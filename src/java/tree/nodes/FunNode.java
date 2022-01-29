package tree.nodes;

import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import symbol_table.ValueType;
import tree.leaves.LeafID;
import visitor.CodeGen_Int_Visitable;
import visitor.CodeGen_Int_Visitor;
import visitor.Semantic_Int_Visitable;
import visitor.Semantic_Int_Visitor;
import visitor.Syntax_Int_Visitable;
import visitor.Syntax_Int_Visitor;

public class FunNode extends DefaultMutableTreeNode implements Syntax_Int_Visitable, Semantic_Int_Visitable, CodeGen_Int_Visitable {
    public String name = "FunNode";
    public LeafID leafID;
    public ArrayList<ParamDecNode> paramDecList;
    public ArrayList<VarDeclNode> varDecList;
    public ArrayList<StatNode> statList;

    // Controllo semantico
    public ValueType type = null;

    public FunNode(LeafID leafID, ArrayList<ParamDecNode> paramDecList, ValueType type,
            ArrayList<VarDeclNode> varDecList, ArrayList<StatNode> statList) {
        super("FunNode");
        this.leafID = leafID;
        this.paramDecList = paramDecList;
        this.type = type;
        this.varDecList = varDecList;
        this.statList = statList;
    }

    public FunNode(LeafID leafID, ArrayList<ParamDecNode> paramDecList, ArrayList<VarDeclNode> varDecList,
            ArrayList<StatNode> statList) {
        super("FunNode");
        this.leafID = leafID;
        this.paramDecList = paramDecList;
        this.varDecList = varDecList;
        this.statList = statList;
    }

    @Override
    public DefaultMutableTreeNode accept(Syntax_Int_Visitor v) {
        return v.visit(this);
    }

    @Override
    public void accept(Semantic_Int_Visitor v) {
        v.visit(this);
    }

    @Override
    public void accept(CodeGen_Int_Visitor v) {
        v.visit(this);
    }
}