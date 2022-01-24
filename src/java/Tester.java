import java.io.FileReader;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JTree;

import cup.parser;
import flex.Lexer;
import tree.nodes.ProgramNode;
import visitor.Syntax_Visitor;

class Tester {
    public static void main(String[] args) throws Exception {
        Lexer lexer = new Lexer(new FileReader(args[0]));
        parser parser = new parser(lexer);

        ProgramNode root = (ProgramNode) parser.parse().value;
        
        Syntax_Visitor syntaxVisitor = new Syntax_Visitor();
        syntaxVisitor.visit(root);

        JFrame frame = new JFrame("Albero Sintattico");

        JTree tree = new JTree(root);
        final Font currentFont = tree.getFont();
        final Font bigFont = new Font(currentFont.getName(), currentFont.getStyle(), currentFont.getSize() + 5);
        tree.setFont(bigFont);
        frame.add(tree);

        frame.setPreferredSize(new Dimension(600,600));
        frame.setMinimumSize(new Dimension(600,600));

        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}