/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tp.road.mapper;

import com.jeeplus.core.persistence.TreeMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.tp.road.entity.MySysArea;

/**
 * 道路MAPPER接口
 * @author 尹彬
 * @version 2018-12-19
 */
@MyBatisMapper
public interface MySysAreaMapper extends TreeMapper<MySysArea> {

    MySysArea getByName(String name);
}