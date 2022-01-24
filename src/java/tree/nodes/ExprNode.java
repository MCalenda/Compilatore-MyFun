package tree.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

import visitor.Syntax_Int_Visitable;
import visitor.Syntax_Int_Visitor;

public class ExprNode extends DefaultMutableTreeNode implements Syntax_Int_Visitable {
    public String name = "ExprNode";
    public String op;
    public Object val_One;
    public Object val_Two;


    public ExprNode(String op, Object val_One) {
        super("ExprNode");
        this.op = op;
        this.val_One = val_One;
    }

    public ExprNode(String op, Object val_One, Object val_Two) {
        super("ExprNode");
        this.op = op;
        this.val_One = val_One;
        this.val_Two = val_Two;
    }

  
    @Override
    public DefaultMutableTreeNode accept(Syntax_Int_Visitor v) {
        return v.visit(this);
    }    

}