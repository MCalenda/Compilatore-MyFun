package tree.nodes;

import java.util.ArrayList;

public class VarDeclNode {
    public String name = "VarDeclNode";
    public TypeNode type = null;
    public ArrayList<IdInitNode> idInitList = null;
    public ArrayList<IdInitObblNode> IdListInitObbl = null;

    public VarDeclNode(TypeNode type, ArrayList<IdInitNode> idInitList) {
        this.type = type;
        this.idInitList = idInitList;
    }

    public VarDeclNode(ArrayList<IdInitObblNode> IdListInitObbl) {
        this.IdListInitObbl = IdListInitObbl;
    }   
}