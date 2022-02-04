package symbol_table;

import java.util.ArrayList;
import java.util.HashMap;

public class SymbolTable extends HashMap<String, SymbolTableEntry> {
    // Nome della ST
    public String symbolTableName;
    // Padre della ST
    public SymbolTable fatherSymbolTable;

    // Costruttore
    public SymbolTable(String name) {
        this.symbolTableName = name;
    }

    // Metodi per l'inseriemento all'interno della ST
    /* ---------------------------------------------------------- */

    // Aggiunge una variabile all'interno della tabella corrente
    public boolean createEntry_variable(String id, ValueType type) {
        if (super.containsKey(id))
            return false;
        else
            super.put(id, new SymbolTableEntry(id, type));
        return true;
    }

    // Aggiunge una funzione all'interno della tabella corrente
    public boolean createEntry_function(String id, ValueType type, ArrayList<ValueType> params, ArrayList<Boolean> isOut) {
        if (super.containsKey(id))
            return false;
        else
            super.put(id, new SymbolTableEntry(id, type, params, isOut));
        return true;
    }

    // Metodi per la verifica del contenuto all'interno della ST
    /* ---------------------------------------------------------- */

    // Verifica se l'ID è contenuto ricorsivamente fino a tabella Global
    public Boolean containsKey(String id) {
        if (super.containsKey(id))
            return true;
        else if (hasFatherSymTab())
            return getFatherSymTab().containsKey(id);
        else
            return false;
    }

    // Metodi per l'estrapolazione del contenuto dalla ST
    /* ---------------------------------------------------------- */

    // Ritorna la variabile con ID se contenuta altrimenti NULL
    public SymbolTableEntry containsEntry(String id) {
        SymbolTableEntry symbolTableEntry = null;
        if (super.containsKey(id))
            symbolTableEntry = super.get(id);
        else if (hasFatherSymTab())
            symbolTableEntry = getFatherSymTab().containsEntry(id);
        return symbolTableEntry;
    }

    // Ritorna la funzione con ID se contenuta altrimenti NULL
    public SymbolTableEntry containsFunctionEntry(String id) {
        SymbolTableEntry symbolTableEntry = null;
        if (super.containsKey(id) && !super.get(id).isVariable())
            symbolTableEntry = super.get(id);
        else if (hasFatherSymTab())
            symbolTableEntry = getFatherSymTab().containsFunctionEntry(id);
        return symbolTableEntry;
    }

    // Metodi getter/setter/has
    /* ---------------------------------------------------------- */
    public void setFatherSymTab(SymbolTable fatherSymbolTable) {
        this.fatherSymbolTable = fatherSymbolTable;
    }

    private SymbolTable getFatherSymTab() {
        return fatherSymbolTable;
    }

    private boolean hasFatherSymTab() {
        return fatherSymbolTable != null;
    }
}