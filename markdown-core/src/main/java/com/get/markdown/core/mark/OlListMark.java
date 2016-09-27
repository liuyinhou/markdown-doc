package com.get.markdown.core.mark;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.get.markdown.core.vo.LineHolder;

/**
 * 数字. 带序号的行
 * 
 * @author liuyinhou
 * 
 */
public class OlListMark implements Mark {

	private static String PATTERN_STR = "^( *\\d+\\. )\\S.*";

	@Override
	public void executeMark(LineHolder lineHolder) {
		String line = lineHolder.getLine();
		Pattern pattern = Pattern.compile(PATTERN_STR);
		Matcher matcher = pattern.matcher(line);
		if (matcher.matches()) {
			StringBuilder strBuilder = new StringBuilder();
			MatchResult mResult = matcher.toMatchResult();
			String space = mResult.group(1);
			line = line.substring(space.length());
			if (!lineHolder.getIsInOlList()) {
				lineHolder.setIsInOlList(true);
				strBuilder.append("<ol>");
			}
			strBuilder.append("<li>").append(line).append("</li>");
			lineHolder.setLine(strBuilder.toString());
		} else if (lineHolder.getIsInOlList()) {
			// 说明上一行是ol标记，需要做一个结尾
			lineHolder.setIsInOlList(false);
			lineHolder.setLine("</ol>" + line);
		}
	}

}
