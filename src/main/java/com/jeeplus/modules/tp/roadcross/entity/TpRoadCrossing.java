/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tp.roadcross.entity;

import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.tp.roadcross.entity.SysArea2;
import javax.validation.constraints.NotNull;
import com.jeeplus.modules.tp.road.entity.TpRoad;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

import java.util.ArrayList;
import java.util.List;

/**
 * 路口管理Entity
 * @author 尹彬
 * @version 2018-12-22
 */
public class TpRoadCrossing extends DataEntity<TpRoadCrossing> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 全称
	private SysArea2 sarea;		// 所属区域 父类
	private TpRoad tpRoad1;		// 道路1
	private TpRoad tpRoad2;		// 道路2
	private TpRoad tpRoad3;		// 道路3
	private TpRoad tpRoad4;		// 道路4
	private Double lng;		// 经度
	private Double lat;		// 维度
	
	public TpRoadCrossing() {
		super();
	}

	public TpRoadCrossing(String id){
		super(id);
	}

	public TpRoadCrossing(SysArea2 sarea){
		this.sarea = sarea;
	}


	@ExcelField(title="全称", align=2, sort=14)
	public String getName() {
		if (StringUtils.isNoneBlank(this.name))return this.name;
		List<String> list = new ArrayList<String>();
		if(tpRoad3!=null)
			list.add(tpRoad1.getName());
		if(tpRoad3!=null)
			list.add(tpRoad2.getName());
		if (tpRoad3 != null)
			list.add(tpRoad3.getName());
		if (tpRoad4 != null)
			list.add(tpRoad4.getName());
		StringBuffer sb = new StringBuffer();
		for (int i= 0;i<list.size();i++) {
			String s = list.get(i);
			if (StringUtils.isNotBlank(s)) {
				sb.append(s);
				if(i < list.size()-2){
					sb.append("与");
				}
			}
		}
		if(StringUtils.isNotBlank(sb.toString())){
			sb.append("路口");
		}
		return sb.toString();
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@NotNull(message="所属区域不能为空")
	public SysArea2 getSarea() {
		return sarea;
	}

	public void setSarea(SysArea2 sarea) {
		this.sarea = sarea;
	}
	
	@NotNull(message="道路1不能为空")
	@ExcelField(title="道路1", fieldType=TpRoad.class, value="tpRoad1.name", align=2, sort=8)
	public TpRoad getTpRoad1() {
		return tpRoad1;
	}

	public void setTpRoad1(TpRoad tpRoad1) {
		this.tpRoad1 = tpRoad1;
	}
	
	@NotNull(message="道路2不能为空")
	@ExcelField(title="道路2", fieldType=TpRoad.class, value="tpRoad2.name", align=2, sort=9)
	public TpRoad getTpRoad2() {
		return tpRoad2;
	}

	public void setTpRoad2(TpRoad tpRoad2) {
		this.tpRoad2 = tpRoad2;
	}
	
	@ExcelField(title="道路3", fieldType=TpRoad.class, value="tpRoad3.name", align=2, sort=10)
	public TpRoad getTpRoad3() {
		return tpRoad3;
	}

	public void setTpRoad3(TpRoad tpRoad3) {
		this.tpRoad3 = tpRoad3;
	}
	
	@ExcelField(title="道路4", fieldType=TpRoad.class, value="tpRoad4.name", align=2, sort=11)
	public TpRoad getTpRoad4() {
		return tpRoad4;
	}

	public void setTpRoad4(TpRoad tpRoad4) {
		this.tpRoad4 = tpRoad4;
	}
	
	@NotNull(message="经度不能为空")
	@ExcelField(title="经度", align=2, sort=12)
	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}
	
	@NotNull(message="维度不能为空")
	@ExcelField(title="维度", align=2, sort=13)
	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}
	
}