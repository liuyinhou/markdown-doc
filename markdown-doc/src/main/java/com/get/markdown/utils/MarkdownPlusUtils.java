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

package com.get.markdown.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.get.markdown.web.controller.MarkdownController;

/**
 * Convert Markdown text into HTML, as per http://daringfireball.net/projects/markdown/ .
 * Usage:
 * <pre><code>
 *     MarkdownProcessor markdown = new MarkdownProcessor();
 *     String html = markdown.markdown("*italic*   **bold**\n_italic_   __bold__");
 * </code></pre>
 */
public class MarkdownPlusUtils {
    
	public String markdown(String str) {
		str = markdownTable(str);
		str = markdownCode(str);
		str = markdownStrikethrough(str);
		//因为技术文档经常会出现下划线这种特殊符号，所以去掉对markdown中对下划线斜体的支持
		str = removeEm(str);
		return str;
	}
	
	/**
	 * 
	 * 对表格做处理
	 * 
	 * @param str
	 * @return
	 */
	private String markdownTable(String str) {
		Pattern pattern = Pattern.compile("(<p>\\|((?!</p>).)*\\|</p>)", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(str);
        StringBuffer strSb = new StringBuffer();
        int lastIndex = 0;
        while(matcher.find()) {
        	String thisMat = matcher.group();
        	StringBuffer tableSb = new StringBuffer();
        	tableSb.append("<table>");
            String[] lines = thisMat.split("\n");
            //thead 前两行
            String[] firstLine = removeEdge(lines[0], "<p>", "|").split("\\|");
            String[] secondLine = removeEdge(lines[1], "|").split("\\|");
            tableSb.append("<thead><tr>\n");
            for (int i=0;i<firstLine.length;i++) {
            	if (secondLine.length<=i) {
            		//第二行短，属于异常，跳过
            		break;
            	}
            	if (secondLine[i].startsWith(":")) {
            		if (secondLine[i].endsWith(":")) {
            			tableSb.append("<th style=\"text-align:center;\">");
            		} else {
            			tableSb.append("<th style=\"text-align:left;\">");
            		}
            	} else {
            		if (secondLine[i].endsWith(":")) {
            			tableSb.append("<th style=\"text-align:right;\">");
            		} else {
            			tableSb.append("<th>");
            		}
            	}
            	tableSb.append(firstLine[i]).append("</th>\n");
            }
            tableSb.append("</tr></thead>");
            //tbody
            tableSb.append("<tbody>\n");
            for (int i=2;i<lines.length;i++) {
            	tableSb.append("<tr>");
            	String[] colums = removeEdge(lines[i].trim(), "</p>", "|").split("\\|");
            	for (String colum : colums) {
            		tableSb.append("<td>").append(colum).append("</td>");
            	}
            	tableSb.append("</tr>\n");
            }
            tableSb.append("</tbody></table>\n");
            //替换掉原来str中的值
            strSb.append(str.substring(lastIndex, matcher.start()));
            strSb.append(tableSb.toString());
            lastIndex = matcher.end();
       }
        strSb.append(str.substring(lastIndex));
		return strSb.toString();
	}
	
	/**
	 * 去掉一个字符串首尾的特定字符，如: 
	 * str=|good|bad|better|
	 * edge=|
	 * 结果：
	 * good|bad|better
	 * 
	 * 当多个待剔除的字符串时，按顺序依次剔除
	 * 
	 * @param str
	 * @return
	 */
	public static String removeEdge(String str, String... edges) {
		str = str.trim();
		for (String edge : edges) {
			if (str.startsWith(edge)) {
				str = str.substring(edge.length());
			}
			if (str.endsWith(edge)) {
				str = str.substring(0, str.length()-edge.length());
			}
		}
		return str;
	}
	
	/**
	 * 
	 * 对code段落做处理
	 * 
	 * @param str
	 * @return
	 */
	private String markdownCode(String str) {
		Pattern pattern = Pattern.compile("(<p>```((?!</p>).)*```</p>)", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(str);
        StringBuffer strSb = new StringBuffer();
        int lastIndex = 0;
        while(matcher.find()) {
        	String thisMat = matcher.group();
        	thisMat = removeEdge(thisMat, "<p>", "</p>", "```", "\n");
        	StringBuffer tableSb = new StringBuffer();
        	tableSb.append("<pre><code>");
        	tableSb.append(thisMat);
            tableSb.append("</code></pre>\n");
            //替换掉原来str中的值
            strSb.append(str.substring(lastIndex, matcher.start()));
            strSb.append(tableSb.toString());
            lastIndex = matcher.end();
       }
        strSb.append(str.substring(lastIndex));
		return strSb.toString();
	}
	
	/**
	 * 
	 * 对删除线做处理
	 * 
	 * @param str
	 * @return
	 */
	private String markdownStrikethrough(String str) {
		Pattern pattern = Pattern.compile("(~~((?!~~).)*~~)", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(str);
        StringBuffer strSb = new StringBuffer();
        int lastIndex = 0;
        while(matcher.find()) {
        	String thisMat = matcher.group();
        	thisMat = removeEdge(thisMat, "~~");
        	StringBuffer tableSb = new StringBuffer();
        	tableSb.append("<del>");
        	tableSb.append(thisMat);
            tableSb.append("</del>");
            //替换掉原来str中的值
            strSb.append(str.substring(lastIndex, matcher.start()));
            strSb.append(tableSb.toString());
            lastIndex = matcher.end();
       }
        strSb.append(str.substring(lastIndex));
		return strSb.toString();
	}
	
	/**
	 * 剔除em标签，还原前后下划线
	 * @param str
	 * @return
	 */
	private String removeEm(String str) {
		str = str.replace("<em>", "_");
		str = str.replace("</em>", "_");
		return str;
	}
	
	public static void main(String[] args) {
//		MarkdownPlusUtils utils = new MarkdownPlusUtils();
//		String filePath = MarkdownController.class.getResource("/demo.txt").getPath();
//		String str = MarkdownController.readFile(filePath);
//		str = utils.markdown(str);
//		System.out.println("final : =\n" + str);
		
//		System.out.println(removeEdge("|good|bad|better|", "|"));
	}
	
}
