/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.modules.tp.car.web;

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
import com.jeeplus.modules.tp.car.entity.TpCar;
import com.jeeplus.modules.tp.car.service.TpCarService;

/**
 * 车辆Controller
 * @author 尹彬
 * @version 2019-01-16
 */
@Controller
@RequestMapping(value = "${adminPath}/tp/car/tpCar")
public class TpCarController extends BaseController {

	@Autowired
	private TpCarService tpCarService;
	
	@ModelAttribute
	public TpCar get(@RequestParam(required=false) String id) {
		TpCar entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tpCarService.get(id);
		}
		if (entity == null){
			entity = new TpCar();
		}
		return entity;
	}
	
	/**
	 * 车辆列表页面
	 */
	@RequiresPermissions("tp:car:tpCar:list")
	@RequestMapping(value = {"list", ""})
	public String list(TpCar tpCar, Model model) {
		model.addAttribute("tpCar", tpCar);
		return "modules/tp/car/tpCarList";
	}
	
		/**
	 * 车辆列表数据
	 */
	@ResponseBody
	@RequiresPermissions("tp:car:tpCar:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(TpCar tpCar, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TpCar> page = tpCarService.findPage(new Page<TpCar>(request, response), tpCar); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑车辆表单页面
	 */
	@RequiresPermissions(value={"tp:car:tpCar:view","tp:car:tpCar:add","tp:car:tpCar:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(TpCar tpCar, Model model) {
		model.addAttribute("tpCar", tpCar);
		return "modules/tp/car/tpCarForm";
	}

	/**
	 * 保存车辆
	 */
	@ResponseBody
	@RequiresPermissions(value={"tp:car:tpCar:add","tp:car:tpCar:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(TpCar tpCar, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(tpCar);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		tpCarService.save(tpCar);//保存
		j.setSuccess(true);
		j.setMsg("保存车辆成功");
		return j;
	}
	
	/**
	 * 删除车辆
	 */
	@ResponseBody
	@RequiresPermissions("tp:car:tpCar:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(TpCar tpCar) {
		AjaxJson j = new AjaxJson();
		tpCarService.delete(tpCar);
		j.setMsg("删除车辆成功");
		return j;
	}
	
	/**
	 * 批量删除车辆
	 */
	@ResponseBody
	@RequiresPermissions("tp:car:tpCar:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tpCarService.delete(tpCarService.get(id));
		}
		j.setMsg("删除车辆成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("tp:car:tpCar:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(TpCar tpCar, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "车辆"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TpCar> page = tpCarService.findPage(new Page<TpCar>(request, response, -1), tpCar);
    		new ExportExcel("车辆", TpCar.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出车辆记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("tp:car:tpCar:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TpCar> list = ei.getDataList(TpCar.class);
			for (TpCar tpCar : list){
				try{
					tpCarService.save(tpCar);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条车辆记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条车辆记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入车辆失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入车辆数据模板
	 */
	@ResponseBody
	@RequiresPermissions("tp:car:tpCar:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "车辆数据导入模板.xlsx";
    		List<TpCar> list = Lists.newArrayList(); 
    		new ExportExcel("车辆数据", TpCar.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

	/**
	 * 实时位置大屏
	 */
	@RequiresPermissions("tp:car:tpCar:realtimeLocations")
	@RequestMapping(value = {"realtimeLocations"})
	public String realtimeLocations( Model model) {
		return "modules/tp/cartrack/tpRealtimeLocations";
	}

	/**
	 * 实时位置大屏，获取位置(伪实时)
	 */
//	@RequiresPermissions("tp:maintenance:tpMaintenance:selectPostion")
	@RequestMapping(value = {"getRealtimeLocations"})
	@ResponseBody
	public List<Map>  getRealtimeLocations( Model model) {
		List<Map> allList = tpCarService.findAllLocation();
		return allList;
	}
}