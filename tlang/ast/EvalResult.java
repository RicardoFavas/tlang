package tlang.ast;

import org.antlr.v4.runtime.ParserRuleContext;

import tlang.objects.TLangObject;

public class EvalResult {
	
	public static final int RETURN = 1;
	public static final int NEXT = 2;
	public static final int BREAK = 3;
	public static final int EXIT = 4;
	public static final int THROW = 5;
	public static final int NO_BREAK = 0;
	
	private final int breakCode;
	
	final private TLangObject value;
	final private ParserRuleContext ctx;
	
	public EvalResult(TLangObject value, int breakCode, ParserRuleContext ctx) {
		this.value = value;
		this.breakCode = breakCode;
		this.ctx = ctx;
	}
	public EvalResult(TLangObject value, int breakCode) {
		this.value = value;
		this.breakCode = breakCode;
		this.ctx = null;
	}
	public EvalResult(TLangObject value) {
		this(value,NO_BREAK,null);
	}
	
	public TLangObject getValue() {
		return this.value;
	}
	public int getBreakCode() {
		return this.breakCode;
	}
	public ParserRuleContext getCtx() {
		return this.ctx;
	}
	
	@Override
	public String toString() {
		String s = "{";
		s += "value = "+getValue();
		s += ", code = "+getBreakCode();
		s += "}";
		return s;
	}
}
