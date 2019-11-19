import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.antlr.v4.gui.TreeViewer;

import tlang.ast.ASTNode;
import tlang.ast.EvalResult;
import tlang.objects.TLangConstants;
import tlang.objects.TLangException;
import tlang.objects.TLangObject;
import tlang.parser.TLangParser;
import tlang.utils.RUtils;
import tlang.utils.TLangEngine;
import tlang.utils.tuples.Pair;
import tlang.utils.tuples.Quartet;

public class Main {


	static ArrayList<Pair<File,String>> scripts = new ArrayList<>();
	public static void parseRecur(File f) throws TLangException {
		if (f.isDirectory()) {
			for (File file : f.listFiles()) {
				parseRecur(file);
			}
		} else {
			if (!f.getName().startsWith("library"))
				scripts.add(new Pair<>(f,null));
		}
	}
	
	
	static int loops = 1;
	public static void main(String[] args) throws TLangException, IOException, InterruptedException {
		
		File filesPath = new File("./src/tlang.test/");
		parseRecur(filesPath);
	
		long start = System.currentTimeMillis();
		for (Pair<File, String> s : scripts) {
			File file = s.getFirst();
			String script = new String(Files.readAllBytes(file.toPath()));
			script = script.replaceAll("\t","    ");
			s.setSecond(script);
		}
		long fetchingTime = System.currentTimeMillis()-start;
		
		scripts.removeIf(s -> s.getSecond().contains("#skip"));
		
		ArrayList<Pair<File,String>> selected = new ArrayList<>();
		for (Pair<File, String> s : scripts) {
			String script = s.getSecond();
			if (script.contains("#!")) {
				selected.add(s);
			}
		}
		if (selected.size() > 0)
			scripts = selected;
		
		System.out.println("Starting "+scripts.size()+" files!");
		TLangEngine engine = new TLangEngine();
		start = System.currentTimeMillis();

		for (int i = 0; i < loops; i++) {
			for (Pair<File, String> s : scripts) {
				String script = s.getSecond();
				if (script.contains("#ast"))
					engine.showAST(script);
				EvalResult result = engine.evaluate(script);
				if (result.getValue() != TLangConstants.TRUE) {
					System.err.println("ERROR");
					System.err.println(s.getFirst().getName());
					System.err.println(result);
				}
			}
		}	
		long evaluatingTime = System.currentTimeMillis()-start;
		TLangConstants.EXECUTOR.shutdown();
		
		Thread.sleep(1000);
		System.out.print("Fetching: "+fetchingTime+"ms ");
		System.out.print("Evaluating: "+(evaluatingTime/loops)+"ms ");
		System.out.print("Total: "+(fetchingTime+evaluatingTime)+"ms ");

		
	}
	
}
