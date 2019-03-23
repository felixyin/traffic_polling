/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.modules.test.grid.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 洲Entity
 * @author lgf
 * @version 2018-06-12
 */
public class TestContinent extends DataEntity<TestContinent> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 洲名
	
	public TestContinent() {
		super();
	}

	public TestContinent(String id){
		super(id);
	}

	@ExcelField(title="洲名", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}