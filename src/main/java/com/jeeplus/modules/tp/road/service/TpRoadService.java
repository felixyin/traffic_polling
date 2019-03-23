/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.modules.tp.road.service;

import java.util.List;

import com.jeeplus.modules.tp.road.entity.SysArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.tp.road.entity.TpRoad;
import com.jeeplus.modules.tp.road.mapper.TpRoadMapper;

/**
 * 道路Service
 * @author 尹彬
 * @version 2018-12-20
 */
@Service
@Transactional(readOnly = true)
public class TpRoadService extends CrudService<TpRoadMapper, TpRoad> {

	@Autowired
	private TpRoadMapper tpRoadMapper;

	public TpRoad get(String id) {
		return super.get(id);
	}
	
	public List<TpRoad> findList(TpRoad tpRoad) {
		return super.findList(tpRoad);
	}
	
	public Page<TpRoad> findPage(Page<TpRoad> page, TpRoad tpRoad) {
		return super.findPage(page, tpRoad);
	}
	
	@Transactional(readOnly = false)
	public void save(TpRoad tpRoad) {
		super.save(tpRoad);
	}
	
	@Transactional(readOnly = false)
	public void delete(TpRoad tpRoad) {
		super.delete(tpRoad);
	}

	public List<TpRoad> findByName(String roadName) {
	    return tpRoadMapper.findByName(roadName);
	}
}