/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.modules.tp.material.web;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.common.utils.text.Charsets;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jgroups.protocols.EXAMPLE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
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
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.tp.material.entity.TpMaterialPart;
import com.jeeplus.modules.tp.material.service.TpMaterialPartService;

/**
 * 零件Controller
 *
 * @author 尹彬
 * @version 2018-12-21
 */
@Controller
@RequestMapping(value = "${adminPath}/tp/material/tpMaterialPart")
public class TpMaterialPartController extends BaseController {

    @Autowired
    private TpMaterialPartService tpMaterialPartService;

    @ModelAttribute
    public TpMaterialPart get(@RequestParam(required = false) String id) {
        TpMaterialPart entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = tpMaterialPartService.get(id);
        }
        if (entity == null) {
            entity = new TpMaterialPart();
        }
        return entity;
    }

    /**
     * 零件列表页面
     */
    @RequiresPermissions("tp:material:tpMaterialPart:list")
    @RequestMapping(value = {"list", ""})
    public String list(TpMaterialPart tpMaterialPart, Model model) {
        model.addAttribute("tpMaterialPart", tpMaterialPart);
        return "modules/tp/material/tpMaterialPartList";
    }

    /**
     * 零件列表数据
     */
    @ResponseBody
    @RequiresPermissions("tp:material:tpMaterialPart:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(TpMaterialPart tpMaterialPart, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<TpMaterialPart> page = tpMaterialPartService.findPage(new Page<TpMaterialPart>(request, response), tpMaterialPart);
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑零件表单页面
     */
    @RequiresPermissions(value = {"tp:material:tpMaterialPart:view", "tp:material:tpMaterialPart:add", "tp:material:tpMaterialPart:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form/{mode}")
    public String form(@PathVariable String mode, TpMaterialPart tpMaterialPart, Model model) {
        model.addAttribute("tpMaterialPart", tpMaterialPart);
        model.addAttribute("mode", mode);
        return "modules/tp/material/tpMaterialPartForm";
    }

    /**
     * 保存零件
     */
    @ResponseBody
    @RequiresPermissions(value = {"tp:material:tpMaterialPart:add", "tp:material:tpMaterialPart:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(TpMaterialPart tpMaterialPart, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        /**
         * 后台hibernate-validation插件校验
         */
        String errMsg = beanValidator(tpMaterialPart);
        if (StringUtils.isNotBlank(errMsg)) {
            j.setSuccess(false);
            j.setMsg(errMsg);
            return j;
        }
        //新增或编辑表单保存
        tpMaterialPartService.save(tpMaterialPart);//保存
        j.setSuccess(true);
        j.setMsg("保存零件成功");
        return j;
    }

    /**
     * 删除零件
     */
    @ResponseBody
    @RequiresPermissions("tp:material:tpMaterialPart:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(TpMaterialPart tpMaterialPart) {
        AjaxJson j = new AjaxJson();
        tpMaterialPartService.delete(tpMaterialPart);
        j.setMsg("删除零件成功");
        return j;
    }

    /**
     * 批量删除零件
     */
    @ResponseBody
    @RequiresPermissions("tp:material:tpMaterialPart:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            tpMaterialPartService.delete(tpMaterialPartService.get(id));
        }
        j.setMsg("删除零件成功");
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("tp:material:tpMaterialPart:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(TpMaterialPart tpMaterialPart, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "零件" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<TpMaterialPart> page = tpMaterialPartService.findPage(new Page<TpMaterialPart>(request, response, -1), tpMaterialPart);
            new ExportExcel("零件", TpMaterialPart.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出零件记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @ResponseBody
    @RequiresPermissions("tp:material:tpMaterialPart:import")
    @RequestMapping(value = "import")
    public AjaxJson importFile(@RequestParam("file") MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<TpMaterialPart> list = ei.getDataList(TpMaterialPart.class);
            for (TpMaterialPart tpMaterialPart : list) {
                try {
                    tpMaterialPartService.save(tpMaterialPart);
                    successNum++;
                } catch (ConstraintViolationException ex) {
                    failureNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条零件记录。");
            }
            j.setMsg("已成功导入 " + successNum + " 条零件记录" + failureMsg);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导入零件失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 下载导入零件数据模板
     */
    @ResponseBody
    @RequiresPermissions("tp:material:tpMaterialPart:import")
    @RequestMapping(value = "import/template")
    public AjaxJson importFileTemplate(HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "零件数据导入模板.xlsx";
            List<TpMaterialPart> list = Lists.newArrayList();
            new ExportExcel("零件数据", TpMaterialPart.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导入模板下载失败！失败信息：" + e.getMessage());
        }
        return j;
    }


    /**
     */
    @ResponseBody
    @RequestMapping(value = "autocomplete")
    public List<Map<String, String>> autocomplete(String query) throws Exception {
        return tpMaterialPartService.autocomplete(URLDecoder.decode(query, Charsets.UTF_8_NAME).trim());
    }


    /**
     */
    @ResponseBody
    @RequestMapping(value = "getById")
    public TpMaterialPart getById(TpMaterialPart tpMaterialPart) {
        return tpMaterialPart;
    }


}