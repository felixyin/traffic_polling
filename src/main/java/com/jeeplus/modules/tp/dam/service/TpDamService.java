/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.modules.tp.dam.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.tp.dam.entity.TpDam;
import com.jeeplus.modules.tp.dam.mapper.TpDamMapper;

/**
 * 资产Service
 * @author 尹彬
 * @version 2019-01-09
 */
@Service
@Transactional(readOnly = true)
public class TpDamService extends CrudService<TpDamMapper, TpDam> {

	public TpDam get(String id) {
		return super.get(id);
	}
	
	public List<TpDam> findList(TpDam tpDam) {
		return super.findList(tpDam);
	}
	
	public Page<TpDam> findPage(Page<TpDam> page, TpDam tpDam) {
		return super.findPage(page, tpDam);
	}
	
	@Transactional(readOnly = false)
	public void save(TpDam tpDam) {
		super.save(tpDam);
	}
	
	@Transactional(readOnly = false)
	public void delete(TpDam tpDam) {
		super.delete(tpDam);
	}
	
}