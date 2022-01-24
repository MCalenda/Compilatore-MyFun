package support.nodes;

import org.w3c.dom.Element;
import support.leafs.LeafID;
import visitor.*;

import java.util.ArrayList;

public class ReadLnStatNode implements StatNode, ISyntaxVisitable, ISemanticVisitable, ICVisitable {

    public String name = null;
    public ArrayList<LeafID> idList;

    public ReadLnStatNode(String name, ArrayList<LeafID> idList) {
        this.name = name;
        this.idList = idList;
    }

    @Override
    public String toString() {
        return "ReadLnStatNode{" +
                "idList=" + idList +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public Element accept(ISyntaxVisitor v) { return v.visit(this); }

    @Override
    public void accept(ISemanticVisitor v) { v.visit(this); }

    @Override
    public void accept(ICVisitor v) { v.visit(this); }
}
