/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tp.guardrail.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.tp.guardrail.entity.TpGuardrail;
import com.jeeplus.modules.tp.guardrail.mapper.TpGuardrailMapper;

/**
 * 护栏Service
 * @author 尹彬
 * @version 2018-12-19
 */
@Service
@Transactional(readOnly = true)
public class TpGuardrailService extends CrudService<TpGuardrailMapper, TpGuardrail> {

	public TpGuardrail get(String id) {
		return super.get(id);
	}
	
	public List<TpGuardrail> findList(TpGuardrail tpGuardrail) {
		return super.findList(tpGuardrail);
	}
	
	public Page<TpGuardrail> findPage(Page<TpGuardrail> page, TpGuardrail tpGuardrail) {
		return super.findPage(page, tpGuardrail);
	}
	
	@Transactional(readOnly = false)
	public void save(TpGuardrail tpGuardrail) {
		super.save(tpGuardrail);
	}
	
	@Transactional(readOnly = false)
	public void delete(TpGuardrail tpGuardrail) {
		super.delete(tpGuardrail);
	}
	
}