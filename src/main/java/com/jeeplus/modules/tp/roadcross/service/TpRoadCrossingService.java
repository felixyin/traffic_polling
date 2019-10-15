/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.modules.tp.roadcross.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.tp.roadcross.entity.TpRoadCrossing;
import com.jeeplus.modules.tp.roadcross.mapper.TpRoadCrossingMapper;

/**
 * 路口管理Service
 * @author 尹彬
 * @version 2018-12-22
 */
@Service
@Transactional(readOnly = true)
public class TpRoadCrossingService extends CrudService<TpRoadCrossingMapper, TpRoadCrossing> {

	@Autowired
	private TpRoadCrossingMapper tpRoadCrossingMapper;

	public TpRoadCrossing get(String id) {
		return super.get(id);
	}
	
	public List<TpRoadCrossing> findList(TpRoadCrossing tpRoadCrossing) {
		return super.findList(tpRoadCrossing);
	}
	
	public Page<TpRoadCrossing> findPage(Page<TpRoadCrossing> page, TpRoadCrossing tpRoadCrossing) {
		return super.findPage(page, tpRoadCrossing);
	}
	
	@Transactional(readOnly = false)
	public void save(TpRoadCrossing tpRoadCrossing) {
		super.save(tpRoadCrossing);
	}
	
	@Transactional(readOnly = false)
	public void delete(TpRoadCrossing tpRoadCrossing) {
		super.delete(tpRoadCrossing);
	}

    public List<TpRoadCrossing> findByName(String fullName) {
		return tpRoadCrossingMapper.findByName(fullName);
    }
}