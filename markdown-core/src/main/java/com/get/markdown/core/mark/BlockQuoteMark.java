package com.get.markdown.core.mark;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.get.markdown.core.vo.BlockQuoteVo;
import com.get.markdown.core.vo.LineHolder;

/**
 * > 引用块
 * @author liuyinhou
 *
 */
public class BlockQuoteMark implements Mark {

	private static String PATTERN_STR = "^( *>)\\S.*";

	@Override
	public void executeMark(LineHolder lineHolder) {
		String line = lineHolder.getLine();
		Pattern pattern = Pattern.compile(PATTERN_STR);
		Matcher matcher = pattern.matcher(line);
		BlockQuoteVo blockQuoteVo = lineHolder.getBlockQuoteVo();
		if (matcher.matches()) {
			StringBuilder strBuilder = new StringBuilder();
			MatchResult mResult = matcher.toMatchResult();
			String space = mResult.group(1);
	        if (blockQuoteVo == null) {
	        	blockQuoteVo = new BlockQuoteVo();
	        	blockQuoteVo.setIsInBlockQuote(true);
	        	lineHolder.setBlockQuoteVo(blockQuoteVo);
	        	strBuilder.append("<blockquote><p>");
	        } else {
	        	strBuilder.append("</p><p>");
	        }
	        blockQuoteVo.setPreBlank(0);
	        strBuilder.append(line.substring(space.length()));
	        lineHolder.setLine(strBuilder.toString());
		} else if (blockQuoteVo != null
				&& blockQuoteVo.getIsInBlockQuote()) {
			if (blockQuoteVo.getPreBlank() == 0) {
				//前面无空行
				if ("".equals(line.trim())) {
					blockQuoteVo.setPreBlank(1);
				}
			} else {
				//前面已经有了空行
				lineHolder.setBlockQuoteVo(null);
				StringBuilder strBuilder = new StringBuilder();
				strBuilder.append("</p></blockquote>").append(line);
				lineHolder.setLine(strBuilder.toString());
			}
		}
	}

}
