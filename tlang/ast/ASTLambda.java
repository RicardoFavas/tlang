package tlang.ast;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.antlr.v4.runtime.ParserRuleContext;

import tlang.objects.TLangConstants;
import tlang.objects.TLangException;
import tlang.objects.TLangList;
import tlang.objects.TLangNumber;
import tlang.objects.TLangObject;
import tlang.objects.TLangText;
import tlang.utils.tuples.Pair;

public class ASTLambda extends ASTNode {

	final Pair<TLangText,ASTNode>[] parameters;
	final ASTNode ast;
	public ASTLambda(ParserRuleContext ctx, Pair<TLangText, ASTNode>[] parameters, ASTNode ast) {
		super(ctx);
		this.parameters = parameters;
		this.ast = ast;
	}

	@Override
	public EvalResult evaluate(Frame frame) throws TLangException {
		TLangObject lambda = new TLangObject(
			parameters,
			ast,
			frame
		);
		return new EvalResult(lambda);
	}
	
	
	@Override
	public TLangObject expression() throws TLangException {
		TLangObject obj = new TLangObject();
		obj.$set(new TLangText("type"), new TLangText("lambda"));

//		var params = new TLangList();
//		for (var p: this.params)
//			params.push(p);
//		obj.$set(new TLangText("parameters"), params);
//
//		obj.$set(new TLangText("lambda"), ast.expression());
		return obj;
	}
	@Override
	public TLangObject pretty() throws TLangException {
		String s = "";
		return new TLangText(s);
	}
}
