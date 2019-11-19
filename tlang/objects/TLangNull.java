package tlang.objects;

public class TLangNull extends TLangObject {

	public static final TLangObject NULL = new TLangNull();
	private TLangNull() {
	}
	@Override
	public String toString() {
		return "null";
	}
}
