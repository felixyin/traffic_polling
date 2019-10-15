/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.modules.tp.roadcross.web;

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
import com.jeeplus.modules.tp.roadcross.entity.TpRoadCrossing;
import com.jeeplus.modules.tp.roadcross.service.TpRoadCrossingService;

/**
 * 路口管理Controller
 * @author 尹彬
 * @version 2018-12-22
 */
@Controller
@RequestMapping(value = "${adminPath}/tp/roadcross/tpRoadCrossing")
public class TpRoadCrossingController extends BaseController {

	@Autowired
	private TpRoadCrossingService tpRoadCrossingService;
	
	@ModelAttribute
	public TpRoadCrossing get(@RequestParam(required=false) String id) {
		TpRoadCrossing entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tpRoadCrossingService.get(id);
		}
		if (entity == null){
			entity = new TpRoadCrossing();
		}
		return entity;
	}
	
	/**
	 * 路口管理列表页面
	 */
	@RequiresPermissions("tp:roadcross:tpRoadCrossing:list")
	@RequestMapping(value = {"list", ""})
	public String list(TpRoadCrossing tpRoadCrossing, Model model) {
		model.addAttribute("tpRoadCrossing", tpRoadCrossing);
		return "modules/tp/roadcross/tpRoadCrossingList";
	}
	
		/**
	 * 路口管理列表数据
	 */
	@ResponseBody
	@RequiresPermissions("tp:roadcross:tpRoadCrossing:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(TpRoadCrossing tpRoadCrossing, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TpRoadCrossing> page = tpRoadCrossingService.findPage(new Page<TpRoadCrossing>(request, response), tpRoadCrossing); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑路口管理表单页面
	 */
	@RequiresPermissions(value={"tp:roadcross:tpRoadCrossing:view","tp:roadcross:tpRoadCrossing:add","tp:roadcross:tpRoadCrossing:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(TpRoadCrossing tpRoadCrossing, Model model) {
		model.addAttribute("tpRoadCrossing", tpRoadCrossing);
		return "modules/tp/roadcross/tpRoadCrossingForm";
	}

	/**
	 * 保存路口管理
	 */
	@ResponseBody
	@RequiresPermissions(value={"tp:roadcross:tpRoadCrossing:add","tp:roadcross:tpRoadCrossing:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(TpRoadCrossing tpRoadCrossing, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(tpRoadCrossing);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		tpRoadCrossingService.save(tpRoadCrossing);//保存
		j.setSuccess(true);
		j.setMsg("保存路口管理成功");
		return j;
	}
	
	/**
	 * 删除路口管理
	 */
	@ResponseBody
	@RequiresPermissions("tp:roadcross:tpRoadCrossing:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(TpRoadCrossing tpRoadCrossing) {
		AjaxJson j = new AjaxJson();
		tpRoadCrossingService.delete(tpRoadCrossing);
		j.setMsg("删除路口管理成功");
		return j;
	}
	
	/**
	 * 批量删除路口管理
	 */
	@ResponseBody
	@RequiresPermissions("tp:roadcross:tpRoadCrossing:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tpRoadCrossingService.delete(tpRoadCrossingService.get(id));
		}
		j.setMsg("删除路口管理成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("tp:roadcross:tpRoadCrossing:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(TpRoadCrossing tpRoadCrossing, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "路口管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TpRoadCrossing> page = tpRoadCrossingService.findPage(new Page<TpRoadCrossing>(request, response, -1), tpRoadCrossing);
    		new ExportExcel("路口管理", TpRoadCrossing.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出路口管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("tp:roadcross:tpRoadCrossing:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TpRoadCrossing> list = ei.getDataList(TpRoadCrossing.class);
			for (TpRoadCrossing tpRoadCrossing : list){
				try{
					tpRoadCrossingService.save(tpRoadCrossing);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条路口管理记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条路口管理记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入路口管理失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入路口管理数据模板
	 */
	@ResponseBody
	@RequiresPermissions("tp:roadcross:tpRoadCrossing:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "路口管理数据导入模板.xlsx";
    		List<TpRoadCrossing> list = Lists.newArrayList(); 
    		new ExportExcel("路口管理数据", TpRoadCrossing.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}