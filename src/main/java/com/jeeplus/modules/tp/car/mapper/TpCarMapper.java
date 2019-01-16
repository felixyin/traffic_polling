/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tp.car.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.tp.car.entity.TpCar;

import java.util.List;
import java.util.Map;

/**
 * 车辆MAPPER接口
 * @author 尹彬
 * @version 2019-01-16
 */
@MyBatisMapper
public interface TpCarMapper extends BaseMapper<TpCar> {

    List<Map> findAllLocation();
}