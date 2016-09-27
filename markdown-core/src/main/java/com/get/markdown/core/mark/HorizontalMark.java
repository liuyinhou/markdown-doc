package com.get.markdown.core.mark;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.get.markdown.core.vo.LineHolder;

/**
 * --- 横线
 * @author liuyinhou
 *
 */
public class HorizontalMark implements Mark {

	private static String PATTERN_STR = "^ *-{3,}";

	@Override
	public void executeMark(LineHolder lineHolder) {
		String line = lineHolder.getLine();
		Pattern pattern = Pattern.compile(PATTERN_STR);
		Matcher matcher = pattern.matcher(line);
		if (matcher.matches()
				&& "".equals(lineHolder.getPreLine())) {
	        lineHolder.setLine("<hr>");
		}
	}

}
