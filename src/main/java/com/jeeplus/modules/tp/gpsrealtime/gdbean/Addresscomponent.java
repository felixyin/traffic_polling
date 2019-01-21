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
public class Addresscomponent {

    private String country;
    private String province;
    private String citycode;
    private String district;
    private String adcode;
//    private String township;
//    private String towncode;
//    @JsonProperty("streetNumber")
//    private Streetnumber streetnumber;
//    @JsonProperty("businessAreas")
//    private List<Businessareas> businessareas;
    public void setCountry(String country) {
         this.country = country;
     }
     public String getCountry() {
         return country;
     }

    public void setProvince(String province) {
         this.province = province;
     }
     public String getProvince() {
         return province;
     }

    public void setCitycode(String citycode) {
         this.citycode = citycode;
     }
     public String getCitycode() {
         return citycode;
     }

    public void setDistrict(String district) {
         this.district = district;
     }
     public String getDistrict() {
         return district;
     }

    public void setAdcode(String adcode) {
         this.adcode = adcode;
     }
     public String getAdcode() {
         return adcode;
     }

//    public void setTownship(String township) {
//         this.township = township;
//     }
//     public String getTownship() {
//         return township;
//     }
//
//    public void setTowncode(String towncode) {
//         this.towncode = towncode;
//     }
//     public String getTowncode() {
//         return towncode;
//     }

//
//
//    public void setStreetnumber(Streetnumber streetnumber) {
//         this.streetnumber = streetnumber;
//     }
//     public Streetnumber getStreetnumber() {
//         return streetnumber;
//     }
//
//    public void setBusinessareas(List<Businessareas> businessareas) {
//         this.businessareas = businessareas;
//     }
//     public List<Businessareas> getBusinessareas() {
//         return businessareas;
//     }

}