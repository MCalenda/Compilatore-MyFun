package symbol_table;

import java.util.ArrayList;
import java.util.HashMap;

public class SymbolTable extends HashMap<String, SymbolTableEntry> {

    public String symbolTableName;
    public SymbolTable fatherSymbolTable;

    public void setFatherSymTab(SymbolTable fatherSymbolTable){
        this.fatherSymbolTable = fatherSymbolTable;
    }

    public boolean hasFatherSymTab(){
        return fatherSymbolTable != null; 
    }

    public SymbolTable getFatherSymTab(){
        return fatherSymbolTable;
    }

    public void createEntry_variable(String id, String type) throws Exception {
        if (this.containsKey(id))
            throw new Exception("Errore Semantico: variabile \"" + id + "\" già dichiarata");
        else {
            super.put(id, new SymbolTableEntry(id, StringToType(type)));
        }
    }

    public void createEntry_function(String id, ArrayList<ValueType> inputParams, ArrayList<ValueType> outputParams) throws Exception {
        if (super.containsKey(id))
            throw new Exception("Errore Semantico: funzione \"" + id + "\" già dichiarata");
        else
            super.put(id, new SymbolTableEntry(id, inputParams, outputParams));
    }

    /* restituisce una entry (cambiare nome)
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
    */

    public Boolean containsKey(String id) throws Exception {
        if (super.containsKey(id)) {
            return true;
        } else if (hasFatherSymTab()) {
            return getFatherSymTab().containsKey(id);
        } else {
            return false;
        }
    }
    
    /* restituisce una entry (cambiare nome)
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
    */

    public static ValueType StringToType(String type) throws Exception {
        if (type.equalsIgnoreCase("integer")) return ValueType.integer;
        if (type.equalsIgnoreCase("string")) return ValueType.string;
        if (type.equalsIgnoreCase("real")) return ValueType.real;
        if (type.equalsIgnoreCase("bool"))  return ValueType.bool;
        throw new Exception("Semantic error: type " + type + " does not exists");
    }
}
