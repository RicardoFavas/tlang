package tlang.ast;

import org.antlr.v4.runtime.ParserRuleContext;

import tlang.objects.TLangException;
import tlang.objects.TLangObject;
import tlang.objects.TLangText;

public class ASTReturn extends ASTNode {

	private ASTNode exp;
	public ASTReturn(ParserRuleContext ctx, ASTNode exp) {
		super(ctx);
		this.exp = exp;
	}

	@Override
	public EvalResult evaluate(Frame frame) throws TLangException {
		EvalResult result = exp.evaluate(frame);
		return new EvalResult(result.getValue(),EvalResult.RETURN);
	}
	
	@Override
	public TLangObject expression() throws TLangException {
		TLangObject obj = new TLangObject();
		obj.$set(new TLangText("type"), new TLangText("return"));
		obj.$set(new TLangText("value"), exp.expression());
		return obj;
	}
	@Override
	public TLangObject pretty() throws TLangException {
		String s = "";
		return new TLangText(s);
	}
}
