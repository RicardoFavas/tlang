package tlang.objects;

import java.math.BigDecimal;

public class TLangText extends TLangObject {
	final public String value;
	public TLangText(String text) {
		super();
		this.value = text;
	}
	
	public TLangObject $add(TLangObject otherObject) throws Exception {
		if (otherObject instanceof TLangText) {
			TLangText otherText = (TLangText)otherObject;
			return new TLangText(this.value+otherText.value);
		}
		throw new Exception();
	}
	
	@Override
	public TLangObject $contains(TLangObject key) {
		if (!(key instanceof TLangText))
			return TLangConstants.FALSE;
		TLangText str = (TLangText)key;
		if (this.value.contains(str.value))
			return TLangConstants.TRUE;
		return TLangConstants.FALSE;
	}
	
	@Override
	public TLangObject $equals(TLangObject otherObject) {		
		if (otherObject instanceof TLangText) {
			TLangText otherText = (TLangText)otherObject;
			if (this.value.equals(otherText.value))
				return TLangConstants.TRUE;
		}
		return TLangConstants.FALSE;	
	}

	public TLangBoolean startsWith(TLangObject otherObject) {
		if (otherObject instanceof TLangText) {
			TLangText otherText = (TLangText)otherObject;
			if (this.value.startsWith(otherText.value))
				return TLangConstants.TRUE;
		}
		return TLangConstants.FALSE;		
	}
	public TLangObject endsWith(TLangObject otherObject) {
		if (otherObject instanceof TLangText) {
			TLangText otherText = (TLangText)otherObject;
			if (this.value.endsWith(otherText.value))
				return TLangConstants.TRUE;
		}
		return TLangConstants.FALSE;		
	}
	public TLangObject indexOf(TLangObject otherObject) {
		if (otherObject instanceof TLangText) {
			TLangText otherText = (TLangText)otherObject;
			int index = this.value.indexOf(otherText.value);
			if (index != -1)
				return new TLangNumber(new BigDecimal(index));
		}
		return TLangConstants.NULL;		
	}
	@Override
	public String toString() {
		return value;
	}
	@Override
	public boolean equals(Object obj) {
		return $equals((TLangObject)obj) == TLangConstants.TRUE;
	}
	
	@Override
	public int hashCode() {
		return value.hashCode();
	}
}
