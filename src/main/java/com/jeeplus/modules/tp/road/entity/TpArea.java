/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tp.road.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import com.jeeplus.core.persistence.TreeEntity;

/**
 * 道路Entity
 * @author 尹彬
 * @version 2018-12-19
 */
public class TpArea extends TreeEntity<TpArea> {
	
	private static final long serialVersionUID = 1L;
	private String acode;		// 区域代码
	
	
	public TpArea() {
		super();
	}

	public TpArea(String id){
		super(id);
	}

	public String getAcode() {
		return acode;
	}

	public void setAcode(String acode) {
		this.acode = acode;
	}
	
	public  TpArea getParent() {
			return parent;
	}
	
	@Override
	public void setParent(TpArea parent) {
		this.parent = parent;
		
	}
	
	public String getParentId() {
		return parent != null && parent.getId() != null ? parent.getId() : "0";
	}
}