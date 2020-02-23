/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.modules.tp.cartrack.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.common.utils.JsonUtils;
import com.jeeplus.modules.tp.gpshistory.entity.TpGpsHistory;
import com.jeeplus.modules.tp.gpshistory.service.TpGpsHistoryService;
import com.jeeplus.modules.tp.gpsrealtime.service.TpGpsRealtimeService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.tp.cartrack.entity.TpCarTrack;
import com.jeeplus.modules.tp.cartrack.service.TpCarTrackService;

/**
 * 出车记录Controller
 *
 * @author 尹彬
 * @version 2019-01-05
 */
@Controller
@RequestMapping(value = "${adminPath}/tp/cartrack/tpCarTrack")
public class TpCarTrackController extends BaseController {

    @Autowired
    private TpCarTrackService tpCarTrackService;

    @Autowired
    private TpGpsHistoryService tpGpsHistoryService;

    @Autowired
    private TpGpsRealtimeService tpGpsRealtimeService;

    @ModelAttribute
    public TpCarTrack get(@RequestParam(required = false) String id) {
        TpCarTrack entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = tpCarTrackService.get(id);
        }
        if (entity == null) {
            entity = new TpCarTrack();
        }
        return entity;
    }

    /**
     * 出车记录列表页面
     */
    @RequiresPermissions("tp:cartrack:tpCarTrack:list")
    @RequestMapping(value = {"list", ""})
    public String list(TpCarTrack tpCarTrack, Model model) {
        model.addAttribute("tpCarTrack", tpCarTrack);
        return "modules/tp/cartrack/tpCarTrackList";
    }

    /**
     * 出车记录列表数据
     */
    @ResponseBody
    @RequiresPermissions("tp:cartrack:tpCarTrack:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(TpCarTrack tpCarTrack, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<TpCarTrack> page = tpCarTrackService.findPage(new Page<TpCarTrack>(request, response), tpCarTrack);
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑出车记录表单页面
     */
    @RequiresPermissions(value = {"tp:cartrack:tpCarTrack:view", "tp:cartrack:tpCarTrack:add", "tp:cartrack:tpCarTrack:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(TpCarTrack tpCarTrack, Model model) {
        model.addAttribute("tpCarTrack", tpCarTrack);
        return "modules/tp/cartrack/tpCarTrackForm";
    }

    /**
     * 保存出车记录
     */
    @ResponseBody
    @RequiresPermissions(value = {"tp:cartrack:tpCarTrack:add", "tp:cartrack:tpCarTrack:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(TpCarTrack tpCarTrack, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        /**
         * 后台hibernate-validation插件校验
         */
        String errMsg = beanValidator(tpCarTrack);
        if (StringUtils.isNotBlank(errMsg)) {
            j.setSuccess(false);
            j.setMsg(errMsg);
            return j;
        }
        //新增或编辑表单保存
        tpCarTrackService.save(tpCarTrack);//保存
        j.setSuccess(true);
        j.setMsg("保存出车记录成功");
        return j;
    }

    /**
     * 删除出车记录
     */
    @ResponseBody
    @RequiresPermissions("tp:cartrack:tpCarTrack:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(TpCarTrack tpCarTrack) {
        AjaxJson j = new AjaxJson();
        tpCarTrackService.delete(tpCarTrack);
        j.setMsg("删除出车记录成功");
        return j;
    }

    /**
     * 批量删除出车记录
     */
    @ResponseBody
    @RequiresPermissions("tp:cartrack:tpCarTrack:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            tpCarTrackService.delete(tpCarTrackService.get(id));
        }
        j.setMsg("删除出车记录成功");
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("tp:cartrack:tpCarTrack:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(TpCarTrack tpCarTrack, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "出车记录" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<TpCarTrack> page = tpCarTrackService.findPage(new Page<TpCarTrack>(request, response, -1), tpCarTrack);
            new ExportExcel("出车记录", TpCarTrack.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出出车记录记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @ResponseBody
    @RequiresPermissions("tp:cartrack:tpCarTrack:import")
    @RequestMapping(value = "import")
    public AjaxJson importFile(@RequestParam("file") MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<TpCarTrack> list = ei.getDataList(TpCarTrack.class);
            for (TpCarTrack tpCarTrack : list) {
                try {
                    tpCarTrackService.save(tpCarTrack);
                    successNum++;
                } catch (ConstraintViolationException ex) {
                    failureNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条出车记录记录。");
            }
            j.setMsg("已成功导入 " + successNum + " 条出车记录记录" + failureMsg);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导入出车记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 下载导入出车记录数据模板
     */
    @ResponseBody
    @RequiresPermissions("tp:cartrack:tpCarTrack:import")
    @RequestMapping(value = "import/template")
    public AjaxJson importFileTemplate(HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "出车记录数据导入模板.xlsx";
            List<TpCarTrack> list = Lists.newArrayList();
            new ExportExcel("出车记录数据", TpCarTrack.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导入模板下载失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 查看历史轨迹
     */
//	@RequiresPermissions("tp:cartrack:tpCarTrack:selectGpsHistory")
    @RequestMapping(value = {"selectGpsHistory"})
    public String selectGpsHistory(TpCarTrack tpCarTrack, Model model) {
        List<TpGpsHistory> gpsHistortoryList = tpGpsHistoryService.findListByCarTrackId(tpCarTrack.getId());
        List<String[]> list = new ArrayList();
        for (TpGpsHistory gpsHistory1 : gpsHistortoryList) {
            list.add(new String[]{gpsHistory1.getLonGD(), gpsHistory1.getLatGD()});
        }
        String  gpsHistories = JsonUtils.objectToJson(list);
        model.addAttribute("gpsHistories", gpsHistories);
        model.addAttribute("tpCarTrack", tpCarTrack);
        return "modules/tp/cartrack/tpShowGpsHistory";
    }

    /**
     * 查看实时轨迹
     * @see spring-context-websocket.xml MyWebSocketHandler MyHandshakeInterceptor
     */
    @Deprecated
	@RequiresPermissions("tp:cartrack:tpCarTrack:showGpsRealtime")
    @RequestMapping(value = {"showGpsRealtime"})
    public String showGpsRealtime(Model model) {
        return "modules/tp/cartrack/tpShowGpsRealtime";
    }

    /**
     * 第一次打开实时轨迹大瓶，初始化位置
     * @return
     */
    @ResponseBody
//    @RequiresPermissions("tp:cartrack:tpCarTrack:getGpsList")
    @RequestMapping(value = "getGpsList")
    public Map<String, Map<String, Object>> getGpsList(){
        Map<String, Map<String, Object>> stringMapMap = tpGpsRealtimeService.loadRealtimeGpsMap();
        return stringMapMap;
    }

    /**
     * 查看最后定位地点
     */
//	@RequiresPermissions("tp:maintenance:tpMaintenance:selectPostion")
    @RequestMapping(value = {"showPostion"})
    public String showPostion(String roadcrossName, String location, Model model) {
        model.addAttribute("roadcrossName", roadcrossName);
        model.addAttribute("location", location);
        return "modules/tp/cartrack/tpShowPostion";
    }



}