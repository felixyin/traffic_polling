/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.modules.tp.material.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.tp.material.entity.TpMaterialPart;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 物料零件MAPPER接口
 *
 * @author 尹彬
 * @version 2018-12-23
 */
@MyBatisMapper
public interface TpMaterialPartMapper extends BaseMapper<TpMaterialPart> {

    List<Map<String, String>> autocomplete(@Param("query") String query);

    List<TpMaterialPart> findListByMaintenance(@Param("maintenanceId") String maintenanceId);

}