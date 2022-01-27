package support.nodes;

import org.w3c.dom.Element;
import visitor.*;

import java.util.ArrayList;

public class VarDeclNode implements ISyntaxVisitable, ISemanticVisitable, ICVisitable {

    public String name = null;
    public TypeNode type = null;
    public ArrayList<IdInitNode> identifiers = null;

    public VarDeclNode(String name,  TypeNode type, ArrayList<IdInitNode> identifiers) {
        this.name = name;
        this.type = type;
        this.identifiers = identifiers;
    }

    @Override
    public String toString() {
        return "VarDeclNode{" +
                "type=" + type +
                ", identifiers=" + identifiers +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public Element accept(ISyntaxVisitor v) {
        return v.visit(this);
    }

    @Override
    public void accept(ISemanticVisitor v) { v.visit(this); }

    @Override
    public void accept(ICVisitor v) { v.visit(this); }
}
