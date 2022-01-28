package tree.nodes;

import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import tree.leaves.LeafID;
import visitor.Semantic_Int_Visitable;
import visitor.Semantic_Int_Visitor;
import visitor.Syntax_Int_Visitable;
import visitor.Syntax_Int_Visitor;

public class ReadStatNode extends DefaultMutableTreeNode implements Syntax_Int_Visitable, Semantic_Int_Visitable {
    public String nome = "ReadStatNode";
    public ArrayList<LeafID> IdList;
    public ExprNode expr;

    public ReadStatNode(ArrayList<LeafID> IdList, ExprNode expr) {
        super("ReadStatNode");
        this.IdList = IdList;
        this.expr = expr;
    }

    public ReadStatNode(ArrayList<LeafID> IdList) {
        super("ReadStatNode");
        this.IdList = IdList;
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