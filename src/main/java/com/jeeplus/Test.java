package com.jeeplus;


import com.jeeplus.common.utils.JsonUtils;
import jsonscn.json2bean.JsonsRootBean;

public class Test {

    public static void main(String[] args) {
        String jsonStr = "{\"status\":\"1\",\"info\":\"OK\",\"infocode\":\"10000\",\"count\":\"1\",\"geocodes\":[{\"formatted_address\":\"山东省青岛市李沧区东山一路/东山七路\",\"country\":\"中国\",\"province\":\"山东省\",\"citycode\":\"0532\",\"city\":\"青岛市\",\"district\":\"李沧区\",\"township\":[],\"neighborhood\":{\"name\":[],\"type\":[]},\"building\":{\"name\":[],\"type\":[]},\"adcode\":\"370213\",\"street\":[],\"number\":[],\"location\":\"120.427404,36.166658\",\"level\":\"道路交叉路口\"}]}";
        JsonsRootBean bean = JsonUtils.jsonToObject(jsonStr, JsonsRootBean.class);
        System.out.println(bean);
    }
}
