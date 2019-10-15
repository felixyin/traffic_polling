/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.modules.tp.gpshistory.entity;

import com.jeeplus.modules.tp.cartrack.entity.TpCarTrack;
import javax.validation.constraints.NotNull;
import com.jeeplus.modules.tp.car.entity.TpCar;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 历史轨迹Entity
 * @author 尹彬
 * @version 2019-01-06
 */
public class TpGpsHistory extends DataEntity<TpGpsHistory> {
	
	private static final long serialVersionUID = 1L;
	private TpCarTrack carTrack;		// 出车记录表外键
	private TpCar car;		// 车辆表外键
	private String deviceId;		// 设备编号
	private Date upTime;		// 上传时间
	private String locationStatus;		// 定位状态
	private String latGps;		// GPS纬度
	private String latHemisphere;		// 纬度半球
	private String lonGps;		// GPS经度
	private String lonHemisphere;		// 经度半球
	private String groundRate;		// 地面速率
	private String groundDirection;		// 地面航向
	private String declination;		// 磁偏角
	private String declinationDirection;		// 磁偏角方向
	private String model;		// 模式指示
	private String latCal;		// 解密后纬度
	private String lonCal;		// 解密后经度
	private String latGD;		// 转换高德纬度
	private String lonGD;		// 转换高德经度
	private Date beginUpTime;		// 开始 上传时间
	private Date endUpTime;		// 结束 上传时间
	
	public TpGpsHistory() {
		super();
	}

	public TpGpsHistory(String id){
		super(id);
	}

	@NotNull(message="出车记录表外键不能为空")
	@ExcelField(title="出车记录表外键", fieldType=TpCarTrack.class, value="carTrack.name", align=2, sort=7)
	public TpCarTrack getCarTrack() {
		return carTrack;
	}

	public void setCarTrack(TpCarTrack carTrack) {
		this.carTrack = carTrack;
	}
	
	@ExcelField(title="车辆表外键", fieldType=TpCar.class, value="car.name", align=2, sort=8)
	public TpCar getCar() {
		return car;
	}

	public void setCar(TpCar car) {
		this.car = car;
	}
	
	@ExcelField(title="设备编号", align=2, sort=9)
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="上传时间不能为空")
	@ExcelField(title="上传时间", align=2, sort=10)
	public Date getUpTime() {
		return upTime;
	}

	public void setUpTime(Date upTime) {
		this.upTime = upTime;
	}
	
	@ExcelField(title="定位状态", align=2, sort=11)
	public String getLocationStatus() {
		return locationStatus;
	}

	public void setLocationStatus(String locationStatus) {
		this.locationStatus = locationStatus;
	}
	
	@ExcelField(title="GPS纬度", align=2, sort=12)
	public String getLatGps() {
		return latGps;
	}

	public void setLatGps(String latGps) {
		this.latGps = latGps;
	}
	
	@ExcelField(title="纬度半球", align=2, sort=13)
	public String getLatHemisphere() {
		return latHemisphere;
	}

	public void setLatHemisphere(String latHemisphere) {
		this.latHemisphere = latHemisphere;
	}
	
	@ExcelField(title="GPS经度", align=2, sort=14)
	public String getLonGps() {
		return lonGps;
	}

	public void setLonGps(String lonGps) {
		this.lonGps = lonGps;
	}
	
	@ExcelField(title="经度半球", align=2, sort=15)
	public String getLonHemisphere() {
		return lonHemisphere;
	}

	public void setLonHemisphere(String lonHemisphere) {
		this.lonHemisphere = lonHemisphere;
	}
	
	@ExcelField(title="地面速率", align=2, sort=16)
	public String getGroundRate() {
		return groundRate;
	}

	public void setGroundRate(String groundRate) {
		this.groundRate = groundRate;
	}
	
	@ExcelField(title="地面航向", align=2, sort=17)
	public String getGroundDirection() {
		return groundDirection;
	}

	public void setGroundDirection(String groundDirection) {
		this.groundDirection = groundDirection;
	}
	
	@ExcelField(title="磁偏角", align=2, sort=18)
	public String getDeclination() {
		return declination;
	}

	public void setDeclination(String declination) {
		this.declination = declination;
	}
	
	@ExcelField(title="磁偏角方向", align=2, sort=19)
	public String getDeclinationDirection() {
		return declinationDirection;
	}

	public void setDeclinationDirection(String declinationDirection) {
		this.declinationDirection = declinationDirection;
	}
	
	@ExcelField(title="模式指示", align=2, sort=20)
	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}
	
	@ExcelField(title="解密后纬度", align=2, sort=21)
	public String getLatCal() {
		return latCal;
	}

	public void setLatCal(String latCal) {
		this.latCal = latCal;
	}
	
	@ExcelField(title="解密后经度", align=2, sort=22)
	public String getLonCal() {
		return lonCal;
	}

	public void setLonCal(String lonCal) {
		this.lonCal = lonCal;
	}
	
	@ExcelField(title="转换高德纬度", align=2, sort=23)
	public String getLatGD() {
		return latGD;
	}

	public void setLatGD(String latGD) {
		this.latGD = latGD;
	}
	
	@ExcelField(title="转换高德经度", align=2, sort=24)
	public String getLonGD() {
		return lonGD;
	}

	public void setLonGD(String lonGD) {
		this.lonGD = lonGD;
	}
	
	public Date getBeginUpTime() {
		return beginUpTime;
	}

	public void setBeginUpTime(Date beginUpTime) {
		this.beginUpTime = beginUpTime;
	}
	
	public Date getEndUpTime() {
		return endUpTime;
	}

	public void setEndUpTime(Date endUpTime) {
		this.endUpTime = endUpTime;
	}
		
}