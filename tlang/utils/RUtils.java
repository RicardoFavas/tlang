package tlang.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.WrongMethodTypeException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.antlr.v4.gui.TreeViewer;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.ANTLRErrorStrategy;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DefaultErrorStrategy;
import org.antlr.v4.runtime.FailedPredicateException;
import org.antlr.v4.runtime.InputMismatchException;
import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.IntervalSet;

import tlang.ast.ASTNode;
import tlang.ast.EvalResult;
import tlang.ast.Frame;
import tlang.ast.TLangVisitor;
import tlang.objects.TLangBoolean;
import tlang.objects.TLangConstants;
import tlang.objects.TLangException;
import tlang.objects.TLangList;
import tlang.objects.TLangNull;
import tlang.objects.TLangNumber;
import tlang.objects.TLangObject;
import tlang.objects.TLangText;
import tlang.parser.TLangLexer;
import tlang.parser.TLangParser;

public class RUtils {

	public static <T> void addAt(List<T> list, int index, T value) { list.add(index, value);}
	public static <T> void addFirst(List<T> list, T value) { list.add(0, value); }
	public static <T> void addLast(List<T> list, T value) { list.add(list.size(), value); }
	public static <T> void addFirst(List<T> list, List<T> value) { list.addAll(0, value); }
	public static <T> void addLast(List<T> list, List<T> value) { list.addAll(list.size(), value); }
	public static <T> T getFirst(List<T> list) { return list.get(0); }
	public static <T> T getLast(List<T> list) { int last = list.size()-1; return list.get(last); }
	
	public static <T> T[] addAt(T[] array, int index, T[] values) {		
		T[] newArray = Arrays.copyOf(array, array.length+values.length);
		System.arraycopy(array, 0, newArray, 0, index);
		System.arraycopy(values, 0, newArray, index, values.length);
		System.arraycopy(array, index, newArray, index+values.length, array.length-index);
		return newArray;
	}
	public static <T> T[] addAt(T[] array, int index, T value) {
		T[] newArray = Arrays.copyOf(array, array.length+1);
		newArray[index] = value;
		System.arraycopy(array, index, newArray, index+1, array.length-index);
		return newArray;
	}
	public static <T> T[] addFirst(T[] array, T value){	return addAt(array,0,value); }
	public static <T> T[] addLast(T[] array, T value){ return addAt(array,array.length,value); }
	public static <T> T[] addFirst(T[] array, T[] values){	return addAt(array,0,values); }
	public static <T> T[] addLast(T[] array, T[] values){ return addAt(array,array.length,values); }
	public static <T> T getFirst(T[] array){ return array[0]; }
	public static <T> T getLast(T[] array){ return array[array.length-1]; }

	
//	public static String getObjectInstance(ParserRuleContext ctx) {
//		//gerar nome da instancia "sline-col:fline:col" ex: "3:1-6:2"
//		String r = "";
//		r += "[";
//		r += ctx.getStart().getLine();
//		r += ":";
//		r += ctx.getStart().getCharPositionInLine()+1;
//		r += "-";
//		r += ctx.getStop().getLine();
//		r += ":";
//		r += ctx.getStop().getCharPositionInLine()+2;
//		r += "]";
//		return r;
//	}
	
	public static String firstGroup(String pattern,String text) {
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(text);
		if (m.find())
			return m.group(1);
		return null;
	}
	
	public static String[] matchGroups(String pattern,String text) {
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(text);
		ArrayList<String> matches = new ArrayList<>();
		while(m.find())
			matches.add(m.group());
		return matches.toArray(new String[matches.size()]);
	}
	
	public static String ctxToString(ParserRuleContext ctx, TLangParser parser,String source) {		
		String r = "";
		r += "[";
		r += ctx.getStart().getLine();
		r += ":";
		r += ctx.getStart().getCharPositionInLine()+1;
		r += "-";
		r += ctx.getStop().getLine();
		r += ":";
		r += ctx.getStop().getCharPositionInLine()+2;
		r += "]";
		r += ctx.getText();
		
		return r;
	}

	


	public static String join(List<?> list) {
		return join(list,", ");
	}
	public static String join(List<?> list, String separator) {
		return list.stream()
		  .map(Object::toString)
		  .collect(Collectors.joining(separator));
	}
	public static String join(Object[] arr) {
		return join(arr,", ");
	}
	public static String join(Object[] arr, String separator) {
		List<String> list = Arrays.asList(toStrList(arr));
		String result = String.join(separator, list);
		return result;
	}
		
	public static String[] toStrList(Object[] arr) {
		String[] strArr = new String[arr.length];
		for(int i = 0; i < strArr.length; i++)
			strArr[i] = ""+arr[i];
		return strArr;
	}
	public static String[] toStrList(Collection col) {
		String[] strArr = new String[col.size()];
		int i = 0;
		for(Object c : col) {
			strArr[i] = ""+c;
			i++;
		}
		return strArr;
	}
	
	
	
	
}
