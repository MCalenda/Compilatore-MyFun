package symbol_table;

import java.util.ArrayList;

public class SymbolTableEntry {
    // ID della entry
    public String id;
    // Tipo della variabile o tipo di ritorno della funzione
    public ValueType valueType;
    // Variabile o Funzione
    public Type type;

    // Parametri della funzione
    public ArrayList<ValueType> params = null;
    public ArrayList<Boolean> isOut = null;

    // Costruttore per variabile
    public SymbolTableEntry(String id, ValueType valueType) {
        this.type = Type.variable;
        this.id = id;
        this.valueType = valueType;
    }

    // Costruttore per funzione
    public SymbolTableEntry(String id, ValueType valueType, ArrayList<ValueType> params, ArrayList<Boolean> isOut) {
        this.type = Type.function;
        this.id = id;
        this.valueType = valueType;
        this.params = params;
        this.isOut = isOut;
    }

    // Metodi per il chechking del tipo
    public boolean isVariable() {
        return this.type == Type.variable;
    }
    public boolean isFunction() {
        return this.type == Type.function;
    }

    @Override
    public String toString() {
        if (isVariable())
            return "Type::" + this.type + " ValueType::" + this.valueType ;
        else  {
            return "Type::" + this.type + " ReturnType::" + this.valueType ;
        }
    }
}
