/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.modules.tp.dam.entity;

import com.jeeplus.modules.tp.road.entity.SysArea;
import javax.validation.constraints.NotNull;
import com.jeeplus.modules.tp.roadcross.entity.TpRoadCrossing;
import com.jeeplus.modules.tp.road.entity.TpRoad;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 资产Entity
 * @author 尹彬
 * @version 2019-01-09
 */
public class TpDam extends DataEntity<TpDam> {
	
	private static final long serialVersionUID = 1L;
	private String damType;		// 资产类型
	private String description;		// 资产描述
	private String pic;		// 图片
	private String size;		// 尺寸
	private String location;		// 经纬度
	private SysArea area;		// 所属区域
	private TpRoadCrossing roadcross;		// 所属路口
	private String nearestJunction;		// 所属路口相对位置
	private TpRoad road;		// 所属道路
	private String address;		// 搜索用地址
	private String nearestPoi;		// 搜索地址相对位置
	
	public TpDam() {
		super();
	}

	public TpDam(String id){
		super(id);
	}

	@ExcelField(title="资产类型", dictType="dam_type", align=2, sort=6)
	public String getDamType() {
		return damType;
	}

	public void setDamType(String damType) {
		this.damType = damType;
	}
	
	@ExcelField(title="资产描述", align=2, sort=7)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@ExcelField(title="图片", align=2, sort=8)
	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}
	
	@ExcelField(title="尺寸", align=2, sort=9)
	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}
	
	@ExcelField(title="经纬度", align=2, sort=10)
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	@NotNull(message="所属区域不能为空")
	@ExcelField(title="所属区域", fieldType=SysArea.class, value="area.name", align=2, sort=11)
	public SysArea getArea() {
		return area;
	}

	public void setArea(SysArea area) {
		this.area = area;
	}
	
	@NotNull(message="所属路口不能为空")
	@ExcelField(title="所属路口", fieldType=TpRoadCrossing.class, value="roadcross.name", align=2, sort=12)
	public TpRoadCrossing getRoadcross() {
		return roadcross;
	}

	public void setRoadcross(TpRoadCrossing roadcross) {
		this.roadcross = roadcross;
	}
	
	@ExcelField(title="所属路口相对位置", align=2, sort=13)
	public String getNearestJunction() {
		return nearestJunction;
	}

	public void setNearestJunction(String nearestJunction) {
		this.nearestJunction = nearestJunction;
	}
	
	@NotNull(message="所属道路不能为空")
	@ExcelField(title="所属道路", fieldType=TpRoad.class, value="road.name", align=2, sort=14)
	public TpRoad getRoad() {
		return road;
	}

	public void setRoad(TpRoad road) {
		this.road = road;
	}
	
	@ExcelField(title="搜索用地址", align=2, sort=15)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@ExcelField(title="搜索地址相对位置", align=2, sort=16)
	public String getNearestPoi() {
		return nearestPoi;
	}

	public void setNearestPoi(String nearestPoi) {
		this.nearestPoi = nearestPoi;
	}
	
}