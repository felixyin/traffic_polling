/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.modules.test.manytoone.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import com.jeeplus.core.persistence.TreeEntity;

/**
 * 商品类型Entity
 * @author liugf
 * @version 2018-06-12
 */
public class Category extends TreeEntity<Category> {
	
	private static final long serialVersionUID = 1L;
	
	
	public Category() {
		super();
	}

	public Category(String id){
		super(id);
	}

	public  Category getParent() {
			return parent;
	}
	
	@Override
	public void setParent(Category parent) {
		this.parent = parent;
		
	}
	
	public String getParentId() {
		return parent != null && parent.getId() != null ? parent.getId() : "0";
	}
}