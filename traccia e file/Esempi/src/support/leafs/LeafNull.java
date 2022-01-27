package support.leafs;

import org.w3c.dom.Element;
import utils.SymbolTable;
import utils.ValueType;
import visitor.*;

public class LeafNull implements ISyntaxVisitable, ISemanticVisitable, ICVisitable {

    public String name = null;
    public Object value = null;

    // Semantic check
    public ValueType type = null;

    public void setType(String t) {
        try {
            this.type = SymbolTable.StringToType(t);
        } catch (Exception e) {
            System.exit(0);
            e.printStackTrace();
        }
    }

    public LeafNull() {
        this.name = "LeafNull";
    }

    @Override
    public String toString() {
        return "LeafNull{" +
                "value=" + value +
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
