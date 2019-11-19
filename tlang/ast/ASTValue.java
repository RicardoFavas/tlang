package tlang.ast;

import org.antlr.v4.runtime.ParserRuleContext;

import tlang.objects.TLangException;
import tlang.objects.TLangObject;
import tlang.objects.TLangPromise;
import tlang.objects.TLangText;

public class ASTValue extends ASTNode {
	private TLangObject value;
	public ASTValue(ParserRuleContext ctx, TLangObject value) {
		super(ctx);
		this.value = value;
	}

	@Override
	public EvalResult evaluate(Frame frame) throws TLangException {
		EvalResult result = new EvalResult(value);
		return result;
	}
	
	@Override
	public TLangObject expression() throws TLangException {
		TLangObject obj = new TLangObject();
		obj.$set(new TLangText("type"), new TLangText("value"));
		obj.$set(new TLangText("value"), value);
		return obj;
	}
	@Override
	public TLangObject pretty() throws TLangException {
		String s = "";
		return new TLangText(s);
	}
}
