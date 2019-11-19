package tlang.ast;

import org.antlr.v4.runtime.ParserRuleContext;

import tlang.objects.TLangConstants;
import tlang.objects.TLangException;
import tlang.objects.TLangObject;
import tlang.objects.TLangText;

public class ASTTryCatch extends ASTNode {

	ASTNode tryBlock, catchBlock, finallyBlock;
	TLangText exceptionName;
	public ASTTryCatch(ParserRuleContext ctx, ASTNode tryBlock, ASTNode catchBlock, TLangText exceptionName, ASTNode finallyBlock) {
		super(ctx);
		this.tryBlock = tryBlock;
		this.catchBlock = catchBlock;
		this.exceptionName = exceptionName;
		this.finallyBlock = finallyBlock;
	}	

	@Override
	public EvalResult evaluate(Frame frame) throws TLangException {
		Frame newFrame = new Frame(frame);
		EvalResult r = tryBlock.evaluate(newFrame);
		
		if (r.getBreakCode() == EvalResult.THROW) {
			if (this.catchBlock != null) {
				newFrame = new Frame(frame);
				if (this.exceptionName != null) {
					newFrame.setInThis(exceptionName, r.getValue());
				}
				r = this.catchBlock.evaluate(newFrame);
			}
		}
		if (r.getBreakCode() == EvalResult.RETURN) {
			return r;
		}
		
		if (this.finallyBlock != null) {
			newFrame = new Frame(frame);
			r = this.finallyBlock.evaluate(newFrame);
		}
		
		return new EvalResult(r.getValue());
	}
	
	@Override
	public TLangObject expression() throws TLangException {
		TLangObject obj = new TLangObject();
		obj.$set(new TLangText("type"), new TLangText("try"));
		obj.$set(new TLangText("try"), tryBlock.expression());
		if (this.catchBlock != null)
			obj.$set(new TLangText("catch"), catchBlock.expression());
		if (this.finallyBlock != null)
			obj.$set(new TLangText("finally"), finallyBlock.expression());
		return obj;
	}
	@Override
	public TLangObject pretty() throws TLangException {
		String s = "";
		return new TLangText(s);
	}
}
