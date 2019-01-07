/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tp.gpshistory.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.tp.gpshistory.entity.TpGpsHistory;
import com.jeeplus.modules.tp.gpshistory.mapper.TpGpsHistoryMapper;

/**
 * 历史轨迹Service
 * @author 尹彬
 * @version 2019-01-06
 */
@Service
@Transactional(readOnly = true)
public class TpGpsHistoryService extends CrudService<TpGpsHistoryMapper, TpGpsHistory> {

	@Autowired
	private  TpGpsHistoryMapper tpGpsHistoryMapper;

	public TpGpsHistory get(String id) {
		return super.get(id);
	}
	
	public List<TpGpsHistory> findList(TpGpsHistory tpGpsHistory) {
		return super.findList(tpGpsHistory);
	}
	
	public Page<TpGpsHistory> findPage(Page<TpGpsHistory> page, TpGpsHistory tpGpsHistory) {
		return super.findPage(page, tpGpsHistory);
	}
	
	@Transactional(readOnly = false)
	public void save(TpGpsHistory tpGpsHistory) {
		super.save(tpGpsHistory);
	}
	
	@Transactional(readOnly = false)
	public void delete(TpGpsHistory tpGpsHistory) {
		super.delete(tpGpsHistory);
	}

    public List<TpGpsHistory> findListByCarTrackId(String carTrackId) {
		return tpGpsHistoryMapper.findListByCarTrackId(carTrackId);
    }
}