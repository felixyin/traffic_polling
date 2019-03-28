/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.modules.tp.cartrack.service;

import java.util.List;

import com.jeeplus.modules.tp.gpshistory.service.TpGpsHistoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.tp.cartrack.entity.TpCarTrack;
import com.jeeplus.modules.tp.cartrack.mapper.TpCarTrackMapper;

import javax.annotation.Resource;

/**
 * 出车记录Service
 * @author 尹彬
 * @version 2019-01-05
 */
@Service
@Transactional(readOnly = true)
public class TpCarTrackService extends CrudService<TpCarTrackMapper, TpCarTrack> {

	@Resource
	private TpCarTrackMapper carTrackMapper;

	@Resource
	private TpGpsHistoryService gpsHistoryService;

	public TpCarTrack get(String id) {
		return super.get(id);
	}
	
	public List<TpCarTrack> findList(TpCarTrack tpCarTrack) {
		return super.findList(tpCarTrack);
	}
	
	public Page<TpCarTrack> findPage(Page<TpCarTrack> page, TpCarTrack tpCarTrack) {
		return super.findPage(page, tpCarTrack);
	}
	
	@Transactional(readOnly = false)
	public void save(TpCarTrack tpCarTrack) {
		super.save(tpCarTrack);
	}
	
	@Transactional(readOnly = false)
	public void delete(TpCarTrack tpCarTrack) {
		super.delete(tpCarTrack);
		gpsHistoryService.deleteByCarTrack(tpCarTrack);
	}

    public TpCarTrack loadCarTrack(String carName, String startTime) {
		return carTrackMapper.loadCarTrack(carName, startTime);
    }
}