/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.modules.tp.gpsrealtime.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.common.utils.JsonUtils;
import com.jeeplus.modules.tp.cartrack.entity.TpCarTrack;
import com.jeeplus.modules.tp.gpshistory.entity.TpGpsHistory;
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
import com.jeeplus.modules.tp.gpsrealtime.entity.TpGpsRealtime;
import com.jeeplus.modules.tp.gpsrealtime.service.TpGpsRealtimeService;

/**
 * 实时轨迹Controller
 * @author 尹彬
 * @version 2019-01-05
 */
@Controller
@RequestMapping(value = "${adminPath}/tp/gpsrealtime/tpGpsRealtime")
public class TpGpsRealtimeController extends BaseController {

	@Autowired
	private TpGpsRealtimeService tpGpsRealtimeService;
	
	@ModelAttribute
	public TpGpsRealtime get(@RequestParam(required=false) String id) {
		TpGpsRealtime entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tpGpsRealtimeService.get(id);
		}
		if (entity == null){
			entity = new TpGpsRealtime();
		}
		return entity;
	}
	
	/**
	 * 实时轨迹列表页面
	 */
	@RequiresPermissions("tp:gpsrealtime:tpGpsRealtime:list")
	@RequestMapping(value = {"list", ""})
	public String list(TpGpsRealtime tpGpsRealtime, Model model) {
		model.addAttribute("tpGpsRealtime", tpGpsRealtime);
		return "modules/tp/gpsrealtime/tpGpsRealtimeList";
	}
	
		/**
	 * 实时轨迹列表数据
	 */
	@ResponseBody
	@RequiresPermissions("tp:gpsrealtime:tpGpsRealtime:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(TpGpsRealtime tpGpsRealtime, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TpGpsRealtime> page = tpGpsRealtimeService.findPage(new Page<TpGpsRealtime>(request, response), tpGpsRealtime); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑实时轨迹表单页面
	 */
	@RequiresPermissions(value={"tp:gpsrealtime:tpGpsRealtime:view","tp:gpsrealtime:tpGpsRealtime:add","tp:gpsrealtime:tpGpsRealtime:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(TpGpsRealtime tpGpsRealtime, Model model) {
		model.addAttribute("tpGpsRealtime", tpGpsRealtime);
		return "modules/tp/gpsrealtime/tpGpsRealtimeForm";
	}

	/**
	 * 保存实时轨迹
	 */
	@ResponseBody
	@RequiresPermissions(value={"tp:gpsrealtime:tpGpsRealtime:add","tp:gpsrealtime:tpGpsRealtime:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(TpGpsRealtime tpGpsRealtime, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(tpGpsRealtime);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		tpGpsRealtimeService.save(tpGpsRealtime);//保存
		j.setSuccess(true);
		j.setMsg("保存实时轨迹成功");
		return j;
	}
	
	/**
	 * 删除实时轨迹
	 */
	@ResponseBody
	@RequiresPermissions("tp:gpsrealtime:tpGpsRealtime:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(TpGpsRealtime tpGpsRealtime) {
		AjaxJson j = new AjaxJson();
		tpGpsRealtimeService.delete(tpGpsRealtime);
		j.setMsg("删除实时轨迹成功");
		return j;
	}
	
	/**
	 * 批量删除实时轨迹
	 */
	@ResponseBody
	@RequiresPermissions("tp:gpsrealtime:tpGpsRealtime:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tpGpsRealtimeService.delete(tpGpsRealtimeService.get(id));
		}
		j.setMsg("删除实时轨迹成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("tp:gpsrealtime:tpGpsRealtime:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(TpGpsRealtime tpGpsRealtime, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "实时轨迹"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TpGpsRealtime> page = tpGpsRealtimeService.findPage(new Page<TpGpsRealtime>(request, response, -1), tpGpsRealtime);
    		new ExportExcel("实时轨迹", TpGpsRealtime.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出实时轨迹记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("tp:gpsrealtime:tpGpsRealtime:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TpGpsRealtime> list = ei.getDataList(TpGpsRealtime.class);
			for (TpGpsRealtime tpGpsRealtime : list){
				try{
					tpGpsRealtimeService.save(tpGpsRealtime);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条实时轨迹记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条实时轨迹记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入实时轨迹失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入实时轨迹数据模板
	 */
	@ResponseBody
	@RequiresPermissions("tp:gpsrealtime:tpGpsRealtime:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "实时轨迹数据导入模板.xlsx";
    		List<TpGpsRealtime> list = Lists.newArrayList(); 
    		new ExportExcel("实时轨迹数据", TpGpsRealtime.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}