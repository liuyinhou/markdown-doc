package com.get.markdown.core.mark;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.get.markdown.core.vo.LineHolder;
import com.get.markdown.core.vo.UlListVo;

/**
 * * + - 行
 * @author liuyinhou
 *
 */
public class UlListMark implements Mark {

	private static String PATTERN_STR = "^( *[*|-] )\\S.*";

	@Override
	public void executeMark(LineHolder lineHolder) {
		String line = lineHolder.getLine();
		Pattern pattern = Pattern.compile(PATTERN_STR);
		Matcher matcher = pattern.matcher(line);
		if (matcher.matches()) {
			StringBuilder strBuilder = new StringBuilder();
			MatchResult mResult = matcher.toMatchResult();
			UlListVo ulListVo = lineHolder.getUlListVo();
	        if (ulListVo == null) {
	        	ulListVo = new UlListVo();
	        	lineHolder.setUlListVo(ulListVo);
	        }
	        String space = mResult.group(1);
	        //列表以1个空格为一个级别
	        int spaceSize = 0;
	        if (space != null && !"".equals(space)) {
	        	spaceSize = space.length();
	        	line = line.substring(space.length());
	        }
	        int currentLevel = 1;
	        if (spaceSize > ulListVo.getPreSpace()) {
	        	//增加一层级别
	        	currentLevel = ulListVo.getPreLevel() + 1;
	        	ulListVo.getSpaceLevel().put(spaceSize, currentLevel);
	        	strBuilder.append("<ul><li>").append(line);
	        } else if (spaceSize == ulListVo.getPreSpace()) {
	        	//同一层级别
	        	currentLevel = ulListVo.getPreLevel();
	        	strBuilder.append("</li><li>").append(line);
	        } else {
	        	//降低N层级别(N>=1)
	        	for (int i = spaceSize; i>=0;i--) {
		        	Integer level = ulListVo.getSpaceLevel().get(i);
		        	if (level != null) {
		        		currentLevel = level;
		        		break;
		        	}
		        }
	        	int reduceLevel = ulListVo.getPreLevel() - currentLevel;
	        	for (int i=0;i<reduceLevel;i++) {
	        		strBuilder.append("</li></ul>");
	        	}
	        	strBuilder.append("<li>").append(line);
	        }
	        
	        ulListVo.setPreSpace(spaceSize);
	        ulListVo.setPreLevel(currentLevel);
	        lineHolder.setLine(strBuilder.toString());
		} else if (lineHolder.getUlListVo() != null){
			//说明上一行是ul标记，需要做一个结尾
			StringBuilder strBuilder = new StringBuilder();
			UlListVo ulListVo = lineHolder.getUlListVo();
			int reduceLevel = ulListVo.getPreLevel();
        	for (int i=0;i<=reduceLevel;i++) {
        		strBuilder.append("</li></ul>");
        	}
        	strBuilder.append(line);
			lineHolder.setUlListVo(null);
			lineHolder.setLine(strBuilder.toString());
		}
	}
	
}
