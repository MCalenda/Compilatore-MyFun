package tree.nodes;

import java.util.ArrayList;
import javax.swing.tree.DefaultMutableTreeNode;

import visitor.Syntax_Int_Visitable;
import visitor.Syntax_Int_Visitor;

public class VarDeclNode extends DefaultMutableTreeNode implements Syntax_Int_Visitable{
    public String name = "VarDeclNode";
    public TypeNode type = null;
    public ArrayList<IdInitNode> idInitList = null;
    public ArrayList<IdInitObblNode> IdListInitObbl = null;

    public VarDeclNode(TypeNode type, ArrayList<IdInitNode> idInitList) {
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
}