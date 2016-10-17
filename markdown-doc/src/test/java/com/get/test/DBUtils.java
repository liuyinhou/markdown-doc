package com.get.test;

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

public class DBUtils {
	/**
	 * 一个非常标准的连接Oracle数据库的示例代码
	 */
	private Properties config = new Properties();

	@Test
	public void testOracle() {
		loadConfig("application.properties");
		Connection con = null;// 创建一个数据库连接
		PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
		ResultSet result = null;// 创建一个结果集对象
		String table = "AX_DAILY_CHANGE_STOCK";
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");// 加载Oracle驱动程序
			String url = "jdbc:oracle:" + "thin:@127.0.0.1:1521:orcl";// 127.0.0.1是本机地址，XE是精简版Oracle的默认数据库名
			String user = "anxl";// 用户名,系统默认的账户名
			String password = "123456";// 你安装时选设置的密码
			con = DriverManager.getConnection(url, user, password);// 获取连接
			String sql = "select tab.table_name,col.column_name,data_type,col.COMMENTS  from user_tab_columns  tab inner join user_col_comments col on tab.column_name = col.column_name where tab.Table_Name='"
					+ table + "' and col.table_name = '" + table + "'  order by column_id";// 预编译语句，“？”代表参数
			pre = con.prepareStatement(sql);// 实例化预编译语句
			result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
			StringBuffer sb = new StringBuffer();
			sb.append(String.format(config.getProperty("lujing"), table) + "\n");
			sb.append(String.format(config.getProperty("table"), table) + "\n");
			sb.append(config.get("title") + "\n");
			sb.append(config.get("split") + "\n");
			while (result.next()) {
				sb.append(String.format(config.getProperty("content"), result.getString("column_name"),
						result.getString("data_type"), result.getString("COMMENTS")) + "\n");
			}
			System.out.println(sb.toString());
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
	}

	private void loadConfig(String configFile) {
		InputStream input = null;
		Reader reader = null;
		try {
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			input = cl.getResourceAsStream(configFile);
			reader = new InputStreamReader(input, "UTF-8");
			config.load(reader);
		} catch (Exception e) {
			System.out.println(e.getMessage());
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
