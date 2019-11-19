package tlang.ast;

import org.antlr.v4.runtime.ParserRuleContext;

import tlang.objects.TLangException;
import tlang.objects.TLangObject;
import tlang.objects.TLangText;

public class ASTAssign extends ASTNode {

	private final ASTNode left;
	private final ASTNode right;
	public ASTAssign(ParserRuleContext ctx, ASTNode left, ASTNode right) {
		super(ctx);
		this.left = left;
		this.right = right;
	}
	
	@Override
	public EvalResult evaluate(Frame frame) throws TLangException {
		EvalResult leftValue = this.left.evaluate(frame);
		EvalResult rightValue = this.right.evaluate(frame);
//		frame.getObject().$set(leftValue.getValue(), rightValue.getValue());
		frame.set(leftValue.getValue(), rightValue.getValue());
		return rightValue;
	}
	
	@Override
	public TLangObject expression() throws TLangException {
		TLangObject obj = new TLangObject();
		
		obj.$set(new TLangText("type"), new TLangText("assign"));
		obj.$set(new TLangText("value"), left.expression());
		obj.$set(new TLangText("expression"), right.expression());
		
		return obj;
	}
	
	
	@Override
	public TLangObject pretty() throws TLangException {
		String s = "";
		return new TLangText(s);
	}

	

}
