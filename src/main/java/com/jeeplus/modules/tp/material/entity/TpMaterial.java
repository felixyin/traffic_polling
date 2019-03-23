/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.modules.tp.material.entity;

import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 物料Entity
 * @author 尹彬
 * @version 2018-12-23
 */
public class TpMaterial extends DataEntity<TpMaterial> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 名称
	private String type;		// 物料类型
	private String company;		// 生产单位
	private String pic;		// 图片
	private String standards;		// 规格描述
	private String name_py;		// 名称拼音
	private List<TpMaterialPart> tpMaterialPartList = Lists.newArrayList();		// 子表列表
	
	public TpMaterial() {
		super();
	}

	public TpMaterial(String id){
		super(id);
	}

	@ExcelField(title="名称", align=2, sort=6)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="物料类型", dictType="material_type", align=2, sort=7)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@ExcelField(title="生产单位", dictType="material_comany", align=2, sort=8)
	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}
	
	@ExcelField(title="图片", align=2, sort=9)
	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}
	
	@ExcelField(title="规格描述", align=2, sort=10)
	public String getStandards() {
		return standards;
	}

	public void setStandards(String standards) {
		this.standards = standards;
	}
	
	@ExcelField(title="名称拼音", align=2, sort=12)
	public String getName_py() {
		return name_py;
	}

	public void setName_py(String name_py) {
		this.name_py = name_py;
	}
	
	public List<TpMaterialPart> getTpMaterialPartList() {
		return tpMaterialPartList;
	}

	public void setTpMaterialPartList(List<TpMaterialPart> tpMaterialPartList) {
		this.tpMaterialPartList = tpMaterialPartList;
	}
}