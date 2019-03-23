/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.modules.tp.road.entity;

import org.hibernate.validator.constraints.Length;
import com.jeeplus.modules.tp.road.entity.SysArea;
import javax.validation.constraints.NotNull;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 道路Entity
 * @author 尹彬
 * @version 2018-12-20
 */
public class TpRoad extends DataEntity<TpRoad> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 道路名称
	private SysArea area;		// 所属区域 父类
	private String roadType;		// 道路类型
	private Integer length;		// 道路长度(m)
	private Integer width;		// 道路宽度(m)
	private Integer acreage;		// 占地面积(m2)
	
	public TpRoad() {
		super();
	}

	public TpRoad(String id){
		super(id);
	}

	public TpRoad(SysArea area){
		this.area = area;
	}

	@Length(min=3, max=100, message="道路名称长度必须介于 3 和 100 之间")
	@ExcelField(title="道路名称", align=2, sort=6)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@NotNull(message="所属区域不能为空")
	public SysArea getArea() {
		return area;
	}

	public void setArea(SysArea area) {
		this.area = area;
	}
	
	@ExcelField(title="道路类型", dictType="road_type", align=2, sort=8)
	public String getRoadType() {
		return roadType;
	}

	public void setRoadType(String roadType) {
		this.roadType = roadType;
	}
	
	@ExcelField(title="道路长度(m)", align=2, sort=9)
	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}
	
	@ExcelField(title="道路宽度(m)", align=2, sort=10)
	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}
	
	@ExcelField(title="占地面积(m2)", align=2, sort=11)
	public Integer getAcreage() {
		return acreage;
	}

	public void setAcreage(Integer acreage) {
		this.acreage = acreage;
	}
	
}