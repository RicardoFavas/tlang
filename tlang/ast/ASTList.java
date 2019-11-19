package tlang.ast;

import java.math.BigDecimal;

import org.antlr.v4.runtime.ParserRuleContext;

import tlang.objects.TLangConstants;
import tlang.objects.TLangException;
import tlang.objects.TLangList;
import tlang.objects.TLangNumber;
import tlang.objects.TLangObject;
import tlang.objects.TLangText;

public class ASTList extends ASTNode {

	ASTNode[] nodes;
	public ASTList(ParserRuleContext ctx, ASTNode... nodes) {
		super(ctx);
		this.nodes = nodes;
	}

	@Override
	public EvalResult evaluate(Frame frame) throws TLangException {
		TLangList list = new TLangList();
		for (ASTNode exp : this.nodes) {
			EvalResult v = exp.evaluate(frame);
			list.push(v.getValue());
		}
		return new EvalResult(list);
	}
	
	@Override
	public TLangObject expression() throws TLangException {
		TLangObject obj = new TLangObject();
		obj.$set(new TLangText("type"), new TLangText("list"));
		TLangList list = new TLangList();
		for (ASTNode exp : this.nodes)
			list.push(exp.expression());
		obj.$set(new TLangText("expressions"), list);
		return obj;
	}
	@Override
	public TLangObject pretty() throws TLangException {
		String s = "[//todo]";
		return new TLangText(s);
	}
}
