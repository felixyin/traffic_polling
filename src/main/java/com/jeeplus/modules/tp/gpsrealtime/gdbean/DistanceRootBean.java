package com.jeeplus.modules.tp.gpsrealtime.gdbean;
import java.util.List;

/**
 * Auto-generated: 2019-01-06 11:46:27
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class DistanceRootBean {

    private String status;
    private String info;
    private String infocode;
    private List<Results> results;
    public void setStatus(String status) {
         this.status = status;
     }
     public String getStatus() {
         return status;
     }

    public void setInfo(String info) {
         this.info = info;
     }
     public String getInfo() {
         return info;
     }

    public void setInfocode(String infocode) {
         this.infocode = infocode;
     }
     public String getInfocode() {
         return infocode;
     }

    public void setResults(List<Results> results) {
         this.results = results;
     }
     public List<Results> getResults() {
         return results;
     }

}