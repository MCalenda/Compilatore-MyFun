package utils;

import java.util.ArrayList;
import java.util.HashMap;

public class SymbolTable extends HashMap<String, SymbolTableEntry> {

    public String symbolTableName;
    public SymbolTable pointerToFather;

    public void setFatherSymTab(SymbolTable symbolTable){
        this.pointerToFather = symbolTable;
    }

    public boolean hasFatherSymTab(){ return pointerToFather != null; }

    public SymbolTable getFatherSymTab(){
        return pointerToFather;
    }

    public void createEntry_variable(String id, String type) throws Exception {
        if (super.containsKey(id)) throw new Exception("Semantic error in " + symbolTableName + ": identifier '" + id + "' already declared in the actual scope");
        else super.put(id, new SymbolTableEntry(id, StringToType(type)));
    }

    public void createEntry_function(String id, ArrayList<ValueType> inputParams, ArrayList<ValueType> outputParams) throws Exception {
        if (super.containsKey(id)) throw new Exception("Semantic error in " + symbolTableName + ": identifier '" + id + "' already declared in the actual scope");
        super.put(id, new SymbolTableEntry(id, inputParams, outputParams));
    }

    public SymbolTableEntry containsEntry(String id) throws Exception {
        SymbolTableEntry symbolTableEntry = null;
        if (super.containsKey(id)) {
            symbolTableEntry = super.get(id);
        } else if (hasFatherSymTab()) {
            symbolTableEntry = getFatherSymTab().containsEntry(id);
        } else {
            throw new Exception("Semantic error: variable " + id + " not declared");
        }
        return symbolTableEntry;
    }

    public Boolean containsKey(String id) throws Exception {
        if (super.containsKey(id)) {
            return true;
        } else if (hasFatherSymTab()) {
            return getFatherSymTab().containsKey(id);
        } else {
            throw new Exception("Semantic error: variable or function " + id + " not declared");
        }
    }

    public SymbolTableEntry containsFunctionEntry(String id) throws Exception {
        SymbolTableEntry symbolTableEntry = null;
        if (super.containsKey(id) && !super.get(id).isVariable()) {
            symbolTableEntry = super.get(id);
        } else if (hasFatherSymTab()) {
            symbolTableEntry = getFatherSymTab().containsFunctionEntry(id);
        } else {
            throw new Exception("Semantic error: proc " + id + " not defined");
        }
        return symbolTableEntry;
    }

    public static ValueType StringToType(String type) throws Exception {
        if (type.equalsIgnoreCase("int")) return ValueType.Integer;
        if (type.equalsIgnoreCase("string")) return ValueType.String;
        if (type.equalsIgnoreCase("float")) return ValueType.Float;
        if (type.equalsIgnoreCase("bool") || type.equalsIgnoreCase("boolean")) return ValueType.Boolean;
        if (type.equalsIgnoreCase("null")) return ValueType.Null;
        if (type.equalsIgnoreCase("void")) return ValueType.Void;
        throw new Exception("Semantic error: type " + type + " does not exists");
    }
}
