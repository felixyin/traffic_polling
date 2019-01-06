package com.jeeplus.modules.tp.maintenance.gdbean;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
/**
 * Auto-generated: 2018-12-23 0:2:39
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class Addresscomponent {

    private String citycode;
    private String adcode;
    @JsonProperty("neighborhoodType")
    private String neighborhoodtype;
    private String neighborhood;
    private String building;
    @JsonProperty("buildingType")
    private String buildingtype;
    private String street;
    @JsonProperty("streetNumber")
    private String streetnumber;
    private String province;
    private String city;
    private String district;
    private String township;
    public void setCitycode(String citycode) {
         this.citycode = citycode;
     }
     public String getCitycode() {
         return citycode;
     }

    public void setAdcode(String adcode) {
         this.adcode = adcode;
     }
     public String getAdcode() {
         return adcode;
     }

    public void setNeighborhoodtype(String neighborhoodtype) {
         this.neighborhoodtype = neighborhoodtype;
     }
     public String getNeighborhoodtype() {
         return neighborhoodtype;
     }

    public void setNeighborhood(String neighborhood) {
         this.neighborhood = neighborhood;
     }
     public String getNeighborhood() {
         return neighborhood;
     }

    public void setBuilding(String building) {
         this.building = building;
     }
     public String getBuilding() {
         return building;
     }

    public void setBuildingtype(String buildingtype) {
         this.buildingtype = buildingtype;
     }
     public String getBuildingtype() {
         return buildingtype;
     }

    public void setStreet(String street) {
         this.street = street;
     }
     public String getStreet() {
         return street;
     }

    public void setStreetnumber(String streetnumber) {
         this.streetnumber = streetnumber;
     }
     public String getStreetnumber() {
         return streetnumber;
     }

    public void setProvince(String province) {
         this.province = province;
     }
     public String getProvince() {
         return province;
     }

    public void setCity(String city) {
         this.city = city;
     }
     public String getCity() {
         return city;
     }

    public void setDistrict(String district) {
         this.district = district;
     }
     public String getDistrict() {
         return district;
     }

    public void setTownship(String township) {
         this.township = township;
     }
     public String getTownship() {
         return township;
     }

}