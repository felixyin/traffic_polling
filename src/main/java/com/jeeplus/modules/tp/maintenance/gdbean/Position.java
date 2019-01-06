package com.jeeplus.modules.tp.maintenance.gdbean;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
/**
 * Auto-generated: 2018-12-23 0:2:39
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class Position {

    @JsonProperty("P")
    private double p;
    @JsonProperty("R")
    private double r;
    private double lng;
    private double lat;
    public void setP(double p) {
         this.p = p;
     }
     public double getP() {
         return p;
     }

    public void setR(double r) {
         this.r = r;
     }
     public double getR() {
         return r;
     }

    public void setLng(double lng) {
         this.lng = lng;
     }
     public double getLng() {
         return lng;
     }

    public void setLat(double lat) {
         this.lat = lat;
     }
     public double getLat() {
         return lat;
     }

}