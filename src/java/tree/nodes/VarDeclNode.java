package tree.nodes;

import java.util.ArrayList;
import javax.swing.tree.DefaultMutableTreeNode;

import visitor.Semantic_Int_Visitable;
import visitor.Semantic_Int_Visitor;
import visitor.Syntax_Int_Visitable;
import visitor.Syntax_Int_Visitor;
import symbol_table.ValueType;

public class VarDeclNode extends DefaultMutableTreeNode implements Syntax_Int_Visitable, Semantic_Int_Visitable{
    public String name = "VarDeclNode";
    public ValueType type = null;
    public ArrayList<IdInitNode> idInitList = null;
    public ArrayList<IdInitObblNode> IdListInitObbl = null;

    public VarDeclNode(ValueType type, ArrayList<IdInitNode> idInitList) {
        super("VarDeclNode");
        this.type = type;
        this.idInitList = idInitList;
    }

    public VarDeclNode(ArrayList<IdInitObblNode> IdListInitObbl) {
        super("VarDeclNode");
        this.IdListInitObbl = IdListInitObbl;
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