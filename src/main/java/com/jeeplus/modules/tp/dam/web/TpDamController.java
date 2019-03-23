/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.modules.tp.dam.web;

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
import com.jeeplus.modules.tp.dam.entity.TpDam;
import com.jeeplus.modules.tp.dam.service.TpDamService;

/**
 * 资产Controller
 * @author 尹彬
 * @version 2019-01-09
 */
@Controller
@RequestMapping(value = "${adminPath}/tp/dam/tpDam")
public class TpDamController extends BaseController {

	@Autowired
	private TpDamService tpDamService;
	
	@ModelAttribute
	public TpDam get(@RequestParam(required=false) String id) {
		TpDam entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tpDamService.get(id);
		}
		if (entity == null){
			entity = new TpDam();
		}
		return entity;
	}
	
	/**
	 * 资产列表页面
	 */
	@RequiresPermissions("tp:dam:tpDam:list")
	@RequestMapping(value = {"list", ""})
	public String list(TpDam tpDam, Model model) {
		model.addAttribute("tpDam", tpDam);
		return "modules/tp/dam/tpDamList";
	}
	
		/**
	 * 资产列表数据
	 */
	@ResponseBody
	@RequiresPermissions("tp:dam:tpDam:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(TpDam tpDam, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TpDam> page = tpDamService.findPage(new Page<TpDam>(request, response), tpDam); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑资产表单页面
	 */
	@RequiresPermissions(value={"tp:dam:tpDam:view","tp:dam:tpDam:add","tp:dam:tpDam:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(TpDam tpDam, Model model) {
		model.addAttribute("tpDam", tpDam);
		return "modules/tp/dam/tpDamForm";
	}

	/**
	 * 保存资产
	 */
	@ResponseBody
	@RequiresPermissions(value={"tp:dam:tpDam:add","tp:dam:tpDam:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(TpDam tpDam, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(tpDam);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		tpDamService.save(tpDam);//保存
		j.setSuccess(true);
		j.setMsg("保存资产成功");
		return j;
	}
	
	/**
	 * 删除资产
	 */
	@ResponseBody
	@RequiresPermissions("tp:dam:tpDam:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(TpDam tpDam) {
		AjaxJson j = new AjaxJson();
		tpDamService.delete(tpDam);
		j.setMsg("删除资产成功");
		return j;
	}
	
	/**
	 * 批量删除资产
	 */
	@ResponseBody
	@RequiresPermissions("tp:dam:tpDam:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tpDamService.delete(tpDamService.get(id));
		}
		j.setMsg("删除资产成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("tp:dam:tpDam:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(TpDam tpDam, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "资产"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TpDam> page = tpDamService.findPage(new Page<TpDam>(request, response, -1), tpDam);
    		new ExportExcel("资产", TpDam.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出资产记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("tp:dam:tpDam:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TpDam> list = ei.getDataList(TpDam.class);
			for (TpDam tpDam : list){
				try{
					tpDamService.save(tpDam);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条资产记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条资产记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入资产失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入资产数据模板
	 */
	@ResponseBody
	@RequiresPermissions("tp:dam:tpDam:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "资产数据导入模板.xlsx";
    		List<TpDam> list = Lists.newArrayList(); 
    		new ExportExcel("资产数据", TpDam.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}