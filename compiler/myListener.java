package compiler;

/**
 * Description: 
 * 
 * @author Justin Mattix
 *
 * @version 13
 * Programming Project 4
 * CS322 - Compiler Construction
 * Fall 2021
 * 
 */

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
	
	int count = 1;
	int labelcounter = 1;
	
	private Label startLabel;
	
	HashMap<Variable, String> variables = new HashMap<Variable, String>();
	
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
		mainVisitor.visitMaxs(3,3);
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
	
	@Override public void enterDeclare(KnightCodeParser.DeclareContext ctx) {
		System.out.println("DECLARE");
	}
	
	@Override public void enterVariable(KnightCodeParser.VariableContext ctx) {
		String declare = ctx.getText();
		Variable temp  = new Variable();
		if(declare.contains("INTEGER")) {
			String varName = declare.substring(7);
			variables.put(new Variable(count, varName, "INTEGER"), "0");
		}
		else {
			String varName = declare.substring(6);
			variables.put(new Variable(count, varName, "STRING"), "0");
		}
		count++;
	}
	
	@Override public void enterVartype(KnightCodeParser.VartypeContext ctx) {
		System.out.println("Vartype: " + ctx.getText());
	}
	
	@Override public void enterIdentifier(KnightCodeParser.IdentifierContext ctx) {
		System.out.println("Identifiy: " + ctx.getText());
	}
	
	@Override public void enterSetvar(KnightCodeParser.SetvarContext ctx) { 
		Variable temp = new Variable();
		int index = ctx.getText().indexOf('=');
		String var = ctx.getText().substring(3, index - 1);
		String value = ctx.getText().substring(index + 1);
		if(value.contains("+") || value.contains("-") || value.contains("*") || value.contains("/"))
			value = "0";
		for(Variable v : variables.keySet()) {
			if(v.getName().equals(var)) {
				temp = v;
			}
		}
		if(temp.getName() != null) {
			for(Variable v : variables.keySet()) {
				if(temp.getIndex() == v.getIndex()) {
					variables.replace(v, value);
				}
			}
			mainVisitor.visitLdcInsn(Integer.parseInt(value));
			mainVisitor.visitVarInsn(Opcodes.ISTORE, temp.getIndex());
		}
		else {
			variables.put(new Variable(count, var, null), value);
			mainVisitor.visitLdcInsn(Integer.parseInt(value));
			mainVisitor.visitVarInsn(Opcodes.ISTORE, count);
			count++;
		}
		
	}
	
	@Override public void exitSetvar(KnightCodeParser.SetvarContext ctx) {
		System.out.println("exitsetVar: " + ctx.getText());
	}
	
	@Override public void enterAddition(KnightCodeParser.AdditionContext ctx) {
		String expression = ctx.getText();
		String leftVar = expression.substring(0, expression.indexOf('+'));
		String rightVar = expression.substring(expression.indexOf('+') + 1);
		int leftIndex = 0, rightIndex = 0, rootIndex = 0;
		
		
		for(Variable var : variables.keySet()) {
			if(var.getName().equals(leftVar)) {
				leftIndex = var.getIndex();
			}
			if(var.getName().equals(rightVar)) {
				rightIndex = var.getIndex();
			}
			if(variables.get(var).equals("0")) {
				rootIndex = var.getIndex();
			}
		}
		
		mainVisitor.visitVarInsn(Opcodes.ILOAD, leftIndex);
		mainVisitor.visitVarInsn(Opcodes.ILOAD, rightIndex);
		mainVisitor.visitInsn(Opcodes.IADD);
		mainVisitor.visitVarInsn(Opcodes.ISTORE, rootIndex);
	}
	
	@Override public void exitAddition(KnightCodeParser.AdditionContext ctx) {
		System.out.println("exitAdd: " + ctx.getText());
	}
	
	@Override public void enterSubtraction(KnightCodeParser.SubtractionContext ctx) {
		String expression = ctx.getText();
		String leftVar = expression.substring(0, expression.indexOf('-'));
		String rightVar = expression.substring(expression.indexOf('-') + 1);
		int leftIndex = 0, rightIndex = 0, rootIndex = 0;
		for(Variable var : variables.keySet()) {
			if(var.getName().equals(leftVar)) {
				leftIndex = var.getIndex();
			}
			if(var.getName().equals(rightVar)) {
				rightIndex = var.getIndex();
			}
			if(variables.get(var).equals("0")) {
				rootIndex = var.getIndex();
			}
		}
		
		mainVisitor.visitVarInsn(Opcodes.ILOAD, leftIndex);
		mainVisitor.visitVarInsn(Opcodes.ILOAD, rightIndex);
		mainVisitor.visitInsn(Opcodes.ISUB);
		mainVisitor.visitVarInsn(Opcodes.ISTORE, rootIndex);
	}
	
	@Override public void exitSubtraction(KnightCodeParser.SubtractionContext ctx) { }
	
	@Override public void enterDivision(KnightCodeParser.DivisionContext ctx) {
		String expression = ctx.getText();
		String leftVar = expression.substring(0, expression.indexOf('/'));
		String rightVar = expression.substring(expression.indexOf('/') + 1);
		int leftIndex = 0, rightIndex = 0, rootIndex = 0;
		for(Variable var : variables.keySet()) {
			if(var.getName().equals(leftVar)) {
				leftIndex = var.getIndex();
			}
			if(var.getName().equals(rightVar)) {
				rightIndex = var.getIndex();
			}
			if(variables.get(var).equals("0")) {
				rootIndex = var.getIndex();
			}
		}
		
		mainVisitor.visitVarInsn(Opcodes.ILOAD, leftIndex);
		mainVisitor.visitVarInsn(Opcodes.ILOAD, rightIndex);
		mainVisitor.visitInsn(Opcodes.IDIV);
		mainVisitor.visitVarInsn(Opcodes.ISTORE, rootIndex);
	}
	
	@Override public void exitDivision(KnightCodeParser.DivisionContext ctx) { }
	
	@Override public void enterMultiplication(KnightCodeParser.MultiplicationContext ctx) {
		String expression = ctx.getText();
		String leftVar = expression.substring(0, expression.indexOf('*'));
		String rightVar = expression.substring(expression.indexOf('*') + 1);
		int leftIndex = 0, rightIndex = 0, rootIndex = 0;
		for(Variable var : variables.keySet()) {
			if(var.getName().equals(leftVar)) {
				leftIndex = var.getIndex();
			}
			if(var.getName().equals(rightVar)) {
				rightIndex = var.getIndex();
			}
			if(variables.get(var).equals("0")) {
				rootIndex = var.getIndex();
			}
		}
		
		mainVisitor.visitVarInsn(Opcodes.ILOAD, leftIndex);
		mainVisitor.visitVarInsn(Opcodes.ILOAD, rightIndex);
		mainVisitor.visitInsn(Opcodes.IMUL);
		mainVisitor.visitVarInsn(Opcodes.ISTORE, rootIndex);
	}
	
	@Override public void exitMultiplication(KnightCodeParser.MultiplicationContext ctx) { }
	
	
	@Override public void enterBody(KnightCodeParser.BodyContext ctx) { 
		System.out.println("EnterBody: " + ctx.getText());
	}
	
	@Override public void exitBody(KnightCodeParser.BodyContext ctx) {
		System.out.println("ExitBody: " + ctx.getText());
	}
	
	@Override public void enterStat(KnightCodeParser.StatContext ctx) {
		System.out.println("EnterStat: " + ctx.getText());
	}
	
	@Override public void exitStat(KnightCodeParser.StatContext ctx) {
		System.out.println("ExitStat: " + ctx.getText());
	}
	
	@Override public void enterNumber(KnightCodeParser.NumberContext ctx) {
		System.out.println("EnterNumber: " + ctx.getText());
		mainVisitor.visitIntInsn(Opcodes.BIPUSH, Integer.parseInt(ctx.getText()));
	}
	
	@Override public void exitNumber(KnightCodeParser.NumberContext ctx) {
		System.out.println("ExitNumber: " + ctx.getText());
	}
	@Override public void enterComparison(KnightCodeParser.ComparisonContext ctx) {
		System.out.println("EnterCompare: " + ctx.getText());
	}
	
	@Override public void exitComparison(KnightCodeParser.ComparisonContext ctx) { 
		System.out.println("ExitCompare: " + ctx.getText());
	}
	
	@Override public void enterDecision(KnightCodeParser.DecisionContext ctx) {
		System.out.println("Decision: " + ctx.getText());
	}
	
	@Override public void exitDecision(KnightCodeParser.DecisionContext ctx) {
		System.out.println("ExitDecision: " + ctx.getText());
	}
	@Override public void enterId(KnightCodeParser.IdContext ctx) {
		System.out.println("EnterID: " + ctx.getText());
	}
	
	@Override public void exitId(KnightCodeParser.IdContext ctx) {
		System.out.println("ExitID: " + ctx.getText());
	}
	@Override public void enterComp(KnightCodeParser.CompContext ctx) {
		System.out.println("EnterComp: " + ctx.getText());
	}
	
	@Override public void exitComp(KnightCodeParser.CompContext ctx) {
		System.out.println("ExitComp: " + ctx.getText());
	}
	
	@Override public void enterRead(KnightCodeParser.ReadContext ctx) {
		String var = ctx.getChild(1).getText();
		Variable temp = new Variable();
		int index = 0;
		for(Variable v : variables.keySet()) {
			if(v.getName().equals(var)) {
				index = v.getIndex();
				temp = v;
			}
		}
		count++;
		mainVisitor.visitTypeInsn(Opcodes.NEW, "java/util/Scanner");
		mainVisitor.visitInsn(Opcodes.DUP);
		mainVisitor.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "in", "Ljava/io/InputStream;");
		mainVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/util/Scanner", "<init>", "(Ljava/io/InputStream;)V", false);
		mainVisitor.visitVarInsn(Opcodes.ASTORE, count);
		mainVisitor.visitVarInsn(Opcodes.ALOAD, count);
		count++;
		mainVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/util/Scanner", "next", "()Ljava/lang/String;", false);
		
		if(temp.getType().equals("INTEGER")) {
			mainVisitor.visitVarInsn(Opcodes.ASTORE, index);
			mainVisitor.visitVarInsn(Opcodes.ALOAD, index);
			mainVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Integer", "parseInt", "(Ljava/lang/String;)I", false);
			mainVisitor.visitVarInsn(Opcodes.ISTORE, index);
		}
		else {
			mainVisitor.visitVarInsn(Opcodes.ASTORE, index);
		}
	}
	
	@Override public void exitRead(KnightCodeParser.ReadContext ctx) {
		
	}
	
	@Override public void enterLoop(KnightCodeParser.LoopContext ctx) {
		String left = ctx.getChild(1).getText();
		String right = ctx.getChild(3).getText();
		startLabel = new Label();
		Variable leftVar = new Variable();
		Variable rightVar = new Variable();
		
		for(Variable var : variables.keySet()) {
			if(var.getName().equals(left)) {
				leftVar = var;
			}
			else if(var.getName().equals(right)) {
				rightVar = var;
			}
		}
		if(variables.keySet().contains(leftVar)){
			mainVisitor.visitVarInsn(Opcodes.ILOAD, leftVar.getIndex());
		}
		else {
			mainVisitor.visitIntInsn(Opcodes.BIPUSH, Integer.parseInt(left));
		}
		if(variables.keySet().contains(rightVar)) {
			mainVisitor.visitVarInsn(Opcodes.ILOAD, rightVar.getIndex());
		}
		else {
			mainVisitor.visitIntInsn(Opcodes.BIPUSH, Integer.parseInt(right));
		}
		
	}
	
	@Override public void exitLoop(KnightCodeParser.LoopContext ctx) {
		//would only compile with this here
		mainVisitor.visitLabel(startLabel);

		if(ctx.getChild(2).getText().equals(">")) {
			mainVisitor.visitJumpInsn(Opcodes.IF_ICMPGT, startLabel);
		}
		else if(ctx.getChild(2).getText().equals("<")) {
			mainVisitor.visitJumpInsn(Opcodes.IF_ICMPLT, startLabel);
		}
		if(ctx.getChild(2).getText().equals("<>")) {
			mainVisitor.visitJumpInsn(Opcodes.IF_ICMPNE, startLabel);
		}
		if(ctx.getChild(2).getText().equals(":=")) {
			mainVisitor.visitJumpInsn(Opcodes.IF_ICMPEQ, startLabel);
		}
	}
	
	@Override public void enterPrint(KnightCodeParser.PrintContext ctx) {
		String output = ctx.getChild(1).getText();
		int outputIndex = 0;
		Variable temp = new Variable();
		for(Variable var : variables.keySet()) {
			
			if(var.getName().equals(output) && var.getType().equals("INTEGER")) {
				outputIndex = var.getIndex();
				temp = var;
				mainVisitor.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
				mainVisitor.visitVarInsn(Opcodes.ILOAD, outputIndex);
				mainVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream",  "println", "(I)V", false);
				break;
			}
			else if(var.getName().equals(output) && var.getType().equals("STRING")) {
				outputIndex = var.getIndex();
				temp = var;
				mainVisitor.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
				mainVisitor.visitVarInsn(Opcodes.ALOAD, outputIndex);
				mainVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream",  "println", "(Ljava/lang/String;)V", false);
				break;
			}
			else {
				mainVisitor.visitLdcInsn(output);
				mainVisitor.visitVarInsn(Opcodes.ASTORE, count);
				mainVisitor.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
				mainVisitor.visitVarInsn(Opcodes.ALOAD, count);
				mainVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream",  "println", "(Ljava/lang/String;)V", false);
				break;

			}
		}
			
	}
	
	@Override public void enterEveryRule(ParserRuleContext ctx) {
		System.out.println(ctx.getText());
	}
}