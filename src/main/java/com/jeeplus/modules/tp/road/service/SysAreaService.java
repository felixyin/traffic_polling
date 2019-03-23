/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.modules.tp.road.service;

import java.util.List;

import com.jeeplus.modules.tp.road.entity.TpRoad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.service.TreeService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.tp.road.entity.SysArea;
import com.jeeplus.modules.tp.road.mapper.SysAreaMapper;

/**
 * 路口Service
 * @author 尹彬
 * @version 2018-12-20
 */
@Service
@Transactional(readOnly = true)
public class SysAreaService extends TreeService<SysAreaMapper, SysArea> {

	@Autowired
	private SysAreaMapper sysAreaMapper;

	public SysArea get(String id) {
		return super.get(id);
	}
	
	public List<SysArea> findList(SysArea sysArea) {
		if (StringUtils.isNotBlank(sysArea.getParentIds())){
			sysArea.setParentIds(","+sysArea.getParentIds()+",");
		}
		return super.findList(sysArea);
	}
	
	@Transactional(readOnly = false)
	public void save(SysArea sysArea) {
		super.save(sysArea);
	}
	
	@Transactional(readOnly = false)
	public void delete(SysArea sysArea) {
		super.delete(sysArea);
	}


	public List<SysArea> findByName(String name) {
		return sysAreaMapper.getByName(name);
	}
}