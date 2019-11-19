package tlang.objects;

public class TLangBoolean extends TLangObject {

	
	
	public boolean yorn;
	protected TLangBoolean(boolean yorn) {
		super();
		this.yorn = yorn;
	}
	
	public TLangBoolean $negate() {
		return this == TLangConstants.TRUE ? TLangConstants.FALSE : TLangConstants.TRUE;
	}
	

	
	
	@Override
	public String toString() {
		return yorn ? "true" : "false";
	}

}
