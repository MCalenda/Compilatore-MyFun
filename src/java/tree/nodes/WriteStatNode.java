package tree.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

import visitor.CodeGen_Int_Visitable;
import visitor.CodeGen_Int_Visitor;
import visitor.Semantic_Int_Visitable;
import visitor.Semantic_Int_Visitor;
import visitor.Syntax_Int_Visitable;
import visitor.Syntax_Int_Visitor;

public class WriteStatNode extends DefaultMutableTreeNode implements Syntax_Int_Visitable, Semantic_Int_Visitable, CodeGen_Int_Visitable {
    public String name = "WriteStatNode";
    public ExprNode expr;
    public String op;

    public WriteStatNode(String op, ExprNode expr) {
        super("WriteStatNode");
        this.op = op;
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