package tree.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

import symbol_table.ValueType;
import tree.leaves.LeafID;
import visitor.Syntax_Int_Visitable;
import visitor.Syntax_Int_Visitor;

public class ParamDecNode extends DefaultMutableTreeNode implements Syntax_Int_Visitable{
    public String name = "ParamDecNode";
    public Boolean out;
    public ValueType type;
    public LeafID leafID;

    public ParamDecNode(Boolean out, ValueType type, LeafID leafID) {
        super("ParamDecNode");
        this.out = out;
        this.type = type;
        this.leafID = leafID;
    }
    
    @Override
    public DefaultMutableTreeNode accept(Syntax_Int_Visitor v) {
        return v.visit(this);    
    }




}