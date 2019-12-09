/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.modules.tp.maintenance.web;

import com.google.common.collect.Lists;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.Encodes;
import com.jeeplus.common.utils.JsonUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.common.utils.text.Charsets;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.tp.maintenance.entity.TpExportReport;
import com.jeeplus.modules.tp.maintenance.entity.TpMaintenance;
import com.jeeplus.modules.tp.maintenance.gdbean.PositionRootBean;
import com.jeeplus.modules.tp.maintenance.service.TpMaintenanceService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.*;

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
        if (null == tpMaintenance.getSendDate()) {
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
     * 导出物料统计报表
     */
    @ResponseBody
//    @RequiresPermissions("tp:maintenance:tpMaintenance:exportReport")
    @RequiresPermissions("tp:maintenance:tpMaintenance:export")
    @RequestMapping(value = "exportReport")
    public AjaxJson _exportFile(TpMaintenance tpMaintenance, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "物料统计-" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            List<TpExportReport> list= tpMaintenanceService.findTongJiList(tpMaintenance);
            new ExportExcel("物料统计", TpExportReport.class).setDataList(list).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出物料统计数据失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导出excel报表
     */
    @RequiresPermissions("tp:maintenance:tpMaintenance:export")
    @RequestMapping(value = "export")
    public void exportReportFile(TpMaintenance tpMaintenance, HttpServletRequest request, HttpServletResponse response) throws IOException {
//        AjaxJson j = new AjaxJson();
//        try {
        String fileSuffix = "";
        Date beginSendDate = tpMaintenance.getBeginSendDate();
        if (null != beginSendDate) {
            fileSuffix = DateUtils.formatDate(beginSendDate, "YYYY年MM月份-");
        }
        String fileName = fileSuffix + "交通设施维保记录-" + DateUtils.getDate("ddHHmmss") + ".xlsx";

        response.reset();
        response.setContentType("application/octet-stream; charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + Encodes.urlEncode(fileName));
        ServletOutputStream outputStream = response.getOutputStream();

//         1、根据搜索条件查询数据
        List<TpMaintenance> tpMaintenanceList = tpMaintenanceService.findReportData(tpMaintenance);

//         2、导入模板，下载
        InputStream is = TpMaintenanceController.class.getResourceAsStream("/template1.xlsx");
        Context context = new Context();
        context.putVar("tpmList", tpMaintenanceList);
        context.putVar("monthStr", fileSuffix);
        JxlsHelper.getInstance().processTemplate(is, outputStream, context);

//            j.setSuccess(true);
//            j.setMsg("导出成功！");
//            return j;
//        } catch (Exception e) {
//            j.setSuccess(false);
//            j.setMsg("导出施工记录失败！失败信息：" + e.getMessage());
//        }
//        return j;
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

        PositionRootBean bean = JsonUtils.jsonToObject(json, PositionRootBean.class);
        TpMaintenance tpMaintenance = tpMaintenanceService.savePosition(bean);

        LinkedHashMap map = new LinkedHashMap();
        map.put("tpMaintenance", tpMaintenance);
        j.setBody(map);
        j.setMsg("位置保存成功");
        return j;
    }


    /**
     * 统计列表页面
     */
    @RequiresPermissions("tp:maintenance:tpMaintenance:tongJi")
    @RequestMapping(value = {"tongJi"})
    public String tongji(TpMaintenance tpMaintenance, Model model) {
        model.addAttribute("tpMaintenance", tpMaintenance);
        return "modules/tp/maintenance/tpTongJiList";
    }

    /**
     * 统计列表数据
     */
    @ResponseBody
    @RequiresPermissions("tp:maintenance:tpMaintenance:tongJi")
    @RequestMapping(value = "tongJidata")
    public Map<String, Object> tongJiData(TpMaintenance tpMaintenance, HttpServletRequest request, HttpServletResponse response, Model model) {
        List<TpExportReport> tongJiList= tpMaintenanceService.findTongJiList(tpMaintenance);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("rows",tongJiList);
        return map;
    }

}