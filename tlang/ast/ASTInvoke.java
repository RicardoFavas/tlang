package tlang.ast;

import java.math.BigDecimal;
import java.util.Arrays;

import org.antlr.v4.runtime.ParserRuleContext;

import tlang.objects.TLangConstants;
import tlang.objects.TLangException;
import tlang.objects.TLangList;
import tlang.objects.TLangNumber;
import tlang.objects.TLangObject;
import tlang.objects.TLangText;
import tlang.utils.RUtils;

public class ASTInvoke extends ASTNode {

	private ASTNode node;
	private ASTNode[] params;
	public ASTInvoke(ParserRuleContext ctx, ASTNode node, ASTNode... params) {
		super(ctx);
		this.node = node;
		this.params = params;
	}
	
	@Override
	public EvalResult evaluate(Frame frame) throws TLangException {	
		TLangObject objValue = node.evaluate(frame).getValue();
		TLangObject[] paramValues = new TLangObject[params.length];
		for (int i = 0; i < params.length; i++) {
			paramValues[i] = params[i].evaluate(frame).getValue();
		}
		TLangObject value = TLangConstants.NULL;
		try { 
			value = objValue.$call(paramValues);
			return new EvalResult(value);
		} catch (TLangException e) {
//			System.out.println(objValue+"."+method+"("+RUtils.join(paramValues,", ")+")"+e.getValue());
			throw new TLangException(e.getValue());
//			return new EvalResult(e.getValue(),EvalResult.THROW);
		}
	}
	
	@Override
	public TLangObject expression() throws TLangException {
		TLangObject obj = new TLangObject();
		
//		obj.$set(new TLangText("type"), new TLangText("invoke"));
//		obj.$set(new TLangText("method"), method);
//		var params = new TLangList();
//		for (var p: this.params)
//			params.push(p.expression());
//		obj.$set(new TLangText("parameters"), params);
		
		return obj;
	}
	@Override
	public TLangObject pretty() throws TLangException {
		return new TLangText("ASTInvoke");
	}

}
