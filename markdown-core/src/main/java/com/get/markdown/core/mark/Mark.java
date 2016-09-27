package com.get.markdown.core.mark;

import com.get.markdown.core.vo.LineHolder;

public interface Mark {

	/**
	 * 处理markdown的标记
	 * @return
	 */
	public void executeMark(LineHolder lineHolder);
	
}
