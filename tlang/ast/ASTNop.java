package tlang.ast;

import org.antlr.v4.runtime.ParserRuleContext;

import tlang.objects.TLangConstants;
import tlang.objects.TLangException;
import tlang.objects.TLangObject;
import tlang.objects.TLangText;

public class ASTNop  extends ASTNode {

	public ASTNop(ParserRuleContext ctx) {
		super(ctx);
	}

	@Override
	public EvalResult evaluate(Frame frame) throws TLangException {
		return new EvalResult(TLangConstants.NULL);
	}
	
	@Override
	public TLangObject expression() throws TLangException {
		TLangObject obj = new TLangObject();
		obj.$set(new TLangText("type"), new TLangText("nop"));
		return obj;
	}
	@Override
	public TLangObject pretty() throws TLangException {
		String s = "nop";
		return new TLangText(s);
	}
}
