package tlang.ast;

import java.math.BigDecimal;

import org.antlr.v4.runtime.ParserRuleContext;

import tlang.objects.TLangConstants;
import tlang.objects.TLangException;
import tlang.objects.TLangNumber;
import tlang.objects.TLangObject;
import tlang.objects.TLangText;

public class ASTNext extends ASTNode {

	public ASTNext(ParserRuleContext ctx) {
		super(ctx);
	}

	@Override
	public EvalResult evaluate(Frame frame) throws TLangException {		
		EvalResult result = new EvalResult(TLangConstants.NULL,EvalResult.NEXT);
		return result;
	}
	
	@Override
	public TLangObject expression() throws TLangException {
		TLangObject obj = new TLangObject();
		obj.$set(new TLangText("type"), new TLangText("next"));
		return obj;
	}
	@Override
	public TLangObject pretty() throws TLangException {
		String s = "next";
		return new TLangText(s);
	}
}
