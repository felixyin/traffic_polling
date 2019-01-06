package com.jeeplus.modules.tp.maintenance.gdbean;
import org.codehaus.jackson.annotate.JsonProperty;
/**
 * Auto-generated: 2018-12-23 0:2:39
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class PositionRootBean {

    private String info;
    private Regeocode regeocode;
    private Position position;
    private String address;
    @JsonProperty("nearestJunction")
    private String nearestjunction;
    @JsonProperty("nearestRoad")
    private String nearestroad;
    @JsonProperty("nearestPOI")
    private String nearestpoi;
    public void setInfo(String info) {
         this.info = info;
     }
     public String getInfo() {
         return info;
     }

    public void setRegeocode(Regeocode regeocode) {
         this.regeocode = regeocode;
     }
     public Regeocode getRegeocode() {
         return regeocode;
     }

    public void setPosition(Position position) {
         this.position = position;
     }
     public Position getPosition() {
         return position;
     }

    public void setAddress(String address) {
         this.address = address;
     }
     public String getAddress() {
         return address;
     }

    public void setNearestjunction(String nearestjunction) {
         this.nearestjunction = nearestjunction;
     }
     public String getNearestjunction() {
         return nearestjunction;
     }

    public void setNearestroad(String nearestroad) {
         this.nearestroad = nearestroad;
     }
     public String getNearestroad() {
         return nearestroad;
     }

    public void setNearestpoi(String nearestpoi) {
         this.nearestpoi = nearestpoi;
     }
     public String getNearestpoi() {
         return nearestpoi;
     }

}