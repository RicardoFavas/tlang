package tlang.objects;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import tlang.ast.ASTNode;
import tlang.ast.EvalResult;
import tlang.ast.Frame;
import tlang.utils.RUtils;
import tlang.utils.tuples.Pair;

public class TLangObject  {

	public HashMap<TLangObject,TLangObject> attributes = new HashMap<>();
	
	
	//lambda
	public Pair<TLangText,ASTNode>[] parameters;
	protected Frame frame;
	protected ASTNode lambda;
	
	public TLangObject() {
	}
	
	public TLangObject(Pair<TLangText,ASTNode>[] parameters, ASTNode lambda, Frame frame) {
		this.parameters = parameters;
		this.lambda = lambda;
		this.frame = frame;
	}


	
	public TLangObject $resolve() throws TLangException {
		return this;
	}
	
	public TLangObject $contains(TLangObject key) {
		return this.attributes.containsKey(key) ? TLangConstants.TRUE : TLangConstants.FALSE;
	}
	
	public TLangObject $in(TLangObject obj) {
		return obj.$contains(this);
	}	
	
	public TLangObject $set(TLangObject key, TLangObject value) throws TLangException {
		return this.attributes.put(key, value);
	}
	public TLangObject $get(TLangObject key) throws TLangException {
//		System.out.println(this+"."+key+ " result = "+$getFrominstance(this,key));
		return $getFrominstance(this,key);
	}
	public static TLangObject $getFrominstance(Object instance, TLangObject key) throws TLangException {
		if (instance instanceof TLangObject) {
			TLangObject tlo = (TLangObject)instance;
			TLangObject attr = tlo.attributes.getOrDefault(key, null);
			if (attr != null)
				return attr;
		}

		//vai buscsar method da classe
		String keyName = key.toString();
		
        for (Method javaMethod : instance.getClass().getMethods()) {
        	if (  javaMethod.getModifiers() == Modifier.PUBLIC && javaMethod.getName().equals(keyName) ){
//        		TLangJavaMethod encapsulatedJavaMethod = new TLangJavaMethod(instance,keyName);
        		TLangJavaObjectProxy encapsulatedJavaMethod = new TLangJavaObjectProxy(instance,javaMethod);
        		return encapsulatedJavaMethod;
        	}
        }

        for (Field field : instance.getClass().getFields()) {
        	if (field.getModifiers() == Modifier.PUBLIC && field.getName().equals(keyName) ) {
        		Object value = null;
        		try {
					value = field.get(instance);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
        		return java2TLangObject(value);
        	}
        }
        
		return TLangConstants.NULL;
	}
	public TLangObject $call(TLangObject ...values) throws TLangException {
		if (this.lambda == null)
			return TLangConstants.NULL;
		
		Frame newFrame = new Frame(this.frame);
		if (parameters != null)
			for (int i = 0; i < this.parameters.length; i++) {
				TLangText name = this.parameters[i].getFirst();
				TLangObject value = this.parameters[i].getSecond().evaluate(this.frame).getValue();
				if (values.length > i)
					value = values[i];
				newFrame.setInThis(name, value);
			}
		
		EvalResult result = lambda.evaluate(newFrame);
		
		if (result.getBreakCode() == EvalResult.THROW) 
			throw new TLangException(result.getValue());

		return result.getValue();
	}
	
	public TLangObject $remove(TLangObject key) throws TLangException {
		return this.attributes.remove(key);
	}
	
	
	public TLangObject $slice(TLangObject start, TLangObject stop, TLangObject step) throws TLangException {
		
		TLangNumber _start = TLangConstants.ZERO;
		if (start != TLangConstants.NULL && start instanceof TLangNumber)
			_start = (TLangNumber)start;
		
		TLangNumber _stop = TLangConstants.MAX;
		if (_stop != TLangConstants.NULL && _stop instanceof TLangNumber)
			_stop = (TLangNumber)stop;
		
		TLangNumber _step = TLangConstants.ONE;
		if (_step != TLangConstants.NULL && _step instanceof TLangNumber)
			_step = (TLangNumber)step;
		
		TLangObject slice = new TLangObject();
		TLangNumber i = _start;
		while( i.$lesserThan(stop) == TLangConstants.TRUE ) {
			
			TLangObject value = this.$get(i);
			slice.$set(i, value);
			
			i = (TLangNumber)i.$add(_step);
		}
		
		return slice;
	}
	
//	public TLangObject filter(TLangObject lambda) throws TLangException {
//		var obj = new TLangObject();
//		TLangObjectIterator itr = (TLangObjectIterator)this.$iterator();
//		while (itr.$hasNext() == TLangConstants.TRUE) {
//			var v = itr.$next();
//			var key = v.$get(TLangConstants.key);
//			var value = v.$get(TLangConstants.value);
//			var result = TLangObject.$call(lambda, TLangConstants.$call, v);
//			if (result == TLangConstants.TRUE)
//				obj.$set(key,value);
//		}
//		return obj;
//	}
//	public TLangObject forEach(TLangObject lambda) throws TLangException {
//		var obj = new TLangObject();
//		TLangObjectIterator itr = (TLangObjectIterator)this.$iterator();
//		while (itr.$hasNext() == TLangConstants.TRUE) {
//			var v = itr.$next();
//			TLangObject.$call(lambda, TLangConstants.$call, v);
//		}
//		return obj;
//	}
//	public TLangObject map(TLangObject lambda) throws TLangException {
//		var obj = new TLangObject();
//		TLangObjectIterator itr = (TLangObjectIterator)this.$iterator();
//		while (itr.$hasNext() == TLangConstants.TRUE) {
//			var v = itr.$next();
////			var key = v.$get(TLangConstants.key);
////			var value = v.$get(TLangConstants.value);
//			
//			var result = TLangObject.$call(lambda, TLangConstants.$call, v);
//			
//			var key = result.$get(TLangConstants.key);
//			var value = result.$get(TLangConstants.value);
//			obj.$set(key,value);
//		}
//		return obj;
//	}
	
	public TLangText $text() throws TLangException {		
		return new TLangText(this.toString());
	}
	@Override
	public String toString() {
		String s = "";
		if (lambda != null)
			s += "i";
		s += attributes;
		return s;
	}
	
	public TLangObject $hash() {
		return new TLangNumber(new BigDecimal(this.hashCode()));
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	

	public TLangObject $implements(TLangObject obj) {
		if (this.attributes.size() < obj.attributes.size())
			return TLangConstants.FALSE;
		
		for (Entry<TLangObject, TLangObject> kv : obj.attributes.entrySet()) {
			TLangObject myValue = this.attributes.get(kv.getKey());
			if (myValue == null)
				return TLangConstants.FALSE;
			if (myValue.$equals(kv.getValue()) != TLangConstants.TRUE)
				return TLangConstants.FALSE;			
		}
		
		return TLangConstants.TRUE;
	}
	public TLangObject $is(TLangObject obj) {
		if (this.attributes.size() != obj.attributes.size())
			return TLangConstants.FALSE;
		for (Entry<TLangObject, TLangObject> kv : obj.attributes.entrySet()) {
			TLangObject myValue = this.attributes.get(kv.getKey());
			if (myValue == null)
				return TLangConstants.FALSE;
			if (myValue.$equals(kv.getValue()) != TLangConstants.TRUE)
				return TLangConstants.FALSE;			
		}
		return TLangConstants.TRUE;
	}
	public TLangObject $equals(TLangObject otherObject) {
		return this.equals(otherObject) ? TLangConstants.TRUE : TLangConstants.FALSE;
	}
	public TLangObject $nequals(TLangObject otherObject) {
		return this.$equals(otherObject) == TLangConstants.TRUE ? TLangConstants.FALSE : TLangConstants.TRUE;
	}
	public TLangObject $lequals(TLangObject otherObject) {
		return this == otherObject ? TLangConstants.TRUE : TLangConstants.FALSE;
	}
	
	public TLangList getKeys() {
		TLangList keyList = new TLangList();
		keyList.array.addAll(attributes.keySet());
		return keyList;
	}
	public TLangList getValues() {
		TLangList keyList = new TLangList();
		keyList.array.addAll(attributes.values());
		return keyList;
	}
	
	public class TLangObjectIterator extends TLangObject {
		Iterator<Entry<TLangObject, TLangObject>> itr;
		public TLangObjectIterator(TLangObject obj) throws TLangException {
			this.itr = obj.attributes.entrySet().iterator();
		}
		public TLangBoolean $hasNext() throws TLangException {
			return itr.hasNext() ? TLangConstants.TRUE : TLangConstants.FALSE;
		}
		int index = 0;
		public TLangObject $next() throws TLangException {
			Entry<TLangObject, TLangObject> n = itr.next();
			TLangObject obj = new TLangObject();
			obj.$set(TLangConstants.key, n.getKey());
			obj.$set(TLangConstants.value, n.getValue());
			obj.$set(TLangConstants.index, new TLangNumber(index));
			index++;
			return obj;
		}
	}
	public TLangObject $iterator() throws TLangException {
		return new TLangObjectIterator(this);
	}

	@Override
	public boolean equals(Object otherObject) {
		return this == otherObject;
	}
	
	
	
	
	
	
	
	protected static TLangObject java2TLangObject(Object obj) throws TLangException {
		if (obj instanceof TLangObject)
			return (TLangObject)obj;
		
		//Null
		if (obj == null)
			return TLangConstants.NULL;

		//TLangBoolean
		if (obj instanceof Boolean){
			Boolean casted = (Boolean)obj;
			return casted ? TLangConstants.TRUE : TLangConstants.FALSE;
		}
		
		//TLangText
		if (obj instanceof CharSequence){
			CharSequence casted = (CharSequence)obj;
			return new TLangText(casted.toString());
		}
		if (obj instanceof String) {
			String casted = (String)obj;
			return new TLangText(casted);
		}
		
		//TLangNumber
		if (obj instanceof Byte) {
			Byte casted = (Byte)obj;
			return new TLangNumber(casted);
		}
		if (obj instanceof Short) {
			Short casted = (Short)obj;
			return new TLangNumber(casted);
		}
		if (obj instanceof Integer) {
			Integer casted = (Integer)obj;
			return new TLangNumber(casted);
		}
		if (obj instanceof Float) {
			Float casted = (Float)obj;
			return new TLangNumber(casted);
		}
		if (obj instanceof Double) {
			Double casted = (Double)obj;
			return new TLangNumber(casted);
		}
		if (obj instanceof Long) {
			Long casted = (Long)obj;
			return new TLangNumber(casted);
		}
		if (obj instanceof BigDecimal) {
			BigDecimal casted = (BigDecimal)obj;
			return new TLangNumber(casted);
		}
		if (obj instanceof Number) {
			Number casted = (Number)obj;
			return new TLangNumber(new BigDecimal(casted.toString()));
		}
		
		//TLangList
		if (obj instanceof Object[]) {
			Object[] casted = (Object[])obj;
			TLangList list = new TLangList();
			for (Object e : casted)
				list.push(java2TLangObject(e));
			return list;
		}
		if (obj instanceof Collection) {
			Collection<?> casted = (Collection<?>)obj;
			TLangList list = new TLangList();
			for (Object e : casted)
				list.push(java2TLangObject(e));
			return list;
		}
		
		//TLangObject
		if (obj instanceof HashMap) {			
			HashMap<?,?> casted = (HashMap<?,?>)obj;
			TLangObject tobj = new TLangObject();
			for (Entry<?, ?> e : casted.entrySet())
				tobj.$set( 
						java2TLangObject(e.getKey()), 
						java2TLangObject(e.getValue()) 
				);
			return tobj;
		}
		
		throw new TLangException();
	}
}
