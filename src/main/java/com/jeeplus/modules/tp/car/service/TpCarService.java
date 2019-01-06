/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tp.car.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.tp.car.entity.TpCar;
import com.jeeplus.modules.tp.car.mapper.TpCarMapper;

/**
 * 车辆Service
 * @author 尹彬
 * @version 2019-01-05
 */
@Service
@Transactional(readOnly = true)
public class TpCarService extends CrudService<TpCarMapper, TpCar> {

	public TpCar get(String id) {
		return super.get(id);
	}
	
	public List<TpCar> findList(TpCar tpCar) {
		return super.findList(tpCar);
	}
	
	public Page<TpCar> findPage(Page<TpCar> page, TpCar tpCar) {
		return super.findPage(page, tpCar);
	}
	
	@Transactional(readOnly = false)
	public void save(TpCar tpCar) {
		super.save(tpCar);
	}
	
	@Transactional(readOnly = false)
	public void delete(TpCar tpCar) {
		super.delete(tpCar);
	}
	
}