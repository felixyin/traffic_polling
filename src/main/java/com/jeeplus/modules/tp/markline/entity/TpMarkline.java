/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tp.markline.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 标线Entity
 * @author 尹彬
 * @version 2018-12-19
 */
public class TpMarkline extends DataEntity<TpMarkline> {
	
	private static final long serialVersionUID = 1L;
	private String road;		// 道路
	private String space;		// 面积(m2)
	private Date newTime;		// 新做时间
	private Date shelfLife;		// 质保期(天)
	private String beginSpace;		// 开始 面积(m2)
	private String endSpace;		// 结束 面积(m2)
	private Date beginNewTime;		// 开始 新做时间
	private Date endNewTime;		// 结束 新做时间
	private Date beginShelfLife;		// 开始 质保期(天)
	private Date endShelfLife;		// 结束 质保期(天)
	
	public TpMarkline() {
		super();
	}

	public TpMarkline(String id){
		super(id);
	}

	@ExcelField(title="道路", fieldType=String.class, value="road.name", align=2, sort=6)
	public String getRoad() {
		return road;
	}

	public void setRoad(String road) {
		this.road = road;
	}
	
	@ExcelField(title="面积(m2)", align=2, sort=7)
	public String getSpace() {
		return space;
	}

	public void setSpace(String space) {
		this.space = space;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="新做时间不能为空")
	@ExcelField(title="新做时间", align=2, sort=8)
	public Date getNewTime() {
		return newTime;
	}

	public void setNewTime(Date newTime) {
		this.newTime = newTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="质保期(天)", align=2, sort=9)
	public Date getShelfLife() {
		return shelfLife;
	}

	public void setShelfLife(Date shelfLife) {
		this.shelfLife = shelfLife;
	}
	
	public String getBeginSpace() {
		return beginSpace;
	}

	public void setBeginSpace(String beginSpace) {
		this.beginSpace = beginSpace;
	}
	
	public String getEndSpace() {
		return endSpace;
	}

	public void setEndSpace(String endSpace) {
		this.endSpace = endSpace;
	}
		
	public Date getBeginNewTime() {
		return beginNewTime;
	}

	public void setBeginNewTime(Date beginNewTime) {
		this.beginNewTime = beginNewTime;
	}
	
	public Date getEndNewTime() {
		return endNewTime;
	}

	public void setEndNewTime(Date endNewTime) {
		this.endNewTime = endNewTime;
	}
		
	public Date getBeginShelfLife() {
		return beginShelfLife;
	}

	public void setBeginShelfLife(Date beginShelfLife) {
		this.beginShelfLife = beginShelfLife;
	}
	
	public Date getEndShelfLife() {
		return endShelfLife;
	}

	public void setEndShelfLife(Date endShelfLife) {
		this.endShelfLife = endShelfLife;
	}
		
}