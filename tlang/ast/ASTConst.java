package tlang.ast;

import org.antlr.v4.runtime.ParserRuleContext;

import tlang.objects.TLangException;
import tlang.objects.TLangObject;
import tlang.objects.TLangText;

public class ASTConst extends ASTNode {

	TLangText id;
	ASTNode value;
	public ASTConst(ParserRuleContext ctx, TLangText id, ASTNode value) {
		super(ctx);
		this.id = id;
		this.value = value;
	}
	
	@Override
	public EvalResult evaluate(Frame frame) throws TLangException {
		EvalResult result = this.value.evaluate(frame);
		frame.setInThis(this.id, result.getValue());
		return result;
	}
	
	@Override
	public TLangObject expression() throws TLangException {
		TLangObject obj = new TLangObject();
		obj.$set(new TLangText("type"), new TLangText("const"));
		obj.$set(new TLangText("id"), id);
		obj.$set(new TLangText("value"), value.expression());
		return obj;
	}
	@Override
	public TLangObject pretty() throws TLangException {
		String s = "";
		return new TLangText(s);
	}
}
