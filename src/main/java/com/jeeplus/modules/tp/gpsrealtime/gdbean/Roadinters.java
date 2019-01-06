package com.jeeplus.modules.tp.gpsrealtime.gdbean;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
/**
 * Auto-generated: 2019-01-06 11:14:5
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class Roadinters {

    private String direction;
    private String distance;
    private String location;
    @JsonProperty("first_id")
    private String firstId;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("second_id")
    private String secondId;
    @JsonProperty("second_name")
    private String secondName;
    public void setDirection(String direction) {
         this.direction = direction;
     }
     public String getDirection() {
         return direction;
     }

    public void setDistance(String distance) {
         this.distance = distance;
     }
     public String getDistance() {
         return distance;
     }

    public void setLocation(String location) {
         this.location = location;
     }
     public String getLocation() {
         return location;
     }

    public void setFirstId(String firstId) {
         this.firstId = firstId;
     }
     public String getFirstId() {
         return firstId;
     }

    public void setFirstName(String firstName) {
         this.firstName = firstName;
     }
     public String getFirstName() {
         return firstName;
     }

    public void setSecondId(String secondId) {
         this.secondId = secondId;
     }
     public String getSecondId() {
         return secondId;
     }

    public void setSecondName(String secondName) {
         this.secondName = secondName;
     }
     public String getSecondName() {
         return secondName;
     }

}