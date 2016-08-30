package com.get.markdown.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author inigo.liu
 *
 */
public class DbInitUtil {

	
	/**
	 * 第一次使用，初始化DB中的表
	 * @return
	 */
	public static boolean initDb() {
		Connection conn = ConnectionFactory.getConnection();
		try {
			Statement  st = conn.createStatement();
			//创建表
			st.execute("CREATE TABLE t_user(id INTEGER GENERATED BY DEFAULT AS IDENTITY, name VARCHAR(200),  passwd VARCHAR(200), nick_name VARCHAR(200), create_time TIMESTAMP, update_time TIMESTAMP);");
			st.execute("CREATE TABLE t_topic(id INTEGER GENERATED BY DEFAULT AS IDENTITY, name VARCHAR(1000), uri VARCHAR(200), operator_id INTEGER, status INTEGER, create_time TIMESTAMP, update_time TIMESTAMP);");
			st.execute("CREATE TABLE t_topic_content(id INTEGER GENERATED BY DEFAULT AS IDENTITY, topic_id INTEGER, content_markdown VARCHAR(20000), content_html VARCHAR(20000), remark VARCHAR(20000), operator_id INTEGER, status INTEGER, create_time TIMESTAMP, update_time TIMESTAMP);");
			//初始化数据
			st.execute("INSERT INTO  t_topic(id,name,uri,operator_id,status,create_time,update_time) values(1,'主页','/markdown',1,8,now(),now())");
			st.execute("INSERT INTO  t_topic_content(id,topic_id,content_markdown,content_html,operator_id,status,create_time) values(1,1,'# Header 1\n## Header 2\n### Header 3\n#### Header 4\n##### Header 5\n###### Header 6\n\n表格\n\n| 姓名 | 年龄 | 性别|\n| -------- |---------|---------|\n| 张三 | 20 |男 |\n| 李四 | 20 |男 |\n\n* 1标题\n * 1.1标题\n * 1.2标题\n* 2标题\n * 2.1标题\n * 2.2标题\n\n显示`高亮` **加粗**\n\n>提示语句\n\n\n---\n\n```\n//代码段\npublic static void main(String[] args) {\n	System.out.println(\"Hellow world!\");\n}\n```','<h1>Header 1</h1> <h2>Header 2</h2> <h3>Header 3</h3> <h4>Header 4</h4> <h5>Header 5</h5> <h6>Header 6</h6> <p>表格</p> <table><thead><tr> <th> 姓名 </th> <th> 年龄 </th> <th> 性别</th> </tr></thead><tbody> <tr><td> 张三 </td><td> 20 </td><td>男 </td></tr> <tr><td> 李四 </td><td> 20 </td><td>男 </td></tr> </tbody></table> <ul> <li>1标题 <ul><li>1.1标题</li> <li>1.2标题</li></ul></li> <li>2标题 <ul><li>2.1标题</li> <li>2.2标题</li></ul></li> </ul> <p>显示<code>高亮</code> <strong>加粗</strong></p> <blockquote> <p>提示语句</p> </blockquote> <hr> <pre><code>//代码段\npublic static void main(String[] args) {\n    System.out.println(\"Hellow world!\");\n}</code></pre>',1,0,now())");
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public static void main(String[] args) {
		initDb();
	}
}
