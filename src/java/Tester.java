import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

import cup.parser;
import cup.sym;
import flex.Lexer;
import java_cup.runtime.Symbol;
import tree.nodes.ProgramNode;
import visitor.CodeGen_Visitor;
import visitor.Semantic_Visitor;

class Tester {
    public static void main(String[] args) throws Exception {
        // Creazione del lexer sul file di input
        Lexer lexer = new Lexer(new FileReader(args[0]));
        // Creazione del parser sul flusso di token
        parser parser = new parser(lexer);

        File file;
        ProgramNode root;
        BufferedWriter bw;
        Symbol token;

        // Creazione del visitor semantico
        Semantic_Visitor semanticVisitor = new Semantic_Visitor();

        System.out.println("Quale operazione vuoi eseguire ?");
        System.out.println("1: Analisi Sintattica (Salvataggio dei Token in un file)");
        System.out.println("2: Analisi Lessicale (Stampa dell'albero di Parsing)");
        System.out.println("3: Analisi Semantica (Stampa dei messaggi di Debug)");
        System.out.println("4: Generazione del codice C (Senza esecuzione)");
        System.out.print("Effettuare una scelta: ");
        try (Scanner scanner = new Scanner(System.in)) {
            // int scelta = scanner.nextInt();
            int scelta = 4;

            System.out.println();

            switch (scelta) {
            case 1:
                file = new File("debug/Lexer_Debug.txt");
                token = lexer.next_token();

                if (!file.exists()) {
                    file.createNewFile();
                }

                bw = new BufferedWriter(new FileWriter(file));
                while (token.sym != sym.EOF) {
                    bw.write("<" + sym.terminalNames[token.sym] + (token.value == null ? ">" : ", " + token.value + ">"));
                    bw.write("\n");

                    token = lexer.next_token();
                }

                System.out.println("Token salvati all'interno del file \"debug/Lexer_Debug.txt\" !!!");

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

                semanticVisitor.visit(root);
                break;

            case 4:
                root = (ProgramNode) parser.parse().value;

                System.out.println();

                semanticVisitor.visit(root);

                CodeGen_Visitor codeGen_Visitor = new CodeGen_Visitor(args[0]);
                codeGen_Visitor.visit(root);
                break;

            default:
                System.out.println("Scelta effettuata non valida !!!");
                System.exit(0);
                break;
            }
        }
    }
}