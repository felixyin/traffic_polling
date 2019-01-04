package com.jeeplus.modules.tp.mqtt.util;

public class GpsBean {
    private String deviceId; //设备id（唯一编号，用于标识不同设备和对应的车辆）
    private String utcHms; //UTC 时间，hhmmss.sss(时分秒.毫秒)格式
    private String locationStatus;//定位状态，A=有效定位，V=无效定位
    private String lat; //纬度 ddmm.mmmm(度分)格式(前面的 0 也将被传输)
    private String latHemisphere; //纬度半球 N(北半球)或 S(南半球)
    private String lon; //经度 dddmm.mmmm(度分)格式(前面的 0 也将被传输)
    private String lonHemisphere; //经度半球 E(东经)或 W(西经)
    private String groundRate; //地面速率(000.0~999.9 节，前面的 0 也将被传输)
    private String groundDirection; //地面速率(000.0~999.9 节，前面的 0 也将被传输)
    private String utcDate; //地面速率(000.0~999.9 节，前面的 0 也将被传输)
    private String declination; //磁偏角(000.0~180.0 度，前面的 0 也将被传输)
    private String declinationDirection; //磁偏角方向，E(东)或 W(西)
    private String model; //模式指示(仅 NMEA0183 3.00 版本输出，A=自主定位，D=差分，E=估算，N=数据无效)
    private String lonGD; //转换后的高德经度
    private String latGD; //转换后的高德纬度

    public GpsBean(String deviceId, String utcHms, String locationStatus, String lat, String latHemisphere, String lon, String lonHemisphere, String groundRate, String groundDirection, String utcDate, String declination, String declinationDirection, String model) {
        this.deviceId = deviceId;
        this.utcHms = utcHms;
        this.locationStatus = locationStatus;
        this.lat = lat;
        this.latHemisphere = latHemisphere;
        this.lon = lon;
        this.lonHemisphere = lonHemisphere;
        this.groundRate = groundRate;
        this.groundDirection = groundDirection;
        this.utcDate = utcDate;
        this.declination = declination;
        this.declinationDirection = declinationDirection;
        this.model = model;
    }

    public GpsBean(String deviceId, String utcHms, String locationStatus, String lat, String latHemisphere, String lon, String lonHemisphere, String groundRate, String groundDirection, String utcDate, String declination, String declinationDirection, String model, String lonGD, String latGD) {
        this.deviceId = deviceId;
        this.utcHms = utcHms;
        this.locationStatus = locationStatus;
        this.lat = lat;
        this.latHemisphere = latHemisphere;
        this.lon = lon;
        this.lonHemisphere = lonHemisphere;
        this.groundRate = groundRate;
        this.groundDirection = groundDirection;
        this.utcDate = utcDate;
        this.declination = declination;
        this.declinationDirection = declinationDirection;
        this.model = model;
        this.lonGD = lonGD;
        this.latGD = latGD;
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

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLatHemisphere() {
        return latHemisphere;
    }

    public void setLatHemisphere(String latHemisphere) {
        this.latHemisphere = latHemisphere;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
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
                ", lat='" + lat + '\'' +
                ", latHemisphere='" + latHemisphere + '\'' +
                ", lon='" + lon + '\'' +
                ", lonHemisphere='" + lonHemisphere + '\'' +
                ", groundRate='" + groundRate + '\'' +
                ", groundDirection='" + groundDirection + '\'' +
                ", utcDate='" + utcDate + '\'' +
                ", declination='" + declination + '\'' +
                ", declinationDirection='" + declinationDirection + '\'' +
                ", model='" + model + '\'' +
                ", lonGD='" + lonGD + '\'' +
                ", latGD='" + latGD + '\'' +
                '}';
    }

    public String toString2() {
        return "GpsBean{" +
                "deviceId='" + deviceId + '\'' +
                ", utcHms='" + utcHms + '\'' +
                ", locationStatus='" + locationStatus + '\'' +
                ", 纬度='" + lat + '\'' +
                ", latHemisphere='" + latHemisphere + '\'' +
                ", 经度='" + lon + '\'' +
                ", lonHemisphere='" + lonHemisphere + '\'' +
                ", groundRate='" + groundRate + '\'' +
                ", groundDirection='" + groundDirection + '\'' +
                ", utcDate='" + utcDate + '\'' +
                ", declination='" + declination + '\'' +
                ", declinationDirection='" + declinationDirection + '\'' +
                ", model='" + model + '\'' +
                ", lonGD='" + lonGD + '\'' +
                ", latGD='" + latGD + '\'' +
                '}';
    }
}
