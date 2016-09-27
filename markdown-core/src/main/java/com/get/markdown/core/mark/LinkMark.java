package com.get.markdown.core.mark;

import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.get.markdown.core.vo.LineHolder;

/**
 * 链接
 * @author liuyinhou
 *
 */
public class LinkMark implements Mark {

	private static String PATTERN_STR_INLINE = "!?\\[[^]]+\\]\\([^)]+\\)";
	private static String PATTERN_STR_REFERENCE = "!?\\[[^]]+\\]\\[[^]]+\\]";
	private static String PATTERN_STR_INLINE_GROUP = "^!?\\[([^]]+)\\]\\(([^)]+)\\)";
	private static String PATTERN_STR_REFERENCE_GROUP = "^!?\\[([^]]+)\\]\\[([^]]+)\\]";

	@Override
	public void executeMark(LineHolder lineHolder) {
		inlineStyleLinks(lineHolder);
		referenceStyleLinks(lineHolder);
	}
	
	private void inlineStyleLinks(LineHolder lineHolder) {
		String line = lineHolder.getLine();
		Pattern pattern = Pattern.compile(PATTERN_STR_INLINE);
		Matcher matcher = pattern.matcher(line);
		while(matcher.find()) {
			String group = matcher.group();
			Pattern patternG = Pattern.compile(PATTERN_STR_INLINE_GROUP);
			Matcher matcherG = patternG.matcher(group);
			matcherG.matches();
			MatchResult matchResult = matcherG.toMatchResult();
			StringBuilder replace = new StringBuilder();
			if (group.startsWith("!")) {
				// image
				replace.append("<img src=\"").append(matchResult.group(2));
				replace.append("\" alt=\"").append(matchResult.group(1)).append("\" >");
			} else {
				// link
				replace.append("<a href=\"").append(matchResult.group(2)).append("\">");
				replace.append(matchResult.group(1)).append("</a>");
			}
			line = line.replace(group, replace);
		}
		lineHolder.setLine(line);
	}
	
	private void referenceStyleLinks(LineHolder lineHolder) {
		String line = lineHolder.getLine();
		Pattern pattern = Pattern.compile(PATTERN_STR_REFERENCE);
		Matcher matcher = pattern.matcher(line);
		while(matcher.find()) {
			String group = matcher.group();
			Pattern patternG = Pattern.compile(PATTERN_STR_REFERENCE_GROUP);
			Matcher matcherG = patternG.matcher(group);
			matcherG.matches();
			MatchResult matchResult = matcherG.toMatchResult();
			StringBuilder replace = new StringBuilder();
			String hrefUrl = lineHolder.getLinksReference().get(matchResult.group(2));
			if (group.startsWith("!")) {
				// image
				replace.append("<img src=\"").append(hrefUrl);
				replace.append("\" alt=\"").append(matchResult.group(1)).append("\" >");
			} else {
				// link
				replace.append("<a href=\"").append(hrefUrl).append("\">");
				replace.append(matchResult.group(1)).append("</a>");
			}
			line = line.replace(group, replace);
		}
		lineHolder.setLine(line);
	}
	
	public String prepareReferenceStyleLinks(String content, LineHolder lineHolder) {
		Map<String, String> linksReference = lineHolder.getLinksReference();
		String patternStr = "\\[[^]]+\\]: +\\S+( +\\S*)*";
		String patternStrGroup = "^\\[([^]]+)\\]: +(\\S+)( +\\S*)*";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(content);
		while(matcher.find()) {
			String group = matcher.group();
			Pattern patternG = Pattern.compile(patternStrGroup);
			Matcher matcherG = patternG.matcher(group);
			matcherG.matches();
			MatchResult matchResult = matcherG.toMatchResult();
			linksReference.put(matchResult.group(1), matchResult.group(2));
			content = content.replace(group, "");
		}
		return content;
	}
	
	public static void main(String[] args) {
		LinkMark mark = new LinkMark();
		LineHolder lineHolder = new LineHolder("sdf：This is an [example link][1]  (http://example.com/).");
		mark.executeMark(lineHolder);
		System.out.println(lineHolder.getLine());
	}

}
