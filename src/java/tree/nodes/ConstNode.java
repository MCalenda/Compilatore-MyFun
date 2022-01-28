package tree.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

import symbol_table.ValueType;
import visitor.CodeGen_Int_Visitable;
import visitor.CodeGen_Int_Visitor;
import visitor.Semantic_Int_Visitable;
import visitor.Semantic_Int_Visitor;
import visitor.Syntax_Int_Visitable;
import visitor.Syntax_Int_Visitor;

public class ConstNode extends DefaultMutableTreeNode implements Syntax_Int_Visitable, Semantic_Int_Visitable, CodeGen_Int_Visitable{
    public String name = "ConstNode";
    public Object value;

    // Controllo semantico
    public ValueType type = null;

    public ConstNode(Object val_One) {
        super("ConstNode");
        this.value = val_One;
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