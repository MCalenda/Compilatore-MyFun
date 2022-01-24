package tree.nodes;

import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import tree.leaves.LeafID;
import visitor.Syntax_Visitable;
import visitor.Syntax_Int_Visitor;

public class CallFunNode extends DefaultMutableTreeNode implements Syntax_Visitable {
    public String name = "CallFunNode";
    public LeafID leafID;
    public ArrayList<ExprNode> exprList;


    public CallFunNode(LeafID leafID, ArrayList<ExprNode> exprList) {
        super("CallFunNode");
        this.leafID = leafID;
        this.exprList = exprList;
    }


    public CallFunNode(LeafID leafID) {
        super("CallFunNode");
        this.leafID = leafID;
    }


    @Override
    public DefaultMutableTreeNode accept(Syntax_Int_Visitor v) {
        return v.visit(this);
    }
}