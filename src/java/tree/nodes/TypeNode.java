package tree.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

import visitor.Semantic_Int_Visitable;
import visitor.Semantic_Int_Visitor;
import visitor.Syntax_Int_Visitable;
import visitor.Syntax_Int_Visitor;

public class TypeNode extends DefaultMutableTreeNode implements Syntax_Int_Visitable, Semantic_Int_Visitable{
    public String name = "TypeNode";
    public String type;

    public TypeNode(String type) {
        super("TypeNode");
        this.type = type;
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
