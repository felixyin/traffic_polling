/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.modules.tp.gpsrealtime.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.tp.gpsrealtime.entity.TpGpsRealtime;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 实时轨迹MAPPER接口
 * @author 尹彬
 * @version 2019-01-05
 */
@MyBatisMapper
public interface TpGpsRealtimeMapper extends BaseMapper<TpGpsRealtime> {

    List<TpGpsRealtime> findGpsList(@Param("deviceId") String deviceId);

}