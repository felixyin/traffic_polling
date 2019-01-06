/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tp.car.entity;

import com.jeeplus.modules.sys.entity.Office;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 车辆Entity
 * @author 尹彬
 * @version 2019-01-05
 */
public class TpCar extends DataEntity<TpCar> {
	
	private static final long serialVersionUID = 1L;
	private String deviceId;		// 车辆编号
	private Office office;		// 所属单位
	private String name;		// 车辆名称
	private String brand;		// 车辆品牌
	private String purpose;		// 车辆用途
	private Integer personCount;		// 载人数量
	private Double carryingCapacity;		// 载货重量（吨）

	private String location;		// 最后GPS位置
	private String locationName;		// 最后位置名称
	private Double startKm;		// 装机时总里程
	private Double sumKm;		// GPS总里程
	private Double currentKm;		// 当前预计总里程
	private Integer sumTime;		// GPS运行总时间

	private Integer beginPersonCount;		// 开始 载人数量
	private Integer endPersonCount;		// 结束 载人数量
	private Double beginCarryingCapacity;		// 开始 载货重量（吨）
	private Double endCarryingCapacity;		// 结束 载货重量（吨）
	private Double beginStartKm;		// 开始 装机时总里程
	private Double endStartKm;		// 结束 装机时总里程
	private Double beginSumKm;		// 开始 GPS总里程
	private Double endSumKm;		// 结束 GPS总里程
	private Double beginCurrentKm;		// 开始 当前预计总里程
	private Double endCurrentKm;		// 结束 当前预计总里程
	private Integer beginSumTime;		// 开始 GPS运行总时间
	private Integer endSumTime;		// 结束 GPS运行总时间
	
	public TpCar() {
		super();
	}

	public TpCar(String id){
		super(id);
	}

	@ExcelField(title="车辆编号", align=2, sort=6)
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	@ExcelField(title="所属单位", fieldType=Office.class, value="office.name", align=2, sort=7)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	@ExcelField(title="车辆名称", align=2, sort=8)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="车辆品牌", align=2, sort=9)
	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}
	
	@ExcelField(title="车辆用途", align=2, sort=10)
	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	
	@ExcelField(title="载人数量", align=2, sort=11)
	public Integer getPersonCount() {
		return personCount;
	}

	public void setPersonCount(Integer personCount) {
		this.personCount = personCount;
	}
	
	@ExcelField(title="载货重量（吨）", align=2, sort=12)
	public Double getCarryingCapacity() {
		return carryingCapacity;
	}

	public void setCarryingCapacity(Double carryingCapacity) {
		this.carryingCapacity = carryingCapacity;
	}
	
	@ExcelField(title="最后GPS位置", align=2, sort=14)
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	@ExcelField(title="最后位置名称", align=2, sort=15)
	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	
	@ExcelField(title="装机时总里程", align=2, sort=16)
	public Double getStartKm() {
		return startKm;
	}

	public void setStartKm(Double startKm) {
		this.startKm = startKm;
	}
	
	@ExcelField(title="GPS总里程", align=2, sort=17)
	public Double getSumKm() {
		return sumKm;
	}

	public void setSumKm(Double sumKm) {
		this.sumKm = sumKm;
	}
	
	@ExcelField(title="当前预计总里程", align=2, sort=18)
	public Double getCurrentKm() {
		return currentKm;
	}

	public void setCurrentKm(Double currentKm) {
		this.currentKm = currentKm;
	}

	@ExcelField(title="GPS运行总时间", align=2, sort=19)
	public Integer getSumTime() {
		return sumTime;
	}

	public void setSumTime(Integer sumTime) {
		this.sumTime = sumTime;
	}
	
	public Integer getBeginPersonCount() {
		return beginPersonCount;
	}

	public void setBeginPersonCount(Integer beginPersonCount) {
		this.beginPersonCount = beginPersonCount;
	}
	
	public Integer getEndPersonCount() {
		return endPersonCount;
	}

	public void setEndPersonCount(Integer endPersonCount) {
		this.endPersonCount = endPersonCount;
	}
		
	public Double getBeginCarryingCapacity() {
		return beginCarryingCapacity;
	}

	public void setBeginCarryingCapacity(Double beginCarryingCapacity) {
		this.beginCarryingCapacity = beginCarryingCapacity;
	}
	
	public Double getEndCarryingCapacity() {
		return endCarryingCapacity;
	}

	public void setEndCarryingCapacity(Double endCarryingCapacity) {
		this.endCarryingCapacity = endCarryingCapacity;
	}
		
	public Double getBeginStartKm() {
		return beginStartKm;
	}

	public void setBeginStartKm(Double beginStartKm) {
		this.beginStartKm = beginStartKm;
	}
	
	public Double getEndStartKm() {
		return endStartKm;
	}

	public void setEndStartKm(Double endStartKm) {
		this.endStartKm = endStartKm;
	}
		
	public Double getBeginSumKm() {
		return beginSumKm;
	}

	public void setBeginSumKm(Double beginSumKm) {
		this.beginSumKm = beginSumKm;
	}
	
	public Double getEndSumKm() {
		return endSumKm;
	}

	public void setEndSumKm(Double endSumKm) {
		this.endSumKm = endSumKm;
	}
		
	public Double getBeginCurrentKm() {
		return beginCurrentKm;
	}

	public void setBeginCurrentKm(Double beginCurrentKm) {
		this.beginCurrentKm = beginCurrentKm;
	}
	
	public Double getEndCurrentKm() {
		return endCurrentKm;
	}

	public void setEndCurrentKm(Double endCurrentKm) {
		this.endCurrentKm = endCurrentKm;
	}
		
	public Integer getBeginSumTime() {
		return beginSumTime;
	}

	public void setBeginSumTime(Integer beginSumTime) {
		this.beginSumTime = beginSumTime;
	}
	
	public Integer getEndSumTime() {
		return endSumTime;
	}

	public void setEndSumTime(Integer endSumTime) {
		this.endSumTime = endSumTime;
	}
		
}