/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.modules.tp.maintenance.entity;

import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.core.persistence.DataEntity;

/**
 * 施工Entity
 *
 * @author 尹彬
 * @version 2018-12-22
 */
public class TpExportReport extends DataEntity<TpExportReport> {


    /*
    	tmi.remarks as verb,
		tmi.category,
		tmp.`name` as item_name,
		concat( tmi.remarks,' ', tmi.category,'-',tmp.`name` ) AS project_name,
		tmp.unit,
		SUM( tmi.count ) AS all_count,
		tmi.price,
		SUM( tmi.money ) AS all_money
     */
    private String verb;
    private String category;
    private String itemName;
    private String projectName;
    private String unit;
    private String price;
    private String allCount;
    private String allMoney;
    private String remarks;

    public String getVerb() {
        return verb;
    }

    public void setVerb(String verb) {
        this.verb = verb;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @ExcelField(title = "维修项目", align = 2, sort = 2)
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @ExcelField(title = "单位", align = 3, sort = 3)
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @ExcelField(title = "单价", align = 4, sort = 4)
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @ExcelField(title = "数量", align = 5, sort = 5)
    public String getAllCount() {
        return allCount;
    }

    public void setAllCount(String allCount) {
        this.allCount = allCount;
    }

    @ExcelField(title = "金额", align = 6, sort = 6)
    public String getAllMoney() {
        return allMoney;
    }

    public void setAllMoney(String allMoney) {
        this.allMoney = allMoney;
    }

    @Override
    @ExcelField(title = "备注", align = 7, sort = 7)
    public String getRemarks() {
        return remarks;
    }

    @Override
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}