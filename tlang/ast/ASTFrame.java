package tlang.ast;

import org.antlr.v4.runtime.ParserRuleContext;

import tlang.objects.TLangException;
import tlang.objects.TLangObject;
import tlang.objects.TLangText;

public class ASTFrame extends ASTNode {

	public ASTFrame(ParserRuleContext ctx) {
		super(ctx);
	}
	
	@Override
	public EvalResult evaluate(Frame frame) throws TLangException {
		EvalResult result = new EvalResult(frame);		
		return result;
	}
	
	@Override
	public TLangObject expression() throws TLangException {
		// TODO Auto-generated method stub
		return new TLangText("frame");
	}
	
	@Override
	public TLangObject pretty() throws TLangException {
		String s = "";
		return new TLangText(s);
	}

	
	
}
