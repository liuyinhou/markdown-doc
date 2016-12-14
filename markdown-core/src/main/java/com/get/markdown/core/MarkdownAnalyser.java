/*
Copyright (c) 2005, Martian Software
Authors: Pete Bevin, John Mutchek
http://www.martiansoftware.com/markdownj

All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are
met:

 * Redistributions of source code must retain the above copyright notice,
  this list of conditions and the following disclaimer.

 * Redistributions in binary form must reproduce the above copyright
  notice, this list of conditions and the following disclaimer in the
  documentation and/or other materials provided with the distribution.

 * Neither the name "Markdown" nor the names of its contributors may
  be used to endorse or promote products derived from this software
  without specific prior written permission.

This software is provided by the copyright holders and contributors "as
is" and any express or implied warranties, including, but not limited
to, the implied warranties of merchantability and fitness for a
particular purpose are disclaimed. In no event shall the copyright owner
or contributors be liable for any direct, indirect, incidental, special,
exemplary, or consequential damages (including, but not limited to,
procurement of substitute goods or services; loss of use, data, or
profits; or business interruption) however caused and on any theory of
liability, whether in contract, strict liability, or tort (including
negligence or otherwise) arising in any way out of the use of this
software, even if advised of the possibility of such damage.

 */

package com.get.markdown.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.get.markdown.core.mark.BlankMark;
import com.get.markdown.core.mark.BlockQuoteMark;
import com.get.markdown.core.mark.BoldStrongMark;
import com.get.markdown.core.mark.CodeBlockMark;
import com.get.markdown.core.mark.CodeMark;
import com.get.markdown.core.mark.DelMark;
import com.get.markdown.core.mark.HeaderMark;
import com.get.markdown.core.mark.HorizontalMark;
import com.get.markdown.core.mark.ItalicEmMark;
import com.get.markdown.core.mark.LinkMark;
import com.get.markdown.core.mark.Mark;
import com.get.markdown.core.mark.OlListMark;
import com.get.markdown.core.mark.TableMark;
import com.get.markdown.core.mark.UlListMark;
import com.get.markdown.core.vo.LineHolder;

public class MarkdownAnalyser {

	private static List<Mark> markContainer;
	
	/**
	 * 初始化Mark容器
	 */
	private static void prepareMarkContainer() {
		markContainer = new ArrayList<Mark>();
		markContainer.add(new CodeBlockMark());//跨行效果，代码块；内部不支持单行效果，需放置于所有单行效果前面
		markContainer.add(new TableMark()); //跨行效果，表格；内部不支持单行效果，需放置于所有单行效果后面
		markContainer.add(new UlListMark());//跨行效果，列表；内部不支持单行效果，需放置于所有单行效果后面
		markContainer.add(new OlListMark());//跨行效果，带序号列表；内部不支持单行效果，需放置于所有单行效果后面
		markContainer.add(new HeaderMark());//单行效果，标题
		markContainer.add(new HorizontalMark());//单行效果，横线
		markContainer.add(new BoldStrongMark());//单词效果，粗体
		markContainer.add(new ItalicEmMark());//单词效果，斜体
		markContainer.add(new DelMark());//单词效果，删除线
		markContainer.add(new LinkMark());//单词效果，链接
		markContainer.add(new CodeMark());//单词效果，代码
		markContainer.add(new BlockQuoteMark()); //跨行效果，引用块；内部支持单行效果，需放置于所有单行效果后面
		markContainer.add(new BlankMark()); //跨行效果，段落；内部支持单行效果，需放置于所有单行效果后面
	}
	
	public static String analyseMarkdown(String content) {
		if (markContainer == null) {
			prepareMarkContainer();
		}
		// 替换windows回车
		content = content.replaceAll("\\r\\n", "\n");
		content = content.replaceAll("\\r", "\n");
		// 替换table符
		content = content.replaceAll("\\t", "    ");
		// 全文查找引用链接
		LineHolder lineHolder = new LineHolder();
		content = new LinkMark().prepareReferenceStyleLinks(content, lineHolder);
		String[] lines = content.split("\n");
		StringBuilder strBuilder = new StringBuilder();
		for (String line : lines) {
			lineHolder = new LineHolder(line, lineHolder);
			for (Mark mark : markContainer) {
				mark.executeMark(lineHolder);
			}
			if (lineHolder.getPreLine() != null) {
				strBuilder.append(lineHolder.getPreLine()).append("\n");
			}
		}
		//最后补一行空白
		lineHolder = new LineHolder("", lineHolder);
		for (Mark mark : markContainer) {
			mark.executeMark(lineHolder);
		}
		if (lineHolder.getPreLine() != null) {
			strBuilder.append(lineHolder.getPreLine()).append("\n");
		}
		strBuilder.append(lineHolder.getLine());
		
		return strBuilder.toString();
	}

	public static String readFile() {
		StringBuilder strBuilder = new StringBuilder();
		BufferedReader reader = null;
		try {
			File readFile = new File("test.txt");
			reader = new BufferedReader(new FileReader(readFile));
			String tmpStr = null;
			while (null != (tmpStr = reader.readLine())) {
				strBuilder.append(tmpStr).append("\n");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return strBuilder.toString();
	}

	public static void main(String[] args) {
		String st = MarkdownAnalyser.analyseMarkdown(readFile());
		System.out.println(st);
	}

}
