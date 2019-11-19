package tlang.ast;

import org.antlr.v4.runtime.ParserRuleContext;

import tlang.objects.TLangException;
import tlang.objects.TLangObject;
import tlang.objects.TLangText;

public class ASTId extends ASTNode {

	private TLangText id;
	public ASTId(ParserRuleContext ctx, TLangText id) {
		super(ctx);
		this.id = id;
	}
	
	@Override
	public EvalResult evaluate(Frame frame) throws TLangException {
		EvalResult result = new EvalResult(frame.get(id));
		return result;
	}

	public TLangObject expression() throws TLangException {
		TLangObject obj = new TLangObject();
		
		obj.$set(new TLangText("type"), new TLangText("id"));
		obj.$set(new TLangText("value"), id);
		
		return obj;
	}
	
	@Override
	public TLangObject pretty() throws TLangException {
		String s = id.toString();
		return new TLangText(s);
	}
	
}
