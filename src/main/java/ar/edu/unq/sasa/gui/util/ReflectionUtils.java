package ar.edu.unq.sasa.gui.util;

import java.lang.reflect.Method;

public class ReflectionUtils {

	public static Object invokeMethod(Object object, String actionName) {
		try {
			Method method = object.getClass().getMethod(actionName, new Class[]{});
			Object res = method.invoke(object, new Object[]{});
			return res;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Object invokeJavaBean(Object object, String method) {
		String name = "get" + Character.toUpperCase(method.charAt(0))
				+ method.substring(1);
		return invokeMethod(object, name);
	}
}
