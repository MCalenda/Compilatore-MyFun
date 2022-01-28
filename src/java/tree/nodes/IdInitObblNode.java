package tree.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

import symbol_table.SymbolTable;
import symbol_table.ValueType;
import tree.leaves.LeafID;
import visitor.CodeGen_Int_Visitable;
import visitor.CodeGen_Int_Visitor;
import visitor.Semantic_Int_Visitable;
import visitor.Semantic_Int_Visitor;
import visitor.Syntax_Int_Visitable;
import visitor.Syntax_Int_Visitor;

public class IdInitObblNode extends DefaultMutableTreeNode implements Syntax_Int_Visitable, Semantic_Int_Visitable, CodeGen_Int_Visitable{
    public String name = "IdInitObblNode";
    public LeafID leafID;
    public ConstNode value;

    // Controllo semantico
    public ValueType type = null;

    public IdInitObblNode(LeafID leafID, ConstNode value) {
        super("IdInitObblNode");
        this.leafID = leafID;
        this.value = value;
    }

    public void setType(String t) {
        try {
            this.type = SymbolTable.StringToValueType(t);
        } catch (Exception e) {
            System.exit(0);
            e.printStackTrace();
        }
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