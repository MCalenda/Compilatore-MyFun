package utils;

import java.util.ArrayList;

public class SymbolTableEntry {
    public String id;
    public ValueType valueType;
    public Type type;
    public ArrayList<ValueType> inputParams;
    public ArrayList<ValueType> outputParams;

    public SymbolTableEntry(String id, ValueType valueType) {
        this.type = Type.Variable;
        this.id = id;
        this.valueType = valueType;
    }

    public SymbolTableEntry(String id, ArrayList<ValueType> inputParams, ArrayList<ValueType> outputParams) {
        this.type = Type.Function;
        this.id = id;
        this.inputParams = inputParams;
        this.outputParams = outputParams;
    }

    public boolean isVariable() {
        return this.type == Type.Variable;
    }

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
}
