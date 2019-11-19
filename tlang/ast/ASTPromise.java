package tlang.ast;

import java.util.ArrayList;

import org.antlr.v4.runtime.ParserRuleContext;

import tlang.objects.TLangConstants;
import tlang.objects.TLangException;
import tlang.objects.TLangObject;
import tlang.objects.TLangPromise;
import tlang.objects.TLangText;

public class ASTPromise extends ASTNode {

	ASTNode ast;
	public ASTPromise(ParserRuleContext ctx, ASTNode ast) {
		super(ctx);
		this.ast = ast;
	}

	@Override
	public EvalResult evaluate(Frame frame) throws TLangException {
		TLangPromise promise = new TLangPromise(ast,frame);
		return new EvalResult(promise);
	}
	
	@Override
	public TLangObject expression() throws TLangException {
		TLangObject obj = new TLangObject();
		obj.$set(new TLangText("type"), new TLangText("async"));
		obj.$set(new TLangText("value"), ast.expression());
		return obj;
	}
	@Override
	public TLangObject pretty() throws TLangException {
		String s = "";
		return new TLangText(s);
	}
}
