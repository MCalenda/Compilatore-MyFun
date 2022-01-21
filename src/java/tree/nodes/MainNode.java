package tree.nodes;

import java.util.ArrayList;

public class MainNode {
    public String name = "MainNode";
    public ArrayList<VarDeclNode> varDeclList;
    public ArrayList<StatNode> StatList;


    public MainNode(ArrayList<VarDeclNode> varDeclList, ArrayList<StatNode> StatList) {
        this.varDeclList = varDeclList;
        this.StatList = StatList;
    }
}
