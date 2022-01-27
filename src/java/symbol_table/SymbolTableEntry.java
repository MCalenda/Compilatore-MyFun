package symbol_table;

import java.util.ArrayList;

public class SymbolTableEntry {
    public String id;
    public ValueType valueType;
    public Type type;
    public ArrayList<ValueType> inputParams;
    public ValueType output;

    public SymbolTableEntry(String id, ValueType valueType) {
        this.type = Type.variable;
        this.id = id;
        this.valueType = valueType;
    }

    public SymbolTableEntry(String id, ArrayList<ValueType> inputParams, ValueType output) {
        this.type = Type.function;
        this.id = id;
        this.inputParams = inputParams;
        this.output = output;
    }

    public boolean isVariable() {
        return this.type == Type.variable;
    }

    /*
    @Override
    public String toString() {
        if (isVariable())
            return "Entry of type Variable :: " + id + " | " + valueType;
        else  {
            String inputs = "";
            for (int i = 0; i < inputParams.size(); i++)
                if (i == inputParams.size()-1)
                    inputs += (inputParams.get(i));
                else inputs += (inputParams.get(i) + ", ");
            String outputs = "";
            for (int i = 0; i < outputParams.size(); i++) {
                if (i == outputParams.size() - 1)
                    outputs += (outputParams.get(i));
                else outputs += (outputParams.get(i) + ", ");
            }
            return "Entry of type Function :: " + id + "(" + inputs + ") -> " + outputs;
        }
    }
    */
}
