package com.jeeplus.modules.tp.gpsrealtime.gdbean;
import java.util.List;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
/**
 * Auto-generated: 2019-01-06 11:14:5
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class Regeocode {

    @JsonProperty("formatted_address")
    private String formattedAddress;
    @JsonProperty("addressComponent")
    private Addresscomponent addresscomponent;
//    private List<String> pois;
//    private List<Roads> roads;
    private List<Roadinters> roadinters;
//    private List<Aois> aois;
    public void setFormattedAddress(String formattedAddress) {
         this.formattedAddress = formattedAddress;
     }
     public String getFormattedAddress() {
         return formattedAddress;
     }

    public void setAddresscomponent(Addresscomponent addresscomponent) {
         this.addresscomponent = addresscomponent;
     }
     public Addresscomponent getAddresscomponent() {
         return addresscomponent;
     }

//    public void setPois(List<String> pois) {
//         this.pois = pois;
//     }
//     public List<String> getPois() {
//         return pois;
//     }
//
//    public void setRoads(List<Roads> roads) {
//         this.roads = roads;
//     }
//     public List<Roads> getRoads() {
//         return roads;
//     }

    public void setRoadinters(List<Roadinters> roadinters) {
         this.roadinters = roadinters;
     }
     public List<Roadinters> getRoadinters() {
         return roadinters;
     }
//
//    public void setAois(List<Aois> aois) {
//         this.aois = aois;
//     }
//     public List<Aois> getAois() {
//         return aois;
//     }

}