package compiler;

import lexparse.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;
import org.objectweb.asm.*;
import org.objectweb.asm.Opcodes;

public class myListener extends KnightCodeBaseListener{
	
	private String programName;
	private ClassWriter cw;
	private MethodVisitor mainVisitor;
	
	HashMap<String, String> variables = new HashMap<String, String>();
	
	public myListener(String programName) {
		this.programName = programName;
	}
	
	public void setupClass() {
		cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		cw.visit(Opcodes.V11, Opcodes.ACC_PUBLIC, this.programName, null, "java/lang/Object", null);
		
		MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
		mv.visitCode();
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(1,1);
		mv.visitEnd();
		
		mainVisitor = cw.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
		mainVisitor.visitCode();
	}
	
	public void closeClass() {
		
		mainVisitor.visitInsn(Opcodes.RETURN);
		mainVisitor.visitMaxs(1,1);
		mainVisitor.visitEnd();
		
		cw.visitEnd();
		
		byte[] b = cw.toByteArray();
		
		Utilities.writeFile(b, this.programName+".class");
		
		System.out.println("Done!");
	}
	
	private void printContext(String ctx) {
		System.out.println(ctx);
	}
	@Override public void enterFile(KnightCodeParser.FileContext ctx) {
		setupClass();
		System.out.println("EnterFile");
	}
	
	@Override public void exitFile(KnightCodeParser.FileContext ctx) { 
		System.out.println("ExitFile");
		printContext(ctx.getText());
		
		closeClass();
	}
	
	@Override public void enterIdentifier(KnightCodeParser.IdentifierContext ctx) {
		System.out.println("Identifier: " + ctx.getText());
		
		variables.put(ctx.getText(), null);
	}
	
	@Override public void enterSetvar(KnightCodeParser.SetvarContext ctx) { 
		String variable = ctx.getText().substring(3,4);
		String value = ctx.getText().substring(6);
		
		variables.replace(variable, value);
	}
	
	@Override public void exitSetvar(KnightCodeParser.SetvarContext ctx) {
		
	}
	
	@Override public void enterAddition(KnightCodeParser.AdditionContext ctx) {
		
		String[] vars = ctx.getText().split("\\+");
		String variable = " ";
		for(String var : variables.keySet()) {
			System.out.println(var);
			if(variables.get(var).equals(ctx.getText()))
				variable = var;
		}
		
		int leftVar = Integer.parseInt(variables.get(vars[0]));
		int rightVar = Integer.parseInt(variables.get(vars[1]));
		
		mainVisitor.visitVarInsn(Opcodes.ILOAD, 4);
		mainVisitor.visitVarInsn(Opcodes.ILOAD, 5);
		mainVisitor.visitInsn(Opcodes.IADD);
		mainVisitor.visitVarInsn(Opcodes.ISTORE, 6);
		
		variables.replace(variable, Integer.toString(newVal));
		
	}
	
	@Override public void exitAddition(KnightCodeParser.AdditionContext ctx) {
		
		System.out.println("Why wont you add???" + ctx.getText());
	}
	
	@Override public void enterPrint(KnightCodeParser.PrintContext ctx) {
		String output = ctx.getChild(1).getText();
		
		mainVisitor.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
		mainVisitor.visitLdcInsn(variables.get(output));
		mainVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream",  "println", "(Ljava/lang/String;)V", false);
	}
}
