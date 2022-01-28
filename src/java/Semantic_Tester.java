import java.io.FileReader;

import cup.parser;
import flex.Lexer;
import tree.nodes.ProgramNode;
import visitor.Semantic_Visitor;
import visitor.Syntax_Visitor;

class Semantic_Tester {
    public static void main(String[] args) throws Exception {
        Lexer lexer = new Lexer(new FileReader(args[0]));
        parser parser = new parser(lexer);

        ProgramNode root = (ProgramNode) parser.parse().value;
        
        Syntax_Visitor syntaxVisitor = new Syntax_Visitor();
        syntaxVisitor.visit(root);
        Semantic_Visitor semanticVisitor = new Semantic_Visitor();
        semanticVisitor.visit(root);

    }
}
