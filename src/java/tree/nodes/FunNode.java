package tree.nodes;

import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import tree.leaves.LeafID;
import visitor.Syntax_Visitable;
import visitor.Syntax_Int_Visitor;

public class FunNode extends DefaultMutableTreeNode implements Syntax_Visitable {
    public String name = "FunNode";
    public LeafID leafID;
    public ArrayList<ParamDecNode> paramDecList;
    public TypeNode type;
    public ArrayList<VarDeclNode> varDecList;
    public ArrayList<StatNode> statList;

    public FunNode(LeafID leafID, ArrayList<ParamDecNode> paramDecList, TypeNode type,
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
}