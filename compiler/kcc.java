package compiler;
/**
 * This class encapsulates a basic grammar test.
 */

import java.io.IOException;

//ANTLR packages
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.gui.Trees;

import lexparse.*;

public class kcc{


    public static void main(String[] args){
        CharStream input;
        KnightCodeLexer lexer;
        CommonTokenStream tokens;
        KnightCodeParser parser;
        
        System.out.println("Starting the compiler...");

        try{
            input = CharStreams.fromFileName(args[0]);  //get the input
            lexer = new KnightCodeLexer(input); //create the lexer
            tokens = new CommonTokenStream(lexer); //create the token stream
            parser = new KnightCodeParser(tokens); //create the parser
       
            ParseTree tree = parser.file();  //set the start location of the parser
            
            String classfile = args[0];
            
            myListener listener = new myListener(classfile);
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }


    }




}//end class