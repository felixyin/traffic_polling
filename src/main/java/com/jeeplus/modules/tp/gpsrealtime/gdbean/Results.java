package com.jeeplus.modules.tp.gpsrealtime.gdbean;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
/**
 * Auto-generated: 2019-01-06 11:46:27
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class Results {

    @JsonProperty("origin_id")
    private String originId;
    @JsonProperty("dest_id")
    private String destId;
    private String distance;
    private String duration;
    public void setOriginId(String originId) {
         this.originId = originId;
     }
     public String getOriginId() {
         return originId;
     }

    public void setDestId(String destId) {
         this.destId = destId;
     }
     public String getDestId() {
         return destId;
     }

    public void setDistance(String distance) {
         this.distance = distance;
     }
     public String getDistance() {
         return distance;
     }

    public void setDuration(String duration) {
         this.duration = duration;
     }
     public String getDuration() {
         return duration;
     }

}