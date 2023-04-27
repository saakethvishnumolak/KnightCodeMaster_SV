package compiler;

/**
 * Necessary imports
 */
import java.util.HashMap;
import java.io.*;
import lexparse.*;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.objectweb.asm.*;
import org.objectweb.asm.Opcodes;

/**
 * This class has methods/calculations to execute basic operations within the compiler
 * 
 * @author Saaki Vishnumolakala
 * @version 1.0
 * Compiler Assignment 4
 * CS322 - Compiler Construction
 * Spring 2023
 */
public class myListener extends KnightCodeBaseListener{
    
    /**
     * Private variables to be manipulated in this class
     */
    private HashMap<Variable, String> SymbolTable = new HashMap<Variable, String>();
    private ClassWriter cw;
    private MethodVisitor mv, mainVisitor;
    private String fileName; //output file
    int count = 0;

    /**
     * Constructor that takes String fileName as a paramter
     */
    public myListener(String fileName)
    {
        this.fileName = fileName;
    }

    /**
     * Sets up the class to allow bytecode use
     */
    public void startClass()
    {
        cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        cw.visit(Opcodes.V11, Opcodes.ACC_PUBLIC, this.fileName, null, "java/lang/Object", null);

        mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(0,0);
        mv.visitEnd();

        //Write the bytecode from the tree
        mainVisitor = cw.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
        mainVisitor.visitCode();

    }

    /**
     * Method closes the startClass. 
     * Finishes the writing of bytecode and writes the binary file.
     */
    public void closeClass()
    {
        mainVisitor.visitInsn(Opcodes.RETURN);
        mainVisitor.visitMaxs(0,0);
        mainVisitor.visitEnd();
        cw.visitEnd();

        byte[] b = cw.toByteArray();
        writeFile.writeFile(b, this.fileName + ".class");

        System.out.println("Done!");
    }

    /**
     * This overrides the enterFile method in 
     * KnightCodeBaseListener and calls the 
     * startClass method above.
     */
    @Override public void enterFile(KnightCodeParser.FileContext ctx)
    {
        System.out.println("Entering the file");
        startClass();
    }
    
    /**
     * Overrides the exitFile and call the
     * closeClass above
     */
    @Override public void exitFile(KnightCodeParser.FileContext ctx)
    {
        System.out.println("Exiting the file");
        closeClass();
    }

    /**
     * Overrides the enterDeclare method
     */
    @Override public void enterDeclare(KnightCodeParser.DeclareContext ctx)
    {
        System.out.println("Enter declare");
    }

    /**
     * Overrides the exitDeclare method
     */
    @Override public void exitDeclare(KnightCodeParser.DeclareContext ctx)
    {
        System.out.println("Exit declare");
    }

    /**
     * Overrides the enterVariable method.
     * It also sets the variable type, name,
     * and value. 
     */
    @Override public void enterVariable(KnightCodeParser.VariableContext ctx)
    {
        System.out.println("Entering variable");

        String newVar = ctx.getText();
        Variable var = new Variable();

        if(newVar.contains("INTEGER"))
        {
            String name = newVar.substring(7);
            SymbolTable.put(new Variable(name, "INTEGER", count), "0");
        } 
        else {
            String name = newVar.substring(6);
            SymbolTable.put(new Variable(name, "STRING", count), "0");
        }
        count++;
    }

    /**
     * Override exitVariable method
     */
    @Override public void exitVariable(KnightCodeParser.VariableContext ctx)
    {
        System.out.println("Exiting variable");
    }

    /**
     * Overrides the enterIdentifier method and
     * prints the type of identifier entered
     */
    @Override public void enterIdentifier(KnightCodeParser.IdentifierContext ctx)
    {
        System.out.println("Identifier type: " + ctx.getText());
    }

    /**
     * Override exitIdentifier method
     */
    @Override public void exitIdentifier(KnightCodeParser.IdentifierContext ctx)
    {
        System.out.println("Exiting Identifier");
    }

    /**
     * Overrides the enterVartype method and
     * prints the type of variable that was entered
     */
    @Override public void enterVartype(KnightCodeParser.VartypeContext ctx)
    {
        System.out.println("Variable Type: " + ctx.getText());
    }

    /**
     * Overrides the exitVartype method
     */
    {
        System.out.println("Exiting variable type");
    }

    /**
     * Overrides the enterBody method.
     * Prints contents in Body
     */
    @Override public void enterBody(KnightCodeParser.BodyContext ctx)
    {
        System.out.println("Body: " + ctx.getText());
    }

    /**
     * Overrides the exitBody method
     */
    @Override public void exitBody(KnightCodeParser.BodyContext ctx)
    {
        System.out.println("Exiting Body");
    }

    /**
     * Overrides the enterStat method.
     * Prints the contents in stat section.
     */
    @Override public void enterStat(KnightCodeParser.StatContext ctx)
    {
        System.out.println("Stat: " + ctx.getText());
    }

    /**
     * Overrides the exitStat method
     */
    @Override public void exitStat(KnightCodeParser.StatContext ctx)
    {
        System.out.println("Exiting Stat");
    }

    /**
     * This method is used to choose the arithmetic
     * that is needed based on what was entered
     */
    @Override public void enterSetvar(KnightCodeParser.SetvarContext ctx)
    {
        Variable newVar = new Variable();
        int index = ctx.getText().indexOf("=");
        String varName = ctx.getText().substring(3, index -1);
        String value = ctx.getText().substring(index + 1);

        if(value.contains("*") || value.contains("/") || value.contains("+") || value.contains("-"))
        {
            value = "0";
        }
        for(Variable sTable : SymbolTable.keySet())
        {
            if(sTable.getName().equals(varName))
            {
                newVar = sTable;
            }
        }

        if(newVar.getName() != null)
        {
            for(Variable sTable : SymbolTable.keySet())
            {
                //replace the previous value with the new value
                if(newVar.getValue() == (sTable.getValue()))
                {
                    SymbolTable.replace(newVar, value);
                }
            }
            mainVisitor.visitLdcInsn(Integer.parseInt(value));
            mainVisitor.visitVarInsn(Opcodes.ISTORE, newVar.getValue());
        } else {
            SymbolTable.put(new Variable(varName, null, count), value);
            mainVisitor.visitLdcInsn(Integer.parseInt(value));
            mainVisitor.visitVarInsn(Opcodes.ISTORE, count);
            count++;
        }

    }

    /**
     * Exits the setvar method
     */
    @Override public void exitSetvar(KnightCodeParser.SetvarContext ctx)
    {
        System.out.println("exiting Setvar");
    }

    /**
     * Override enterParenthesis method
     */
    @Override public void enterParenthesis(KnightCodeParser.ParenthesisContext ctx)
    {
        System.out.println("Entering parenthesis");
    }

    /**
     * Exiting parenthesis
     */
    @Override public void exitParenthesis(KnightCodeParser.ParenthesisContext ctx)
    {
        System.out.println("Exiting parenthesis");
    }

    /**
     * Performs multiplication. 
     * Takes a left and right child and multiplies them. 
     * Stores the product in a root variable in the hashmap.
     */
    @Override public void enterMultiplication(KnightCodeParser.MultiplicationContext ctx)
    {
        String mathEquation = ctx.getText();
        String leftVariable = mathEquation.substring(0, mathEquation.indexOf("*"));
        String rightVariable = mathEquation.substring(mathEquation.indexOf("*") + 1);
        int left = 0;
        int right = 0;
        int root = 0;

        for(Variable variable : SymbolTable.keySet())
        {
            if(variable.getName().equals(leftVariable))
            {
                left = variable.getValue();
            }
            if(variable.getName().equals(rightVariable))
            {
                right = variable.getValue();
            }
            if(SymbolTable.get(variable).equals("0"))
            {
                root = variable.getValue();
            }

            mainVisitor.visitVarInsn(Opcodes.ILOAD, left);
            mainVisitor.visitVarInsn(Opcodes.ILOAD, right);
            mainVisitor.visitInsn(Opcodes.IMUL);
            mainVisitor.visitVarInsn(Opcodes.ISTORE, root);
        }

    }

    /**
     * Exit multiplication method
     */
    @Override public void exitMultiplication(KnightCodeParser.MultiplicationContext ctx)
    {
        System.out.println("Exiting multiplication");
    }

        /**
     * Performs division. 
     * Takes a left and right child and divides them. 
     * Stores the quotient in a root variable in the hashmap.
     */
    @Override public void enterDivision(KnightCodeParser.DivisionContext ctx)
    {
        String mathEquation = ctx.getText();
        String leftVariable = mathEquation.substring(0, mathEquation.indexOf("/"));
        String rightVariable = mathEquation.substring(mathEquation.indexOf("/") + 1);
        int left = 0;
        int right = 0;
        int root = 0;

        for(Variable variable : SymbolTable.keySet())
        {
            if(variable.getName().equals(leftVariable))
            {
                left = variable.getValue();
            }
            if(variable.getName().equals(rightVariable))
            {
                right = variable.getValue();
            }
            if(SymbolTable.get(variable).equals("0"))
            {
                root = variable.getValue();
            }

            mainVisitor.visitVarInsn(Opcodes.ILOAD, left);
            mainVisitor.visitVarInsn(Opcodes.ILOAD, right);
            mainVisitor.visitInsn(Opcodes.IDIV);
            mainVisitor.visitVarInsn(Opcodes.ISTORE, root);
        }

    }

    /**
     * Exit division method
     */
    @Override public void exitDivision(KnightCodeParser.DivisionContext ctx)
    {
        System.out.println("Exiting division");
    }

        /**
     * Performs addition. 
     * Takes a left and right child and adds them. 
     * Stores the sum in a root variable in the hashmap.
     */
    @Override public void enterAddition(KnightCodeParser.AdditionContext ctx)
    {
        String mathEquation = ctx.getText();
        String leftVariable = mathEquation.substring(0, mathEquation.indexOf("+"));
        String rightVariable = mathEquation.substring(mathEquation.indexOf("+") + 1);
        int left = 0;
        int right = 0;
        int root = 0;

        for(Variable variable : SymbolTable.keySet())
        {
            if(variable.getName().equals(leftVariable))
            {
                left = variable.getValue();
            }
            if(variable.getName().equals(rightVariable))
            {
                right = variable.getValue();
            }
            if(SymbolTable.get(variable).equals("0"))
            {
                root = variable.getValue();
            }

            mainVisitor.visitVarInsn(Opcodes.ILOAD, left);
            mainVisitor.visitVarInsn(Opcodes.ILOAD, right);
            mainVisitor.visitInsn(Opcodes.IADD);
            mainVisitor.visitVarInsn(Opcodes.ISTORE, root);
        }

    }

    /**
     * Exit addition method
     */
    @Override public void exitAddition(KnightCodeParser.AdditionContext ctx)
    {
        System.out.println("Exiting addition");
    }

        /**
     * Performs subtraction. 
     * Takes a left and right child and subtracts them. 
     * Stores the difference in a root variable in the hashmap.
     */
    @Override public void enterSubtraction(KnightCodeParser.SubtractionContext ctx)
    {
        String mathEquation = ctx.getText();
        String leftVariable = mathEquation.substring(0, mathEquation.indexOf("-"));
        String rightVariable = mathEquation.substring(mathEquation.indexOf("-") + 1);
        int left = 0;
        int right = 0;
        int root = 0;

        for(Variable variable : SymbolTable.keySet())
        {
            if(variable.getName().equals(leftVariable))
            {
                left = variable.getValue();
            }
            if(variable.getName().equals(rightVariable))
            {
                right = variable.getValue();
            }
            if(SymbolTable.get(variable).equals("0"))
            {
                root = variable.getValue();
            }

            mainVisitor.visitVarInsn(Opcodes.ILOAD, left);
            mainVisitor.visitVarInsn(Opcodes.ILOAD, right);
            mainVisitor.visitInsn(Opcodes.ISUB);
            mainVisitor.visitVarInsn(Opcodes.ISTORE, root);
        }

    }

    /**
     * Exit subtraction method
     */
    @Override public void exitSubtraction(KnightCodeParser.SubtractionContext ctx)
    {
        System.out.println("Exiting subtraction");
    }

    /**
     * Enters number
     */
    @Override public void enterNumber(KnightCodeParser.NumberContext ctx)
    {
        System.out.println("Number entered: " + ctx.getText());
    }

    /**
     * Exits number
     */
    @Override public void exitNumber(KnightCodeParser.NumberContext ctx)
    {
        System.out.println("Exit Number: " + ctx.getText());
    }

    /**
     * Enters comparison
     */
    @Override public void enterComparison(KnightCodeParser.ComparisonContext ctx)
    {
        System.out.println("comparison entered: " + ctx.getText());
    }

    /**
     * Exits comparsion
     */
    @Override public void exitComparison(KnightCodeParser.ComparisonContext ctx)
    {
        System.out.println("Exit comparison: " + ctx.getText());
    }

    /**
     * Enters Id
     */
    @Override public void enterId(KnightCodeParser.IdContext ctx)
    {
        System.out.println("ID entered: " + ctx.getText());
    }

    /**
     * Exits Id
     */
    @Override public void exitId(KnightCodeParser.IdContext ctx)
    {
        System.out.println("Exit ID: " + ctx.getText());
    }

    /**
     * Takes both children and decides if it is
     * greater than, less than, not equal to, or
     * equal to. Then it will execute.
     */
    @Override public void enterDecision(KnightCodeParser.DecisionContext ctx)
    {
        System.out.println("Decision entered: " + ctx.getText());

        Variable left = new Variable();
        Variable right = new Variable();

        for(Variable variable : SymbolTable.keySet())
        {
            if(variable.getName().equals(ctx.getChild(1).getText()))
            {
                left = variable;
            }
            if(variable.getName().equals(ctx.getChild(3).getText()))
            {
                right = variable;
            }
        }
        
        //Added Label object
        Label label = new Label();
        if(ctx.getChild(2).getText().equals("<"))
        {
            mainVisitor.visitJumpInsn(Opcodes.IF_ICMPLT, label);
        } 
        else if (ctx.getChild(2).getText().equals(">"))
        {
            mainVisitor.visitJumpInsn(Opcodes.IF_ICMPGT, label);
        }
        else if (ctx.getChild(2).getText().equals("="))
        {
            mainVisitor.visitJumpInsn(Opcodes.IF_ICMPEQ, label);
        }
        else if (ctx.getChild(2).getText().equals("<>"))
        {
            mainVisitor.visitJumpInsn(Opcodes.IF_ICMPNE, label);
        }
        mainVisitor.visitLabel(label);
    }

    /**
     * Exits decision method
     */
    @Override public void exitDecision(KnightCodeParser.DecisionContext ctx)
    {
        System.out.println("Exit decision: " + ctx.getText());
    }

    /**
     * Enters the print method. Checks the child 
     * and determines what to print
     */
    @Override public void enterPrint(KnightCodeParser.PrintContext ctx)
    {
        System.out.println("Entering print");

        String child = ctx.getChild(1).getText();
        int childValue = 0;
        Variable newVar = new Variable();

        for(Variable sTable : SymbolTable.keySet())
        {
            if(newVar.getName().equals(child) && newVar.getType().equals("STRING"))
            {
                newVar = sTable;
                childValue = newVar.getValue();
                mainVisitor.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                mainVisitor.visitVarInsn(Opcodes.ALOAD, childValue);
                mainVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
                break;
            }
            if(newVar.getName().equals(child) && newVar.getType().equals("INTEGER"))
            {
                newVar = sTable;
                childValue = newVar.getValue();
                mainVisitor.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                mainVisitor.visitVarInsn(Opcodes.ILOAD, childValue);
                mainVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
                break;
            }
            else 
            {
                mainVisitor.visitLdcInsn(child);
                mainVisitor.visitVarInsn(Opcodes.ASTORE, childValue);
                mainVisitor.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream");
                mainVisitor.visitVarInsn(Opcodes.ALOAD, childValue);
                mainVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "{Ljava/lang/String;}V", false);
                break;
            }
            
        }
    }

    /**
     * Exits print method
     */
    @Override public void exitPrint(KnightCodeParser.PrintContext ctx)
    {
        System.out.println("Exiting print: " + ctx.getText());
    }

    /**
     * Enter read method
     */
    @Override public void enterRead(KnightCodeParser.ReadContext ctx)
    {
        System.out.println("Entering read");
    }

    /**
     * Exit read method
     */
    @Override public void exitRead(KnightCodeParser.ReadContext ctx)
    {
        System.out.println("Exiting read");
    }
    

}
