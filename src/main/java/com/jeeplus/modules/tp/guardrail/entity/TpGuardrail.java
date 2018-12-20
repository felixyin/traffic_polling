/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tp.guardrail.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 护栏Entity
 * @author 尹彬
 * @version 2018-12-19
 */
public class TpGuardrail extends DataEntity<TpGuardrail> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 名称
	private String office;		// 生产单位
	private String price;		// 单价(元)
	private String standards;		// 规格
	private String shelfLife;		// 质保期(天)
	private String beginPrice;		// 开始 单价(元)
	private String endPrice;		// 结束 单价(元)
	
	public TpGuardrail() {
		super();
	}

	public TpGuardrail(String id){
		super(id);
	}

	@ExcelField(title="名称", align=2, sort=6)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="生产单位", fieldType=String.class, value="office.name", align=2, sort=7)
	public String getOffice() {
		return office;
	}

	public void setOffice(String office) {
		this.office = office;
	}
	
	@ExcelField(title="单价(元)", align=2, sort=8)
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	
	@ExcelField(title="规格", align=2, sort=9)
	public String getStandards() {
		return standards;
	}

	public void setStandards(String standards) {
		this.standards = standards;
	}
	
	@ExcelField(title="质保期(天)", align=2, sort=10)
	public String getShelfLife() {
		return shelfLife;
	}

	public void setShelfLife(String shelfLife) {
		this.shelfLife = shelfLife;
	}
	
	public String getBeginPrice() {
		return beginPrice;
	}

	public void setBeginPrice(String beginPrice) {
		this.beginPrice = beginPrice;
	}
	
	public String getEndPrice() {
		return endPrice;
	}

	public void setEndPrice(String endPrice) {
		this.endPrice = endPrice;
	}
		
}