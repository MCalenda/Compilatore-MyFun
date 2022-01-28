package symbol_table;

import java.util.ArrayList;

public class SymbolTableEntry {
    public String id;

    // tipo della variabile o tipo di ritorno della funzione
    public ValueType valueType;

    // variabile o funzione
    public Type type;

    // parametri della funzione
    public ArrayList<ValueType> params = null;

    public SymbolTableEntry(String id, ValueType valueType) {
        this.type = Type.variable;
        this.id = id;
        this.valueType = valueType;
    }

    public SymbolTableEntry(String id, ValueType valueType, ArrayList<ValueType> params) {
        this.type = Type.function;
        this.id = id;
        this.valueType = valueType;
        this.params = params;
    }

    public boolean isVariable() {
        return this.type == Type.variable;
    }

    public boolean isFunction() {
        return this.type == Type.function;
    }


    @Override
    public String toString() {
        if (isVariable())
            return "type::" + this.type + " valueType::" + this.valueType ;
        else  {
            return "type::" + this.type + " returnType::" + this.valueType ;
        }
    }
}
