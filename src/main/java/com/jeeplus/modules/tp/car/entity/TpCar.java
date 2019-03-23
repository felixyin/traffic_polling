/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.modules.tp.car.entity;

import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 车辆Entity
 * @author 尹彬
 * @version 2019-01-16
 */
public class TpCar extends DataEntity<TpCar> {
	
	private static final long serialVersionUID = 1L;
	private String deviceId;		// 车辆编号
	private Office office;		// 所属单位
	private User user;		// 主要驾驶人
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
	private Long sumTime;		// GPS运行总时间
	private Double consumption;		// 油耗（升/每百公里）
	private String insuranceCompany;		// 投保公司
	private Date insuranceDate;		// 投保日期
	private Double maintainKm;		// 保养时公里数
	private Date maintainDate;		// 保养日期
	private Double beginStartKm;		// 开始 装机时总里程
	private Double endStartKm;		// 结束 装机时总里程
	private Double beginSumKm;		// 开始 GPS总里程
	private Double endSumKm;		// 结束 GPS总里程
	private Double beginCurrentKm;		// 开始 当前预计总里程
	private Double endCurrentKm;		// 结束 当前预计总里程
	private Long beginSumTime;		// 开始 GPS运行总时间
	private Long endSumTime;		// 结束 GPS运行总时间
	private Date beginInsuranceDate;		// 开始 投保日期
	private Date endInsuranceDate;		// 结束 投保日期
	private Double beginMaintainKm;		// 开始 保养时公里数
	private Double endMaintainKm;		// 结束 保养时公里数
	private Date beginMaintainDate;		// 开始 保养日期
	private Date endMaintainDate;		// 结束 保养日期
	
	public TpCar() {
		super();
	}

	public TpCar(String id){
		super(id);
	}

	@ExcelField(title="车辆编号", dictType="del_flag", align=2, sort=5)
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	@ExcelField(title="所属单位", fieldType=Office.class, value="office.name", align=2, sort=6)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	@ExcelField(title="主要驾驶人", fieldType=User.class, value="user.name", align=2, sort=7)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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
	
	@ExcelField(title="最后GPS位置", align=2, sort=13)
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	@ExcelField(title="最后位置名称", align=2, sort=14)
	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	
	@ExcelField(title="装机时总里程", align=2, sort=15)
	public Double getStartKm() {
		return startKm;
	}

	public void setStartKm(Double startKm) {
		this.startKm = startKm;
	}
	
	@ExcelField(title="GPS总里程", align=2, sort=16)
	public Double getSumKm() {
		return sumKm;
	}

	public void setSumKm(Double sumKm) {
		this.sumKm = sumKm;
	}
	
	@ExcelField(title="当前预计总里程", align=2, sort=17)
	public Double getCurrentKm() {
		return currentKm;
	}

	public void setCurrentKm(Double currentKm) {
		this.currentKm = currentKm;
	}
	
	@ExcelField(title="GPS运行总时间", align=2, sort=18)
	public Long getSumTime() {
		return sumTime;
	}

	public void setSumTime(Long sumTime) {
		this.sumTime = sumTime;
	}
	
	@ExcelField(title="油耗（升/每百公里）", align=2, sort=19)
	public Double getConsumption() {
		return consumption;
	}

	public void setConsumption(Double consumption) {
		this.consumption = consumption;
	}
	
	@ExcelField(title="投保公司", align=2, sort=20)
	public String getInsuranceCompany() {
		return insuranceCompany;
	}

	public void setInsuranceCompany(String insuranceCompany) {
		this.insuranceCompany = insuranceCompany;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="投保日期", align=2, sort=21)
	public Date getInsuranceDate() {
		return insuranceDate;
	}

	public void setInsuranceDate(Date insuranceDate) {
		this.insuranceDate = insuranceDate;
	}
	
	@ExcelField(title="保养时公里数", align=2, sort=22)
	public Double getMaintainKm() {
		return maintainKm;
	}

	public void setMaintainKm(Double maintainKm) {
		this.maintainKm = maintainKm;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="保养日期", align=2, sort=23)
	public Date getMaintainDate() {
		return maintainDate;
	}

	public void setMaintainDate(Date maintainDate) {
		this.maintainDate = maintainDate;
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
		
	public Long getBeginSumTime() {
		return beginSumTime;
	}

	public void setBeginSumTime(Long beginSumTime) {
		this.beginSumTime = beginSumTime;
	}
	
	public Long getEndSumTime() {
		return endSumTime;
	}

	public void setEndSumTime(Long endSumTime) {
		this.endSumTime = endSumTime;
	}
		
	public Date getBeginInsuranceDate() {
		return beginInsuranceDate;
	}

	public void setBeginInsuranceDate(Date beginInsuranceDate) {
		this.beginInsuranceDate = beginInsuranceDate;
	}
	
	public Date getEndInsuranceDate() {
		return endInsuranceDate;
	}

	public void setEndInsuranceDate(Date endInsuranceDate) {
		this.endInsuranceDate = endInsuranceDate;
	}
		
	public Double getBeginMaintainKm() {
		return beginMaintainKm;
	}

	public void setBeginMaintainKm(Double beginMaintainKm) {
		this.beginMaintainKm = beginMaintainKm;
	}
	
	public Double getEndMaintainKm() {
		return endMaintainKm;
	}

	public void setEndMaintainKm(Double endMaintainKm) {
		this.endMaintainKm = endMaintainKm;
	}
		
	public Date getBeginMaintainDate() {
		return beginMaintainDate;
	}

	public void setBeginMaintainDate(Date beginMaintainDate) {
		this.beginMaintainDate = beginMaintainDate;
	}
	
	public Date getEndMaintainDate() {
		return endMaintainDate;
	}

	public void setEndMaintainDate(Date endMaintainDate) {
		this.endMaintainDate = endMaintainDate;
	}
		
}