package support.nodes;

import org.w3c.dom.Element;
import visitor.*;

import java.util.ArrayList;

public class ProcBodyNode implements ISyntaxVisitable, ISemanticVisitable, ICVisitable {

    public String name = null;
    public ArrayList<VarDeclNode> varDeclList = null;
    public StatListNode statListNode = null;
    public ArrayList<ExprNode> returnExprs = null;

    public ProcBodyNode(ArrayList<VarDeclNode> varDeclList, StatListNode statListNode, ArrayList<ExprNode> returnsExpr) {
        this.name = "ProcBodyNode";
        this.varDeclList = varDeclList;
        this.statListNode = statListNode;
        this.returnExprs = returnsExpr;
    }

    public ProcBodyNode(ArrayList<VarDeclNode> varDeclList, ArrayList<ExprNode> returnsExpr) {
        this.name = "ProcBodyNode";
        this.varDeclList = varDeclList;
        this.returnExprs = returnsExpr;
    }

    @Override
    public String toString() {
        return "ProcBodyNode{" +
                "varDeclList=" + varDeclList +
                ", statListNode=" + statListNode +
                ", returnsExpr=" + returnExprs +
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
