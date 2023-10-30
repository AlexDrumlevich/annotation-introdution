package telran.configuration;

import java.lang.reflect.*;
import java.util.*;

import telran.configuration.annotation.Value;

public class Configuration {
 private Object obj;
 
 private Properties properties;

public Configuration(Object obj) {
	this.obj = obj;
	
}
public Configuration(Object testObj, Properties properties) throws Exception {
	obj = testObj;
	//just prototype of the HW #54 solution
	this.properties = properties;
		 
}
public void configInjection() {
	Field [] fields = obj.getClass().getDeclaredFields();
	Arrays.stream(fields).filter(f -> f.isAnnotationPresent(Value.class)).forEach(this::injection);
}

void injection(Field field) {
	Value valueAnnotation = field.getAnnotation(Value.class);
	String[] values = valueAnnotation.value().split(":");
	String propertyName = values[0];
	String defaultValue = values.length > 1 ? values[1] : null;
	
	String propertyValue = (String) properties.getOrDefault(propertyName, defaultValue);
	String convertionMethodName = getConvertionMethodName(field.getType().getSimpleName());
	try {
		Method method = this.getClass().getDeclaredMethod(convertionMethodName, String.class);
		Object convertedObject = method.invoke(this, propertyValue); //TODO updating for HW #55
		field.setAccessible(true);
		
		setValue(field, convertedObject);
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
}
private void setValue(Field field, Object convertedObject) throws IllegalAccessException {
	field.set(obj, convertedObject);
	//TODO HW #55
}
private String getConvertionMethodName(String type) {
	
	return type + "Convertion";
}
Integer intConvertion(String value) {
	return Integer.valueOf(value);
}
Long longConvertion(String value) {
	return Long.valueOf(value);
}
Float floatConvertion(String value) { 
	return Float.valueOf(value);
}
Double doubleConvertion(String value) {
	return Double.valueOf(value);
}
String StringConvertion(String value) {
	return value;
}
 
}
