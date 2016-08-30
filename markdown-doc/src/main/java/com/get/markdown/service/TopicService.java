package com.get.markdown.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.markdownj.MarkdownProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.get.markdown.dao.TopicContentDao;
import com.get.markdown.dao.TopicDao;
import com.get.markdown.entity.enumeration.ResultCodeEnum;
import com.get.markdown.entity.enumeration.TopicContentStatusEnum;
import com.get.markdown.entity.enumeration.TopicStatusEnum;
import com.get.markdown.entity.po.Topic;
import com.get.markdown.entity.po.TopicContent;
import com.get.markdown.entity.vo.JsonResponse;
import com.get.markdown.entity.vo.Page;
import com.get.markdown.utils.Constants;
import com.get.markdown.utils.MarkdownPlusUtils;

@Service
public class TopicService {
	
	private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm";
	private SimpleDateFormat dateformat = new SimpleDateFormat(DEFAULT_FORMAT);
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private TopicDao topicDao;
	
	@Autowired
	private TopicContentDao topicContentDao;
	
	public JsonResponse getTopicList(Integer pageNum, Integer pageSize) {
		JsonResponse jr = new JsonResponse();
		List<Topic> list = topicDao.findPage(pageNum, pageSize, null, "uri asc"); // TODO uri asc
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("STATUS", TopicContentStatusEnum.DEFAULT.getCode());
		for (Topic topic : list) {
			Map<String, Object> oneMap = new HashMap<String, Object>();
			result.add(oneMap);
			oneMap.put("id", topic.getId());
			oneMap.put("name", topic.getName());
			oneMap.put("updateTime", dateformat.format(topic.getUpdateTime()));
			oneMap.put("operator", topic.getOperatorId());
			oneMap.put("status", topic.getStatus());
			oneMap.put("uri", topic.getUri());
			
			params.put("TOPIC_ID", topic.getId());
			List<TopicContent> topicContentList = topicContentDao.find(params, "CREATE_TIME desc");
			if (topicContentList.isEmpty()) {
				oneMap.put("remark", "");
				continue;
			}
			TopicContent topicContent = topicContentList.get(0);
			if (topicContent.getRemark() != null) {
				oneMap.put("remark", topicContent.getRemark());
			} else {
				oneMap.put("remark", "");
			}
		}
		jr.setData(result);
		Page page = new Page(pageNum, pageSize);
		Integer count = topicDao.count(null);
		page.setTotal(count);
		jr.setPage(page);
		return jr;
	}

	/**
	 * 首页
	 * @return
	 */
	public String topicIndex() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("URI", Constants.MARKDOWN_INDEX_URI);
		List<Topic> topicList = topicDao.find(params, "CREATE_TIME asc");
		if (topicList.isEmpty()) {
			return Constants.MARKDOWN_INDEX_EMPTY;
		}
		Topic topic = topicList.get(0);
		params.clear();
		params.put("TOPIC_ID", topic.getId());
		params.put("STATUS", TopicContentStatusEnum.DEFAULT.getCode());
		List<TopicContent> topicContentList = topicContentDao.find(params, "CREATE_TIME desc");
		if (topicContentList.isEmpty()) {
			return Constants.MARKDOWN_INDEX_EMPTY;
		}
		return topicContentList.get(0).getContentHtml();
	}

	public JsonResponse addTopic(String name, String uri) {
		JsonResponse jr = new JsonResponse();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("URI", uri);
		List<Topic> topicList = topicDao.find(params, "CREATE_TIME asc");
		if (!topicList.isEmpty()) {
			jr.setCode(ResultCodeEnum.BIZ_ERROR.getCode());
			jr.setMessage("保存失败，URI已经存在！");
			return jr;
		}
		Topic topic = new Topic();
		topic.setName(name);
		topic.setUri(uri);
		//topic.setOperatorId(x); // TODO
		topic.setStatus(TopicStatusEnum.DEFAULT.getCode());
		topic.setCreateTime(new Date());
		topic.setUpdateTime(new Date());
		topicDao.save(topic);
		// 初始化一个内容页
		TopicContent newTopicContent = new TopicContent();
		newTopicContent.setCreateTime(new Date());
		newTopicContent.setUpdateTime(new Date());
//		newTopicContent.setOperatorId(operatorId);  // TODO
		newTopicContent.setRemark("");
		newTopicContent.setStatus(TopicContentStatusEnum.DEFAULT.getCode());
		newTopicContent.setTopicId(topic.getId());
		newTopicContent.setContentMarkdown(Constants.MARKDOWN_INIT_CONTENT);
		MarkdownProcessor markdownProcessor = new MarkdownProcessor();
		String html = markdownProcessor.markdown(Constants.MARKDOWN_INIT_CONTENT);
		MarkdownPlusUtils markdownPlus = new MarkdownPlusUtils();
		html = markdownPlus.markdown(html);
		newTopicContent.setContentHtml(html);
		topicContentDao.save(newTopicContent);
		return jr;
	}

	public Map<String, Object> getTopicContentByUri(String uri) {
		logger.debug("请求页面：" + uri);
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("URI", uri);
		params.put("STATUS !=", TopicStatusEnum.DELETED.getCode());
		List<Topic> topicList = topicDao.find(params, "CREATE_TIME asc");
		if (topicList.isEmpty()) {
			result.put("contentHtml", Constants.MARKDOWN_INDEX_EMPTY);
			return result;
		}
		Topic topic = topicList.get(0);
		params.clear();
		params.put("TOPIC_ID", topic.getId());
		params.put("STATUS", TopicContentStatusEnum.DEFAULT.getCode());
		List<TopicContent> topicContentList = topicContentDao.find(params, "CREATE_TIME desc");
		if (topicContentList.isEmpty()) {
			result.put("contentHtml", Constants.MARKDOWN_INDEX_EMPTY);
			return result;
		}
		TopicContent topicContent = topicContentList.get(0);
		result.put("contentHtml", topicContent.getContentHtml());
		result.put("contentId", topicContent.getId());
		return result;
	}
	
	public JsonResponse getTopicContentById(Integer id, String action) {
		JsonResponse jr = new JsonResponse();
		TopicContent topicContent = topicContentDao.findById(id);
		if (topicContent == null) {
			jr.setCode(ResultCodeEnum.BIZ_ERROR.getCode());
			jr.setMessage("未知的内容ID");
			return jr;
		}
		jr.setData(topicContent);
		return jr;
	}

	public JsonResponse addTopicContent(String contentMarkdown, String remark, Integer preId) {
		JsonResponse jr = new JsonResponse();
		TopicContent topicContent = topicContentDao.findById(preId);
		if (topicContent == null) {
			logger.warn("提交失败，文档已经被修改");
			jr.setCode(ResultCodeEnum.BIZ_ERROR.getCode());
			jr.setMessage("提交失败，文档已经被修改");
			return jr;
		}
		if (!TopicContentStatusEnum.DEFAULT.getCode().equals(topicContent.getStatus())) {
			logger.warn("提交失败，文档已经被修改" + topicContent.getRemark());
			jr.setCode(ResultCodeEnum.BIZ_ERROR.getCode());
			jr.setMessage("提交失败，文档已经被修改：" + topicContent.getRemark());
			return jr;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("status", TopicContentStatusEnum.EXPIRE.getCode());
		params.put("update_time", new Date());
		topicContentDao.update(topicContent.getId(), params);
		
		TopicContent newTopicContent = new TopicContent();
		newTopicContent.setCreateTime(new Date());
		newTopicContent.setUpdateTime(new Date());
//		newTopicContent.setOperatorId(operatorId);  // TODO
		newTopicContent.setRemark(remark);
		newTopicContent.setStatus(TopicContentStatusEnum.DEFAULT.getCode());
		newTopicContent.setTopicId(topicContent.getTopicId());
		newTopicContent.setContentMarkdown(contentMarkdown);
		
		MarkdownProcessor markdownProcessor = new MarkdownProcessor();
		String html = markdownProcessor.markdown(contentMarkdown);
		MarkdownPlusUtils markdownPlus = new MarkdownPlusUtils();
		html = markdownPlus.markdown(html);
		newTopicContent.setContentHtml(html);
		topicContentDao.save(newTopicContent);
		jr.setData(newTopicContent);
		return jr;
	}
	
	public JsonResponse getTopic(Integer id) {
		JsonResponse jr = new JsonResponse();
		Topic topic = topicDao.findById(id);
		if (topic == null) {
			jr.setCode(ResultCodeEnum.BIZ_ERROR.getCode());
			jr.setMessage("未知的内容ID");
			return jr;
		}
		jr.setData(topic);
		return jr;
	}
	
	public JsonResponse editTopic(Integer id, String name, String uri) {
		JsonResponse jr = new JsonResponse();
		Topic topic = topicDao.findById(id);
		if (topic == null) {
			jr.setCode(ResultCodeEnum.BIZ_ERROR.getCode());
			jr.setMessage("未知的内容ID");
			return jr;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);
		params.put("uri", uri);
		params.put("update_time", new Date());
		topicDao.update(id, params);
		return jr;
	}
	
}
