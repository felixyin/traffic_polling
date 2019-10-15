/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.modules.tp.road.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.tp.road.entity.TpRoad;

import java.util.List;

/**
 * 道路MAPPER接口
 * @author 尹彬
 * @version 2018-12-20
 */
@MyBatisMapper
public interface TpRoadMapper extends BaseMapper<TpRoad> {

    List<TpRoad> findByName(String roadName);
}