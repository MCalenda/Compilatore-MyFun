import java.awt.Dimension;
import java.awt.Font;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JTree;

import cup.parser;
import cup.sym;
import flex.Lexer;
import java_cup.runtime.Symbol;
import tree.nodes.ProgramNode;
import visitor.Semantic_Visitor;
import visitor.Syntax_Visitor;

class Tester {
    public static void main(String[] args) throws Exception {
        // Creazione del lexer sul file di input
        Lexer lexer = new Lexer(new FileReader(args[0]));
        // Creazione del parser sul flusso di token
        parser parser = new parser(lexer);

        ProgramNode root;

        System.out.println("Quale operazione vuoi eseguire ?");
        System.out.println("1: Analisi Sintattica (Salvataggio dei Token in un file)");
        System.out.println("2: Analisi Lessicale (Stampa dell'albero di Parsing)");
        System.out.println("3: Analisi Semantica (Stampa dei messaggi di Debug)");
        System.out.print("Effettuare una scelta: ");
        try (Scanner scanner = new Scanner(System.in)) {
            int scelta = scanner.nextInt();

            System.out.println();

            switch (scelta) {
            case 1:
                File file = new File("debug/Lexer_Debug.txt");
                Symbol token = lexer.next_token();

                if (!file.exists()) {
                    file.createNewFile();
                }

                BufferedWriter bw = new BufferedWriter(new FileWriter(file));
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
                root = (ProgramNode) parser.parse().value;

                Syntax_Visitor syntaxVisitor = new Syntax_Visitor();
                syntaxVisitor.visit(root);

                System.out.println("Visualizzo l'albero !!!");

                JFrame frame = new JFrame("Albero Sintattico");

                JTree tree = new JTree(root);
                final Font currentFont = tree.getFont();
                final Font bigFont = new Font(currentFont.getName(), currentFont.getStyle(), currentFont.getSize() + 5);
                tree.setFont(bigFont);
                frame.add(tree);

                frame.setPreferredSize(new Dimension(600, 600));
                frame.setMinimumSize(new Dimension(600, 600));

                frame.pack();
                frame.setVisible(true);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                break;

            case 3:
                root = (ProgramNode) parser.parse().value;

                System.out.println("Visualizzo fasi di Debug !!!");
                System.out.println();

                Semantic_Visitor semanticVisitor = new Semantic_Visitor();
                semanticVisitor.visit(root);
                break;

            default:
                System.out.println("Scelta effettuata non valida !");
                System.exit(0);
                break;
            }
        }
    }
}