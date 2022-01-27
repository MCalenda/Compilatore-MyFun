package support.nodes;

import org.w3c.dom.Element;
import visitor.*;

import java.util.ArrayList;

public class IfStatNode implements StatNode, ISyntaxVisitable, ISemanticVisitable, ICVisitable {

    //ifStat ::= IF Expr:expr THEN StatList:statlist ElifList:eliflist Else:else1 FI
    //ifStat ::= if CONDITION then IFBODY ELIFLIST else ELSEBODY

    public String name = null;
    public ExprNode condition = null;
    public StatListNode ifBody = null;
    public ArrayList<ElifNode> elifList = null;
    public StatListNode elseBody = null;

    public IfStatNode(String name, ExprNode condition, StatListNode ifBody, ArrayList<ElifNode> elifList, StatListNode elseBody) {
        this.name = name;
        this.condition = condition;
        this.ifBody = ifBody;
        this.elifList = elifList;
        this.elseBody = elseBody;
    }

    public IfStatNode(String name, ExprNode condition, StatListNode ifBody, ArrayList<ElifNode> elifList) {
        this.name = name;
        this.condition = condition;
        this.ifBody = ifBody;
        this.elifList = elifList;
        this.elseBody = null;
    }

    @Override
    public Element accept(ISyntaxVisitor v) {
        return v.visit(this);
    }

    @Override
    public void accept(ISemanticVisitor v) { v.visit(this); }

    @Override
    public void accept(ICVisitor v) { v.visit(this); }

    @Override
    public String toString() {
        return "IfStatNode{" +
                "condition=" + condition +
                ", ifBody=" + ifBody +
                ", elifList=" + elifList +
                ", elseBody=" + elseBody +
                ", name='" + name + '\'' +
                '}';
    }
}
