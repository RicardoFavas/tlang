package tlang.ast;

import java.util.HashMap;

import org.antlr.v4.runtime.ParserRuleContext;

import tlang.objects.TLangConstants;
import tlang.objects.TLangException;
import tlang.objects.TLangObject;
import tlang.objects.TLangText;

public class ASTGet extends ASTNode {

	ASTNode left, right;
	
	public ASTGet(ParserRuleContext ctx, ASTNode left, ASTNode right) {
		super(ctx);
		this.left = left;
		this.right = right;
	}
	
	@Override
	public EvalResult evaluate(Frame frame) throws TLangException {
		TLangObject leftValue = left.evaluate(frame).getValue();
		TLangObject rightValue = right.evaluate(frame).getValue();
		TLangObject operatorMethod = leftValue.$get(TLangConstants.$get);
		EvalResult result = new EvalResult(operatorMethod.$call(rightValue));
		return result;
	}

	@Override
	public TLangObject expression() throws TLangException {
		TLangObject obj = new TLangObject();
		obj.$set(new TLangText("type"), new TLangText("operation"));
		obj.$set(new TLangText("left"), left.expression());
		obj.$set(new TLangText("right"), right.expression());
		obj.$set(new TLangText("operator"), TLangConstants.$get);
		return obj;
	}

	@Override
	public TLangObject pretty() throws TLangException {
		// TODO Auto-generated method stub
		return null;
	}

}
