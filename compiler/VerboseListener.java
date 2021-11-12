package compiler;

import org.antlr.v4.runtime.*;
import java.util.List;
import java.util.Collections;
public class VerboseListener extends BaseErrorListener {
	public void syntaxError(Recognizer<?, ?> recognizer
			, Object offendingSymbol, 
			int line, 
			int charPositionInLine, 
			String msg, 
			RecognitionException e) {
		
		List<String> stack = ((Parser)recognizer).getRuleInvocationStack();
		Collections.reverse(stack);
		
		System.err.println("Error");
		underlineError(recognizer, (Token)offendingSymbol, line, charPositionInLine);
		
	}
	
	
	
	
}
