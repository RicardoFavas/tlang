package tlang.ast;

import java.util.HashMap;

import org.antlr.v4.runtime.ParserRuleContext;

import tlang.objects.TLangConstants;
import tlang.objects.TLangException;
import tlang.objects.TLangObject;
import tlang.objects.TLangText;

public class ASTOperation extends ASTNode {

	ASTNode left, right;
	String operator;
	TLangText mappedOperand;
	HashMap<String,TLangText> mapping = new HashMap<>();
	{
		mapping.put("==", TLangConstants.$equals);
		mapping.put("!=", TLangConstants.$nequals);
		mapping.put("===", TLangConstants.$equals);
		mapping.put("!==", TLangConstants.$nequals);

		mapping.put("+", TLangConstants.$add);
		mapping.put("-", TLangConstants.$sub);
		mapping.put("*", TLangConstants.$multiply);
		mapping.put("/", TLangConstants.$divide);
		mapping.put("%", TLangConstants.$mod);
		mapping.put("^", TLangConstants.$pow);

		mapping.put("<", TLangConstants.$lesserThan);
		mapping.put(">", TLangConstants.$greaterThan);
		mapping.put(">=", TLangConstants.$lesserEqualsThan);
		mapping.put("<=", TLangConstants.$greaterEqualsThan);

		mapping.put("implements", TLangConstants.$implements);
		mapping.put("contains", TLangConstants.$contains);
		mapping.put("in", TLangConstants.$in);
		mapping.put("is", TLangConstants.$is);
	}
	
	public ASTOperation(ParserRuleContext ctx, ASTNode left, ASTNode right, String operand) {
		super(ctx);
		this.left = left;
		this.right = right;
		this.operator = operand;
		this.mappedOperand = mapping.get(operand);
	}
	
	@Override
	public EvalResult evaluate(Frame frame) throws TLangException {
		TLangObject leftValue = left.evaluate(frame).getValue();
		TLangObject rightValue = right.evaluate(frame).getValue();
		
		TLangObject operatorMethod = leftValue.$get(this.mappedOperand);
		EvalResult result = new EvalResult(operatorMethod.$call(rightValue));
		return result;
	}

	@Override
	public TLangObject expression() throws TLangException {
		TLangObject obj = new TLangObject();
		obj.$set(new TLangText("type"), new TLangText("operation"));
		obj.$set(new TLangText("left"), left.expression());
		obj.$set(new TLangText("right"), right.expression());
		obj.$set(new TLangText("operator"), new TLangText(operator));
		return obj;
	}

	@Override
	public TLangObject pretty() throws TLangException {
		// TODO Auto-generated method stub
		return null;
	}

}
