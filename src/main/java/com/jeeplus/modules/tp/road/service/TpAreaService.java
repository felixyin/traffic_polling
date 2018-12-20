/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tp.road.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.service.TreeService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.tp.road.entity.TpArea;
import com.jeeplus.modules.tp.road.mapper.TpAreaMapper;

/**
 * 道路Service
 * @author 尹彬
 * @version 2018-12-19
 */
@Service
@Transactional(readOnly = true)
public class TpAreaService extends TreeService<TpAreaMapper, TpArea> {

	public TpArea get(String id) {
		return super.get(id);
	}
	
	public List<TpArea> findList(TpArea tpArea) {
		if (StringUtils.isNotBlank(tpArea.getParentIds())){
			tpArea.setParentIds(","+tpArea.getParentIds()+",");
		}
		return super.findList(tpArea);
	}
	
	@Transactional(readOnly = false)
	public void save(TpArea tpArea) {
		super.save(tpArea);
	}
	
	@Transactional(readOnly = false)
	public void delete(TpArea tpArea) {
		super.delete(tpArea);
	}
	
}