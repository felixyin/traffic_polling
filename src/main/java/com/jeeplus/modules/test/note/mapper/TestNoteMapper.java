/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.modules.test.note.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.test.note.entity.TestNote;

/**
 * 富文本测试MAPPER接口
 * @author liugf
 * @version 2018-06-12
 */
@MyBatisMapper
public interface TestNoteMapper extends BaseMapper<TestNote> {
	
}