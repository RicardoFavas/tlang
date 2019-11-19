package tlang.ast;

import org.antlr.v4.runtime.ParserRuleContext;

import tlang.objects.TLangConstants;
import tlang.objects.TLangException;
import tlang.objects.TLangObject;
import tlang.objects.TLangText;

public class ASTBreak extends ASTNode {

	public ASTBreak(ParserRuleContext ctx) {
		super(ctx);
	}

	@Override
	public EvalResult evaluate(Frame frame) throws TLangException {
		EvalResult result = new EvalResult(TLangConstants.NULL,EvalResult.BREAK);
		return result;
	}
	
	@Override
	public TLangObject expression() throws TLangException {
		TLangObject obj = new TLangObject();
		obj.$set(new TLangText("type"), new TLangText("break"));
		return obj;
	}
	
	@Override
	public TLangObject pretty() throws TLangException {
		String s = "break";
		return new TLangText(s);
	}
}
