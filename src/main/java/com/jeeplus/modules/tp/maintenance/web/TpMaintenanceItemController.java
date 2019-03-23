/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.modules.tp.maintenance.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

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
import com.jeeplus.modules.tp.maintenance.entity.TpMaintenanceItem;
import com.jeeplus.modules.tp.maintenance.service.TpMaintenanceItemService;

/**
 * 施工物料Controller
 * @author 尹彬
 * @version 2018-12-21
 */
@Controller
@RequestMapping(value = "${adminPath}/tp/maintenance/tpMaintenanceItem")
public class TpMaintenanceItemController extends BaseController {

	@Autowired
	private TpMaintenanceItemService tpMaintenanceItemService;
	
	@ModelAttribute
	public TpMaintenanceItem get(@RequestParam(required=false) String id) {
		TpMaintenanceItem entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tpMaintenanceItemService.get(id);
		}
		if (entity == null){
			entity = new TpMaintenanceItem();
		}
		return entity;
	}
	
	/**
	 * 施工物料列表页面
	 */
	@RequiresPermissions("tp:maintenance:tpMaintenanceItem:list")
	@RequestMapping(value = {"list", ""})
	public String list(TpMaintenanceItem tpMaintenanceItem, Model model) {
		model.addAttribute("tpMaintenanceItem", tpMaintenanceItem);
		return "modules/tp/maintenance/tpMaintenanceItemList";
	}
	
		/**
	 * 施工物料列表数据
	 */
	@ResponseBody
	@RequiresPermissions("tp:maintenance:tpMaintenanceItem:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(TpMaintenanceItem tpMaintenanceItem, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TpMaintenanceItem> page = tpMaintenanceItemService.findPage(new Page<TpMaintenanceItem>(request, response), tpMaintenanceItem); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑施工物料表单页面
	 */
	@RequiresPermissions(value={"tp:maintenance:tpMaintenanceItem:view","tp:maintenance:tpMaintenanceItem:add","tp:maintenance:tpMaintenanceItem:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(TpMaintenanceItem tpMaintenanceItem, Model model) {
		model.addAttribute("tpMaintenanceItem", tpMaintenanceItem);
		return "modules/tp/maintenance/tpMaintenanceItemForm";
	}

	/**
	 * 保存施工物料
	 */
	@ResponseBody
	@RequiresPermissions(value={"tp:maintenance:tpMaintenanceItem:add","tp:maintenance:tpMaintenanceItem:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(TpMaintenanceItem tpMaintenanceItem, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(tpMaintenanceItem);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		tpMaintenanceItemService.save(tpMaintenanceItem);//保存
		j.setSuccess(true);
		j.setMsg("保存施工物料成功");
		return j;
	}
	
	/**
	 * 删除施工物料
	 */
	@ResponseBody
	@RequiresPermissions("tp:maintenance:tpMaintenanceItem:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(TpMaintenanceItem tpMaintenanceItem) {
		AjaxJson j = new AjaxJson();
		tpMaintenanceItemService.delete(tpMaintenanceItem);
		j.setMsg("删除施工物料成功");
		return j;
	}
	
	/**
	 * 批量删除施工物料
	 */
	@ResponseBody
	@RequiresPermissions("tp:maintenance:tpMaintenanceItem:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tpMaintenanceItemService.delete(tpMaintenanceItemService.get(id));
		}
		j.setMsg("删除施工物料成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("tp:maintenance:tpMaintenanceItem:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(TpMaintenanceItem tpMaintenanceItem, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "施工物料"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TpMaintenanceItem> page = tpMaintenanceItemService.findPage(new Page<TpMaintenanceItem>(request, response, -1), tpMaintenanceItem);
    		new ExportExcel("施工物料", TpMaintenanceItem.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出施工物料记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("tp:maintenance:tpMaintenanceItem:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TpMaintenanceItem> list = ei.getDataList(TpMaintenanceItem.class);
			for (TpMaintenanceItem tpMaintenanceItem : list){
				try{
					tpMaintenanceItemService.save(tpMaintenanceItem);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条施工物料记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条施工物料记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入施工物料失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入施工物料数据模板
	 */
	@ResponseBody
	@RequiresPermissions("tp:maintenance:tpMaintenanceItem:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "施工物料数据导入模板.xlsx";
    		List<TpMaintenanceItem> list = Lists.newArrayList(); 
    		new ExportExcel("施工物料数据", TpMaintenanceItem.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}