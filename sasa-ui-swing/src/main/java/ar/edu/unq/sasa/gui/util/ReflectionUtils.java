package ar.edu.unq.sasa.gui.util;

import java.lang.reflect.Method;

public final class ReflectionUtils {

	private ReflectionUtils() { }

	public static Object invokeMethod(Object object, String actionName) {
		try {
			Method method = object.getClass().getMethod(actionName);
			return method.invoke(object);
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}

	public static Object invokeJavaBean(Object object, String method) {
		String name = "get" + Character.toUpperCase(method.charAt(0)) + method.substring(1);
		return invokeMethod(object, name);
	}

}
