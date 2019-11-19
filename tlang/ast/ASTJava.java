package tlang.ast;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.antlr.v4.runtime.ParserRuleContext;

import tlang.objects.TLangException;
import tlang.objects.TLangObject;
import tlang.objects.TLangText;
import tlang.utils.RUtils;

public class ASTJava extends ASTNode {

	private ASTNode exp;
//	TLangText instanceName;
	public ASTJava(ParserRuleContext ctx, ASTNode exp) {
		super(ctx);
		this.exp = exp;
		
//		String r = RUtils.getObjectInstance(getCtx());
//		instanceName = new TLangText();

	}
	@Override
	public EvalResult evaluate(Frame frame) throws TLangException {
		TLangText source = (TLangText)(exp.evaluate(frame).getValue());
		String s = source.toString();		
		Class<?> clazz = r(s);
		try {

			Object obj = clazz.getConstructor().newInstance();
			
			TLangObject to = (TLangObject)obj;
			return new EvalResult(to);
		} catch (Exception e) {
        	throw new TLangException(e);
        }	
	}

	public static Class<?> r(String source) throws TLangException {
		String className = RUtils.firstGroup("class\\s+(\\w+)\\s*", source);
		
        String tmpProperty = System.getProperty("java.io.tmpdir");
        Path sourcePath = Paths.get(tmpProperty, className+".java");
        try {
	        Files.write(sourcePath, source.getBytes("utf-8"));
	        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
	        compiler.run(null, null, null, sourcePath.toFile().getAbsolutePath());
	        URL classUrl = sourcePath.getParent().toFile().toURI().toURL();
	        URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{classUrl});
	        Class<?> clazz = Class.forName(className, true, classLoader);
	        return clazz;
        } catch (Exception e) {
        	throw new TLangException(e);
        }
	}
	
	@Override
	public TLangObject expression() throws TLangException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public TLangObject pretty() throws TLangException {
		String s = "";
		return new TLangText(s);
	}
	
}
