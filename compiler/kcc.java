package compiler;
/**
 * 
 * Description: This class is used to create the basic tools needed for the compiler and sets a starting point for the parser
 * 
 * @author Justin Mattix
 * @author David Jones
 * @author Taden Duerod
 * version 13.0
 * Programming Project 4
 * CS322 - Compiler Construction
 * Fall 2021
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

	/**
	 * Main method
	 * @param args argument to be entered into the main method
	 */
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
            
            String classfile = args[1];
            
            myListener listener = new myListener(classfile);
            ParseTreeWalker walker = new ParseTreeWalker();
            walker.walk(listener, tree);
            
            
            
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }


    }




}//end class
