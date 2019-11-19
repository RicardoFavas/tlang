package tlang.objects;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import tlang.ast.EvalResult;
import tlang.ast.Frame;
import tlang.utils.RUtils;
import tlang.utils.TLangEngine;

public class TLangJavaObjectProxy extends TLangObject {

	Object context;
	Object obj;
	public TLangJavaObjectProxy(Object context, Object obj) {
		this.context = context;
		this.obj = obj;
	}
	
	public TLangObject $get(TLangObject key) throws TLangException {
		return $getFrominstance(obj,key);
	}
	
	public TLangObject $call(TLangObject ...values) throws TLangException {
		
		Object result = null;

		try {
			if (obj instanceof Method) {
				Method method = (Method)obj;
				result = invoke(context, method.getName(), values);
			}
		} catch( Exception e) {
			throw e;
		}

		if (result instanceof TLangObject)
			return (TLangObject)result;
		
		return java2TLangObject(result);
	}
	
	
	@Override
	public String toString() {
		return "["+obj.getClass()+"]";
	}
	

	public static Object invoke(Object caller, String method, Object ...values) throws TLangException {
		try {
			return invokeL0(caller,method,values);
		} catch (NoSuchMethodException e) {
		} catch (SecurityException e) {

		} catch (IllegalAccessException e) {

		} catch (IllegalArgumentException e) {

		} catch (InvocationTargetException e) {
			throw new TLangException(e.getTargetException());
		}

		try {
			return invokeL1(caller,method,values);
		} catch (NoSuchMethodException e) {
		} catch (SecurityException e) {
		} catch (IllegalAccessException e) {
		} catch (IllegalArgumentException e) {
		} catch (InvocationTargetException e) {
			throw new TLangException(e.getTargetException());
		}
		
		try {
			return invokeL2(caller,method,values);
		} catch (NoSuchMethodException e) {
			throw new TLangException(e);
		} catch (SecurityException e) {
			throw new TLangException(e);
		} catch (IllegalAccessException e) {
			throw new TLangException(e);
		} catch (IllegalArgumentException e) {
			throw new TLangException(e);
		} catch (InvocationTargetException e) {
			throw new TLangException(e.getTargetException());
		}
	}
	//forma optimista
	private static Object invokeL0(Object caller, String method, Object ...values) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		Object result = null;       
        Class<?>[] types = new Class<?>[values.length];
        for (int i = 0; i < values.length; i++) {
        	Object value = values[i];
        	if (value != null)
        		types[i] = value.getClass();
        }
              
        Method javaMethod = caller.getClass().getMethod(method,types);
    	result = javaMethod.invoke(caller,values);
        return result;
	}
	//tudo é TLangObject
	private static Object invokeL1(Object caller, String method, Object ...values) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        
        Class<?>[] types = new Class<?>[values.length];
        for (int i = 0; i < values.length; i++) {
        	Object value = values[i];
        	if (value != null)
        		types[i] = TLangObject.class;
        }
        
        
    	Method javaMethod = caller.getClass().getMethod(method,types);
		return javaMethod.invoke(caller,values);
	}
	//UIIIIIIIIIII agora é que é
	private static Object invokeL2(Object caller, String method, Object ...values) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //precorre todos o metodos
        for (Method javaMethod : caller.getClass().getMethods()) {
        	//o method tem de ter o mesmo nome
        	if (!javaMethod.getName().equals(method))
        		continue;
        	
        	//o method tem de ter o mesmo numero de params
        	Class<?>[] paramTypes = javaMethod.getParameterTypes();
        	if (paramTypes.length != values.length)
        		continue;
        	
        	//precorre todos os params
        	Object[] convertedValues = new Object[paramTypes.length];
        	for (int i = 0; i < values.length; i++) {
        		Class<?> paramType = paramTypes[i];
        		Object value = values[i];
        		if (value instanceof TLangObject) {
        			TLangObject tlvalue = (TLangObject)value; 
        			try {
        				convertedValues[i] = TLangEngine.langObject2Java(tlvalue,paramType);
					} catch (TLangException e) {
						break;
					}
        		}
        		return javaMethod.invoke(caller,convertedValues);
        	}
        }
        
        throw new NoSuchMethodException(caller+"."+method+"("+RUtils.join(values,", ")+")");
	}
	
	
	
}


