/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tp.markline.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.tp.markline.entity.TpMarkline;
import com.jeeplus.modules.tp.markline.mapper.TpMarklineMapper;

/**
 * 标线Service
 * @author 尹彬
 * @version 2018-12-19
 */
@Service
@Transactional(readOnly = true)
public class TpMarklineService extends CrudService<TpMarklineMapper, TpMarkline> {

	public TpMarkline get(String id) {
		return super.get(id);
	}
	
	public List<TpMarkline> findList(TpMarkline tpMarkline) {
		return super.findList(tpMarkline);
	}
	
	public Page<TpMarkline> findPage(Page<TpMarkline> page, TpMarkline tpMarkline) {
		return super.findPage(page, tpMarkline);
	}
	
	@Transactional(readOnly = false)
	public void save(TpMarkline tpMarkline) {
		super.save(tpMarkline);
	}
	
	@Transactional(readOnly = false)
	public void delete(TpMarkline tpMarkline) {
		super.delete(tpMarkline);
	}
	
}