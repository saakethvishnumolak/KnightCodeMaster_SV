package compiler;

import lexparse.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.objectweb.asm.*;
import org.objectweb.asm.Opcodes;

public class myListener extends KnightCodeBaseListener{
	
	private String programName;
	private ClassWriter cw;
	private MethodVisitor mainVisitor;
	
	public myListener(String programName) {
		this.programName = programName;
	}
	
	public void enterFile(KnightCodeParser.FileContext ctx) {
		System.out.println("Enter program rule for first time");
	}
	
	public void exitFile(KnightCodeParser.FileContext ctx) {
		System.out.println("Leaving program rule...");
		byte[] b = cw.toByteArray();
		Utilities.writeFile(b,this.programName+".class");
		
		System.out.println("Done!");
	}
	
	private void printContext(String ctx) {
		System.out.println(ctx);
	}
	
}
