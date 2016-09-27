package com.get.markdown.doc.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.get.markdown.core.MarkdownAnalyser;
import com.get.markdown.doc.web.controller.MarkdownController;

@Service
public class MarkdownService {
	
	public static final String MARKDOWN_FILE_PRE = "/markdown";
	public static final String COMMON_404 = "/common/404.txt";
	public static final String COMMON_INDEX = "/common/index.txt";
	public static final String REFRESH_EXEC_STR = "/get/deploy/markdown-api/markdown-api-deploy-git.sh";
	public static Map<String, String> fileMap = new HashMap<String, String>();

	public String markdown(String... path) {
		String fileName = MARKDOWN_FILE_PRE + "/" + path;
		fileName += "/index.txt";
		URL url = MarkdownController.class.getResource(fileName);
		if (url == null) {
			url = MarkdownController.class.getResource(MARKDOWN_FILE_PRE + COMMON_404);
		}
		String filePath = url.getPath();
		String str = readFile(filePath);
//		MarkdownProcessor markdownProcessor = new MarkdownProcessor();
//		String result = markdownProcessor.markdown(str);
//		MarkdownPlusUtils markdownPlus = new MarkdownPlusUtils();
//		result = markdownPlus.markdown(result);
		String result = MarkdownAnalyser.analyseMarkdown(str);
		return result;
	}
	
	private String readFile(String fileName) {
		String fileStr = fileMap.get(fileName);
		if (fileStr != null) {
			return fileStr;
		}
		fileStr = "";
		BufferedReader bufReader = null;
		try {
			bufReader = new BufferedReader(new FileReader(new File(fileName)));
			String tmpStr = null;
			while (null != (tmpStr = bufReader.readLine())) {
				fileStr += "\n" + tmpStr;
			}
			fileMap.put(fileName, fileStr);
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
}
