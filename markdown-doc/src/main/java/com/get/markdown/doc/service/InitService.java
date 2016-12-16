package com.get.markdown.doc.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.get.markdown.doc.dao.ConnectionFactory;
import com.get.markdown.doc.dao.TopicContentDao;
import com.get.markdown.doc.dao.TopicDao;
import com.get.markdown.doc.dao.UserDao;
import com.get.markdown.doc.entity.enumeration.ResultCodeEnum;
import com.get.markdown.doc.entity.enumeration.TopicContentStatusEnum;
import com.get.markdown.doc.entity.enumeration.TopicStatusEnum;
import com.get.markdown.doc.entity.po.Topic;
import com.get.markdown.doc.entity.po.TopicContent;
import com.get.markdown.doc.entity.vo.JsonResponse;
import com.get.markdown.doc.search.TopicSearch;
import com.get.markdown.doc.utils.MD5Encrypt;

@Service
public class InitService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private TopicDao topicDao;
	@Autowired
	private TopicContentDao topicContentDao;
	@Autowired
	private TopicSearch topicSearch;
	
	public boolean checkNeedInit() {
		Integer count = userDao.count(null);
		if (count > 0) {
			logger.warn("已经有数据，不能初始化");
			return false;
		} else {
			logger.info("用户表为空，项目初始化");
			return true;
		}
	}
	
	public JsonResponse initDoc(String adminName, String passwd) {
		JsonResponse jr = new JsonResponse();
		Connection conn = ConnectionFactory.getConnection();
		try {
			Statement  st = conn.createStatement();
			List<String> sqlList = readSqlFile();
			for (String sql : sqlList) {
				st.execute(sql);
			}
			//加密3次
			String md5 = MD5Encrypt.encrypt(MD5Encrypt.encrypt(MD5Encrypt.encrypt(passwd)));
			st.execute("UPDATE t_user SET name='" + adminName + "' ,passwd='" + md5 + "' where id=1;");
			//初始化索引
			initSearchIndex();
			conn.close();
		} catch (SQLException e) {
			logger.error("", e);
			jr.setCode(ResultCodeEnum.SYSTEM_ERROR.getCode());
			jr.setMessage(ResultCodeEnum.SYSTEM_ERROR.getMessage());
		}
		return jr;
	}
	
	public JsonResponse initSearchIndex() {
		JsonResponse jr = new JsonResponse();
		long curTime = System.currentTimeMillis();
		logger.info("重建搜索索引开始");
		//先清空索引
		topicSearch.deleteAll();
		Map<String, Object> queryTopic = new HashMap<String, Object>();
		queryTopic.put("status !=", TopicStatusEnum.DELETED.getCode());
		List<Topic> topicList = topicDao.find(queryTopic, "create_time");
		for (Topic topic : topicList) {
			Map<String, Object> queryTopicContent = new HashMap<String, Object>();
			queryTopicContent.put("status =", TopicContentStatusEnum.DEFAULT.getCode());
			queryTopicContent.put("topic_id", topic.getId());
			List<TopicContent> topicContentList = topicContentDao.find(queryTopicContent, "create_time desc");
			if (topicContentList.isEmpty()) {
				topicSearch.addIndex(topic.getId(), null, topic.getUri(), topic.getName(), null);
			} else {
				TopicContent topicContent = topicContentList.get(0);
				topicSearch.addIndex(topic.getId(), topicContent.getId(), topic.getUri(), topic.getName(), 
						topicContent.getContentMarkdown());
			}
		}
		String msg = "重建搜索索引结束，条数：" + topicList.size() + "，耗时：" + (System.currentTimeMillis()-curTime) + "ms";
		logger.info(msg);
		jr.setMessage(msg);
		return jr;
	}
	
	public List<String> readSqlFile() {
		List<String> sqlList = new ArrayList<String>();
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		InputStream input = null;
		try {
			input = cl.getResourceAsStream("init.sql" );
			Reader reader = new InputStreamReader(input, "UTF-8" );
			BufferedReader bufReader = new BufferedReader(reader);
			StringBuilder strBuilder = new StringBuilder();
			String tmpStr = null;
			while (null != (tmpStr = bufReader.readLine())) {
				strBuilder.append(tmpStr);
				if (tmpStr.endsWith(";")) {
					sqlList.add(strBuilder.toString());
					strBuilder = new StringBuilder();
				}
			}
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				logger.error("", e);
			}
		}
		return sqlList;
	}
	
}
