package com.cfm.bankinterface.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 对象打印工具
 * 
 * @author admin
 *
 */
public class PrintUtil {
	private final static String TEM = "\t";

	/**
	 * 打印对象
	 * 
	 * @param obj
	 * @return
	 */
	public static String objToString(Object obj) {
		return objToString(obj, 0);
	}

	private static String objToString(Object obj, int level) {
		if (obj == null) {
			return "null\n";
		} else if (isCollectionType(obj)) {
			return collectionTypeToString(obj, level);
		} else if (isComplexType(obj)) {
			return complexTypeToString(obj, level);
		} else {
			return obj.toString() + "\n";

		}
	}

	private static String levelPrint(int level) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < level; i++) {
			buffer.append(TEM);
		}
		return buffer.toString();
	}

	/**
	 * 判读对象是否是集合对象
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isCollectionType(Object obj) {

		return (obj.getClass().isArray() || (obj instanceof Collection) || (obj instanceof Hashtable)
				|| (obj instanceof HashMap) || (obj instanceof HashSet) || (obj instanceof List)
				|| (obj instanceof AbstractMap));
	}

	/**
	 * 判读对象是否是Map对象
	 * 
	 * @param obj
	 * @return
	 */
	private static boolean isMap(Object obj) {
		return ((obj instanceof AbstractMap) || (obj instanceof HashMap) || (obj instanceof Hashtable));

	}

	private static boolean isCollection(Object obj) {
		return (obj instanceof Collection);
	}

	/**
	 * 判读对象是否collection或set对象
	 * 
	 * @param obj
	 * @return
	 */
	private static boolean isCollectionOrSet(Object obj) {
		return ((obj instanceof Collection) || (obj instanceof HashSet)) || (obj instanceof List);
	}

	/**
	 * 判断对象是否非基础数据、集合类，以及基础数据包装类
	 * 
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static boolean isComplexType(Object obj) {
		if (obj instanceof Boolean || obj instanceof Short || obj instanceof Byte || obj instanceof Integer
				|| obj instanceof Long || obj instanceof Float || obj instanceof Character || obj instanceof Double
				|| obj instanceof String) {

			return false;
		} else {
			Class objectClass = obj.getClass();

			if (objectClass == boolean.class || objectClass == Boolean.class || objectClass == short.class
					|| objectClass == Short.class || objectClass == byte.class || objectClass == Byte.class
					|| objectClass == int.class || objectClass == Integer.class || objectClass == long.class
					|| objectClass == Long.class || objectClass == float.class || objectClass == Float.class
					|| objectClass == char.class || objectClass == Character.class || objectClass == double.class
					|| objectClass == Double.class || objectClass == String.class) {
				return false;
			}

			else {
				return true;
			}
		}
	}

	/**
	 * 打印复杂对象
	 * 
	 * @param obj
	 * @param level
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static String complexTypeToString(Object obj, int level) {
		StringBuffer buffer = new StringBuffer(obj.getClass().getName() + ":\n");
		try {
			Class cl = obj.getClass();
			processFields(cl.getDeclaredFields(), obj, buffer, level);
		} catch (IllegalAccessException iae) {
			buffer.append(iae.toString());
		}
		return buffer.toString();
	}

	/**
	 * 处理对象的字段
	 * 
	 * @param declaredFields
	 * @param obj
	 * @param buffer
	 * @param level
	 */
	private static void processFields(Field[] declaredFields, Object obj, StringBuffer buffer, int level)
			throws IllegalAccessException {
		for (Field field : declaredFields) {
			// 屏蔽内部类机特殊构造类
			if (field.getName().equals("__discriminator") || field.getName().equals("__uninitialized")
					|| field.getName().startsWith("this$")) {
				continue;
			}
			field.setAccessible(true);
			if (Modifier.isStatic(field.getModifiers())) {

			} else {
				buffer.append(levelPrint(level)+field.getName()+":");
				buffer.append(objToString(field.get(obj),level+1));
			}
		}
	}

	/**
	 * 打印集合对象
	 * 
	 * @param obj
	 * @param level
	 *            打印层级
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static String collectionTypeToString(Object obj, int level) {
		StringBuffer buffer = new StringBuffer("");
		if (level != 0) {
			buffer.append("\n");

		}
		// 判断如果是数组对象
		if (obj.getClass().isArray()) {
			if (Array.getLength(obj) > 0) {
				level++;
				for (int j = 0; j < Array.getLength(obj); j++) {
					Object x = Array.get(obj, j);
					buffer.append(levelPrint(level) + "[" + j + "]:");
					buffer.append(objToString(x, level+1));
				}
			}

		} else {
			if (isMap(obj)) {
				Set keySet = ((Map) obj).keySet();
				Iterator iterator = keySet.iterator();
				int size = keySet.size();
				if (size > 0) {
					level++;
					while(iterator.hasNext()){
						Object key = iterator.next();
						Object x = ((Map) obj).get(key);
						buffer.append(levelPrint(level) + "[" + key + "]:");
						buffer.append(objToString(x, level+1));
					}
				} else {
					buffer.append(levelPrint(level) + "[]: empty\n");
				}
			} else if (isCollectionOrSet(obj)) {
				Iterator iterator = null;
				int size = 0;
				if (isCollection(obj)) {
					iterator = ((Collection) obj).iterator();
					size = ((Collection) obj).size();
				} else {
					iterator = ((HashSet) obj).iterator();
					size = ((HashSet) obj).size();
				}
				if (size > 0) {
					level++;
					for (int j = 0; iterator.hasNext(); j++) {
						Object x = iterator.next();
						buffer.append(levelPrint(level) + "[" + j + "]:");
						buffer.append(objToString(x, level+1));
					}
				} else {
					buffer.append(levelPrint(level) + "[]: empty\n");
				}
			}
		}

		return buffer.toString();
	}
}
