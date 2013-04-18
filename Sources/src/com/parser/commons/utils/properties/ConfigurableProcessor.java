package com.parser.commons.utils.properties;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Properties;

import com.parser.commons.utils.Logger;

/**
 * This class is designed to process classes and interfaces that have fields marked with {@link Property} annotation
 * 
 * @author SoulKeeper
 */
public class ConfigurableProcessor {

	static final Logger log = new Logger().getInstance();

	/**
	 * This method is an entry point to the parser logic.<br>
	 * Any object or class that have {@link Property} annotation in it or it's parent class/interface can be submitted
	 * here.<br>
	 * If object(new Something()) is submitted, object fields are parsed. (non-static)<br>
	 * If class is submitted(Sotmething.class), static fields are parsed.<br>
	 * <p/>
	 * 
	 * @param object
	 *          Class or Object that has {@link Property} annotations.
	 * @param properties
	 *          Properties that should be used while seraching for a {@link Property#key()}
	 */
	public static void process(Object object, Properties... properties) {
		Class<?> clazz;

		if (object instanceof Class) {
			clazz = (Class<?>) object;
			object = null;
		}
		else {
			clazz = object.getClass();
		}

		process(clazz, object, properties);
	}

	/**
	 * This method uses recurcieve calls to launch search for {@link Property} annotation on itself and
	 * parents\interfaces.
	 * 
	 * @param clazz
	 *          Class of object
	 * @param obj
	 *          Object if any, null if parsing class (static fields only)
	 * @param props
	 *          Properties with keys\values
	 */
	private static void process(Class<?> clazz, Object obj, Properties[] props) {
		processFields(clazz, obj, props);

		// Interfaces can't have any object fields, only static
		// So there is no need to parse interfaces for instances of objects
		// Only classes (static fields) can be located in interfaces
		if (obj == null) {
			for (Class<?> itf : clazz.getInterfaces()) {
				process(itf, obj, props);
			}
		}

		Class<?> superClass = clazz.getSuperclass();
		if (superClass != null && superClass != Object.class) {
			process(superClass, obj, props);
		}
	}

	/**
	 * This method runs throught the declared fields watching for the {@link Property} annotation. It also watches for the
	 * field modifiers like {@link java.lang.reflect.Modifier#STATIC} and {@link java.lang.reflect.Modifier#FINAL}
	 * 
	 * @param clazz
	 *          Class of object
	 * @param obj
	 *          Object if any, null if parsing class (static fields only)
	 * @param props
	 *          Properties with keys\values
	 */
	private static void processFields(Class<?> clazz, Object obj, Properties[] props) {
		for (Field f : clazz.getDeclaredFields()) {
			// Static fields should not be modified when processing object
			if (Modifier.isStatic(f.getModifiers()) && obj != null) {
				continue;
			}

			// Not static field should not be processed when parsing class
			if (!Modifier.isStatic(f.getModifiers()) && obj == null) {
				continue;
			}

			if (f.isAnnotationPresent(Property.class)) {
				// Final fields should not be processed
				if (Modifier.isFinal(f.getModifiers())) {
					log.error("Attempt to proceed final field " + f.getName() + " of class " + clazz.getName());
					throw new RuntimeException();
				}
				processField(f, obj, props);
			}
		}
	}

	/**
	 * This method takes {@link Property} annotation and does sets value according to annotation property. For this reason
	 * {@link #getFieldValue(java.lang.reflect.Field, java.util.Properties[])} can be called, however if method sees that
	 * there is no need - field can remain with it's initial value.
	 * <p/>
	 * Also this method is capturing and logging all {@link Exception} that are thrown by underlying methods.
	 * 
	 * @param f
	 *          field that is going to be processed
	 * @param obj
	 *          Object if any, null if parsing class (static fields only)
	 * @param props
	 *          Properties with kyes\values
	 */
	private static void processField(Field f, Object obj, Properties[] props) {
		boolean oldAccessible = f.isAccessible();
		f.setAccessible(true);
		try {
			Property property = f.getAnnotation(Property.class);
			if (!Property.DEFAULT_VALUE.equals(property.defaultValue()) || isKeyPresent(property.key(), props)) {
				f.set(obj, getFieldValue(f, props));
			}
			else
				log.warn("Field " + f.getName() + " of class " + f.getDeclaringClass().getName() + " wasn't modified !");
		}
		catch (Exception e) {
			log.error("Can't transform field " + f.getName() + " of class " + f.getDeclaringClass());
			throw new RuntimeException();
		}
		f.setAccessible(oldAccessible);
	}

	/**
	 * This method is responsible for receiving field value.<br>
	 * It tries to load property by key, if not found - it uses default value.<br>
	 * Transformation is done using {@link com.parser.commons.utils.properties.PropertyTransformerFactory}
	 * 
	 * @return transformed object that will be used as field value
	 * @throws TransformationException
	 *           if something goes wrong during transformation
	 */
	private static Object getFieldValue(Field field, Properties[] props) throws TransformationException {
		Property property = field.getAnnotation(Property.class);
		String defaultValue = property.defaultValue();
		String key = property.key();
		String value = null;

		if (key.isEmpty()) {
			log.warn("Property " + field.getName() + " of class " + field.getDeclaringClass().getName() + " has empty key");
		}
		else {
			value = findPropertyByKey(key, props);
		}

		if (value == null || value.trim().equals("")) {
			value = defaultValue;
			log.warn("Using default value for field " + field.getName() + " of class " + field.getDeclaringClass().getName());
		}

		PropertyTransformer<?> pt = PropertyTransformerFactory.newTransformer(field.getType(), property.propertyTransformer());
		
		return pt.transform(value, field);
	}

	/**
	 * Finds value by key in properties
	 * 
	 * @return value if found, null otherwise
	 */
	private static String findPropertyByKey(String key, Properties[] props) {
		for (Properties p : props)
			if (p.containsKey(key))
				return p.getProperty(key);
		return null;
	}

	/**
	 * Checks if key is present in the given properties
	 * 
	 * @return true if key present, false in other case
	 */
	private static boolean isKeyPresent(String key, Properties[] props) {
		return findPropertyByKey(key, props) != null;
	}
}
