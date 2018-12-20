/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tp.road.entity;

import com.jeeplus.modules.tp.road.service.TpRoadService;
import org.hibernate.validator.constraints.Length;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import org.omg.CORBA.SystemException;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

/**
 * 道路Entity
 *
 * @author 尹彬
 * @version 2018-12-19
 */
public class TpRoad extends DataEntity<TpRoad> {

    private static final long serialVersionUID = 1L;
    private String name;        // 道路名称
    private MySysArea area;        // 所属区域 父类
    private String roadType;        // 道路类型
    private Integer length;        // 道路长度(m)
    private Integer width;        // 道路宽度(m)
    private Integer acreage;        // 占地面积(m2)


    public TpRoad() {
        super();
    }

    public TpRoad(String id) {
        super(id);
    }

    public TpRoad(MySysArea area) {
        this.area = area;
    }

    @Length(min = 3, max = 100, message = "道路名称长度必须介于 3 和 100 之间")
    @ExcelField(title = "道路名称", align = 2, sort = 6)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ExcelField(title = "所属区域", align = 2, sort = 7)
    public MySysArea getArea() {
        return area;
    }

    public void setArea(MySysArea area) {
        this.area = area;
    }

    @ExcelField(title = "道路类型", dictType = "road_type", align = 2, sort = 8)
    public String getRoadType() {
        return roadType;
    }

    public void setRoadType(String roadType) {
        this.roadType = roadType;
    }

    @ExcelField(title = "道路长度(m)", align = 2, sort = 9)
    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    @ExcelField(title = "道路宽度(m)", align = 2, sort = 10)
    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    @ExcelField(title = "占地面积(m2)", align = 2, sort = 11)
    public Integer getAcreage() {
        return acreage;
    }

    public void setAcreage(Integer acreage) {
        this.acreage = acreage;
    }

    public TpRoad getValue(String name) throws Exception {
        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        TpRoadService service = wac.getBean(TpRoadService.class);
        TpRoad param = new TpRoad();
        param.setName(name);
        TpRoad tpRoad = service.getByName(name);

        if (tpRoad == null) {
            param.setId("");
//             Todo 界面显示了一个-，能否跟新建的数据一样呢？
            param.setRemarks("");
            return param;
//            throw new Exception("路口不存在：" + name);
        } else {
            return tpRoad;
        }
    }
}