/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tp.markline.web;

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
import com.jeeplus.modules.tp.markline.entity.TpMarkline;
import com.jeeplus.modules.tp.markline.service.TpMarklineService;

/**
 * 标线Controller
 * @author 尹彬
 * @version 2018-12-19
 */
@Controller
@RequestMapping(value = "${adminPath}/tp/markline/tpMarkline")
public class TpMarklineController extends BaseController {

	@Autowired
	private TpMarklineService tpMarklineService;
	
	@ModelAttribute
	public TpMarkline get(@RequestParam(required=false) String id) {
		TpMarkline entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tpMarklineService.get(id);
		}
		if (entity == null){
			entity = new TpMarkline();
		}
		return entity;
	}
	
	/**
	 * 标线列表页面
	 */
	@RequiresPermissions("tp:markline:tpMarkline:list")
	@RequestMapping(value = {"list", ""})
	public String list(TpMarkline tpMarkline, Model model) {
		model.addAttribute("tpMarkline", tpMarkline);
		return "modules/tp/markline/tpMarklineList";
	}
	
		/**
	 * 标线列表数据
	 */
	@ResponseBody
	@RequiresPermissions("tp:markline:tpMarkline:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(TpMarkline tpMarkline, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TpMarkline> page = tpMarklineService.findPage(new Page<TpMarkline>(request, response), tpMarkline); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑标线表单页面
	 */
	@RequiresPermissions(value={"tp:markline:tpMarkline:view","tp:markline:tpMarkline:add","tp:markline:tpMarkline:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(TpMarkline tpMarkline, Model model) {
		model.addAttribute("tpMarkline", tpMarkline);
		return "modules/tp/markline/tpMarklineForm";
	}

	/**
	 * 保存标线
	 */
	@ResponseBody
	@RequiresPermissions(value={"tp:markline:tpMarkline:add","tp:markline:tpMarkline:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(TpMarkline tpMarkline, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(tpMarkline);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		tpMarklineService.save(tpMarkline);//保存
		j.setSuccess(true);
		j.setMsg("保存标线成功");
		return j;
	}
	
	/**
	 * 删除标线
	 */
	@ResponseBody
	@RequiresPermissions("tp:markline:tpMarkline:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(TpMarkline tpMarkline) {
		AjaxJson j = new AjaxJson();
		tpMarklineService.delete(tpMarkline);
		j.setMsg("删除标线成功");
		return j;
	}
	
	/**
	 * 批量删除标线
	 */
	@ResponseBody
	@RequiresPermissions("tp:markline:tpMarkline:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tpMarklineService.delete(tpMarklineService.get(id));
		}
		j.setMsg("删除标线成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("tp:markline:tpMarkline:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(TpMarkline tpMarkline, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "标线"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TpMarkline> page = tpMarklineService.findPage(new Page<TpMarkline>(request, response, -1), tpMarkline);
    		new ExportExcel("标线", TpMarkline.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出标线记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("tp:markline:tpMarkline:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TpMarkline> list = ei.getDataList(TpMarkline.class);
			for (TpMarkline tpMarkline : list){
				try{
					tpMarklineService.save(tpMarkline);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条标线记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条标线记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入标线失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入标线数据模板
	 */
	@ResponseBody
	@RequiresPermissions("tp:markline:tpMarkline:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "标线数据导入模板.xlsx";
    		List<TpMarkline> list = Lists.newArrayList(); 
    		new ExportExcel("标线数据", TpMarkline.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}