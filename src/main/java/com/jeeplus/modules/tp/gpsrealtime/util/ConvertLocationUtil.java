package com.jeeplus.modules.tp.gpsrealtime.util;

import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.JsonUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.tp.gpsrealtime.entity.TpGpsRealtime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class ConvertLocationUtil {

    private static Logger logger = LoggerFactory.getLogger(ConvertLocationUtil.class);
    
    private static String GD_CONVERT_URL = "https://restapi.amap.com/v3/assistant/coordinate/convert?key=806cfc91a232e5be93e358b5af52f1c9&coordsys=gps&locations=";

    public static TpGpsRealtime convert(String dtuMsg) {
        if (StringUtils.isNotBlank(dtuMsg) && dtuMsg.length() > 80) {// 小于60长度，说明没有gps定位信息，忽略

            TpGpsRealtime gpsRealtime = new TpGpsRealtime();

            String[] list = dtuMsg.split(",");


            String deviceId = list[1]; //设备id（唯一编号，用于标识不同设备和对应的车辆）
            gpsRealtime.setDeviceId(deviceId);


            String locationStatus = list[4];//定位状态，A=有效定位，V=无效定位
            gpsRealtime.setLocationStatus(locationStatus);


            String latStr = list[5]; //纬度 ddmm.mmmm(度分)格式(前面的 0 也将被传输)
            gpsRealtime.setLatGps(latStr);


            String latHemisphere = list[6]; //纬度半球 N(北半球)或 S(南半球)
            gpsRealtime.setLatHemisphere(latHemisphere);


            String lonStr = list[7]; //经度 dddmm.mmmm(度分)格式(前面的 0 也将被传输)
            gpsRealtime.setLonGps(lonStr);


            String lonHemisphere = list[8]; //经度半球 E(东经)或 W(西经)
            gpsRealtime.setLonHemisphere(lonHemisphere);


            String groundRate = list[9]; //地面速率(000.0~999.9 节，前面的 0 也将被传输)
            gpsRealtime.setGroundRate(groundRate);


            String groundDirection = list[10]; //地面速率(000.0~999.9 节，前面的 0 也将被传输)
            gpsRealtime.setGroundDirection(groundDirection);


            String utcDate = list[11]; //地面速率(000.0~999.9 节，前面的 0 也将被传输)
            logger.debug(utcDate);


            String utcHms = list[3]; //UTC 时间，hhmmss.sss(时分秒.毫秒)格式
            logger.debug("-------------------------");
            logger.debug(utcHms);


//            计算时间
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmss.SSS");
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date upTime = sdf.parse(utcDate + utcHms);
                gpsRealtime.setUpTime(upTime);
                logger.debug("-----------------------------------------------------------------");
                logger.debug(DateUtils.formatDateTime(upTime));
            } catch (Exception e) {
                e.printStackTrace();
            }


            String declination = list[12]; //磁偏角(000.0~180.0 度，前面的 0 也将被传输)
            if(StringUtils.isNotBlank(declination)){ // 兼容2g设备没有次数的问题
                gpsRealtime.setDeclination(declination);
            }


            String declinationDirection = list[13]; //磁偏角方向，E(东)或 W(西)
            if(StringUtils.isNotBlank(declinationDirection)) { // 兼容2g设备没有次数的问题
                gpsRealtime.setDeclinationDirection(declinationDirection);
            }


            String model = list[14]; //模式指示(仅 NMEA0183 3.00 版本输出，A=自主定位，D=差分，E=估算，N=数据无效)
            gpsRealtime.setModel(model);


//            解密纬度
            String latStr1 = latStr.substring(0, 2);
            String latStr2 = latStr.substring(2, 4);
            String latStr3 = latStr.substring(5, latStr.length());
            logger.debug("分割后纬度：" + latStr1 + "," + latStr2 + "," + latStr3);
            double latCal = Double.parseDouble(latStr1) + BigDecimalUtil.div(Double.parseDouble(latStr2), 60) + BigDecimalUtil.div(Double.parseDouble(latStr3), 600000);
            gpsRealtime.setLatCal(String.valueOf(latCal));


//            解密经度
            String lonStr1 = lonStr.substring(0, 3);
            String lonStr2 = lonStr.substring(3, 5);
            String lonStr3 = lonStr.substring(6, lonStr.length());
            logger.debug("分割后经度：" + lonStr1 + "," + lonStr2 + "," + lonStr3);
            double lonCal = Double.parseDouble(lonStr1) + BigDecimalUtil.div(Double.parseDouble(lonStr2), 60) + BigDecimalUtil.div(Double.parseDouble(lonStr3), 600000);
            gpsRealtime.setLonCal(String.valueOf(lonCal));


//            计算高德经纬度
            String lonGD = null, latGD = null;
            String json = HttpUtil.get(GD_CONVERT_URL + lonCal + "," + latCal);
            if (StringUtils.isNotBlank(json)) {
                ConvertLocationBean convertLocationBean = JsonUtils.jsonToObject(json, ConvertLocationBean.class);
                if (convertLocationBean != null) {
                    String[] split = convertLocationBean.getLocations().split(",");
                    lonGD = split[0];
                    latGD = split[1];
                }
            }
            gpsRealtime.setLatGD(latGD);
            gpsRealtime.setLonGD(lonGD);


            return gpsRealtime;
//            return new GpsBean(deviceId, utcHms, locationStatus, latStr, latHemisphere, lonStr, lonHemisphere, groundRate, groundDirection,
//                    utcDate, declination, declinationDirection, model, String.valueOf(latCal), String.valueOf(lonCal), latGD, lonGD);
        }
        return null;
    }

    /*        *//*
     * double[] res1 = gcj02towgs84(114.109374d, 22.619651d); double[] res2
     * = wgs84tobd09(res1[0], res1[1]); logger.debug(res2[0] + "," +
     * res2[1]);
     *
     * double gpsLng = 114.109374d; double gpsLat = 22.619651d; double[] res
     * = gcj02tobd09(gpsLng, gpsLat); logger.debug(res[0]);
     * logger.debug(res[1]);
     *
     * double[] res3 = wgs84tobd09(113.968498,22.595167);
     * logger.debug(res3[0]+","+res3[1]);
     *//*

        double[] res4 = wgs84togcj02(114.103458, 22.561741);
        logger.debug(res4[0] + "," + res4[1]);*/

    static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
    // π
    static double pi = 3.1415926535897932384626;
    // 长半轴
    static double a = 6378245.0;
    // 扁率
    static double ee = 0.00669342162296594323;

    /**
     * 百度坐标系(BD-09)转WGS坐标
     *
     * @param lng 百度坐标纬度
     * @param lat 百度坐标经度
     * @return WGS84坐标数组
     */
    public static double[] bd09towgs84(double lng, double lat) {
        double[] gcj = bd09togcj02(lng, lat);
        double[] wgs84 = gcj02towgs84(gcj[0], gcj[1]);
        return wgs84;
    }

    /**
     * WGS坐标转百度坐标系(BD-09)
     *
     * @param lng WGS84坐标系的经度
     * @param lat WGS84坐标系的纬度
     * @return 百度坐标数组
     */
    public static double[] wgs84tobd09(double lng, double lat) {
        double[] gcj = wgs84togcj02(lng, lat);
        double[] bd09 = gcj02tobd09(gcj[0], gcj[1]);
        return bd09;
    }

    /**
     * 火星坐标系(GCJ-02)转百度坐标系(BD-09)
     *
     * @param lng 火星坐标经度
     * @param lat 火星坐标纬度
     * @return 百度坐标数组
     * 谷歌、高德——>百度
     */
    public static double[] gcj02tobd09(double lng, double lat) {
        double z = Math.sqrt(lng * lng + lat * lat) + 0.00002 * Math.sin(lat * x_pi);
        double theta = Math.atan2(lat, lng) + 0.000003 * Math.cos(lng * x_pi);
        double bd_lng = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;
        return new double[]{bd_lng, bd_lat};
    }

    /**
     * 百度坐标系(BD-09)转火星坐标系(GCJ-02)
     *
     * @param bd_lon 百度坐标纬度
     * @param bd_lat 百度坐标经度
     * @return 火星坐标数组
     * 百度——>谷歌、高德
     */
    public static double[] bd09togcj02(double bd_lon, double bd_lat) {
        double x = bd_lon - 0.0065;
        double y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        double gg_lng = z * Math.cos(theta);
        double gg_lat = z * Math.sin(theta);
        return new double[]{gg_lng, gg_lat};
    }

    /**
     * WGS84转GCJ02(火星坐标系)
     *
     * @param lng WGS84坐标系的经度
     * @param lat WGS84坐标系的纬度
     * @return 火星坐标数组
     */
    public static double[] wgs84togcj02(double lng, double lat) {
        if (out_of_china(lng, lat)) {
            return new double[]{lng, lat};
        }
        double dlat = transformlat(lng - 105.0, lat - 35.0);
        double dlng = transformlng(lng - 105.0, lat - 35.0);
        double radlat = lat / 180.0 * pi;
        double magic = Math.sin(radlat);
        magic = 1 - ee * magic * magic;
        double sqrtmagic = Math.sqrt(magic);
        dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtmagic) * pi);
        dlng = (dlng * 180.0) / (a / sqrtmagic * Math.cos(radlat) * pi);
        double mglat = lat + dlat;
        double mglng = lng + dlng;
        return new double[]{mglng, mglat};
    }

    /**
     * GCJ02(火星坐标系)转GPS84
     *
     * @param lng 火星坐标系的经度
     * @param lat 火星坐标系纬度
     * @return WGS84坐标数组
     */
    public static double[] gcj02towgs84(double lng, double lat) {
        if (out_of_china(lng, lat)) {
            return new double[]{lng, lat};
        }
        double dlat = transformlat(lng - 105.0, lat - 35.0);
        double dlng = transformlng(lng - 105.0, lat - 35.0);
        double radlat = lat / 180.0 * pi;
        double magic = Math.sin(radlat);
        magic = 1 - ee * magic * magic;
        double sqrtmagic = Math.sqrt(magic);
        dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtmagic) * pi);
        dlng = (dlng * 180.0) / (a / sqrtmagic * Math.cos(radlat) * pi);
        double mglat = lat + dlat;
        double mglng = lng + dlng;
        return new double[]{lng * 2 - mglng, lat * 2 - mglat};
    }

    /**
     * 纬度转换
     *
     * @param lng
     * @param lat
     * @return
     */
    public static double transformlat(double lng, double lat) {
        double ret = -100.0 + 2.0 * lng + 3.0 * lat + 0.2 * lat * lat + 0.1 * lng * lat
                + 0.2 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * pi) + 20.0 * Math.sin(2.0 * lng * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lat * pi) + 40.0 * Math.sin(lat / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(lat / 12.0 * pi) + 320 * Math.sin(lat * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    /**
     * 经度转换
     *
     * @param lng
     * @param lat
     * @return
     */
    public static double transformlng(double lng, double lat) {
        double ret = 300.0 + lng + 2.0 * lat + 0.1 * lng * lng + 0.1 * lng * lat + 0.1 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * pi) + 20.0 * Math.sin(2.0 * lng * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lng * pi) + 40.0 * Math.sin(lng / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(lng / 12.0 * pi) + 300.0 * Math.sin(lng / 30.0 * pi)) * 2.0 / 3.0;
        return ret;
    }

    /**
     * 判断是否在国内，不在国内不做偏移
     *
     * @param lng
     * @param lat
     * @return
     */
    public static boolean out_of_china(double lng, double lat) {
        if (lng < 72.004 || lng > 137.8347) {
            return true;
        } else if (lat < 0.8293 || lat > 55.8271) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        String gpsMsg = "$$$,car00001,$GPRMC,124757.000,A,3609.8498,N,12024.9730,E,0.0,0.0,030119,6.0,W,A*10";
        gpsMsg = "$$$,1440031793292,$GPRMC,040823.000,A,3611.9304,N,11646.2414,E,0.01,279.49,210220,,,A*6C";
        System.out.println("gps上传信息：" + gpsMsg);
        TpGpsRealtime gpsBean = ConvertLocationUtil.convert(gpsMsg);
        System.out.println("解析处理后的信息：" + gpsBean.toString());
    }
}
