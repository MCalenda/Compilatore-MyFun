package tree.nodes;

import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProgramNode {
    public String name = "ProgramNode";
    public ArrayList<VarDeclNode> varDecList;       // DA IMPLEMENTARE NEL CASO SIA VUOTA !!!
    public ArrayList<FunNode> funList;              // DA IMPLEMENTARE NEL CASO SIA VUOTA !!!
    public MainNode main;

    private ObjectMapper mapper = new ObjectMapper(); 

    public ProgramNode(ArrayList<VarDeclNode> varDecList, ArrayList<FunNode> funList, MainNode main) {
        this.varDecList = varDecList;
        this.funList = funList;
        this.main = main;

    }

    @Override
    public String toString() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "Error, node: " + this.name;
    }
}