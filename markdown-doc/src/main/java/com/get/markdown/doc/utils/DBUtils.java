package com.get.markdown.doc.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import org.junit.Test;
import org.springframework.util.StringUtils;

public class DBUtils {
	/**
	 * 一个非常标准的连接Oracle数据库的示例代码
	 */
	private static Properties config = new Properties();

	public static String getTopicContent(String table) {
		if(!StringUtils.isEmpty(table)){
			loadConfig("application.properties");
			Connection con = null;																// 创建一个数据库连接
			PreparedStatement pre = null;														// 创建预编译语句对象，一般都是用这个而不用Statement
			ResultSet result = null;															// 创建一个结果集对象
			String columnStr;
			String typeStr;
			String commentstr;
			StringBuffer sb = new StringBuffer();
			try {
				Class.forName(config.getProperty("jdbc.driver"));								// 加载Oracle驱动程序
				String url = config.getProperty("jdbc.url");									// 127.0.0.1是本机地址，XE是精简版Oracle的默认数据库名
				String user = config.getProperty("jdbc.user");									// 用户名,系统默认的账户名
				String password = config.getProperty("jdbc.password");							// 你安装时选设置的密码
				con = DriverManager.getConnection(url, user, password);							// 获取连接
				String sql;																		// 预编译语句，“？”代表参数
				if("oracle.jdbc.driver.OracleDriver".equals(config.getProperty("jdbc.driver"))){//oracle数据库
					sql = "select tab.table_name,col.column_name,data_type,col.COMMENTS  from user_tab_columns  tab inner join user_col_comments col on tab.column_name = col.column_name where tab.Table_Name='"
							+ table + "' and col.table_name = '" + table.split("`")[0].trim().replaceAll(" ", "") + "'  order by column_id";
					columnStr = "column_name";
					typeStr = "data_type";
					commentstr = "COMMENTS";
				}else if("com.mysql.jdbc.Driver".equals(config.getProperty("jdbc.driver"))){	//mysql数据库
					sql = "show full columns from " + table.split("`")[0].trim().replaceAll(" ", "") + ";";
					columnStr = "Field";
					typeStr = "Type";
					commentstr = "Comment";
				}else{
					return null;
				}
				pre = con.prepareStatement(sql);												// 实例化预编译语句
				result = pre.executeQuery();													// 执行查询，注意括号中不需要再加参数
				sb.append(String.format(config.getProperty("lujing"), table) + "\n");
				sb.append(String.format(config.getProperty("table"), table) + "\n");
				sb.append(config.get("title") + "\n");
				sb.append(config.get("split") + "\n");
				while (result.next()) {
					sb.append(String.format(config.getProperty("content"),"`" +  result.getString(columnStr) + "`", result.getString(typeStr), result.getString(commentstr)) + "\n");
				}
				sb.append("\n" + config.get("weihu") + "\n");
				sb.append(config.get("split2") + "\n");
				sb.append(String.format(config.getProperty("content2"), DateUtils.getDateTimeString()) + "\n");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					// 逐一将上面的几个对象关闭，因为不关闭的话会影响性能、并且占用资源
					// 注意关闭的顺序，最后使用的最先关闭
					if (result != null)
						result.close();
					if (pre != null)
						pre.close();
					if (con != null)
						con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return sb.toString();
		}else{
			return null;
		}
	}

	private static void loadConfig(String configFile) {
		InputStream input = null;
		Reader reader = null;
		try {
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			input = cl.getResourceAsStream(configFile);
			reader = new InputStreamReader(input, "UTF-8");
			config.load(reader);
		} catch (Exception e) {
			System.out.println("xx");
		} finally {
			try {
				if (null != reader)
					reader.close();
				if (null != input)
					input.close();
			} catch (IOException e) {
			}
		}
	}
}
