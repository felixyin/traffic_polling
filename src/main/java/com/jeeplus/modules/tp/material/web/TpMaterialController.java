/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.modules.tp.material.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
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
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.tp.material.entity.TpMaterial;
import com.jeeplus.modules.tp.material.service.TpMaterialService;

/**
 * 物料Controller
 * @author 尹彬
 * @version 2018-12-23
 */
@Controller
@RequestMapping(value = "${adminPath}/tp/material/tpMaterial")
public class TpMaterialController extends BaseController {

	@Autowired
	private TpMaterialService tpMaterialService;
	
	@ModelAttribute
	public TpMaterial get(@RequestParam(required=false) String id) {
		TpMaterial entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tpMaterialService.get(id);
		}
		if (entity == null){
			entity = new TpMaterial();
		}
		return entity;
	}
	
	/**
	 * 物料列表页面
	 */
	@RequiresPermissions("tp:material:tpMaterial:list")
	@RequestMapping(value = {"list", ""})
	public String list(TpMaterial tpMaterial, Model model) {
		model.addAttribute("tpMaterial", tpMaterial);
		return "modules/tp/material/tpMaterialList";
	}
	
		/**
	 * 物料列表数据
	 */
	@ResponseBody
	@RequiresPermissions("tp:material:tpMaterial:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(TpMaterial tpMaterial, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TpMaterial> page = tpMaterialService.findPage(new Page<TpMaterial>(request, response), tpMaterial); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑物料表单页面
	 */
	@RequiresPermissions(value={"tp:material:tpMaterial:view","tp:material:tpMaterial:add","tp:material:tpMaterial:edit"},logical=Logical.OR)
	@RequestMapping(value = "form/{mode}")
	public String form(@PathVariable String mode, TpMaterial tpMaterial, Model model) {
		model.addAttribute("tpMaterial", tpMaterial);
		model.addAttribute("mode", mode);
		return "modules/tp/material/tpMaterialForm";
	}

	/**
	 * 保存物料
	 */
	@ResponseBody
	@RequiresPermissions(value={"tp:material:tpMaterial:add","tp:material:tpMaterial:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(TpMaterial tpMaterial, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(tpMaterial);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		tpMaterialService.save(tpMaterial);//保存
		j.setSuccess(true);
		j.setMsg("保存物料成功");
		return j;
	}
	
	/**
	 * 删除物料
	 */
	@ResponseBody
	@RequiresPermissions("tp:material:tpMaterial:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(TpMaterial tpMaterial) {
		AjaxJson j = new AjaxJson();
		tpMaterialService.delete(tpMaterial);
		j.setMsg("删除物料成功");
		return j;
	}
	
	/**
	 * 批量删除物料
	 */
	@ResponseBody
	@RequiresPermissions("tp:material:tpMaterial:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tpMaterialService.delete(tpMaterialService.get(id));
		}
		j.setMsg("删除物料成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("tp:material:tpMaterial:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(TpMaterial tpMaterial, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "物料"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TpMaterial> page = tpMaterialService.findPage(new Page<TpMaterial>(request, response, -1), tpMaterial);
    		new ExportExcel("物料", TpMaterial.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出物料记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public TpMaterial detail(String id) {
		return tpMaterialService.get(id);
	}
	

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("tp:material:tpMaterial:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TpMaterial> list = ei.getDataList(TpMaterial.class);
			for (TpMaterial tpMaterial : list){
				try{
					tpMaterialService.save(tpMaterial);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条物料记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条物料记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入物料失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入物料数据模板
	 */
	@ResponseBody
	@RequiresPermissions("tp:material:tpMaterial:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "物料数据导入模板.xlsx";
    		List<TpMaterial> list = Lists.newArrayList(); 
    		new ExportExcel("物料数据", TpMaterial.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }
	

}