package compiler;

/**
 * This class is tests and parses through inputed program
 * 
 * @author Saaki Vishnumolakala
 * @version 1.0
 * Compiler Assignment 4
 * CS322 - Compiler Construction
 * Spring 2023
 */

 import java.io.IOException;
 import java.util.Scanner;
 //ANTLR packages
 import org.antlr.v4.runtime.*;
 import org.antlr.v4.runtime.CharStream;
 import org.antlr.v4.runtime.CharStreams;
 import org.antlr.v4.runtime.tree.*;
 import org.antlr.v4.gui.Trees;
 
 import lexparse.*;

public class kcc {
    public static void mian(String [] args) throws Exception {

        CharStream input;
        KnightCodeLexer lexer;
        CommonTokenStream tokens;
        KnightCodeParser parser;

        try{
            input = CharStreams.fromFileName(args[0]);  //get the input
            lexer = new KnightCodeLexer(input); //create the lexer
            tokens = new CommonTokenStream(lexer); //create the token stream
            parser = new KnightCodeParser(tokens); //create the parser
       
            ParseTree tree = parser.file();  //set the start location of the parser
             
            Scanner scan = new Scanner(System.in); //Scanner
            System.out.println("Enter class file: ");
            String classFile = scan.next();
            
            Trees.inspect(tree, parser);  //displays the parse tree

            myListener listener = new myListener(classFile);
            ParseTreeWalker walker = new ParseTreeWalker();
            walker.walk(listener, tree);

            scan.close();
            
            //System.out.println(tree.toStringTree(parser));
        
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }

    }
}
