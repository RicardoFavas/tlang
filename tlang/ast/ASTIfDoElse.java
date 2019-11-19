package tlang.ast;

import org.antlr.v4.runtime.ParserRuleContext;

import tlang.objects.TLangBoolean;
import tlang.objects.TLangConstants;
import tlang.objects.TLangException;
import tlang.objects.TLangObject;
import tlang.objects.TLangText;

public class ASTIfDoElse extends ASTNode {

	ASTNode exp, doBlock, elseBlock;
	public ASTIfDoElse(ParserRuleContext ctx, ASTNode condition, ASTNode doBlock, ASTNode elseBlock) {
		super(ctx);
		this.exp = condition;
		this.doBlock = doBlock;
		this.elseBlock = elseBlock;
	}
	
	@Override
	public EvalResult evaluate(Frame frame) throws TLangException {
		EvalResult conditionResult = exp.evaluate(frame);
		if (conditionResult.getValue() == TLangConstants.TRUE) {
			Frame newFrame = new Frame(frame);
			return doBlock.evaluate(newFrame);
		} else
		if (elseBlock != null) {
			Frame newFrame = new Frame(frame);
			return elseBlock.evaluate(newFrame);
		}
		return new EvalResult(TLangConstants.NULL);
	}
	
	public TLangObject expression() throws TLangException {
		TLangObject obj = new TLangObject();
		
		obj.$set(new TLangText("type"), new TLangText("if"));
		obj.$set(new TLangText("condition"), exp.expression());
		if (doBlock != null)
			obj.$set(new TLangText("do"), doBlock.expression());
		if (elseBlock != null)
			obj.$set(new TLangText("else"), elseBlock.expression());
		
		return obj;
	}
	@Override
	public TLangObject pretty() throws TLangException {
		String s = "";
		return new TLangText(s);
	}
}
