import java_cup.runtime.Symbol;

import java.io.FileReader;
import java.io.IOException;

import cup.sym;
import flex.Lexer;

class Lexer_Tester {
    public static void main(String[] args) throws IOException {
        FileReader inFile = new FileReader(args[0]);

        Lexer lexer = new Lexer(inFile);

        Symbol token = lexer.next_token();

        while (token.sym != sym.EOF) {

            System.out.println(
                    "<" + sym.terminalNames[token.sym] + (token.value == null ? ">" : ", " + token.value + ">"));

            token = lexer.next_token();
        }
    }
}