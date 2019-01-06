package com.jeeplus.modules.tp.maintenance.gdbean;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
/**
 * Auto-generated: 2018-12-23 0:2:39
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class Regeocode {

    @JsonProperty("addressComponent")
    private Addresscomponent addresscomponent;
    @JsonProperty("formattedAddress")
    private String formattedaddress;
    private List<Roads> roads;
    private List<Crosses> crosses;
    public void setAddresscomponent(Addresscomponent addresscomponent) {
         this.addresscomponent = addresscomponent;
     }
     public Addresscomponent getAddresscomponent() {
         return addresscomponent;
     }

    public void setFormattedaddress(String formattedaddress) {
         this.formattedaddress = formattedaddress;
     }
     public String getFormattedaddress() {
         return formattedaddress;
     }

    public void setRoads(List<Roads> roads) {
         this.roads = roads;
     }
     public List<Roads> getRoads() {
         return roads;
     }

    public void setCrosses(List<Crosses> crosses) {
         this.crosses = crosses;
     }
     public List<Crosses> getCrosses() {
         return crosses;
     }

}