/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.modules.tp.maintenance.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.tp.maintenance.entity.TpMaintenanceItem;
import com.jeeplus.modules.tp.maintenance.mapper.TpMaintenanceItemMapper;

/**
 * 施工物料Service
 * @author 尹彬
 * @version 2018-12-21
 */
@Service
@Transactional(readOnly = true)
public class TpMaintenanceItemService extends CrudService<TpMaintenanceItemMapper, TpMaintenanceItem> {

	public TpMaintenanceItem get(String id) {
		return super.get(id);
	}
	
	public List<TpMaintenanceItem> findList(TpMaintenanceItem tpMaintenanceItem) {
		return super.findList(tpMaintenanceItem);
	}
	
	public Page<TpMaintenanceItem> findPage(Page<TpMaintenanceItem> page, TpMaintenanceItem tpMaintenanceItem) {
		return super.findPage(page, tpMaintenanceItem);
	}
	
	@Transactional(readOnly = false)
	public void save(TpMaintenanceItem tpMaintenanceItem) {
		super.save(tpMaintenanceItem);
	}
	
	@Transactional(readOnly = false)
	public void delete(TpMaintenanceItem tpMaintenanceItem) {
		super.delete(tpMaintenanceItem);
	}
	
}