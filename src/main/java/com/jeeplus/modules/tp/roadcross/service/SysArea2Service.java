/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.modules.tp.roadcross.service;

import java.util.List;

import com.jeeplus.modules.tp.road.entity.SysArea;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.service.TreeService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.tp.roadcross.mapper.SysArea2Mapper;

/**
 * 路口Service
 * @author 尹彬
 * @version 2018-12-22
 */
@Service
@Transactional(readOnly = true)
public class SysArea2Service extends TreeService<SysArea2Mapper, SysArea> {

	public SysArea get(String id) {
		return super.get(id);
	}
	
	public List<SysArea> findList(SysArea sysArea2) {
		if (StringUtils.isNotBlank(sysArea2.getParentIds())){
			sysArea2.setParentIds(","+sysArea2.getParentIds()+",");
		}
		return super.findList(sysArea2);
	}
	
	@Transactional(readOnly = false)
	public void save(SysArea sysArea2) {
		super.save(sysArea2);
	}
	
	@Transactional(readOnly = false)
	public void delete(SysArea sysArea2) {
		super.delete(sysArea2);
	}
	
}