package tlang.ast;

import org.antlr.v4.runtime.ParserRuleContext;

import tlang.objects.TLangException;
import tlang.objects.TLangObject;

public abstract class ASTNode {
	
	private final ParserRuleContext ctx;
	public ASTNode(ParserRuleContext ctx) {
		this.ctx = ctx;
	}
	public ParserRuleContext getCtx() {
		return this.ctx;
	}
	public abstract EvalResult evaluate(Frame frame) throws TLangException;
	public abstract TLangObject expression() throws TLangException;
	public abstract TLangObject pretty() throws TLangException;
}
