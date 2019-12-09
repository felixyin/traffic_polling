/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.modules.tp.maintenance.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.tp.maintenance.entity.TpExportReport;
import com.jeeplus.modules.tp.maintenance.entity.TpMaintenance;

import java.util.List;
import java.util.Map;

/**
 * 施工MAPPER接口
 * @author 尹彬
 * @version 2018-12-22
 */
@MyBatisMapper
public interface TpMaintenanceMapper extends BaseMapper<TpMaintenance> {

    List<TpExportReport> findTongJiList(TpMaintenance tpMaintenance);

}