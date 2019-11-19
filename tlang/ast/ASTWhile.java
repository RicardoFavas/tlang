package tlang.ast;

import org.antlr.v4.runtime.ParserRuleContext;

import tlang.objects.TLangBoolean;
import tlang.objects.TLangConstants;
import tlang.objects.TLangException;
import tlang.objects.TLangObject;
import tlang.objects.TLangText;

public class ASTWhile extends ASTNode {

	private ASTNode exp, block;
	public ASTWhile(ParserRuleContext ctx, ASTNode exp, ASTNode block) {
		super(ctx);
		this.exp = exp;
		this.block = block;
	}

	@Override
	public EvalResult evaluate(Frame frame) throws TLangException {

		EvalResult result = new EvalResult(TLangConstants.NULL);
		while (exp.evaluate(frame).getValue() == TLangConstants.TRUE) {
			Frame newFrame = new Frame(frame);
			
			result = block.evaluate(newFrame);
			
			int breakCode = result.getBreakCode();
			if (breakCode > 0) {
				if (breakCode == EvalResult.BREAK)
					break;
				if (breakCode == EvalResult.RETURN)
					break;
				if (breakCode == EvalResult.EXIT)
					break;
			}
		}
		return result;
	}

	@Override
	public TLangObject expression() throws TLangException {
		TLangObject obj = new TLangObject();
		
		obj.$set(new TLangText("type"), new TLangText("while"));
		obj.$set(new TLangText("condition"), exp.expression());
		obj.$set(new TLangText("block"), block.expression());
		
		return obj;
	}
	@Override
	public TLangObject pretty() throws TLangException {
		String s = "while "+exp.pretty()+"do"+block.pretty()+";";
		return new TLangText(s);
	}
	
}
