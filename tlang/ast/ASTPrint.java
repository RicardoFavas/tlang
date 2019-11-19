package tlang.ast;

import org.antlr.v4.runtime.ParserRuleContext;

import tlang.objects.TLangBoolean;
import tlang.objects.TLangConstants;
import tlang.objects.TLangException;
import tlang.objects.TLangObject;
import tlang.objects.TLangText;

public class ASTPrint extends ASTNode {

	ASTNode exp;
	public ASTPrint(ParserRuleContext ctx, ASTNode exp) {
		super(ctx);
		this.exp = exp;
	}
	
	@Override
	public EvalResult evaluate(Frame frame) throws TLangException {
		TLangObject value = this.exp.evaluate(frame).getValue();
		TLangObject method = value.$get(TLangConstants.$text);
		TLangObject text = method.$call();
		System.out.println(text);
		return new EvalResult(TLangConstants.TRUE);
	}

	@Override
	public TLangObject expression() throws TLangException {
		TLangObject obj = new TLangObject();
		obj.$set(new TLangText("type"), new TLangText("print"));
		obj.$set(new TLangText("value"), exp.expression());
		return obj;
	}
	@Override
	public TLangObject pretty() throws TLangException {
		String s = "";
		return new TLangText(s);
	}
}
