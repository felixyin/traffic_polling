/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.modules.tp.cartrack.entity;

import com.jeeplus.modules.tp.car.entity.TpCar;
import javax.validation.constraints.NotNull;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.modules.tp.maintenance.entity.TpMaintenance;
import com.jeeplus.modules.sys.entity.User;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 出车记录Entity
 * @author 尹彬
 * @version 2019-01-05
 */
public class TpCarTrack extends DataEntity<TpCarTrack> {
	
	private static final long serialVersionUID = 1L;
	private TpCar car;		// 关联车辆
	private String locationBegin;		// 开始位置
	private String nameBegin;		// 开始位置地名
	private String locationEnd;		// 结束位置
	private String nameEnd;		// 结束位置地名
	private Date timeBegin;		// 开始时间
	private Date timeEnd;		// 结束时间
	private Double km;		// 行驶里程
	private String driverType;		// 用车类型
	private TpMaintenance maintenance;		// 关联任务
	private String jobDesc;		// 任务描述
	private User user;		// 关联驾驶人
    private String whatDay;  	// 星期几

	private Date beginTimeBegin;		// 开始 开始时间
	private Date endTimeBegin;		// 结束 开始时间
	private Date beginTimeEnd;		// 开始 结束时间
	private Date endTimeEnd;		// 结束 结束时间
	private Double beginKm;		// 开始 行驶里程
	private Double endKm;		// 结束 行驶里程
	
	public TpCarTrack() {
		super();
	}

	public TpCarTrack(String id){
		super(id);
	}

	@NotNull(message="关联车辆不能为空")
	@ExcelField(title="关联车辆", fieldType=TpCar.class, value="car.name", align=2, sort=6)
	public TpCar getCar() {
		return car;
	}

	public void setCar(TpCar car) {
		this.car = car;
	}
	
	@ExcelField(title="开始位置", align=2, sort=7)
	public String getLocationBegin() {
		return locationBegin;
	}

	public void setLocationBegin(String locationBegin) {
		this.locationBegin = locationBegin;
	}
	
	@ExcelField(title="开始位置地名", align=2, sort=8)
	public String getNameBegin() {
		return nameBegin;
	}

	public void setNameBegin(String nameBegin) {
		this.nameBegin = nameBegin;
	}
	
	@ExcelField(title="结束位置", align=2, sort=9)
	public String getLocationEnd() {
		return locationEnd;
	}

	public void setLocationEnd(String locationEnd) {
		this.locationEnd = locationEnd;
	}
	
	@ExcelField(title="结束位置地名", align=2, sort=10)
	public String getNameEnd() {
		return nameEnd;
	}

	public void setNameEnd(String nameEnd) {
		this.nameEnd = nameEnd;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="开始时间不能为空")
	@ExcelField(title="开始时间", align=2, sort=11)
	public Date getTimeBegin() {
		return timeBegin;
	}

	public void setTimeBegin(Date timeBegin) {
		this.timeBegin = timeBegin;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="结束时间不能为空")
	@ExcelField(title="结束时间", align=2, sort=12)
	public Date getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(Date timeEnd) {
		this.timeEnd = timeEnd;
	}
	
	@NotNull(message="行驶里程不能为空")
	@ExcelField(title="行驶里程", align=2, sort=13)
	public Double getKm() {
		return km;
	}

	public void setKm(Double km) {
		this.km = km;
	}
	
	@ExcelField(title="用车类型", dictType="driver_type", align=2, sort=14)
	public String getDriverType() {
		return driverType;
	}

	public void setDriverType(String driverType) {
		this.driverType = driverType;
	}
	
	@ExcelField(title="关联任务", fieldType=TpMaintenance.class, value="maintenance.num", align=2, sort=15)
	public TpMaintenance getMaintenance() {
		return maintenance;
	}

	public void setMaintenance(TpMaintenance maintenance) {
		this.maintenance = maintenance;
	}
	
	@ExcelField(title="任务描述", align=2, sort=16)
	public String getJobDesc() {
		return jobDesc;
	}

	public void setJobDesc(String jobDesc) {
		this.jobDesc = jobDesc;
	}
	
	@NotNull(message="关联驾驶人不能为空")
	@ExcelField(title="关联驾驶人", fieldType=User.class, value="user.name", align=2, sort=17)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getWhatDay() {
		return whatDay;
	}

	public void setWhatDay(String whatDay) {
		this.whatDay = whatDay;
	}

	public Date getBeginTimeBegin() {
		return beginTimeBegin;
	}

	public void setBeginTimeBegin(Date beginTimeBegin) {
		this.beginTimeBegin = beginTimeBegin;
	}
	
	public Date getEndTimeBegin() {
		return endTimeBegin;
	}

	public void setEndTimeBegin(Date endTimeBegin) {
		this.endTimeBegin = endTimeBegin;
	}
		
	public Date getBeginTimeEnd() {
		return beginTimeEnd;
	}

	public void setBeginTimeEnd(Date beginTimeEnd) {
		this.beginTimeEnd = beginTimeEnd;
	}
	
	public Date getEndTimeEnd() {
		return endTimeEnd;
	}

	public void setEndTimeEnd(Date endTimeEnd) {
		this.endTimeEnd = endTimeEnd;
	}
		
	public Double getBeginKm() {
		return beginKm;
	}

	public void setBeginKm(Double beginKm) {
		this.beginKm = beginKm;
	}
	
	public Double getEndKm() {
		return endKm;
	}

	public void setEndKm(Double endKm) {
		this.endKm = endKm;
	}
		
}