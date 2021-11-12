package compiler;

import lexparse.*

public class myListener extends KnightCodeBaseListener{
	
	public void enterFile(KnightCodeParser.FileContext ctx) {
		System.out.println("Enter program rule for first time");
	}
	
	public void exitFile(KnightCodeParser.FileContext ctx) {
		System.out.println("Leaving program rule...");
	}
	
	public void enterDeclare(KnightCodeParser.DeclareContext ctx) {
		System.out.println("Entering Declare");
	}
	public void exitDeclare(KnightCodeParser.DeclareContext ctx) {
		System.out.println("Exiting Declare");
	}
	public void exitVariable(KnightCodeParser.VariableContext ctx) {
		System.out.println(ctx.getText());
	}
	public void exitIdentifier(KnightCodeParser.VariableContext ctx) {
		System.out.println(ctx.getText());
	}
	public void exitVartype(KnightCodeParser.VartypeContext ctx) {
		System.out.println(ctx.getText());
	}
	public void exitBody(KnightCodeParser.BodyContext ctx) {
		System.out.println(ctx.getText());
	}
	public void exitStat
}
