package tree.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

import tree.leaves.LeafID;
import visitor.CodeGen_Int_Visitable;
import visitor.CodeGen_Int_Visitor;
import visitor.Semantic_Int_Visitable;
import visitor.Semantic_Int_Visitor;
import visitor.Syntax_Int_Visitable;
import visitor.Syntax_Int_Visitor;

public class AssignStatNode extends DefaultMutableTreeNode implements Syntax_Int_Visitable, Semantic_Int_Visitable, CodeGen_Int_Visitable {
    public String name = "AssignStatNode";
    public LeafID leafID;
    public ExprNode expr;

    public AssignStatNode(LeafID id, ExprNode expr) {
        super("AssignStatNode");
        this.leafID = id;
        this.expr = expr;
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