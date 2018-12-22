/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tp.road.web;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.modules.tp.road.entity.SysArea;
import com.jeeplus.modules.tp.road.service.SysAreaService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.tp.road.entity.TpRoad;
import com.jeeplus.modules.tp.road.service.TpRoadService;

/**
 * 道路Controller
 * @author 尹彬
 * @version 2018-12-20
 */
@Controller
@RequestMapping(value = "${adminPath}/tp/road/tpRoad")
public class TpRoadController extends BaseController {

	@Autowired
	private TpRoadService tpRoadService;

	@Autowired
	private SysAreaService sysAreaService;
	
	@ModelAttribute
	public TpRoad get(@RequestParam(required=false) String id) {
		TpRoad entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tpRoadService.get(id);
		}
		if (entity == null){
			entity = new TpRoad();
		}
		return entity;
	}
	
	/**
	 * 道路列表页面
	 */
	@RequiresPermissions("tp:road:tpRoad:list")
	@RequestMapping(value = {"list", ""})
	public String list(TpRoad tpRoad, Model model) {
		model.addAttribute("tpRoad", tpRoad);
		return "modules/tp/road/tpRoadList";
	}
	
		/**
	 * 道路列表数据
	 */
	@ResponseBody
	@RequiresPermissions("tp:road:tpRoad:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(TpRoad tpRoad, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TpRoad> page = tpRoadService.findPage(new Page<TpRoad>(request, response), tpRoad); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑道路表单页面
	 */
	@RequiresPermissions(value={"tp:road:tpRoad:view","tp:road:tpRoad:add","tp:road:tpRoad:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(TpRoad tpRoad, Model model) {
		model.addAttribute("tpRoad", tpRoad);
		return "modules/tp/road/tpRoadForm";
	}

	/**
	 * 保存道路
	 */
	@ResponseBody
	@RequiresPermissions(value={"tp:road:tpRoad:add","tp:road:tpRoad:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(TpRoad tpRoad, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(tpRoad);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		tpRoadService.save(tpRoad);//保存
		j.setSuccess(true);
		j.setMsg("保存道路成功");
		return j;
	}
	
	/**
	 * 删除道路
	 */
	@ResponseBody
	@RequiresPermissions("tp:road:tpRoad:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(TpRoad tpRoad) {
		AjaxJson j = new AjaxJson();
		tpRoadService.delete(tpRoad);
		j.setMsg("删除道路成功");
		return j;
	}
	
	/**
	 * 批量删除道路
	 */
	@ResponseBody
	@RequiresPermissions("tp:road:tpRoad:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tpRoadService.delete(tpRoadService.get(id));
		}
		j.setMsg("删除道路成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("tp:road:tpRoad:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(TpRoad tpRoad, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "道路"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TpRoad> page = tpRoadService.findPage(new Page<TpRoad>(request, response, -1), tpRoad);
    		new ExportExcel("道路", TpRoad.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出道路记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("tp:road:tpRoad:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TpRoad> list = ei.getDataList(TpRoad.class);
			for (TpRoad tpRoad : list){
				try{
					tpRoadService.save(tpRoad);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条道路记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条道路记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入道路失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入道路数据模板
	 */
	@ResponseBody
	@RequiresPermissions("tp:road:tpRoad:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "道路数据导入模板.xlsx";
    		List<TpRoad> list = Lists.newArrayList(); 
    		new ExportExcel("道路数据", TpRoad.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }


	/**
	 * 解析 图吧 数据，分析导入数据库
	 */
	@ResponseBody
	@RequiresPermissions("tp:road:tpRoad:import")
	@RequestMapping(value = "parse", method = RequestMethod.GET)
	public AjaxJson parseDataFromTuBa() {
		AjaxJson j = new AjaxJson();
		try {
			new Thread() {
				@Override
				public void run() {
					try {

						List<String> list = tpRoadService.findAllList(new TpRoad()).stream().map(TpRoad::getName).collect(Collectors.toList());

						Document doc = Jsoup.connect("http://poi.mapbar.com/jinan/G70/").userAgent("Chrome").cookie("auth", "toke22").timeout(90000).post();
//            String title = doc.title();

						Element e = doc.getElementsByClass("sortC").get(0);
						Elements eles = e.getElementsByTag("a");
						for (Element ee : eles) {
							String roadName = ee.text().trim();
							System.out.println(roadName);
							if (list.contains(roadName)) continue;

							boolean b = true;
							try {
								List<TpRoad> roads =  tpRoadService.findByName(roadName);
							} catch (Exception e3) {
								b = false;
							}
							if (b) {
								String url = ee.attributes().get("href");
//                System.out.println(url);
								Document doc2 = Jsoup.connect(url).userAgent("Mis").cookie("auth", "toke23").timeout(90000).post();
								Element e1 = doc2.getElementsByClass("POI_ulA").get(0);
								Element ele2 = e1.children().get(1).children().get(2);
								String areaName = ele2.text();
								System.out.println(areaName);

								TpRoad tpRoad = new TpRoad();
								List<SysArea> areas= sysAreaService.findByName(areaName);
								if(areas != null && areas.size()>0){
									tpRoad.setArea(areas.get(0));
								}
								tpRoad.setName(roadName);
								tpRoad.setRoadType("3");
								tpRoad.setRoadType("《图吧》导入");
								tpRoadService.save(tpRoad);
							}
						}
					} catch (Exception e) {

					}
				}
			}.start();
//            System.out.println(title);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入道路失败！失败信息：" + e.getMessage());
		}
		return j;
	}


}