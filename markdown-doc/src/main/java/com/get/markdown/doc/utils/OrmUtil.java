package com.get.markdown.doc.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 对象关系映射(Object Relation Mapping)工具类
 * @author liuyinhou
 *
 */
public class OrmUtil {

	/**
	 * 匹配类字段名与数据库列名
	 * @param tarClass
	 * @return
	 */
	public static Map<String, Field> metchClassFieldForDb(Class tarClass) {
		Map<String, Field> map = new HashMap<String, Field>();
		for (Field field : tarClass.getDeclaredFields()) {
			field.setAccessible(true);
			String colum = convertClassFieldToDbColumn(field.getName());
			map.put(colum, field);
		}
		return map;
	}
	
	/**
	 * 将类字段名转换为数据库列名
	 * <p>
	 * createTime -> CREATE_TIME
	 * @param tarClass
	 * @return
	 */
	public static String convertClassFieldToDbColumn(String field) {
		String colum = "";
		for (char one : field.toCharArray()) {
			if (Character.isUpperCase(one)) {
				colum += "_" + one;
			} else {
				colum += one;
			}
		}
		return colum.toUpperCase();
	}
	
	/**
	 * 将数据库列名转换为类字段名
	 * <p>
	 * CREATE_TIME -> createTime
	 * @param tarClass
	 * @return
	 */
	public static String convertDbColumnToClassField(String colum) {
		String field = "";
		boolean isUpper = false;
		for (char one : colum.toCharArray()) {
			if ("_".equals(colum)) {
				isUpper = true;
				continue;
			}
			if (isUpper) {
				field += Character.toUpperCase(one);
				isUpper = false;
			} else {
				field += Character.toLowerCase(one);
			}
		}
		return field;
	}
	
	/**
	 * 获取数据库表名
	 * 
	 * @return
	 */
	public static String getTableName(Class tarClass) {
		String className = tarClass.getSimpleName();
		String tableName = "t_";
		boolean isFirst = true;
		for (char one : className.toCharArray()) {
			if (Character.isUpperCase(one) && !isFirst) {
				tableName += "_" + one;
			} else {
				tableName += one;
			}
			isFirst = false;
		}
		return tableName.toLowerCase();
	}
	
	/**
	 * 判断字符串是否以运算符结尾
	 * <p>
	 * =、 >、 >=、 <、 <=、 <>、 !=
	 * @param str
	 * @return
	 */
	public static boolean isEndWithOperator(String str) {
        String sp = "(\\w|\\s)+((=)|(>[=]{0,1})|(<[=>]{0,1})|(!=))";
        Pattern pattern = Pattern. compile(sp.trim());
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
	}
	
	public static void main(String[] args) {
		System.out.println(isEndWithOperator("name ="));
		System.out.println(isEndWithOperator("name>"));
		System.out.println(isEndWithOperator("name >="));
		System.out.println(isEndWithOperator("name<>"));
		System.out.println(isEndWithOperator("name!="));
		System.out.println(isEndWithOperator("name! ="));
		System.out.println(isEndWithOperator("name> ="));
		System.out.println(isEndWithOperator("name !="));
	}
	
}
