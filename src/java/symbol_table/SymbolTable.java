package symbol_table;

import java.util.ArrayList;
import java.util.HashMap;

public class SymbolTable extends HashMap<String, SymbolTableEntry> {

    public String symbolTableName;
    public SymbolTable fatherSymbolTable;

    // Aggiunge una variabile all'interno della tabella
    public void createEntry_variable(String id, ValueType type) throws Exception {
        if (super.containsKey(id))
            throw new Exception("[SEMANTIC ERROR] variabile " + id + " già dichiarata");
        else {
            super.put(id, new SymbolTableEntry(id, type));
        }
    }

    // Aggiunge una funzione all'interno della tabella
    public void createEntry_function(String id, ArrayList<ValueType> inputParams, ValueType output) throws Exception {
        if (super.containsKey(id))
            throw new Exception("[SEMANTIC ERROR] funzione " + id + " già dichiarata");
        else
            super.put(id, new SymbolTableEntry(id, inputParams, output));
    }

    // Verifica se l'ID è semplicemente contenuto
    public Boolean containsKey(String id) {
        if (super.containsKey(id)) {
            return true;
        } else if (hasFatherSymTab()) {
            return getFatherSymTab().containsKey(id);
        } else {
            return false;
        }
    }

    // Ritorna la variabile con ID
    public SymbolTableEntry containsEntry(String id) throws Exception {
        SymbolTableEntry symbolTableEntry = null;
        if (super.containsKey(id)) {
            symbolTableEntry = super.get(id);
        } else if (hasFatherSymTab()) {
            symbolTableEntry = getFatherSymTab().containsEntry(id);
        } else {
            throw new Exception("[SEMANTIC ERROR] variabile " + id + " non dichiarata");
        }
        return symbolTableEntry;
    }

    // Ritorna la funzione con ID
    public SymbolTableEntry containsFunctionEntry(String id) throws Exception {
        SymbolTableEntry symbolTableEntry = null;
        if (super.containsKey(id) && !super.get(id).isVariable()) {
            symbolTableEntry = super.get(id);
        } else if (hasFatherSymTab()) {
            symbolTableEntry = getFatherSymTab().containsFunctionEntry(id);
        } else {
            throw new Exception("[SEMANTIC ERROR] funzione " + id + " non dichiarata");
        }
        return symbolTableEntry;
    }

    public void setFatherSymTab(SymbolTable fatherSymbolTable) {
        this.fatherSymbolTable = fatherSymbolTable;
    }

    public SymbolTable getFatherSymTab() {
        return fatherSymbolTable;
    }

    public boolean hasFatherSymTab() {
        return fatherSymbolTable != null;
    }

    public static ValueType StringToValueType(String type) throws Exception {
        if (type.equalsIgnoreCase("integer"))
            return ValueType.integer;
        if (type.equalsIgnoreCase("string"))
            return ValueType.string;
        if (type.equalsIgnoreCase("real"))
            return ValueType.real;
        if (type.equalsIgnoreCase("bool"))
            return ValueType.bool;
        throw new Exception("SEMANTIC ERROR: il tipo " + type + " non esiste");
    }
}