package tlang.ast;

import org.antlr.v4.runtime.ParserRuleContext;

import tlang.objects.TLangBoolean;
import tlang.objects.TLangConstants;
import tlang.objects.TLangException;
import tlang.objects.TLangObject;
import tlang.objects.TLangText;

public class ASTAssert extends ASTNode {

	ASTNode exp;
	public ASTAssert(ParserRuleContext ctx, ASTNode condition) {
		super(ctx);
		this.exp = condition;
	}
	
	@Override
	public EvalResult evaluate(Frame frame) throws TLangException {
		EvalResult conditionResult = exp.evaluate(frame);
		if (conditionResult.getValue() == TLangConstants.FALSE)
			return new EvalResult(TLangConstants.NULL,EvalResult.THROW);
		return new EvalResult(TLangConstants.NULL);
	}
	
	public TLangObject expression() throws TLangException {
		TLangObject obj = new TLangObject();
		return obj;
	}
	@Override
	public TLangObject pretty() throws TLangException {
		String s = "";
		return new TLangText(s);
	}
}
