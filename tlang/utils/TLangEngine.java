package tlang.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.gui.TreeViewer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DefaultErrorStrategy;
import org.antlr.v4.runtime.FailedPredicateException;
import org.antlr.v4.runtime.InputMismatchException;
import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;

import tlang.ast.ASTNode;
import tlang.ast.EvalResult;
import tlang.ast.Frame;
import tlang.ast.TLangVisitor;
import tlang.objects.TLangBoolean;
import tlang.objects.TLangConstants;
import tlang.objects.TLangException;
import tlang.objects.TLangNumber;
import tlang.objects.TLangObject;
import tlang.objects.TLangText;
import tlang.parser.TLangLexer;
import tlang.parser.TLangParser;

public class TLangEngine {
	
	
	private static TLangVisitor visitor = new TLangVisitor();
	private boolean useCache = true;
	private static HashMap<String,ASTNode> scriptCache = new HashMap<>();
	private TLangErrorHandler errorHandler = new TLangErrorHandler();


	public class TLangErrorHandler extends DefaultErrorStrategy {
		@Override
		protected void reportFailedPredicate(Parser recognizer, FailedPredicateException e) {
			super.reportFailedPredicate(recognizer, e);
			throw new RuntimeException();
		}
		@Override
		protected void reportNoViableAlternative(Parser recognizer, NoViableAltException e) {
			super.reportNoViableAlternative(recognizer, e);
			throw new RuntimeException();
		}
		@Override
		protected void reportInputMismatch(Parser recognizer, InputMismatchException e) {
			super.reportInputMismatch(recognizer, e);
			throw new RuntimeException();
		}
		@Override
		protected void reportUnwantedToken(Parser recognizer) {
			super.reportUnwantedToken(recognizer);
			throw new RuntimeException(recognizer.getSourceName());
		}
		@Override
		protected void reportMissingToken(Parser recognizer) {
			super.reportMissingToken(recognizer);
			throw new RuntimeException();
		}
		@Override
		public void reportError(Parser recognizer, RecognitionException e) {
			super.reportError(recognizer, e);
			throw new RuntimeException();
		}
	}
	
	public ASTNode parse(String script) {
		if (useCache) { 
			ASTNode program = scriptCache.get(script);
			if (program != null)
				return program;
		}
		
		CharStream fileStream = CharStreams.fromString(script);
		TLangLexer lexer = new TLangLexer(fileStream);		
        CommonTokenStream tokens = new CommonTokenStream(lexer);
		TLangParser parser = new TLangParser(tokens);
		
		parser.removeErrorListeners();
		
		parser.setErrorHandler(errorHandler);
		
		ASTNode program = visitor.visit(parser.program());
		if (useCache)
			scriptCache.put(script, program);
		return program;
	}
	
	
	public EvalResult evaluate(File file) throws TLangException {
		try {
			String script = new String(Files.readAllBytes(file.toPath()));
			script = script.replaceAll("\t","    ");
			return evaluate(script);
		} catch (IOException e) {
			throw new TLangException(e);
		}
	}
	
	
	public EvalResult evaluate(String script) throws TLangException {
		ASTNode program = parse(script);

		TLangObject topLevelObject = new TLangObject();
		Frame frame = new Frame(topLevelObject);
		EvalResult result = program.evaluate(frame);
		return result;
	}

	public void showAST(String script) throws TLangException {
		CharStream fileStream = CharStreams.fromString(script);
		
		TLangLexer lexer = new TLangLexer(fileStream);		
        CommonTokenStream tokens = new CommonTokenStream(lexer);
		TLangParser parser = new TLangParser(tokens);
		
        TreeViewer viewr = new TreeViewer(
    		Arrays.asList(parser.getRuleNames()),
    		parser.program()
		);
        viewr.open();
	}
	
	public static Object langObject2Java(TLangObject obj, Class<?> type) throws TLangException {
		if (obj == TLangConstants.NULL) {
			return type.cast(null);
		}
		
		if (type.isAssignableFrom(List.class)) {
			return RUtils.toStrList(obj.attributes.values());
		}
		if (type.isAssignableFrom(Map.class)) {
			return RUtils.toStrList(obj.attributes.values());
		}

		//TLangBoolean
		if (obj instanceof TLangBoolean) {
			if (type == Boolean.class || type == boolean.class)
				return obj == TLangConstants.TRUE ? Boolean.TRUE : Boolean.FALSE;
		}
		
		//TLangText
		if (obj instanceof TLangText) {
			TLangText casted = (TLangText)obj;
			if (type.isAssignableFrom(casted.value.getClass()))
				return casted.value;
		}
		
		//TLangNumber
		if (obj instanceof TLangNumber) {
			TLangNumber casted = (TLangNumber)obj;
			if (type == Double.class || type == double.class)
				return casted.value.doubleValue();
			if (type == Float.class || type == float.class)
				return casted.value.floatValue();
			if (type == Long.class || type == long.class)
				return casted.value.longValue();
			if (type == Integer.class || type == int.class)
				return casted.value.intValue();
			if (type == Short.class || type == short.class)
				return casted.value.shortValue();
		}
		throw new TLangException();
	}

}
