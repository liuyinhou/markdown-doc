package com.get.markdown.core.mark;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.get.markdown.core.vo.LineHolder;
import com.get.markdown.core.vo.TableVo;

/**
 * | | 表格 table
 * 
 * @author liuyinhou
 * 
 */
public class TableMark implements Mark {

	private static String PATTERN_STR_HEADER = "^\\| *([:-]+ *\\|)+";
	private static String PATTERN_STR_CONTENT = "^\\|([^\\|]*\\|)+";
	private static String PATTERN_STR_CONTENT_FIND = "([^\\|]+)";

	@Override
	public void executeMark(LineHolder lineHolder) {
		String line = lineHolder.getLine();
		TableVo tableVo = lineHolder.getTableVo();
		if (line.startsWith("|")) {
			if (tableVo == null) {
				Pattern pattern = Pattern.compile(PATTERN_STR_CONTENT);
				Matcher matcher = pattern.matcher(line);
				if (matcher.matches()) {
					// first line of the table
					tableVo = new TableVo();
					tableVo.setIsInTable(true);
					lineHolder.setTableVo(tableVo);
					pattern = Pattern.compile(PATTERN_STR_CONTENT_FIND);
					matcher = pattern.matcher(line.replace("||", "| |"));
					List<String> headerList = tableVo.getHeaderList();
					while (matcher.find()) {
						headerList.add(matcher.group());
					}
					line = "<table>";
				}
			} else if (tableVo.getIsInTable()){
				Pattern pattern = Pattern.compile(PATTERN_STR_HEADER);
				Matcher matcher = pattern.matcher(line);
				if (matcher.matches()) {
					// header of the table
					pattern = Pattern.compile(PATTERN_STR_CONTENT_FIND);
					matcher = pattern.matcher(line.replace("||", "| |"));
					List<String> alignList = tableVo.getAlignList();
					while (matcher.find()) {
						String curGroup = matcher.group();
						if (curGroup.startsWith(":")) {
							if (curGroup.endsWith(":")) {
								alignList.add("center");
							} else {
								alignList.add("left");
							}
						} else {
							if (curGroup.endsWith(":")) {
								alignList.add("right");
							} else {
								alignList.add("");
							}
						}
					}
					// add table header
					StringBuilder strBuilder = new StringBuilder();
					List<String> headerList = tableVo.getHeaderList();
					strBuilder.append("<thead>\n<tr>\n");
					for (int i=0;i<headerList.size();i++) {
						String align = "";
						if (alignList.size()>i) {
							align = alignList.get(i);
						}
						strBuilder.append("<th align=\"").append(align).append("\">");
						strBuilder.append(headerList.get(i)).append("</th>\n");
						
					}
					strBuilder.append("</tr>\n</thead>\n<tbody>");
					line = strBuilder.toString();
				} else {
					// content of the table
					pattern = Pattern.compile(PATTERN_STR_CONTENT_FIND);
					matcher = pattern.matcher(line.replace("||", "| |"));
					List<String> alignList = tableVo.getAlignList();
					int i = 0;
					StringBuilder strBuilder = new StringBuilder();
					// add table content
					strBuilder.append("<tr>\n");
					while (matcher.find()) {
						String align = "";
						if (alignList.size()>i) {
							align = alignList.get(i);
						}
						strBuilder.append("<td align=\"").append(align).append("\">");
						strBuilder.append(matcher.group()).append("</td>\n");
						i++;
					}
					strBuilder.append("</tr>\n");
					line = strBuilder.toString();
				}
			}
		} else {
			if (tableVo != null) {
				lineHolder.setTableVo(null);
				// append ending of the table
				StringBuilder strBuilder = new StringBuilder();
				strBuilder.append("\n</tbody>\n</table>\n").append(line);
				line = strBuilder.toString();
			}
		}
		lineHolder.setLine(line);
	}

}
