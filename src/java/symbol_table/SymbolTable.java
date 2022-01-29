package symbol_table;

import java.util.ArrayList;
import java.util.HashMap;

public class SymbolTable extends HashMap<String, SymbolTableEntry> {

    public String symbolTableName;
    public SymbolTable fatherSymbolTable;

    // Aggiunge una variabile all'interno della tabella
    public boolean createEntry_variable(String id, ValueType type) {
        if (super.containsKey(id)) return false;
        else super.put(id, new SymbolTableEntry(id, type));
        return true;
    }

    // Aggiunge una funzione all'interno della tabella
    public boolean createEntry_function(String id, ValueType type, ArrayList<ValueType> params, ArrayList<Boolean> isOut) {
        if (super.containsKey(id)) return false;
        else super.put(id, new SymbolTableEntry(id, type, params, isOut));
        return true;
    }

    // Verifica se l'ID è contenuto
    public Boolean containsKey(String id) {
        if (super.containsKey(id)) return true;
        else if (hasFatherSymTab())  return getFatherSymTab().containsKey(id);
        else return false;
    }

    // Ritorna la variabile con ID
    public SymbolTableEntry containsEntry(String id) {
        SymbolTableEntry symbolTableEntry = null;
        if (super.containsKey(id)) symbolTableEntry = super.get(id);
        else if (hasFatherSymTab()) symbolTableEntry = getFatherSymTab().containsEntry(id);
        return symbolTableEntry;
    }

    // Ritorna la funzione con ID
    public SymbolTableEntry containsFunctionEntry(String id) {
        SymbolTableEntry symbolTableEntry = null;
        if (super.containsKey(id) && !super.get(id).isVariable()) symbolTableEntry = super.get(id);
        else if (hasFatherSymTab()) symbolTableEntry = getFatherSymTab().containsFunctionEntry(id);
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
}