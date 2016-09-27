package com.get.markdown.core.mark;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.get.markdown.core.vo.LineHolder;

/**
 * * _ 斜体
 * @author liuyinhou
 *
 */
public class ItalicEmMark implements Mark {

	private static String PATTERN_STR = "((_[^_]+_)|(\\*[^*]+\\*))";

	@Override
	public void executeMark(LineHolder lineHolder) {
		String line = lineHolder.getLine();
		Pattern pattern = Pattern.compile(PATTERN_STR);
		Matcher matcher = pattern.matcher(line);
		while(matcher.find()) {
			String group = matcher.group();
			String replace = "<em>" + group.substring(1, group.length()-1) + "</em>";
			line = line.replace(group, replace);
		}
		lineHolder.setLine(line);
	}
	
	public static void main(String[] args) {
		ItalicEmMark mark = new ItalicEmMark();
		LineHolder lineHolder = new LineHolder("*good**sdf**sdsf");
		mark.executeMark(lineHolder);
	}

}
