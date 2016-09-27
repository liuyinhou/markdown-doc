package com.get.markdown.core.mark;

import com.get.markdown.core.vo.LineHolder;

/**
 * 
 * @author liuyinhou
 *
 */
public class BlankMark implements Mark {

	@Override
	public void executeMark(LineHolder lineHolder) {
		String line = lineHolder.getLine();
		if ("".equals(line.trim())) {
			if (lineHolder.getIsInPline() == null) {
				lineHolder.setIsInPline(false);
			} else if (lineHolder.getIsInPline()) {
				lineHolder.setIsInPline(false);
				String preLine = lineHolder.getPreLine();
				lineHolder.setPreLine(preLine + "</p>");
			}
		} else {
			if (lineHolder.getIsInPline() == null
					|| !lineHolder.getIsInPline()) {
				line = "<p>" + line;
				lineHolder.setIsInPline(true);
			}
		}
		lineHolder.setLine(line);
	}
	
}
