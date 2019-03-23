/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.modules.tp.car.service;

import java.util.List;
import java.util.Map;

import com.jeeplus.common.utils.CacheUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.tp.car.entity.TpCar;
import com.jeeplus.modules.tp.car.mapper.TpCarMapper;

/**
 * 车辆Service
 *
 * @author 尹彬
 * @version 2019-01-16
 */
@Service
@Transactional(readOnly = true)
public class TpCarService extends CrudService<TpCarMapper, TpCar> {

    @Autowired
    private TpCarMapper tpCarMapper;

    public TpCar get(String id) {
        return super.get(id);
    }

    public List<TpCar> findList(TpCar tpCar) {
        return super.findList(tpCar);
    }

    public Page<TpCar> findPage(Page<TpCar> page, TpCar tpCar) {
        return super.findPage(page, tpCar);
    }

    @Transactional(readOnly = false)
    public void save(TpCar tpCar) {
        super.save(tpCar);
    }

    @Transactional(readOnly = false)
    public void delete(TpCar tpCar) {
        super.delete(tpCar);
    }

    public List<Map> findAllLocation() {
        List<Map> list = tpCarMapper.findAllLocation();
        for (Map map : list) {
            String deviceId = map.get("deviceId").toString();
            try {
                Object carObj = CacheUtils.get(deviceId);
                if (null != carObj) {
                    map.put("online", true);
                }
            } catch (Exception e) {
                map.put("online", false);
            }
        }
        return list;
    }

    public TpCar findByDeviceId(String deviceId) {
        return tpCarMapper.findByDeviceId(deviceId);
    }
}