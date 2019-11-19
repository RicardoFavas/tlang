package tlang.ast;

import org.antlr.v4.runtime.ParserRuleContext;

import tlang.objects.TLangException;
import tlang.objects.TLangObject;
import tlang.objects.TLangText;

public class ASTThrow extends ASTNode {

	ASTNode throwValue;
	public ASTThrow(ParserRuleContext ctx, ASTNode throwValue) {
		super(ctx);
		this.throwValue = throwValue;
	}
	
	@Override
	public EvalResult evaluate(Frame frame) throws TLangException {
		EvalResult exception = this.throwValue.evaluate(frame);
		
		TLangObject value = exception.getValue();
		value.$set(new TLangText("stacktrace"), frame);
		
		
		EvalResult result = new EvalResult(exception.getValue(),EvalResult.THROW,this.getCtx());
		return result;
	}

	
	@Override
	public TLangObject expression() throws TLangException {
		TLangObject obj = new TLangObject();
		obj.$set(new TLangText("type"), new TLangText("throw"));
		obj.$set(new TLangText("expression"), throwValue.expression());
		return obj;
	}
	@Override
	public TLangObject pretty() throws TLangException {
		String s = "throw " + getCtx().getText();
		return new TLangText(s);
	}
}
