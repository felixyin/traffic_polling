/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.modules.tp.road.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.core.persistence.TreeEntity;

/**
 * 路口Entity
 * @author 尹彬
 * @version 2018-12-20
 */
public class SysArea extends TreeEntity<SysArea> {
	
	private static final long serialVersionUID = 1L;
	private String code;		// 区域编码
	private String type;		// 区域类型
	
	private List<TpRoad> tpRoadList = Lists.newArrayList();		// 子表列表
	
	public SysArea() {
		super();
	}

	public SysArea(String id){
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
	
	public  SysArea getParent() {
			return parent;
	}
	
	@Override
	public void setParent(SysArea parent) {
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
}