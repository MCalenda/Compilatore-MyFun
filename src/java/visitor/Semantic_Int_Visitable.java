package visitor;

// Interfaccia per l'implementazione del metodo accept all'interno 
// dei vari nodi e foglie.   
public interface Semantic_Int_Visitable {
    void accept(Semantic_Int_Visitor v);
}