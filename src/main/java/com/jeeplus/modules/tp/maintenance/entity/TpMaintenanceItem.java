/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.modules.tp.maintenance.entity;

import com.jeeplus.modules.tp.maintenance.entity.TpMaintenance;
import javax.validation.constraints.NotNull;
import com.jeeplus.modules.tp.material.entity.TpMaterialPart;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 施工物料Entity
 * @author 尹彬
 * @version 2018-12-22
 */
public class TpMaintenanceItem extends DataEntity<TpMaintenanceItem> {
	
	private static final long serialVersionUID = 1L;
	private TpMaintenance maintenance;		// 维保单编号 父类
	private TpMaterialPart materialPart;		// 零件名称
	private String category;		// 所属品类
	private String unit;		// 单位
	private Double price;		// 单价
	private Integer count;		// 数量
	private Double money;		// 金额
	private Integer beginCount;		// 开始 数量
	private Integer endCount;		// 结束 数量
	private Double beginMoney;		// 开始 金额
	private Double endMoney;		// 结束 金额
	
	public TpMaintenanceItem() {
		super();
	}

	public TpMaintenanceItem(String id){
		super(id);
	}

	public TpMaintenanceItem(TpMaintenance maintenance){
		this.maintenance = maintenance;
	}

	@NotNull(message="维保单编号不能为空")
	public TpMaintenance getMaintenance() {
		return maintenance;
	}

	public void setMaintenance(TpMaintenance maintenance) {
		this.maintenance = maintenance;
	}
	
	@NotNull(message="零件名称不能为空")
	@ExcelField(title="零件名称", fieldType=TpMaterialPart.class, value="materialPart.name", align=2, sort=7)
	public TpMaterialPart getMaterialPart() {
		return materialPart;
	}

	public void setMaterialPart(TpMaterialPart materialPart) {
		this.materialPart = materialPart;
	}
	
	@ExcelField(title="所属品类", align=2, sort=8)
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	@ExcelField(title="单位", align=2, sort=9)
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	@ExcelField(title="单价", align=2, sort=10)
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	@NotNull(message="数量不能为空")
	@ExcelField(title="数量", align=2, sort=11)
	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
	
	@ExcelField(title="金额", align=2, sort=12)
	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}
	
	public Integer getBeginCount() {
		return beginCount;
	}

	public void setBeginCount(Integer beginCount) {
		this.beginCount = beginCount;
	}
	
	public Integer getEndCount() {
		return endCount;
	}

	public void setEndCount(Integer endCount) {
		this.endCount = endCount;
	}
		
	public Double getBeginMoney() {
		return beginMoney;
	}

	public void setBeginMoney(Double beginMoney) {
		this.beginMoney = beginMoney;
	}
	
	public Double getEndMoney() {
		return endMoney;
	}

	public void setEndMoney(Double endMoney) {
		this.endMoney = endMoney;
	}
		
}