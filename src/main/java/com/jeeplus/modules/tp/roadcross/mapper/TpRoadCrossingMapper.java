/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tp.roadcross.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.tp.roadcross.entity.TpRoadCrossing;

import java.util.List;

/**
 * 路口管理MAPPER接口
 * @author 尹彬
 * @version 2018-12-22
 */
@MyBatisMapper
public interface TpRoadCrossingMapper extends BaseMapper<TpRoadCrossing> {

    List<TpRoadCrossing> findByName(String fullName);
}