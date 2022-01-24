package tree.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

import visitor.Syntax_Visitable;
import visitor.Syntax_Int_Visitor;

public class TypeNode extends DefaultMutableTreeNode implements Syntax_Visitable{
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
}
