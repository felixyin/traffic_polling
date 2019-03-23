/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.modules.tp.gpshistory.web;

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
import com.jeeplus.modules.tp.gpshistory.entity.TpGpsHistory;
import com.jeeplus.modules.tp.gpshistory.service.TpGpsHistoryService;

/**
 * 历史轨迹Controller
 * @author 尹彬
 * @version 2019-01-06
 */
@Controller
@RequestMapping(value = "${adminPath}/tp/gpshistory/tpGpsHistory")
public class TpGpsHistoryController extends BaseController {

	@Autowired
	private TpGpsHistoryService tpGpsHistoryService;
	
	@ModelAttribute
	public TpGpsHistory get(@RequestParam(required=false) String id) {
		TpGpsHistory entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tpGpsHistoryService.get(id);
		}
		if (entity == null){
			entity = new TpGpsHistory();
		}
		return entity;
	}
	
	/**
	 * 历史轨迹列表页面
	 */
	@RequiresPermissions("tp:gpshistory:tpGpsHistory:list")
	@RequestMapping(value = {"list", ""})
	public String list(TpGpsHistory tpGpsHistory, Model model) {
		model.addAttribute("tpGpsHistory", tpGpsHistory);
		return "modules/tp/gpshistory/tpGpsHistoryList";
	}
	
		/**
	 * 历史轨迹列表数据
	 */
	@ResponseBody
	@RequiresPermissions("tp:gpshistory:tpGpsHistory:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(TpGpsHistory tpGpsHistory, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TpGpsHistory> page = tpGpsHistoryService.findPage(new Page<TpGpsHistory>(request, response), tpGpsHistory); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑历史轨迹表单页面
	 */
	@RequiresPermissions(value={"tp:gpshistory:tpGpsHistory:view","tp:gpshistory:tpGpsHistory:add","tp:gpshistory:tpGpsHistory:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(TpGpsHistory tpGpsHistory, Model model) {
		model.addAttribute("tpGpsHistory", tpGpsHistory);
		return "modules/tp/gpshistory/tpGpsHistoryForm";
	}

	/**
	 * 保存历史轨迹
	 */
	@ResponseBody
	@RequiresPermissions(value={"tp:gpshistory:tpGpsHistory:add","tp:gpshistory:tpGpsHistory:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(TpGpsHistory tpGpsHistory, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(tpGpsHistory);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		tpGpsHistoryService.save(tpGpsHistory);//保存
		j.setSuccess(true);
		j.setMsg("保存历史轨迹成功");
		return j;
	}
	
	/**
	 * 删除历史轨迹
	 */
	@ResponseBody
	@RequiresPermissions("tp:gpshistory:tpGpsHistory:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(TpGpsHistory tpGpsHistory) {
		AjaxJson j = new AjaxJson();
		tpGpsHistoryService.delete(tpGpsHistory);
		j.setMsg("删除历史轨迹成功");
		return j;
	}
	
	/**
	 * 批量删除历史轨迹
	 */
	@ResponseBody
	@RequiresPermissions("tp:gpshistory:tpGpsHistory:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tpGpsHistoryService.delete(tpGpsHistoryService.get(id));
		}
		j.setMsg("删除历史轨迹成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("tp:gpshistory:tpGpsHistory:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(TpGpsHistory tpGpsHistory, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "历史轨迹"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TpGpsHistory> page = tpGpsHistoryService.findPage(new Page<TpGpsHistory>(request, response, -1), tpGpsHistory);
    		new ExportExcel("历史轨迹", TpGpsHistory.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出历史轨迹记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("tp:gpshistory:tpGpsHistory:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TpGpsHistory> list = ei.getDataList(TpGpsHistory.class);
			for (TpGpsHistory tpGpsHistory : list){
				try{
					tpGpsHistoryService.save(tpGpsHistory);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条历史轨迹记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条历史轨迹记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入历史轨迹失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入历史轨迹数据模板
	 */
	@ResponseBody
	@RequiresPermissions("tp:gpshistory:tpGpsHistory:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "历史轨迹数据导入模板.xlsx";
    		List<TpGpsHistory> list = Lists.newArrayList(); 
    		new ExportExcel("历史轨迹数据", TpGpsHistory.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}