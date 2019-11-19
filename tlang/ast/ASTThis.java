package tlang.ast;

import org.antlr.v4.runtime.ParserRuleContext;

import tlang.objects.TLangException;
import tlang.objects.TLangObject;
import tlang.objects.TLangText;

public class ASTThis extends ASTNode {

	public ASTThis(ParserRuleContext ctx) {
		super(ctx);
	}
	
	@Override
	public EvalResult evaluate(Frame frame) throws TLangException {
		EvalResult result = new EvalResult(frame.getObject());
		return result;
	}

	@Override
	public TLangObject expression() throws TLangException {
		TLangObject obj = new TLangObject();
		obj.$set(new TLangText("type"), new TLangText("this"));
		return obj;
	}
	@Override
	public TLangObject pretty() throws TLangException {
		String s = "";
		return new TLangText(s);
	}
	
}
