/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.core.persistence;

import java.util.HashMap;

/**
 * 查询参数类
 * @author jeeplus
 * @version 2016-8-23
 */
public class Parameter extends HashMap<String, Object> {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 构造类，例：new Parameter(id, parentIds)
	 * @param values 参数值
	 */
	public Parameter(Object... values) {
		if (values != null){
			for (int i=0; i<values.length; i++){
				put("p"+(i+1), values[i]);
			}
		}
	}
	
	/**
	 * 构造类，例：new Parameter(new Object[][]{{"id", id}, {"parentIds", parentIds}})
	 * @param parameters 参数二维数组
	 */
	public Parameter(Object[][] parameters) {
		if (parameters != null){
			for (Object[] os : parameters){
				if (os.length == 2){
					put((String)os[0], os[1]);
				}
			}
		}
	}
	
}
