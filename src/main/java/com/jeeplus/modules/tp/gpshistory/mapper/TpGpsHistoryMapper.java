/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.modules.tp.gpshistory.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.tp.gpshistory.entity.TpGpsHistory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 历史轨迹MAPPER接口
 * @author 尹彬
 * @version 2019-01-06
 */
@MyBatisMapper
public interface TpGpsHistoryMapper extends BaseMapper<TpGpsHistory> {

    List<TpGpsHistory> findListByCarTrackId(@Param("carTrackId") String carTrackId);

    void deleteByCarTrackId(@Param("carTrackId") String carTrackId);
}