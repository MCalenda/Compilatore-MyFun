import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import cup.parser;
import cup.sym;
import flex.Lexer;
import java_cup.runtime.Symbol;
import tree.nodes.ProgramNode;
import visitor.CodeGen_Visitor;
import visitor.Semantic_Visitor;

class Tester {
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void main(String[] args) throws Exception {
        // Creazione del lexer sul file di input
        Lexer lexer = new Lexer(new FileReader(args[0]));
        // Creazione del parser sul flusso di token
        parser parser = new parser(lexer);
        // Creazione del visitor semantico
        Semantic_Visitor semanticVisitor = new Semantic_Visitor();

        // Definizione dei vari oggetti di esecuzione
        File file;
        ProgramNode root;
        BufferedWriter bw;
        Symbol token;
        CodeGen_Visitor codeGen_Visitor;

        /* --------------------------------------------------------- */
        /* --------------------------------------------------------- */
        // Modalità di esecuzione:
        // 1: Stampa token Lexer
        // 2: Stampa albero versione JSON
        // 3: Debug analisi semantica
        // 4: Generazione del codice C @DEFAULT
        /* --------------------------------------------------------- */
        /* --------------------------------------------------------- */
        int scelta = 4;

        switch (scelta) {
            case 1:
                file = new File("debug/Lexer_Debug.txt");
                token = lexer.next_token();

                if (!file.exists()) {
                    file.createNewFile();
                }

                bw = new BufferedWriter(new FileWriter(file));
                while (token.sym != sym.EOF && token.sym != sym.error) {
                    bw.write("<" + sym.terminalNames[token.sym] + (token.value == null ? ">" : ", " + token.value + ">"));
                    bw.write("\n");

                    token = lexer.next_token();
                }

                if (token.sym == sym.error) {
                    bw.write("<" + sym.terminalNames[token.sym] + (token.value == null ? ">" : ", " + token.value + ">"));
                    System.out.println("Errore durante l'esecuzione del Lexer !!!");
                } else {   
                    System.out.println("Token salvati all'interno del file \"debug/Lexer_Debug.txt\" !!!");
                }

                bw.flush();
                bw.close();
                break;

            case 2:
                file = new File("debug/Syntax_Debug.json");
                root = (ProgramNode) parser.parse().value;

                if (!file.exists()) {
                    file.createNewFile();
                }

                bw = new BufferedWriter(new FileWriter(file));
                bw.write(root.toString());

                System.out.println("Albero JSON salvato all'interno del file \"debug/Syntax_Debug.json\" !!!");

                bw.flush();
                bw.close();
                break;

            case 3:
                root = (ProgramNode) parser.parse().value;

                System.out.println("Visualizzo fasi di Debug !!!");
                System.out.println();

                // Attivazione del DEBUG per il Semantic_Visitor
                semanticVisitor.debugTab = true;
                semanticVisitor.debugVar = true;

                semanticVisitor.visit(root);
                break;

            case 4:
                root = (ProgramNode) parser.parse().value;

                System.out.println();

                semanticVisitor.visit(root);

                codeGen_Visitor = new CodeGen_Visitor(args[0]);
                codeGen_Visitor.visit(root);
                break;

            default:
                System.out.println("Scelta effettuata non valida !!!");
                System.exit(0);
                break;
        }
    }
}
