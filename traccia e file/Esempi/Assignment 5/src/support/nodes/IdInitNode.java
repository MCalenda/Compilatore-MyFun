package support.nodes;

import org.w3c.dom.Element;
import support.leafs.LeafID;
import utils.SymbolTable;
import utils.ValueType;
import visitor.*;

public class IdInitNode implements ISyntaxVisitable, ISemanticVisitable, ICVisitable {

    public String name = null;
    public LeafID varName = null;
    public ExprNode initValue = null;

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

    public IdInitNode(LeafID varName, ExprNode initValue) {
        this.name = "IdInitOP";
        this.varName = varName;
        this.initValue = initValue;
    }

    public IdInitNode(LeafID varName) {
        this.name = "IdInitOP";
        this.varName = varName;
        this.initValue = null;
    }

    @Override
    public String toString() {
        return "IdInitNode{" +
                "varName=" + varName +
                ", initValue=" + initValue +
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
