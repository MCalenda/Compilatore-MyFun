package tree.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

import symbol_table.SymbolTable;
import symbol_table.ValueType;
import tree.leaves.LeafID;
import visitor.Semantic_Int_Visitable;
import visitor.Semantic_Int_Visitor;
import visitor.Syntax_Int_Visitable;
import visitor.Syntax_Int_Visitor;

public class IdInitNode extends DefaultMutableTreeNode implements Syntax_Int_Visitable, Semantic_Int_Visitable {
    public String name = "IdInitNode";
    public LeafID leafID = null;
    public ExprNode exprNode = null;

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

    public IdInitNode(LeafID leafID) {
        super("IdInitNode");
        this.leafID = leafID;
    }

    public IdInitNode(LeafID leafID, ExprNode exprNode) {
        super("IdInitNode");
        this.leafID = leafID;
        this.exprNode = exprNode;
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