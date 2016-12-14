package com.get.markdown.core.mark;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.get.markdown.core.vo.LineHolder;

/**
 * * ~~ 删除线
 * @author liuyinhou
 *
 */
public class DelMark implements Mark {

	private static String PATTERN_STR = "(~~[^~]+~~)";

	@Override
	public void executeMark(LineHolder lineHolder) {
		String line = lineHolder.getLine();
		Pattern pattern = Pattern.compile(PATTERN_STR);
		Matcher matcher = pattern.matcher(line);
		while(matcher.find()) {
			String group = matcher.group();
			String replace = "<del>" + group.substring(2, group.length()-2) + "</del>";
			line = line.replace(group, replace);
		}
		lineHolder.setLine(line);
	}
	
	public static void main(String[] args) {
		DelMark mark = new DelMark();
		LineHolder lineHolder = new LineHolder("~~good**sdf~~sd~~sf~~");
		mark.executeMark(lineHolder);
		System.out.println(lineHolder.getLine());
	}

}
