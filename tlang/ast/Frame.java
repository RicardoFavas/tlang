package tlang.ast;
import java.util.HashMap;

import tlang.objects.TLangBoolean;
import tlang.objects.TLangConstants;
import tlang.objects.TLangException;
import tlang.objects.TLangObject;
import tlang.objects.TLangText;

public class Frame extends TLangObject {
	
	private final TLangObject object;
	public Frame parent;
	
	public Frame(TLangObject object) {
		super();
		this.object = object;
		this.parent = null;
	}
	public Frame(Frame parent) {
		super();
		this.parent = parent;
		this.object = parent.object;
	}
	public Frame(Frame parent, TLangObject object) {
		super();
		this.parent = parent;
		this.object = object;
	}
	
	

	
	@Override
	public TLangObject $contains(TLangObject key) {
		if (this.attributes.containsKey(key))
			return TLangConstants.TRUE;
		return TLangConstants.FALSE;
	}
	
	public void initName(TLangObject key, TLangObject value) throws Exception {
		TLangObject name = attributes.get(key);
		if (name == null)
			attributes.put(key, value);
	}
	public TLangObject setInThis(TLangObject key, TLangObject value) {
		TLangObject ret = this.attributes.put(key, value);
		return ret == null ? TLangConstants.NULL : ret;
	}
	
	
	public TLangObject getObject() {
		return object;
	}
	
	public TLangObject set(TLangObject key, TLangObject value) throws TLangException {
		Frame t = this;
		do {
			if ( t.attributes.containsKey(key) )
				return t.attributes.put(key, value);
			t = t.parent;
		} while (t != null);
		return this.object.$set(key,value);
	}
	public TLangObject get(TLangObject key) throws TLangException {
		Frame t = this;
		do {
			TLangObject value = t.attributes.get(key);
			if (value != null)
				return value;
			t = t.parent;
		} while (t != null);
		return this.object.$get(key);
	}
	
	
	@Override
	public String toString() {
		HashMap<String,Object> str = new HashMap<>();

		str.put("object", object);
		str.put("vars", attributes);
		
		return str.toString();
	}
	

}

