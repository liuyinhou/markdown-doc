package com.get.markdown.core.mark;

import com.get.markdown.core.vo.LineHolder;

/**
 * ``` 代码块
 * @author liuyinhou
 *
 */
public class CodeBlockMark implements Mark {

	private static String PATTERN_STR = "```";

	@Override
	public void executeMark(LineHolder lineHolder) {
		String line = lineHolder.getLine();
		if (PATTERN_STR.equals(line)) {
			StringBuilder strBuilder = new StringBuilder();
			if (lineHolder.getIsInCode()) {
				//结束上面的code代码块
				lineHolder.setIsInCode(false);
				strBuilder.append("\n</code></pre>");
			} else {
				//开始一个code代码块
				lineHolder.setIsInCode(true);
				strBuilder.append("<pre><code>");
			}
			lineHolder.setLine(strBuilder.toString());
		}
	}

}
