package tree.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

import symbol_table.SymbolTable;
import symbol_table.ValueType;
import visitor.Semantic_Int_Visitable;
import visitor.Semantic_Int_Visitor;
import visitor.Syntax_Int_Visitable;
import visitor.Syntax_Int_Visitor;

public class ExprNode extends DefaultMutableTreeNode implements Syntax_Int_Visitable, Semantic_Int_Visitable {
    public String name = "ExprNode";
    public String op;
    public Object val_One;
    public Object val_Two;

    // Semantic check
    public ValueType type = null;

    public void setType(String t) {
        try {
            this.type = SymbolTable.StringToValueType(t);
        } catch (Exception e) {
            System.exit(0);
            e.printStackTrace();
        }
    }


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

    @Override
    public void accept(Semantic_Int_Visitor v) {
        v.visit(this);
    }    

}