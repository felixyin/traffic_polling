/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.modules.test.grid.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.test.grid.entity.TestCountry;

/**
 * 国家MAPPER接口
 * @author lgf
 * @version 2018-04-10
 */
@MyBatisMapper
public interface TestCountryMapper extends BaseMapper<TestCountry> {
	
}