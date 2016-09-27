package com.get.markdown.doc.dao;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * 获得数据库连接
 * 
 * @author inigo.liu
 */
public class ConnectionFactory {

	private static Connection conn;

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
				conn = DriverManager.getConnection("jdbc:hsqldb:file:db/mddb;shutdown=true", "ORIN", "12369");
			}
		} catch (Exception e) {
			e.printStackTrace();
			conn = null;
		}
		return conn;
	}

}
