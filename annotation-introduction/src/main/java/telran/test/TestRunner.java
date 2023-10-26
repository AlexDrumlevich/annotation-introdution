package telran.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import telran.test.annotation.BeforeEach;
import telran.test.annotation.Test;

public class TestRunner implements Runnable {
	private Object testObj;
	private List<Method> beforEachMethods = new ArrayList<>();  
	private List<Method> testMethods = new ArrayList<>();  
	
	public TestRunner(Object testObj) {
		super();
		this.testObj = testObj;
	}

	@Override
	public void run() {
		Class<?> clazz = testObj.getClass();
		Method[] methods = clazz.getDeclaredMethods();
		for(Method method: methods) {
			if(method.isAnnotationPresent(Test.class)) {
				testMethods.add(method);
			} else if(method.isAnnotationPresent(BeforeEach.class)) {
				beforEachMethods.add(method);
			}
		}
		
		for(Method method : beforEachMethods) {
			invoke(method);
		}
		for(Method method : testMethods) {
			invoke(method);
		}
	}
	
	private void invoke(Method method) {
		method.setAccessible(true);
		try {
			method.invoke(testObj);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			System.out.println("error: " + e.getMessage());
		}
	}
}
