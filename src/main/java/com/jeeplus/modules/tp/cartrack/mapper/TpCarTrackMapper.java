/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.modules.tp.cartrack.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.tp.cartrack.entity.TpCarTrack;
import org.apache.ibatis.annotations.Param;

/**
 * 出车记录MAPPER接口
 *
 * @author 尹彬
 * @version 2019-01-05
 */
@MyBatisMapper
public interface TpCarTrackMapper extends BaseMapper<TpCarTrack> {

    TpCarTrack loadCarTrack(@Param("carName") String carName, @Param("startTime") String startTime);
}