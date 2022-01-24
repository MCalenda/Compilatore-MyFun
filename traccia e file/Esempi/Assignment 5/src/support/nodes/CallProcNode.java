package support.nodes;

import org.w3c.dom.Element;
import support.leafs.LeafID;
import utils.SymbolTable;
import utils.ValueType;
import visitor.*;

import java.util.ArrayList;

public class CallProcNode implements StatNode, ISyntaxVisitable, ISemanticVisitable, ICVisitable {

    public String name = null;
    public LeafID leafID = null;
    public ArrayList<ExprNode> exprList = null;

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

    public CallProcNode(String name, LeafID leafID){
        this.name = name;
        this.leafID = leafID;
    }

    public CallProcNode(String name, LeafID leafID, ArrayList<ExprNode> exprList){
        this.name = name;
        this.leafID = leafID;
        this.exprList = exprList;
    }

    @Override
    public String toString() {
        return "CallProcNode{" +
                "leafID=" + leafID +
                ", exprList=" + exprList +
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
