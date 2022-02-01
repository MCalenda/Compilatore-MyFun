package tree.nodes;

import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import visitor.CodeGen_Int_Visitable;
import visitor.CodeGen_Int_Visitor;

public class ProgramNode implements CodeGen_Int_Visitable {
    // Attributi
    public String name = "ProgramNode";
    public ArrayList<VarDeclNode> varDecList;
    public ArrayList<FunNode> funList;
    public MainNode main;

    // Mapper per la visualizzazione in formato JSON
    private ObjectMapper mapper = new ObjectMapper();

    // Costruttore
    public ProgramNode(ArrayList<VarDeclNode> varDecList, ArrayList<FunNode> funList, MainNode main) {
        this.varDecList = varDecList;
        this.funList = funList;
        this.main = main;
    }

    // Metodo polimorfi per i visitor
    @Override
    public void accept(CodeGen_Int_Visitor v) {
        v.visit(this);
    }

    // Sovrascrittura metodo toString per la visualizzazione in formato JSON
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