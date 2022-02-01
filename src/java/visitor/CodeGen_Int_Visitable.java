package visitor;

// Interfaccia per l'implementazione del metodo accept all'interno 
// dei vari nodi e foglie.   
public interface CodeGen_Int_Visitable {
    void accept(CodeGen_Int_Visitor v);
}