package compiler;

import lexparse.*;

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
		for(Map.Entry me : variables.entrySet()) {
			System.out.println("Key: " + me.getKey() + " Value: " + me.getValue());
		}
		
		closeClass();
	}
	
	@Override public void enterDeclare(KnightCodeParser.DeclareContext ctx) { 
		System.out.println("enterDeclare");
	}
	
	@Override public void exitDeclare(KnightCodeParser.DeclareContext ctx) {
		System.out.println("exitDeclare");
		printContext(ctx.getText());
	}
	
	@Override public void enterVariable(KnightCodeParser.VariableContext ctx) {
		System.out.println("EnterVariable");
	}
	
	@Override public void exitVariable(KnightCodeParser.VariableContext ctx) {
		System.out.println("ExitVariable");
		printContext(ctx.getText());
	}
	
	@Override public void enterIdentifier(KnightCodeParser.IdentifierContext ctx) {
		System.out.println("EnterIdentifier");
	}
	
	@Override public void exitIdentifier(KnightCodeParser.IdentifierContext ctx) {
		System.out.println("ExitIdentifier");
		printContext(ctx.getText());
		
	}
	
	@Override public void enterVartype(KnightCodeParser.VartypeContext ctx) {
		System.out.println("EnterVartype");
	}
	
	@Override public void exitVartype(KnightCodeParser.VartypeContext ctx) {
		System.out.println("ExitVartype");
		printContext(ctx.getText());
		
	}
	
	@Override public void enterBody(KnightCodeParser.BodyContext ctx) { 
		System.out.println("EnterBody");
	}
	
	@Override public void exitBody(KnightCodeParser.BodyContext ctx) {
		System.out.println("ExitBody");
		printContext(ctx.getText());
	}
	
	@Override public void enterStat(KnightCodeParser.StatContext ctx) {
		System.out.println("EnterStat");
	}
	
	@Override public void exitStat(KnightCodeParser.StatContext ctx) {
		System.out.println("ExitStat");
		printContext(ctx.getText());
	}
	
	@Override public void enterSetvar(KnightCodeParser.SetvarContext ctx) { 
		System.out.println("EnterSetvar");
	}
	
	@Override public void exitSetvar(KnightCodeParser.SetvarContext ctx) {
		System.out.println("ExitSetvar");
		printContext(ctx.getText());
		String var = ctx.getText();
		var = var.substring(var.indexOf("SET") + 3);
		var = var.substring(0, var.indexOf(":="));
		String value = ctx.getText().substring(ctx.getText().lastIndexOf(":=") + 2);
		
		variables.put(var, value);
		
	}
	
	@Override public void enterParenthesis(KnightCodeParser.ParenthesisContext ctx) {
		System.out.println("EnterParanthesis");
	}
	
	@Override public void exitParenthesis(KnightCodeParser.ParenthesisContext ctx) {
		System.out.println("ExitParantheis");
		printContext(ctx.getText());
	}
	
	@Override public void enterMultiplication(KnightCodeParser.MultiplicationContext ctx) {
		System.out.println("EnterMult");
	}
	@Override public void exitMultiplication(KnightCodeParser.MultiplicationContext ctx) {
		System.out.println("ExitMult");
		printContext(ctx.getText());
	}
	
	@Override public void enterAddition(KnightCodeParser.AdditionContext ctx) {
		System.out.println("EnterAddition");
	}
	
	@Override public void exitAddition(KnightCodeParser.AdditionContext ctx) {
		System.out.println("ExitAddition");
		printContext(ctx.getText());
	}
	
	@Override public void enterSubtraction(KnightCodeParser.SubtractionContext ctx) {
		System.out.println("EnterSubtraction");
		
	}
	
	@Override public void exitSubtraction(KnightCodeParser.SubtractionContext ctx) {
		System.out.println("ExitSubtraction");
		printContext(ctx.getText());
	}

	@Override public void enterNumber(KnightCodeParser.NumberContext ctx) { 
		System.out.println("EnterNumber");
	}
	
	@Override public void exitNumber(KnightCodeParser.NumberContext ctx) { 
		System.out.println("ExitNumber");
		printContext(ctx.getText());
	}
	
	@Override public void enterComparison(KnightCodeParser.ComparisonContext ctx) { 
		System.out.println("EnterCompare");
		
	}
	
	@Override public void exitComparison(KnightCodeParser.ComparisonContext ctx) { 
		System.out.println("ExitCompare");
		printContext(ctx.getText());
	}

	@Override public void enterDivision(KnightCodeParser.DivisionContext ctx) {
		System.out.println("EnterDivision");
		
	}
	
	@Override public void exitDivision(KnightCodeParser.DivisionContext ctx) {
		System.out.println("ExitDivision");
		printContext(ctx.getText());
	}
	
	@Override public void enterId(KnightCodeParser.IdContext ctx) {
		System.out.println("EnterId");
		
	}

	@Override public void exitId(KnightCodeParser.IdContext ctx) {
		System.out.println("ExitId");
		printContext(ctx.getText());
	}
	
	@Override public void enterComp(KnightCodeParser.CompContext ctx) { 
		System.out.println("EnterComp");
		
	}
	
	@Override public void exitComp(KnightCodeParser.CompContext ctx) { 
		System.out.println("ExitComp");
		
	}

	@Override public void enterPrint(KnightCodeParser.PrintContext ctx) { 
		System.out.println("EnterPrint");
		
	}
	
	@Override public void exitPrint(KnightCodeParser.PrintContext ctx) { 
		System.out.println("ExitPrint");
		printContext(ctx.getText());
	}
	
	@Override public void enterRead(KnightCodeParser.ReadContext ctx) { 
		System.out.println("EnterRead");
		
	}
	
	@Override public void exitRead(KnightCodeParser.ReadContext ctx) { 
		System.out.println("ExitRead");
		printContext(ctx.getText());
	}

	@Override public void enterDecision(KnightCodeParser.DecisionContext ctx) {
		System.out.println("EnterDecision");
		
	}
	
	@Override public void exitDecision(KnightCodeParser.DecisionContext ctx) { 
		System.out.println("ExitDecision");
		printContext(ctx.getText());
	}
	
	@Override public void enterLoop(KnightCodeParser.LoopContext ctx) { 
		System.out.println("EnterLoop");
		
	}
	
	@Override public void exitLoop(KnightCodeParser.LoopContext ctx) {
		System.out.println("ExitLoop");
		printContext(ctx.getText());
	}

	@Override public void enterEveryRule(ParserRuleContext ctx) {
		
	}
	
	@Override public void exitEveryRule(ParserRuleContext ctx) { }

}
