package support.leafs;

import org.w3c.dom.Element;
import utils.SymbolTable;
import utils.ValueType;
import visitor.*;

public class LeafBool implements ISyntaxVisitable, ISemanticVisitable, ICVisitable {

    public String name = null;
    public boolean value;

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

    public LeafBool(boolean value) {
        this.value = value;
        this.name = "LeafBool";
    }

    @Override
    public String toString() {
        return "LeafBool{" +
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
