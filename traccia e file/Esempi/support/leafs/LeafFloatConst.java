package support.leafs;

import org.w3c.dom.Element;
import utils.SymbolTable;
import utils.ValueType;
import visitor.*;

public class LeafFloatConst implements ISyntaxVisitable, ISemanticVisitable, ICVisitable {

    public String name = null;
    public Float value;

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

    public LeafFloatConst(Float value) {
        this.value = value;
        this.name = "LeafFloatConst";
    }

    @Override
    public String toString() {
        return "LeafFloatConst{" +
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
