/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tp.road.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.core.persistence.TreeEntity;
import com.jeeplus.modules.tp.road.service.MySysAreaService;
import com.jeeplus.modules.tp.road.service.TpRoadService;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

/**
 * 道路Entity
 * @author 尹彬
 * @version 2018-12-19
 */
public class MySysArea extends TreeEntity<MySysArea> {
	
	private static final long serialVersionUID = 1L;
	private String code;		// 区域编码
	private String type;		// 区域类型
	
	private List<TpRoad> tpRoadList = Lists.newArrayList();		// 子表列表
	
	public MySysArea() {
		super();
	}

	public MySysArea(String id){
		super(id);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public  MySysArea getParent() {
			return parent;
	}
	
	@Override
	public void setParent(MySysArea parent) {
		this.parent = parent;
		
	}
	
	public List<TpRoad> getTpRoadList() {
		return tpRoadList;
	}

	public void setTpRoadList(List<TpRoad> tpRoadList) {
		this.tpRoadList = tpRoadList;
	}
	public String getParentId() {
		return parent != null && parent.getId() != null ? parent.getId() : "0";
	}


	public MySysArea getValue(String name) throws Exception {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		MySysAreaService service = wac.getBean(MySysAreaService.class);
		MySysArea param = new MySysArea();
		param.setName(name);
		MySysArea tpRoad = service.getByName(name);
		if (tpRoad == null) {
			param.setId("");
			// FIXME 界面显示了一个-，能否跟新建的数据一样呢？
			param.setRemarks("");
			return param;//            throw new Exception("路口不存在：" + name);
		} else {
			return tpRoad;
		}
	}
}