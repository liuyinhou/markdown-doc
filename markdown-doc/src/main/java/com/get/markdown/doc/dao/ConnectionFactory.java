package com.get.markdown.doc.dao;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * 获得数据库连接
 * 
 * @author inigo.liu
 */
public class ConnectionFactory {

	private static Connection conn;
	private static Properties properties = null;

	private static void loadProperties() {
		InputStream input = null;
		try {
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			input = cl.getResourceAsStream("hsqldb.properties");
			InputStreamReader reader = new InputStreamReader(input, "UTF-8");
			properties = new Properties();
			properties.load(reader);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 获得数据库连接
	 * 
	 * @return
	 */
	public static Connection getConnection() {
		try {
			if (null == conn || conn.isClosed() || !conn.isValid(1)) {
				try {
					Class.forName("org.hsqldb.jdbcDriver");
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				loadProperties();
				String dbPath = properties.getProperty("HSQL_DB_PATH");
				String user = properties.getProperty("HSQL_DB_USER");
				String pwd = properties.getProperty("HSQL_DB_PWD");
				conn = DriverManager.getConnection("jdbc:hsqldb:file:" + dbPath +";shutdown=true", user, pwd);
			}
		} catch (Exception e) {
			e.printStackTrace();
			conn = null;
		}
		return conn;
	}

}
