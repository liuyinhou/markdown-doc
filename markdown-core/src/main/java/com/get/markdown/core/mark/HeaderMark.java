package com.get.markdown.core.mark;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.get.markdown.core.vo.LineHolder;

/**
 * # 标题
 * = - 标题
 * @author liuyinhou
 *
 */
public class HeaderMark implements Mark {

	private static String PATTERN_STR_HASH = "^(#+).+";
	private static String PATTERN_STR_EQUAL = "^=+";
	private static String PATTERN_STR_HORIZONTAL_LINE = "^-+";

	@Override
	public void executeMark(LineHolder lineHolder) {
		boolean marked = hashMark(lineHolder);
		if (marked) {
			return ;
		}
		marked = equalMark(lineHolder);
		if (marked) {
			return ;
		}
		horizontalLineMark(lineHolder);
	}
	
	/**
	 * 处理=标题 h1
	 * @return
	 */
	private boolean equalMark(LineHolder lineHolder) {
		boolean marked = false;
		String line = lineHolder.getLine();
		Pattern pattern = Pattern.compile(PATTERN_STR_EQUAL);
		Matcher matcher = pattern.matcher(line);
		if (matcher.matches()
				&& !"".equals(lineHolder.getPreLine())) {
	        StringBuilder strBuilder = new StringBuilder();
	        strBuilder.append("<h1>").append(lineHolder.getPreLine()).append("</h1>");
	        lineHolder.setPreLine(strBuilder.toString());
	        lineHolder.setLine("");
	        marked = true;
		}
		return marked;
	}
	
	/**
	 * 处理-标题 h2
	 * @return
	 */
	private boolean horizontalLineMark(LineHolder lineHolder) {
		boolean marked = false;
		String line = lineHolder.getLine();
		Pattern pattern = Pattern.compile(PATTERN_STR_HORIZONTAL_LINE);
		Matcher matcher = pattern.matcher(line);
		if (matcher.matches()
				&& !"".equals(lineHolder.getPreLine())) {
	        StringBuilder strBuilder = new StringBuilder();
	        strBuilder.append("<h2>").append(lineHolder.getPreLine()).append("</h2>");
	        lineHolder.setPreLine(strBuilder.toString());
	        lineHolder.setLine("");
	        marked = true;
		}
		return marked;
	}
	
	/**
	 * 处理#标题 h1~h6
	 * @return
	 */
	private boolean hashMark(LineHolder lineHolder) {
		boolean marked = false;
		String line = lineHolder.getLine();
		Pattern pattern = Pattern.compile(PATTERN_STR_HASH);
		Matcher matcher = pattern.matcher(line);
		if (matcher.matches()) {
			MatchResult mResult = matcher.toMatchResult();
	        String head = mResult.group(1);
	        int headInt = head.length();
	        if (headInt > 6) {
	        	headInt = 6;
	        }
	        StringBuilder strBuilder = new StringBuilder();
	        strBuilder.append("<h").append(headInt).append(">").append(line.substring(head.length()));
	        strBuilder.append("</h").append(headInt).append(">");
	        lineHolder.setLine(strBuilder.toString());
	        marked = true;
		}
		return marked;
	}
	

}
