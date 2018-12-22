/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tp.maintenance.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.tp.maintenance.entity.TpMaintenance;
import com.jeeplus.modules.tp.maintenance.mapper.TpMaintenanceMapper;
import com.jeeplus.modules.tp.maintenance.entity.TpMaintenanceItem;
import com.jeeplus.modules.tp.maintenance.mapper.TpMaintenanceItemMapper;

/**
 * 施工Service
 * @author 尹彬
 * @version 2018-12-22
 */
@Service
@Transactional(readOnly = true)
public class TpMaintenanceService extends CrudService<TpMaintenanceMapper, TpMaintenance> {

	@Autowired
	private TpMaintenanceItemMapper tpMaintenanceItemMapper;
	
	public TpMaintenance get(String id) {
		TpMaintenance tpMaintenance = super.get(id);
		tpMaintenance.setTpMaintenanceItemList(tpMaintenanceItemMapper.findList(new TpMaintenanceItem(tpMaintenance)));
		return tpMaintenance;
	}
	
	public List<TpMaintenance> findList(TpMaintenance tpMaintenance) {
		return super.findList(tpMaintenance);
	}
	
	public Page<TpMaintenance> findPage(Page<TpMaintenance> page, TpMaintenance tpMaintenance) {
		return super.findPage(page, tpMaintenance);
	}
	
	@Transactional(readOnly = false)
	public void save(TpMaintenance tpMaintenance) {
		super.save(tpMaintenance);
		for (TpMaintenanceItem tpMaintenanceItem : tpMaintenance.getTpMaintenanceItemList()){
			if (tpMaintenanceItem.getId() == null){
				continue;
			}
			if (TpMaintenanceItem.DEL_FLAG_NORMAL.equals(tpMaintenanceItem.getDelFlag())){
				if (StringUtils.isBlank(tpMaintenanceItem.getId())){
					tpMaintenanceItem.setMaintenance(tpMaintenance);
					tpMaintenanceItem.preInsert();
					tpMaintenanceItemMapper.insert(tpMaintenanceItem);
				}else{
					tpMaintenanceItem.preUpdate();
					tpMaintenanceItemMapper.update(tpMaintenanceItem);
				}
			}else{
				tpMaintenanceItemMapper.delete(tpMaintenanceItem);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(TpMaintenance tpMaintenance) {
		super.delete(tpMaintenance);
		tpMaintenanceItemMapper.delete(new TpMaintenanceItem(tpMaintenance));
	}
	
}