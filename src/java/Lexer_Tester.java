import java_cup.runtime.Symbol;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import cup.sym;
import flex.Lexer;

class Lexer_Tester {
    public static void main(String[] args) throws IOException {
        File file = new File("src/test_files/debug/Lexer_Debug.txt");
        Lexer lexer = new Lexer(new FileReader(args[0]));
        Symbol token = lexer.next_token();

        if (!file.exists()) {
            file.createNewFile();
        }

        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        while (token.sym != sym.EOF) {

            System.out.println("<" + sym.terminalNames[token.sym] + (token.value == null ? ">" : ", " + token.value + ">"));
            bw.write("<" + sym.terminalNames[token.sym] + (token.value == null ? ">" : ", " + token.value + ">"));
            bw.write("\n");

            token = lexer.next_token();
        }

        bw.flush();
        bw.close();
    }
}