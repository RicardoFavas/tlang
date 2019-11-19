package tlang.ast;

import org.antlr.v4.runtime.ParserRuleContext;

import tlang.objects.TLangConstants;
import tlang.objects.TLangException;
import tlang.objects.TLangObject;
import tlang.objects.TLangText;

public class ASTFor extends ASTNode {

	TLangText id;
	ASTNode iterable;
	ASTNode block;
	public ASTFor(ParserRuleContext ctx, TLangText id, ASTNode iterable, ASTNode block) {
		super(ctx);
		this.id = id;
		this.iterable = iterable;
		this.block = block;
	}

	@Override
	public EvalResult evaluate(Frame frame) throws TLangException {
		EvalResult result = new EvalResult(TLangConstants.NULL);
		
		TLangObject iterableObject = iterable.evaluate(frame).getValue();
		
		TLangObject iterator = iterableObject.$call(iterableObject,TLangConstants.$iterator);
		while ( iterator.$call(iterator,TLangConstants.$hasNext) == TLangConstants.TRUE ) {
			TLangObject next = iterator.$call(iterator,TLangConstants.$next);
			Frame newFrame = new Frame(frame);
			newFrame.setInThis(this.id, next);			
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TLangObject pretty() throws TLangException {
		// TODO Auto-generated method stub
		return null;
	}

}
