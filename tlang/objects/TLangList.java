package tlang.objects;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

import tlang.objects.TLangObject.TLangObjectIterator;

public class TLangList extends TLangObject {

	ArrayList<TLangObject> array = new ArrayList<>();
	
	public TLangList() {
	}
	
	public TLangObject push(TLangObject value) throws TLangException {
		return array.add(value) ? TLangConstants.TRUE : TLangConstants.FALSE;
	}
	
	public TLangObject pop() throws TLangException {
		return array.remove(array.size()-1);	
	}
	
	@Override
	public TLangObject $set(TLangObject key, TLangObject value) throws TLangException {
		if (key instanceof TLangNumber) {
			TLangNumber keyno = (TLangNumber)key;
			if (keyno.isInteger() == TLangConstants.TRUE) {
				array.ensureCapacity(keyno.value.intValue());
				array.add(keyno.value.intValue(), value);
			}
		}
		return super.$set(key, value);
	}
	
	public TLangNumber size() {
		return new TLangNumber(array.size());
	}
	
	@Override
	public String toString() {
		return array.toString();
	}
	
	public class TLangObjectIterator extends TLangObject {
		Iterator<TLangObject> itr;
		public TLangObjectIterator(TLangList obj) throws TLangException {
			this.itr = obj.array.iterator();
		}
		public TLangBoolean $hasNext() throws TLangException {
			return itr.hasNext() ? TLangConstants.TRUE : TLangConstants.FALSE;
		}
		int index = 0;
		public TLangObject $next() throws TLangException {
			TLangObject n = itr.next();
			TLangObject obj = new TLangObject();
			obj.$set(TLangConstants.key, new TLangNumber(index));
			obj.$set(TLangConstants.value, n);
			obj.$set(TLangConstants.index, new TLangNumber(index));
			index++;
			return obj;
		}
	}
	public TLangObject $iterator() throws TLangException {
		return new TLangObjectIterator(this);
	}
}
