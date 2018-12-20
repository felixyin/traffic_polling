/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tp.guardrail.web;

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
import com.jeeplus.modules.tp.guardrail.entity.TpGuardrail;
import com.jeeplus.modules.tp.guardrail.service.TpGuardrailService;

/**
 * 护栏Controller
 * @author 尹彬
 * @version 2018-12-19
 */
@Controller
@RequestMapping(value = "${adminPath}/tp/guardrail/tpGuardrail")
public class TpGuardrailController extends BaseController {

	@Autowired
	private TpGuardrailService tpGuardrailService;
	
	@ModelAttribute
	public TpGuardrail get(@RequestParam(required=false) String id) {
		TpGuardrail entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tpGuardrailService.get(id);
		}
		if (entity == null){
			entity = new TpGuardrail();
		}
		return entity;
	}
	
	/**
	 * 护栏列表页面
	 */
	@RequiresPermissions("tp:guardrail:tpGuardrail:list")
	@RequestMapping(value = {"list", ""})
	public String list(TpGuardrail tpGuardrail, Model model) {
		model.addAttribute("tpGuardrail", tpGuardrail);
		return "modules/tp/guardrail/tpGuardrailList";
	}
	
		/**
	 * 护栏列表数据
	 */
	@ResponseBody
	@RequiresPermissions("tp:guardrail:tpGuardrail:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(TpGuardrail tpGuardrail, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TpGuardrail> page = tpGuardrailService.findPage(new Page<TpGuardrail>(request, response), tpGuardrail); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑护栏表单页面
	 */
	@RequiresPermissions(value={"tp:guardrail:tpGuardrail:view","tp:guardrail:tpGuardrail:add","tp:guardrail:tpGuardrail:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(TpGuardrail tpGuardrail, Model model) {
		model.addAttribute("tpGuardrail", tpGuardrail);
		return "modules/tp/guardrail/tpGuardrailForm";
	}

	/**
	 * 保存护栏
	 */
	@ResponseBody
	@RequiresPermissions(value={"tp:guardrail:tpGuardrail:add","tp:guardrail:tpGuardrail:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(TpGuardrail tpGuardrail, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(tpGuardrail);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		tpGuardrailService.save(tpGuardrail);//保存
		j.setSuccess(true);
		j.setMsg("保存护栏成功");
		return j;
	}
	
	/**
	 * 删除护栏
	 */
	@ResponseBody
	@RequiresPermissions("tp:guardrail:tpGuardrail:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(TpGuardrail tpGuardrail) {
		AjaxJson j = new AjaxJson();
		tpGuardrailService.delete(tpGuardrail);
		j.setMsg("删除护栏成功");
		return j;
	}
	
	/**
	 * 批量删除护栏
	 */
	@ResponseBody
	@RequiresPermissions("tp:guardrail:tpGuardrail:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tpGuardrailService.delete(tpGuardrailService.get(id));
		}
		j.setMsg("删除护栏成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("tp:guardrail:tpGuardrail:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(TpGuardrail tpGuardrail, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "护栏"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TpGuardrail> page = tpGuardrailService.findPage(new Page<TpGuardrail>(request, response, -1), tpGuardrail);
    		new ExportExcel("护栏", TpGuardrail.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出护栏记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("tp:guardrail:tpGuardrail:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TpGuardrail> list = ei.getDataList(TpGuardrail.class);
			for (TpGuardrail tpGuardrail : list){
				try{
					tpGuardrailService.save(tpGuardrail);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条护栏记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条护栏记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入护栏失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入护栏数据模板
	 */
	@ResponseBody
	@RequiresPermissions("tp:guardrail:tpGuardrail:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "护栏数据导入模板.xlsx";
    		List<TpGuardrail> list = Lists.newArrayList(); 
    		new ExportExcel("护栏数据", TpGuardrail.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}