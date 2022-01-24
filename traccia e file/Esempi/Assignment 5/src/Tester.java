import java_cup.runtime.Symbol;
import support.nodes.ProgramNode;
import visitor.CVisitor;
import visitor.SemanticVisitor;
import visitor.SyntaxVisitor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Tester {

    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";

    public static void main(String[] args) throws Exception {
        Lexer myLexer = new Lexer(new FileReader(args[0]));
        parser myParser = new parser(myLexer);
        Symbol s = myParser.parse();

        ProgramNode programNode = (ProgramNode) s.value;

        System.out.println("Cosa vuoi fare?\n1- Generazione XML\n2- Analisi semantica\n3- Generazione codice C (con analisi semantica)\n4- Generazione codice C (con analisi semantica) ed esecuzione");
        int scelta = new Scanner(System.in).nextInt();

        switch (scelta) {
            case 1:
                System.out.println("Generating XML for Syntax Visit...");
                SyntaxVisitor syntaxVisitor = new SyntaxVisitor();
                syntaxVisitor.visit(programNode);
                syntaxVisitor.saveXML(args[0]);
                System.out.println(GREEN + "XML for Syntax Visit generated." + RESET);
                break;
            case 2:
                System.out.println("\nStarting semantic analysis...");
                SemanticVisitor semanticVisitor = new SemanticVisitor();
                semanticVisitor.visit(programNode);
                System.out.println(GREEN + "Semantic analysis completed." + RESET);
                break;
            case 3:
                // Analisi semantica
                System.out.println("\nStarting semantic analysis...");
                SemanticVisitor semanticVisitor1 = new SemanticVisitor();
                semanticVisitor1.visit(programNode);
                System.out.println(GREEN + "Semantic analysis completed." + RESET);

                // Generazione codice
                System.out.println("\nGenerating C code...");
                CVisitor cVisitor = new CVisitor(args[0]);
                cVisitor.visit(programNode);
                try {
                    File file = new File(args[0].substring(0, args[0].length() - 4).split("/")[1] + ".c");
                    if (System.getProperty("os.name").toLowerCase().contains("win")) {
                        int index=file.getAbsolutePath().lastIndexOf("\\");
                        String cFilesPath=file.getAbsolutePath().substring(0,index)+"\\test_files\\c_outputs\\";
                        Runtime.getRuntime().exec("cmd /b start cmd.exe /k \" "+"clang-format --style=google -i "+file.getAbsolutePath()+" && move "+file.getAbsolutePath()+" "+cFilesPath+" && exit\" ");
                    } else {
                        // $1 = path assoluto del file per formattarlo
                        // $2 = folder di destinazione per spostarlo
                        Runtime.getRuntime().exec("./scripts/format_Mac.sh " + file.getAbsolutePath() + " " + file.getAbsoluteFile().getParent() + "/test_files/c_outputs/");
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                System.out.println(GREEN + "C code generated." + RESET);
                break;
            case 4:
                // Analisi semantica
                System.out.println("\nStarting semantic analysis...");
                SemanticVisitor semanticVisitor2 = new SemanticVisitor();
                semanticVisitor2.visit(programNode);
                System.out.println(GREEN + "Semantic analysis completed." + RESET);

                // Generazione codice
                System.out.println("\nGenerating C code...");
                CVisitor cVisitor1 = new CVisitor(args[0]);
                int arguments=cVisitor1.visit(programNode);
                System.out.println(GREEN + "C code generated." + RESET);

                // Esecuzione programma
                System.out.println("\nExecuting program...");
                try {
                    // Preparo gli oggetti necessari
                    File file = new File(args[0].substring(0, args[0].length() - 4).split("/")[1] + ".c");
                    String windowsArguments="";
                    String macArguments = "";
                    Scanner in=new Scanner(System.in);

                    // Stampo l'avviso
                    if(arguments>0)
                        System.out.println("You have to insert "+arguments+" arguments");

                    // Prendo i parametri in input
                    for(int i=0; i<arguments; i++)  {
                        System.out.println("Insert " + ordinal(i+1) + " parameter");
                        String input = in.nextLine();
                        windowsArguments+=" "+input;
                        macArguments += input;
                        if(i != arguments-1)
                            macArguments += ",";
                    }

                    // Windows
                    if (System.getProperty("os.name").toLowerCase().contains("win")) {
                        int index = file.getAbsolutePath().lastIndexOf("\\");
                        String cFilesPath = file.getAbsolutePath().substring(0, index) + "\\test_files\\c_outputs\\";
                        File []filesDelete=new File(cFilesPath).listFiles();
                        for(File fileOfDirectory: filesDelete) {
                            if(fileOfDirectory.getAbsolutePath().indexOf(".exe")>0) {
                                fileOfDirectory.delete();
                            }
                        }
                        Runtime.getRuntime().exec("cmd /b start cmd.exe /k \" " + "clang-format --style=google -i " + file.getAbsolutePath() + " && move " + file.getAbsolutePath() + " " + cFilesPath + " && exit\" ");
                        Runtime.getRuntime().exec("cmd /c start cmd.exe /k \" " + "clang -o " + cFilesPath + file.getName().substring(0, file.getName().length() - 2) + ".exe " + cFilesPath + file.getName().substring(0, file.getName().length() - 2) + ".c && " + cFilesPath + file.getName().substring(0, file.getName().length() - 2) + ".exe " + windowsArguments + " && del /f " + cFilesPath + file.getName().substring(0, file.getName().length() - 2) + ".exe \"");
                    }

                    // Mac/Linux
                    else {
                        // $1 = path assoluto del file per formattarlo
                        // $2 = folder di destinazione per spostarlo
                        // $3 = parametri in input al main
                        Runtime.getRuntime().exec("./scripts/format&run_Mac.sh " + file.getAbsolutePath() + " " + file.getAbsoluteFile().getParent() + "/test_files/c_outputs/" + " " +macArguments);
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                break;
            default:
                System.out.println(RED + "Error! Choose a valid option." + RESET);
                System.exit(0);
                break;
        }
    }


    public static String ordinal(int i) {
        int mod100 = i % 100;
        int mod10 = i % 10;
        if(mod10 == 1 && mod100 != 11) {
            return i + "st";
        } else if(mod10 == 2 && mod100 != 12) {
            return i + "nd";
        } else if(mod10 == 3 && mod100 != 13) {
            return i + "rd";
        } else {
            return i + "th";
        }
    }
}
