/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tp.maintenance.service;

import java.util.*;

import com.jeeplus.common.config.Global;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.modules.tp.maintenance.gdbean.*;
import com.jeeplus.modules.tp.road.entity.SysArea;
import com.jeeplus.modules.tp.road.entity.TpRoad;
import com.jeeplus.modules.tp.road.service.SysAreaService;
import com.jeeplus.modules.tp.road.service.TpRoadService;
import com.jeeplus.modules.tp.roadcross.entity.TpRoadCrossing;
import com.jeeplus.modules.tp.roadcross.service.TpRoadCrossingService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.tp.maintenance.entity.TpMaintenance;
import com.jeeplus.modules.tp.maintenance.mapper.TpMaintenanceMapper;
import com.jeeplus.modules.tp.maintenance.entity.TpMaintenanceItem;
import com.jeeplus.modules.tp.maintenance.mapper.TpMaintenanceItemMapper;

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
            switch (tpMaintenance.getJobType()) {
                case "1":
                    num.append("XHD");
                    break;
                case "2":
                    num.append("HL");
                    break;
                case "3":
                    num.append("BX");
                    break;
                case "4":
                    num.append("BZBP");
                    break;
            }
            num.append(DateUtils.getDate("-yyMMdd-HHmmss-"));
            num.append(new Random().nextInt(10));
            tpMaintenance.setNum(num.toString());
        }

//        更新 道路级别
        TpRoad tpRoad = tpMaintenance.getRoad();
        if (tpRoad != null && StringUtils.isNoneBlank(tpRoad.getRoadType())) {
            tpRoadService.save(tpRoad); // update
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
}