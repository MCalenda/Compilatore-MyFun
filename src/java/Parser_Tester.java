import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import cup.parser;
import flex.Lexer;

class Parser_Tester {
    public static void main(String[] args) throws Exception {
        File file = new File("src/test_files/debug/Parser_Debug.json");

        Lexer lexer = new Lexer(new FileReader(args[0]));
        parser parser = new parser(lexer);

        if (!file.exists()) {
            file.createNewFile();
        }

        BufferedWriter bw = new BufferedWriter(new FileWriter(file));

        bw.write(parser.parse().value.toString());
        bw.flush();
        bw.close();
    }
}