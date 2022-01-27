package support.nodes;

import org.w3c.dom.Element;
import support.leafs.LeafID;
import visitor.*;

import java.util.ArrayList;

public class ParDeclNode implements ISyntaxVisitable, ISemanticVisitable, ICVisitable {

    public String name = null;
    public TypeNode typeNode = null;
    public ArrayList<LeafID> identifiers = null;

    public ParDeclNode(String name, TypeNode typeNode, ArrayList<LeafID> identifiers) {
        this.name = name;
        this.typeNode = typeNode;
        this.identifiers = identifiers;
    }

    public void addIdentifier(LeafID id) { identifiers.add(0, id); }

    @Override
    public String toString() {
        return "ParDeclNode{" +
                "typeNode=" + typeNode +
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
