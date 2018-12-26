/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tp.maintenance.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.common.utils.JsonUtils;
import com.jeeplus.common.utils.base.PropertiesUtil;
import com.jeeplus.common.utils.text.Charsets;
import com.jeeplus.modules.iim.entity.LayGroup;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.tp.road.entity.SysArea;
import com.jeeplus.modules.tp.road.service.SysAreaService;
import com.jeeplus.modules.tp.road.service.TpRoadService;
import com.jeeplus.modules.tp.roadcross.service.TpRoadCrossingService;
import jsonscn.json2bean.Addresscomponent;
import jsonscn.json2bean.PositionRootBean;
import jsonscn.json2bean.Regeocode;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.tp.maintenance.entity.TpMaintenance;
import com.jeeplus.modules.tp.maintenance.service.TpMaintenanceService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 施工Controller
 *
 * @author 尹彬
 * @version 2018-12-22
 */
@Controller
@RequestMapping(value = "${adminPath}/tp/maintenance/tpMaintenance")
public class TpMaintenanceController extends BaseController {

    @Autowired
    private TpMaintenanceService tpMaintenanceService;

    @ModelAttribute
    public TpMaintenance get(@RequestParam(required = false) String id) {
        TpMaintenance entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = tpMaintenanceService.get(id);
        }
        if (entity == null) {
            entity = new TpMaintenance();
        }
        return entity;
    }

    /**
     * 施工列表页面
     */
    @RequiresPermissions("tp:maintenance:tpMaintenance:list")
    @RequestMapping(value = {"list", ""})
    public String list(TpMaintenance tpMaintenance, Model model) {
        model.addAttribute("tpMaintenance", tpMaintenance);
        return "modules/tp/maintenance/tpMaintenanceList";
    }

    /**
     * 施工列表数据
     */
    @ResponseBody
    @RequiresPermissions("tp:maintenance:tpMaintenance:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(TpMaintenance tpMaintenance, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<TpMaintenance> page = tpMaintenanceService.findPage(new Page<TpMaintenance>(request, response), tpMaintenance);
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑施工表单页面
     */
    @RequiresPermissions(value = {"tp:maintenance:tpMaintenance:view", "tp:maintenance:tpMaintenance:add", "tp:maintenance:tpMaintenance:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form/{mode}")
    public String form(@PathVariable String mode, TpMaintenance tpMaintenance, Model model) {
        User currentUser = UserUtils.getUser();
        if (null == tpMaintenance.getSendBy()) {
            tpMaintenance.setSendBy(currentUser);
        }
        if(null == tpMaintenance.getSendDate()){
            tpMaintenance.setSendDate(new Date());
        }
        model.addAttribute("tpMaintenance", tpMaintenance);
        model.addAttribute("mode", mode);
        return "modules/tp/maintenance/tpMaintenanceForm";
    }

    /**
     * 保存施工
     */
    @ResponseBody
    @RequiresPermissions(value = {"tp:maintenance:tpMaintenance:add", "tp:maintenance:tpMaintenance:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(TpMaintenance tpMaintenance, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        /**
         * 后台hibernate-validation插件校验
         */
        String errMsg = beanValidator(tpMaintenance);
        if (StringUtils.isNotBlank(errMsg)) {
            j.setSuccess(false);
            j.setMsg(errMsg);
            return j;
        }
        //新增或编辑表单保存
        tpMaintenanceService.save(tpMaintenance);//保存
        j.setSuccess(true);
        j.setMsg("保存施工成功");
        return j;
    }

    /**
     * 删除施工
     */
    @ResponseBody
    @RequiresPermissions("tp:maintenance:tpMaintenance:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(TpMaintenance tpMaintenance) {
        AjaxJson j = new AjaxJson();
        tpMaintenanceService.delete(tpMaintenance);
        j.setMsg("删除施工成功");
        return j;
    }

    /**
     * 批量删除施工
     */
    @ResponseBody
    @RequiresPermissions("tp:maintenance:tpMaintenance:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            tpMaintenanceService.delete(tpMaintenanceService.get(id));
        }
        j.setMsg("删除施工成功");
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("tp:maintenance:tpMaintenance:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(TpMaintenance tpMaintenance, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "施工" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<TpMaintenance> page = tpMaintenanceService.findPage(new Page<TpMaintenance>(request, response, -1), tpMaintenance);
            new ExportExcel("施工", TpMaintenance.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出施工记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "detail")
    public TpMaintenance detail(String id) {
        return tpMaintenanceService.get(id);
    }


    /**
     * 导入Excel数据
     */
    @ResponseBody
    @RequiresPermissions("tp:maintenance:tpMaintenance:import")
    @RequestMapping(value = "import")
    public AjaxJson importFile(@RequestParam("file") MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<TpMaintenance> list = ei.getDataList(TpMaintenance.class);
            for (TpMaintenance tpMaintenance : list) {
                try {
                    tpMaintenanceService.save(tpMaintenance);
                    successNum++;
                } catch (ConstraintViolationException ex) {
                    failureNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条施工记录。");
            }
            j.setMsg("已成功导入 " + successNum + " 条施工记录" + failureMsg);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导入施工失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 下载导入施工数据模板
     */
    @ResponseBody
    @RequiresPermissions("tp:maintenance:tpMaintenance:import")
    @RequestMapping(value = "import/template")
    public AjaxJson importFileTemplate(HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "施工数据导入模板.xlsx";
            List<TpMaintenance> list = Lists.newArrayList();
            new ExportExcel("施工数据", TpMaintenance.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导入模板下载失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 选择地点
     */
//	@RequiresPermissions("tp:maintenance:tpMaintenance:selectPostion")
    @RequestMapping(value = {"selectPostion"})
    public String selectPosition(String roadcrossName, String location, Model model) {
        model.addAttribute("roadcrossName", roadcrossName);
        model.addAttribute("location", location);
        return "modules/tp/maintenance/tpSelectPostion";
    }

    /**
     * 保存群组
     */
    @ResponseBody
    @RequestMapping(value = "savePosition")
    public AjaxJson savePosition(@RequestParam(required = false) String json) throws Exception {
        AjaxJson j = new AjaxJson();
        json = URLDecoder.decode(json, Charsets.UTF_8_NAME);
        System.out.println(json);

        PositionRootBean bean = JsonUtils.jsonToObject(json, PositionRootBean.class);
        TpMaintenance tpMaintenance = tpMaintenanceService.savePosition(bean);

        LinkedHashMap map = new LinkedHashMap();
        map.put("tpMaintenance", tpMaintenance);
        j.setBody(map);
        j.setMsg("位置保存成功");
        return j;
    }

}