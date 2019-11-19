package tlang.ast;

import org.antlr.v4.runtime.ParserRuleContext;

import tlang.objects.TLangException;
import tlang.objects.TLangObject;
import tlang.objects.TLangText;
import tlang.utils.RUtils;

public class ASTObject extends ASTNode {

	private ASTNode block;
	public ASTObject(ParserRuleContext ctx, ASTNode block) {
		super(ctx);
		this.block = block;
	}

	@Override
	public EvalResult evaluate(Frame frame) throws TLangException {
		
		TLangObject topLevelObject = new TLangObject();
		
		Frame f = new Frame(frame,topLevelObject);
		EvalResult obj = block.evaluate(f);
		
		EvalResult result = new EvalResult(topLevelObject);
		return result;
	}

	@Override
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
