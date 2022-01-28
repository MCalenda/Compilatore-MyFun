package support.nodes;

import org.w3c.dom.Element;
import visitor.*;

public class ResultTypeNode implements ISyntaxVisitable, ISemanticVisitable, ICVisitable {

    public String name = null;
    public TypeNode typeNode = null;
    public boolean isVoid = false;

    public ResultTypeNode(String name, TypeNode typeNode) {
        this.name = name;
        this.typeNode = typeNode;
    }

    public ResultTypeNode(String name, boolean isVoid) {
        this.name = name;
        this.isVoid = isVoid;
    }

    @Override
    public String toString() {
        return "ResultTypeNode{" +
                "typeNode=" + typeNode +
                ", isVoid=" + isVoid +
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
