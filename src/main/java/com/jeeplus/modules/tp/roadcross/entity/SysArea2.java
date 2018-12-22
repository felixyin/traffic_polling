/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tp.roadcross.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.core.persistence.TreeEntity;

/**
 * 路口Entity
 * @author 尹彬
 * @version 2018-12-22
 */
public class SysArea2 extends TreeEntity<SysArea2> {
	
	private static final long serialVersionUID = 1L;
	private String code;		// 区域编码
	private String type;		// 区域类型
	
	private List<TpRoadCrossing> tpRoadCrossingList = Lists.newArrayList();		// 子表列表
	
	public SysArea2() {
		super();
	}

	public SysArea2(String id){
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
	
	public  SysArea2 getParent() {
			return parent;
	}
	
	@Override
	public void setParent(SysArea2 parent) {
		this.parent = parent;
		
	}
	
	public List<TpRoadCrossing> getTpRoadCrossingList() {
		return tpRoadCrossingList;
	}

	public void setTpRoadCrossingList(List<TpRoadCrossing> tpRoadCrossingList) {
		this.tpRoadCrossingList = tpRoadCrossingList;
	}
	public String getParentId() {
		return parent != null && parent.getId() != null ? parent.getId() : "0";
	}
}