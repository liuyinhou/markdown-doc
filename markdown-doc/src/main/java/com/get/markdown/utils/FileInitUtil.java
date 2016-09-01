package com.get.markdown.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.markdownj.MarkdownProcessor;

import com.get.markdown.dao.ConnectionFactory;
import com.get.markdown.dao.TopicContentDao;
import com.get.markdown.dao.TopicDao;
import com.get.markdown.entity.enumeration.TopicContentStatusEnum;
import com.get.markdown.entity.enumeration.TopicStatusEnum;
import com.get.markdown.entity.po.Topic;
import com.get.markdown.entity.po.TopicContent;

/**
 * @author inigo.liu
 *
 */
public class FileInitUtil {

	private TopicDao topicDao = new TopicDao();
	private TopicContentDao topicContentDao = new TopicContentDao();
	
	public static final String FILE_PATH = "/Users/liuyinhou/Documents/tmp/markdown";
	private String name = "";
	
	public void readFile(File file) {
		System.out.println(file.getAbsolutePath());
		if (!file.exists()) {
			return ;
		}
		if (file.isDirectory()) {
			for (File one : file.listFiles()) {
				readFile(one);
			}
			return ;
		}
		String uri = file.getAbsolutePath();
		if (file.getName().startsWith(".")) {
			System.out.println("忽略" + uri);
			return ;
		}
		uri = uri.replace(FILE_PATH, "").replace(".txt", "");
		if (uri.endsWith("index")) {
			uri = uri.substring(0, uri.length()-5);
		}
		uri = "/markdown" + uri;
		if (uri.endsWith("/")) {
			uri = uri.substring(0, uri.length()-1);
		}
		Integer topicId = 1;
		if (!"/markdown".equals(uri)) {
			//创建 topic
			Topic topic = new Topic();
			topic.setCreateTime(new Date());
			topic.setName(file.getName());
			topic.setStatus(TopicStatusEnum.DEFAULT.getCode());
			topic.setUpdateTime(new Date());
			topic.setUri(uri);
			topicDao.save(topic);
			topicId = topic.getId();
		} else {
			topicId = 1;
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("status", TopicContentStatusEnum.RECOVERABLE.getCode());
			params.put("update_time", new Date());
			topicContentDao.update(1, params);
		}
		
		//创建topicContent
		TopicContent topicContent = new TopicContent();
		name = null;
		String contentMarkdown = readFileBody(file);
		topicContent.setContentMarkdown(contentMarkdown);
		MarkdownProcessor markdownProcessor = new MarkdownProcessor();
		String contentHtml = markdownProcessor.markdown(contentMarkdown);
		MarkdownPlusUtils markdownPlus = new MarkdownPlusUtils();
		contentHtml = markdownPlus.markdown(contentHtml);
		topicContent.setContentHtml(contentHtml);
		topicContent.setCreateTime(new Date());
		topicContent.setOperatorId(1);
		topicContent.setRemark("初始化");
		topicContent.setStatus(TopicContentStatusEnum.DEFAULT.getCode());
		topicContent.setTopicId(topicId);
		topicContent.setUpdateTime(new Date());
		topicContentDao.save(topicContent);
		if (name != null) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("name", name);
			topicDao.update(topicId, params);
		}
	}
	
	private String readFileBody(File file) {
		String fileStr = "";
		BufferedReader bufReader = null;
		try {
			bufReader = new BufferedReader(new FileReader(file));
			String tmpStr = null;
			while (null != (tmpStr = bufReader.readLine())) {
				tmpStr = tmpStr.replaceAll("\\.txt", "");
				if (tmpStr.startsWith("##")
						&& '#' != tmpStr.charAt(2)
						&& name == null) {
					name = tmpStr.replace("##", "");
				}
				fileStr += "\n" + tmpStr;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			try {
				bufReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fileStr;
	}
	
	public static void main(String[] args) throws Exception {
		FileInitUtil util = new FileInitUtil();
		util.readFile(new File(FILE_PATH));
		ConnectionFactory.getConnection().close();
	}
}
