package com.jeeplus.modules.tp.road.web;

import com.jeeplus.modules.tp.road.entity.MySysArea;
import com.jeeplus.modules.tp.road.entity.TpRoad;
import com.jeeplus.modules.tp.road.service.MySysAreaService;
import com.jeeplus.modules.tp.road.service.TpRoadService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

public class Main {

    public static void main(String arg[]) throws Exception {

        ApplicationContext wac = new ClassPathXmlApplicationContext("spring-context.xml");
//        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        TpRoadService tpRoadService = wac.getBean(TpRoadService.class);
        MySysAreaService mySysAreaService = wac.getBean(MySysAreaService.class);


        Document doc = Jsoup.connect("http://poi.mapbar.com/jinan/G70/").userAgent("Mozilla").cookie("auth", "token").timeout(90000).post();
//            String title = doc.title();

        Element e = doc.getElementsByClass("sortC").get(0);
        Elements eles = e.getElementsByTag("a");
        for (Element ee : eles) {
            String roadName = ee.text().trim();
            System.out.println(roadName);
            boolean b = true;
            try {
                tpRoadService.getByName(roadName);
            } catch (Exception e3) {
                b = false;
            }
            if (b) {
                String url = ee.attributes().get("href");
//                System.out.println(url);
                Document doc2 = Jsoup.connect(url).userAgent("Mozilla").cookie("auth", "token").timeout(90000).post();
                Element e1 = doc2.getElementsByClass("POI_ulA").get(0);
                Element ele2 = e1.children().get(1).children().get(2);
                String areaName = ele2.text();
                System.out.println(areaName);

                TpRoad tpRoad = new TpRoad();
                MySysArea dbSysArea = mySysAreaService.getByName(areaName);
                if (dbSysArea != null) {
                    tpRoad.setArea(dbSysArea);
                }
                tpRoad.setName(roadName);
                tpRoad.setRoadType("3");
                tpRoad.setRoadType("《图吧》导入");
                tpRoadService.save(tpRoad);
            }
        }
    }

}
