package tree.nodes;

import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import symbol_table.ValueType;
import tree.leaves.LeafID;
import visitor.Semantic_Int_Visitable;
import visitor.Semantic_Int_Visitor;
import visitor.Syntax_Int_Visitable;
import visitor.Syntax_Int_Visitor;

public class CallFunNode extends DefaultMutableTreeNode implements Syntax_Int_Visitable, Semantic_Int_Visitable {
    public String name = "CallFunNode";
    public LeafID leafID;
    public ArrayList<ExprNode> exprList;
    
    // Controllo semantico
    public ArrayList<ValueType> types = new ArrayList<>();


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

    @Override
    public void accept(Semantic_Int_Visitor v) {
        v.visit(this); 
    }
}