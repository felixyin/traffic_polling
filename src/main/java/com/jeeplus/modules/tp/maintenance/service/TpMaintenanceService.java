/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.modules.tp.maintenance.service;

import com.jeeplus.common.config.Global;
import com.jeeplus.common.utils.FileUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.time.DateUtil;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.sys.utils.DictUtils;
import com.jeeplus.modules.tp.maintenance.entity.TpExportReport;
import com.jeeplus.modules.tp.maintenance.entity.TpMaintenance;
import com.jeeplus.modules.tp.maintenance.entity.TpMaintenanceItem;
import com.jeeplus.modules.tp.maintenance.gdbean.*;
import com.jeeplus.modules.tp.maintenance.mapper.TpMaintenanceItemMapper;
import com.jeeplus.modules.tp.maintenance.mapper.TpMaintenanceMapper;
import com.jeeplus.modules.tp.material.entity.TpMaterialPart;
import com.jeeplus.modules.tp.material.mapper.TpMaterialPartMapper;
import com.jeeplus.modules.tp.road.entity.SysArea;
import com.jeeplus.modules.tp.road.entity.TpRoad;
import com.jeeplus.modules.tp.road.service.SysAreaService;
import com.jeeplus.modules.tp.road.service.TpRoadService;
import com.jeeplus.modules.tp.roadcross.entity.TpRoadCrossing;
import com.jeeplus.modules.tp.roadcross.service.TpRoadCrossingService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.util.UriUtils;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;

/**
 * 施工Service
 *
 * @author 尹彬
 * @version 2018-12-22
 */
@Service
@Transactional(readOnly = true)
public class TpMaintenanceService extends CrudService<TpMaintenanceMapper, TpMaintenance> {

    @Autowired
    private TpMaintenanceItemMapper tpMaintenanceItemMapper;

    @Autowired
    private TpMaterialPartMapper tpMaterialPartMapper;

    @Autowired
    private TpRoadService tpRoadService;

    @Autowired
    private SysAreaService sysAreaService;

    @Autowired
    private TpRoadCrossingService tpRoadCrossingService;


    public TpMaintenance get(String id) {
        TpMaintenance tpMaintenance = super.get(id);
        tpMaintenance.setTpMaintenanceItemList(tpMaintenanceItemMapper.findList(new TpMaintenanceItem(tpMaintenance)));
        return tpMaintenance;
    }

    public List<TpMaintenance> findList(TpMaintenance tpMaintenance) {
        return super.findList(tpMaintenance);
    }

    public Page<TpMaintenance> findPage(Page<TpMaintenance> page, TpMaintenance tpMaintenance) {
        return super.findPage(page, tpMaintenance);
    }

    @Transactional(readOnly = false)
    public void save(TpMaintenance tpMaintenance) {

//        按照规则生成施工单号
        if (StringUtils.isEmpty(tpMaintenance.getNum())) {
            StringBuilder num = new StringBuilder();
            /*
                信号灯	1
                护栏	    2
                标线	    3
                标识标牌	4
             */
//            String jt = tpMaintenance.getJobType();
//            switch (jt) {
//                case "1":
//                    num.append("XHD");
//                    break;
//                case "2":
//                    num.append("HL");
//                    break;
//                case "3":
//                    num.append("BX");
//                    break;
//                case "4":
//                    num.append("BZBP");
//                    break;
//            }

            num.append("A");
            Date sendDate = tpMaintenance.getSendDate();
            if (null == sendDate) sendDate = new Date();
            num.append(DateFormatUtils.format(sendDate, "yyMMdd-HHmmss-"));
            num.append(new Random().nextInt(10));
            tpMaintenance.setNum(num.toString());
        }

//        更新 道路级别
        TpRoad tpRoad = tpMaintenance.getRoad();
        if (tpRoad != null && StringUtils.isNoneBlank(tpRoad.getRoadType())) {
            tpRoadService.save(tpRoad); // update
        }

//        计算星期几
        Date sendDate = tpMaintenance.getSendDate();
        if (null != sendDate) {
            tpMaintenance.setWhatDay("" + DateUtil.getDayOfWeek(sendDate));
        }

        super.save(tpMaintenance);
        for (TpMaintenanceItem tpMaintenanceItem : tpMaintenance.getTpMaintenanceItemList()) {
            if (tpMaintenanceItem.getId() == null) {
                continue;
            }
            if (TpMaintenanceItem.DEL_FLAG_NORMAL.equals(tpMaintenanceItem.getDelFlag())) {
                if (StringUtils.isBlank(tpMaintenanceItem.getId())) {
                    tpMaintenanceItem.setMaintenance(tpMaintenance);
                    tpMaintenanceItem.preInsert();
                    tpMaintenanceItemMapper.insert(tpMaintenanceItem);
                } else {
                    tpMaintenanceItem.preUpdate();
                    tpMaintenanceItemMapper.update(tpMaintenanceItem);
                }
            } else {
                tpMaintenanceItemMapper.delete(tpMaintenanceItem);
            }
        }
    }

    @Transactional(readOnly = false)
    public void delete(TpMaintenance tpMaintenance) {
        super.delete(tpMaintenance);
        tpMaintenanceItemMapper.delete(new TpMaintenanceItem(tpMaintenance));
    }

    @Transactional(readOnly = false)
    public TpMaintenance savePosition(PositionRootBean bean) {
        Regeocode regeocode = bean.getRegeocode();

        /*
              ------------------------------------------------------------------------------------------------------
                检查区域是否存在，创建省市区
              ------------------------------------------------------------------------------------------------------
        */

        Addresscomponent addresscomponent = regeocode.getAddresscomponent();
        String province = addresscomponent.getProvince();
        System.out.println(province);

        // 1. 创建省份
        SysArea provinceArea = new SysArea();
        List<SysArea> provinceAreas = sysAreaService.findByName(addresscomponent.getProvince());
        if (CollectionUtils.isEmpty(provinceAreas)) {
            provinceArea.setName(addresscomponent.getProvince());
            provinceArea.setType("2"); // 2表示省
            sysAreaService.save(provinceArea);
        } else {
            provinceArea = provinceAreas.get(0);
        }

        // 2. 创建城市
        SysArea cityArea = new SysArea();
        List<SysArea> cityAreas = sysAreaService.findByName(addresscomponent.getCity());
        if (CollectionUtils.isEmpty(cityAreas)) {
            cityArea.setParent(provinceArea);
            cityArea.setCode(addresscomponent.getCitycode());
            cityArea.setName(addresscomponent.getCity());
            cityArea.setType("3"); // 2表示省
            sysAreaService.save(cityArea);
        } else {
            cityArea = cityAreas.get(0);
        }

        // 3. 创建区县
        SysArea districtArea = new SysArea();
        List<SysArea> districtAreas = sysAreaService.findByName(addresscomponent.getDistrict());
        if (CollectionUtils.isEmpty(districtAreas)) {
            districtArea.setParent(cityArea);
            districtArea.setName(addresscomponent.getDistrict());
            districtArea.setType("4"); // 2表示省
            sysAreaService.save(districtArea);
        } else {
            districtArea = districtAreas.get(0);
        }

        /*
              ------------------------------------------------------------------------------------------------------
                检查道路是否存在，创建道路
              ------------------------------------------------------------------------------------------------------
        */

        Map<String, TpRoad> roadMap = new HashMap<>();
        List<Roads> roads = regeocode.getRoads();
        for (Roads road : roads) {
            TpRoad tpRoad = new TpRoad();
            List<TpRoad> tpRoads = tpRoadService.findByName(road.getName());
            if (CollectionUtils.isEmpty(tpRoads)) {
                tpRoad.setArea(districtArea);
                tpRoad.setRoadType(Global.getConfig("road.level"));
                tpRoad.setName(road.getName());
                tpRoad.setRemarks(road.getId()); // 存储地图用id，可能用得着
                tpRoadService.save(tpRoad);
            } else {
                tpRoad = tpRoads.get(0);
            }
            roadMap.put(road.getName(), tpRoad);
        }

         /*
              ------------------------------------------------------------------------------------------------------
                检查路口是否存在，创建路口
              ------------------------------------------------------------------------------------------------------
         */

        Map<String, TpRoadCrossing> roadCrossMap = new HashMap<>();
        List<Crosses> crosses = regeocode.getCrosses();
        for (Crosses cross : crosses) {
            TpRoadCrossing tpRoadCrossing = new TpRoadCrossing();
            String fullName = cross.getFirstName() + "与" + cross.getSecondName() + "交叉口";
            List<TpRoadCrossing> tpRoadCrossings = tpRoadCrossingService.findByName(fullName);
            if (CollectionUtils.isEmpty(tpRoadCrossings)) {
                tpRoadCrossing.setSarea(districtArea);
                tpRoadCrossing.setTpRoad1(roadMap.get(cross.getFirstName()));
                tpRoadCrossing.setTpRoad2(roadMap.get(cross.getSecondName()));
                tpRoadCrossing.setTownship(addresscomponent.getTownship());
                tpRoadCrossing.setName(fullName);
                Location location = cross.getLocation();
                tpRoadCrossing.setLng(location.getLng());
                tpRoadCrossing.setLat(location.getLat());
                tpRoadCrossing.setRemarks(cross.getFirstId() + ',' + cross.getSecondId()); // 存储地图用id，可能用得着
                tpRoadCrossingService.save(tpRoadCrossing);
            } else {
                tpRoadCrossing = tpRoadCrossings.get(0);
            }
            roadCrossMap.put(fullName, tpRoadCrossing);
        }

         /*
              ------------------------------------------------------------------------------------------------------
                组织施工管理界面需要回显的内容，返回json
              ------------------------------------------------------------------------------------------------------
         */

        TpMaintenance tpMaintenance = new TpMaintenance();
        tpMaintenance.setArea(districtArea);
        Position position = bean.getPosition();
        tpMaintenance.setLocation(position.getLng() + "," + position.getLat());
        String roadCrossName = bean.getNearestjunction();
        String nearestjunction = ""; // 相对位置
        if (roadCrossName.contains("交叉口")) {
            String[] jck = roadCrossName.split("交叉口");
            roadCrossName = jck[0];
            nearestjunction = jck[1];
        }
        tpMaintenance.setRoadcross(roadCrossMap.get(roadCrossName + "交叉口"));
        tpMaintenance.setNearestJunction(nearestjunction);
        tpMaintenance.setRoad(roadMap.get(bean.getNearestroad()));
        tpMaintenance.setAddress(bean.getAddress());
        tpMaintenance.setNearestPoi(bean.getNearestpoi());

        return tpMaintenance;
    }

    /**
     * 根据搜索条件返回报表所需数据
     *
     * @param tpMaintenance
     * @return
     */
    public List<TpMaintenance> findReportData(TpMaintenance tpMaintenance) throws IOException {
        List<TpMaintenance> allList = this.mapper.findList(tpMaintenance);
        int i = 1;
        for (TpMaintenance maintenance : allList) {
            maintenance.setRemarks(String.valueOf(i++));
            //            设置任务来源
            String sourceValue = maintenance.getSource();
            maintenance.setSource(DictUtils.getDictLabel(sourceValue, "job_source", "未知"));

//            设置图片
            setReportPicture(maintenance);

//            清理施工过程html
            String p = HtmlUtils.htmlUnescape(maintenance.getProcess());
            maintenance.setProcess(stripHtml(p));

//            设置物料明细
            List<TpMaterialPart> materialParts = tpMaterialPartMapper.findListByMaintenance(maintenance.getId());
            for (TpMaterialPart materialPart : materialParts) {
                materialPart.setUnit(DictUtils.getDictLabel(materialPart.getUnit(), "material_unit", ""));
            }
            maintenance.setMaterialParts(materialParts);
        }
        return allList;
    }

    //方法一
    private String stripHtml(String content) {
        // <p>段落替换为换行
        content = content.replaceAll("<p .*?>", "\r\n");
        // <br><br/>替换为换行
        content = content.replaceAll("<br\\s*/?>", "\r\n");
        // 去掉其它的<>之间的东西
        content = content.replaceAll("\\<.*?>", "");
        // 去掉空格
        content = content.replaceAll(" ", "");
        return content;
    }

    /**
     * 设置导出的excel中的图片
     *
     * @param maintenance
     * @throws IOException
     */
    private void setReportPicture(TpMaintenance maintenance) throws IOException {
        List<String> allPicList1 = new ArrayList<>();
        allPicList1.addAll(Arrays.asList(maintenance.getPrePic().split("\\|")));
        allPicList1.addAll(Arrays.asList(maintenance.getMiddlePic().split("\\|")));
        allPicList1.addAll(Arrays.asList(maintenance.getAfterPic().split("\\|")));
/*
/traffic_polling/userfiles/c879047cb47b423b95bfe1cb4e379760/程序附件//tp/maintenance/tpMaintenance/2019/3/730461337314237595.jpg
 */
        List<String> allPicList = new ArrayList<>();
        for (String pic : allPicList1) {
            if (StringUtils.isBlank(pic)) continue;
            allPicList.add(pic);
        }
        int len = allPicList.size() > 4 ? 4 : allPicList.size(); // 最多导出4张照片
        for (int i = 0; i < len; i++) {
            String filepath = allPicList.get(i);
            if (StringUtils.isBlank(filepath)) continue;

            int index = filepath.indexOf(Global.USERFILES_BASE_URL);
            if (index >= 0) {
                filepath = filepath.substring(index + Global.USERFILES_BASE_URL.length());
            }
            filepath = UriUtils.decode(filepath, "UTF-8");

            String pathname = Global.getUserfilesBaseDir() + Global.USERFILES_BASE_URL + filepath;
            pathname = pathname.replaceAll("//", "/");
            pathname = URLDecoder.decode(pathname, "utf-8");
            System.out.println(pathname);

            try {
                File file = new File(pathname);
//                InputStream imageInputStream = new FileInputStream(file);
//                byte[] imageBytes = new byte[]{};//Util.toByteArray(imageInputStream);
                byte[] imageBytes = FileUtils.readFileToByteArray(file);
//                IOUtils.readFully(imageInputStream,imageBytes);
                switch (i) {
                    case 0:
                        maintenance.setImage1(imageBytes);
                        break;
                    case 1:
                        maintenance.setImage2(imageBytes);
                        break;
                    case 2:
                        maintenance.setImage3(imageBytes);
                        break;
                    case 3:
                        maintenance.setImage4(imageBytes);
                        break;
                }
            } catch (Exception e1) {
                e1.printStackTrace();
                logger.error(String.format("解释文件路径失败，URL地址为%s", filepath), e1);
            }
        }
    }

    /**
     * 查询统计报表
     * @param mapPage
     * @param tpMaintenance
     * @return
     */
    public List<TpExportReport> findTongJiList(TpMaintenance tpMaintenance) {
        List<TpExportReport> tongJiList = mapper.findTongJiList(tpMaintenance);
        Double all_money  = 0D;
        for (TpExportReport m : tongJiList) {
           all_money += Double.parseDouble(m.getAllMoney());
        }
        TpExportReport tongJi = new TpExportReport();
        tongJi.setProjectName("合计");
        tongJi.setAllMoney(all_money.toString());
        tongJiList.add(tongJi);
        return tongJiList;
    }
}