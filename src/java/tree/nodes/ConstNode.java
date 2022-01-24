package tree.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

import visitor.Syntax_Int_Visitable;
import visitor.Syntax_Int_Visitor;

public class ConstNode extends DefaultMutableTreeNode implements Syntax_Int_Visitable {
    public String name = "ConstNode";
    public Object value;

    public ConstNode(Object val_One) {
        super("ConstNode");
        this.value = val_One;
    }

    @Override
    public DefaultMutableTreeNode accept(Syntax_Int_Visitor v) {
        return v.visit(this);
    }
}