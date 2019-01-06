package com.jeeplus.modules.tp.gpsrealtime.util;

public class GpsBean {
    private String deviceId; //设备id（唯一编号，用于标识不同设备和对应的车辆）
    private String utcHms; //UTC 时间，hhmmss.sss(时分秒.毫秒)格式
    private String locationStatus;//定位状态，A=有效定位，V=无效定位
    private String latGps;//纬度 ddmm.mmmm(度分)格式(前面的 0 也将被传输)
    private String latHemisphere; //纬度半球 N(北半球)或 S(南半球)
    private String lonGps;//经度 dddmm.mmmm(度分)格式(前面的 0 也将被传输)
    private String lonHemisphere; //经度半球 E(东经)或 W(西经)
    private String groundRate; //地面速率(000.0~999.9 节，前面的 0 也将被传输)
    private String groundDirection; //地面航向(000.0~359.9 度，以正北为参考基准，前面的 0 也将被传输)
    private String utcDate; //UTC 日期，ddmmyy(日月年)格式
    private String declination; //磁偏角(000.0~180.0 度，前面的 0 也将被传输)
    private String declinationDirection; //磁偏角方向，E(东)或 W(西)
    private String model; //模式指示(仅 NMEA0183 3.00 版本输出，A=自主定位，D=差分，E=估算，N=数据无效)

    private String latCal; //dtu提供算法计算后的纬度
    private String lonCal; //dtu提供算法计算后的经度
    private String latGD; //高德坐标转换服务转换后的高德纬度
    private String lonGD; //高德坐标转换服务转换后的高德经度

    public GpsBean(String deviceId, String utcHms, String locationStatus, String latGps, String latHemisphere, String lonGps, String lonHemisphere, String groundRate, String groundDirection, String utcDate, String declination, String declinationDirection, String model, String latCal, String lonCal, String latGD, String lonGD) {
        this.deviceId = deviceId;
        this.utcHms = utcHms;
        this.locationStatus = locationStatus;
        this.latGps = latGps;
        this.latHemisphere = latHemisphere;
        this.lonGps = lonGps;
        this.lonHemisphere = lonHemisphere;
        this.groundRate = groundRate;
        this.groundDirection = groundDirection;
        this.utcDate = utcDate;
        this.declination = declination;
        this.declinationDirection = declinationDirection;
        this.model = model;
        this.latCal = latCal;
        this.lonCal = lonCal;
        this.latGD = latGD;
        this.lonGD = lonGD;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getUtcHms() {
        return utcHms;
    }

    public void setUtcHms(String utcHms) {
        this.utcHms = utcHms;
    }

    public String getLocationStatus() {
        return locationStatus;
    }

    public void setLocationStatus(String locationStatus) {
        this.locationStatus = locationStatus;
    }

    public String getLatGps() {
        return latGps;
    }

    public void setLatGps(String latGps) {
        this.latGps = latGps;
    }

    public String getLatHemisphere() {
        return latHemisphere;
    }

    public void setLatHemisphere(String latHemisphere) {
        this.latHemisphere = latHemisphere;
    }

    public String getLonGps() {
        return lonGps;
    }

    public void setLonGps(String lonGps) {
        this.lonGps = lonGps;
    }

    public String getLonHemisphere() {
        return lonHemisphere;
    }

    public void setLonHemisphere(String lonHemisphere) {
        this.lonHemisphere = lonHemisphere;
    }

    public String getGroundRate() {
        return groundRate;
    }

    public void setGroundRate(String groundRate) {
        this.groundRate = groundRate;
    }

    public String getGroundDirection() {
        return groundDirection;
    }

    public void setGroundDirection(String groundDirection) {
        this.groundDirection = groundDirection;
    }

    public String getUtcDate() {
        return utcDate;
    }

    public void setUtcDate(String utcDate) {
        this.utcDate = utcDate;
    }

    public String getDeclination() {
        return declination;
    }

    public void setDeclination(String declination) {
        this.declination = declination;
    }

    public String getDeclinationDirection() {
        return declinationDirection;
    }

    public void setDeclinationDirection(String declinationDirection) {
        this.declinationDirection = declinationDirection;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getLatCal() {
        return latCal;
    }

    public void setLatCal(String latCal) {
        this.latCal = latCal;
    }

    public String getLonCal() {
        return lonCal;
    }

    public void setLonCal(String lonCal) {
        this.lonCal = lonCal;
    }

    public String getLonGD() {
        return lonGD;
    }

    public void setLonGD(String lonGD) {
        this.lonGD = lonGD;
    }

    public String getLatGD() {
        return latGD;
    }

    public void setLatGD(String latGD) {
        this.latGD = latGD;
    }

    @Override
    public String toString() {
        return "GpsBean{" +
                "deviceId='" + deviceId + '\'' +
                ", utcHms='" + utcHms + '\'' +
                ", locationStatus='" + locationStatus + '\'' +
                ", latGps='" + latGps + '\'' +
                ", latHemisphere='" + latHemisphere + '\'' +
                ", lonGps='" + lonGps + '\'' +
                ", lonHemisphere='" + lonHemisphere + '\'' +
                ", groundRate='" + groundRate + '\'' +
                ", groundDirection='" + groundDirection + '\'' +
                ", utcDate='" + utcDate + '\'' +
                ", declination='" + declination + '\'' +
                ", declinationDirection='" + declinationDirection + '\'' +
                ", model='" + model + '\'' +
                ", latCal='" + latCal + '\'' +
                ", lonCal='" + lonCal + '\'' +
                ", lonGD='" + lonGD + '\'' +
                ", latGD='" + latGD + '\'' +
                '}';
    }
}
