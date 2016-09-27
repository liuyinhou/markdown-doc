package com.get.markdown.core.mark;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.get.markdown.core.vo.LineHolder;

/**
 * ** __ 粗体
 * @author liuyinhou
 *
 */
public class BoldStrongMark implements Mark {

	private static String PATTERN_STR = "((_{2}[^_]+_{2})|(\\*{2}[^*]+\\*{2}))";

	@Override
	public void executeMark(LineHolder lineHolder) {
		String line = lineHolder.getLine();
		Pattern pattern = Pattern.compile(PATTERN_STR);
		Matcher matcher = pattern.matcher(line);
		while(matcher.find()) {
			String group = matcher.group();
			String replace = "<strong>" + group.substring(2, group.length()-2) + "</strong>";
			line = line.replace(group, replace);
		}
		lineHolder.setLine(line);
		
	}
	
	public static void main(String[] args) {
		BoldStrongMark mark = new BoldStrongMark();
		LineHolder lineHolder = new LineHolder("_good__sdf__sdsf");
		mark.executeMark(lineHolder);
	}

}
