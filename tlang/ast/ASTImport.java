package tlang.ast;

import java.io.File;
import java.util.HashMap;

import org.antlr.v4.runtime.ParserRuleContext;

import tlang.objects.TLangException;
import tlang.objects.TLangObject;
import tlang.objects.TLangText;
import tlang.utils.RUtils;
import tlang.utils.TLangEngine;


public class ASTImport extends ASTNode {
	private ASTNode path;
	public ASTImport(ParserRuleContext ctx, ASTNode path) {
		super(ctx);
		this.path = path;
	}
	
	
	private HashMap<String,TLangObject> imports = new HashMap<>();

	@Override
	public EvalResult evaluate(Frame frame) throws TLangException {
		EvalResult r = path.evaluate(frame);
//		
		TLangObject path = r.getValue();
		String p = path.$text().toString();
		TLangObject imp = imports.get(p);
		if (imp != null)
			return new EvalResult(imp);
//		
		File f = new File(p);
		EvalResult result = new TLangEngine().evaluate(f);
//		imports.put(p, parsed);
		
		
		
		return result;
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
