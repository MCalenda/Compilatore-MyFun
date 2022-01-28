package support.nodes;

import org.w3c.dom.Element;
import utils.SymbolTable;
import utils.ValueType;
import visitor.*;

import java.util.ArrayList;

public class ExprNode implements ISyntaxVisitable, ISemanticVisitable, ICVisitable {

    public String name = null;
    public Object value1 = null;
    public Object value2 = null;

    // Semantic check
    public ArrayList<ValueType> types = new ArrayList<>();

    public void setTypes(ArrayList t) {
        try {
            for (int i = 0; i < t.size(); i++) {
                if (t.get(i) instanceof String)
                    this.types.add(SymbolTable.StringToType((String) t.get(i)));
                else this.types.add((ValueType) t.get(i));
            }
        } catch (Exception e) {
            System.exit(0);
            e.printStackTrace();
        }
    }

    public void setType(String t) {
        try {
            this.types.add(SymbolTable.StringToType(t));
        } catch (Exception e) {
            System.exit(0);
            e.printStackTrace();
        }
    }

    public void setType(ValueType t) {
        try {
            this.types.add(t);
        } catch (Exception e) {
            System.exit(0);
            e.printStackTrace();
        }
    }

    public ExprNode(String name, Object n) {
        this.name = name;
        this.value1 = n;
    }

    public ExprNode(String name, Object n1, Object n2) {
        this.name = name;
        this.value1 = n1;
        this.value2 = n2;
    }

    @Override
    public String toString() {
        return "ExprNode{" +
                "value1=" + value1 +
                ", value2=" + value2 +
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
