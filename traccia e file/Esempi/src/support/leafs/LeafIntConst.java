package support.leafs;

import org.w3c.dom.Element;
import utils.SymbolTable;
import utils.ValueType;
import visitor.*;

public class LeafIntConst implements ISyntaxVisitable, ISemanticVisitable, ICVisitable {

    public String name = null;
    public Integer value;

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

    public LeafIntConst(Integer value) {
        this.value = value;
        this.name = "LeafIntConst";
    }

    @Override
    public String toString() {
        return "LeafIntConst{" +
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
