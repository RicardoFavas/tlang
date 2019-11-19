package tlang.objects;

public class TLangException extends Exception {
	private static final long serialVersionUID = 1L;

	final TLangObject value;
	public TLangException() {
		value = TLangConstants.NULL;
	}
	public TLangException(TLangObject value) {
		super(value.toString());
		this.value = value;
	}
	public TLangException(String message) {
		super(message);
		this.value = new TLangText(message);
	}
	public TLangException(Exception e) {
		super(e);
		this.value = new TLangText(e.getMessage());
	}
	public TLangException(Throwable e) {
		super(e);
		this.value = new TLangText(e.getMessage());
	}
	
	public TLangObject getValue() {
		return this.value;
	}
}
