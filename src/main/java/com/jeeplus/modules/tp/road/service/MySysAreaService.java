/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tp.road.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.service.TreeService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.tp.road.entity.MySysArea;
import com.jeeplus.modules.tp.road.mapper.MySysAreaMapper;

/**
 * 道路Service
 * @author 尹彬
 * @version 2018-12-19
 */
@Service
@Transactional(readOnly = true)
public class MySysAreaService extends TreeService<MySysAreaMapper, MySysArea> {

	@Autowired
	private MySysAreaMapper mySysAreaMapper;

	public MySysArea get(String id) {
		return super.get(id);
	}
	
	public List<MySysArea> findList(MySysArea mySysArea) {
		if (StringUtils.isNotBlank(mySysArea.getParentIds())){
			mySysArea.setParentIds(","+mySysArea.getParentIds()+",");
		}
		return super.findList(mySysArea);
	}
	
	@Transactional(readOnly = false)
	public void save(MySysArea mySysArea) {
		super.save(mySysArea);
	}
	
	@Transactional(readOnly = false)
	public void delete(MySysArea mySysArea) {
		super.delete(mySysArea);
	}

    public MySysArea getByName(String name) {
		return mySysAreaMapper.getByName(name);
    }
}