package tlang.ast;

import java.util.Arrays;

import org.antlr.v4.runtime.ParserRuleContext;

import tlang.objects.TLangConstants;
import tlang.objects.TLangException;
import tlang.objects.TLangList;
import tlang.objects.TLangObject;
import tlang.objects.TLangText;

public class ASTBlock extends ASTNode {

	private final ASTNode[] nodes;
	
	public ASTBlock(ParserRuleContext ctx, ASTNode... nodes) {
		super(ctx);
		this.nodes = nodes;
	}
	
	public ASTNode[] getNodes() {
		return this.nodes;
	}
	
	@Override
	public EvalResult evaluate(Frame frame) throws TLangException {
		EvalResult ret = new EvalResult(TLangConstants.NULL);
		EvalResult r = ret;
		for (ASTNode node : nodes) {
			try {
				r = node.evaluate(frame);
				if (r.getBreakCode() > 0) {
					return r;
				}
			} catch (TLangException e) {
				return new EvalResult(e.getValue(),EvalResult.THROW);
			}
			ret = r;
			
		}
		return ret;
	}
	
	@Override
	public TLangObject expression() throws TLangException {
		TLangObject obj = new TLangObject();
		obj.$set(new TLangText("type"), new TLangText("block"));
		TLangList list = new TLangList();
		for (ASTNode exp : this.nodes)
			list.push(exp.expression());
		obj.$set(new TLangText("expressions"), list);
		return obj;
	}
	
	@Override
	public TLangObject pretty() throws TLangException {
		String s = "";
		return new TLangText(s);
	}

}
