package tree.nodes;

import java.util.ArrayList;

public class ElseNode {
    public String name = "ElseNode";
    public ArrayList<VarDeclNode> varDeclList;
    public ArrayList<StatNode> statList;

    public ElseNode(ArrayList<VarDeclNode> varDeclList, ArrayList<StatNode> statList) {
        this.varDeclList = varDeclList;
        this.statList = statList;
    }
}