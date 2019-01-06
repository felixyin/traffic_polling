package com.jeeplus.modules.tp.maintenance.gdbean;
import org.codehaus.jackson.annotate.JsonProperty;
/**
 * Auto-generated: 2018-12-23 0:2:39
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class Crosses {

    private int distance;
    private String direction;
    @JsonProperty("first_id")
    private String firstId;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("second_name")
    private String secondName;
    @JsonProperty("second_id")
    private String secondId;
    private Location location;
    public void setDistance(int distance) {
         this.distance = distance;
     }
     public int getDistance() {
         return distance;
     }

    public void setDirection(String direction) {
         this.direction = direction;
     }
     public String getDirection() {
         return direction;
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

    public void setSecondName(String secondName) {
         this.secondName = secondName;
     }
     public String getSecondName() {
         return secondName;
     }

    public void setSecondId(String secondId) {
         this.secondId = secondId;
     }
     public String getSecondId() {
         return secondId;
     }

    public void setLocation(Location location) {
         this.location = location;
     }
     public Location getLocation() {
         return location;
     }

}