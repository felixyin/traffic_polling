/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tp.road.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.config.Global;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.tp.road.entity.MySysArea;
import com.jeeplus.modules.tp.road.service.MySysAreaService;

/**
 * 道路Controller
 * @author 尹彬
 * @version 2018-12-19
 */
@Controller
@RequestMapping(value = "${adminPath}/tp/road/mySysArea")
public class MySysAreaController extends BaseController {

	@Autowired
	private MySysAreaService mySysAreaService;
	
	@ModelAttribute
	public MySysArea get(@RequestParam(required=false) String id) {
		MySysArea entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mySysAreaService.get(id);
		}
		if (entity == null){
			entity = new MySysArea();
		}
		return entity;
	}
	
	/**
	 * 道路列表页面
	 */
	@RequestMapping(value = {"list", ""})
	public String list(MySysArea mySysArea,  HttpServletRequest request, HttpServletResponse response, Model model) {
		
		return "modules/tp/road/mySysAreaList";
	}

	/**
	 * 查看，增加，编辑道路表单页面
	 */
	@RequestMapping(value = "form")
	public String form(MySysArea mySysArea, Model model) {
		if (mySysArea.getParent()!=null && StringUtils.isNotBlank(mySysArea.getParent().getId())){
			mySysArea.setParent(mySysAreaService.get(mySysArea.getParent().getId()));
			// 获取排序号，最末节点排序号+30
			if (StringUtils.isBlank(mySysArea.getId())){
				MySysArea mySysAreaChild = new MySysArea();
				mySysAreaChild.setParent(new MySysArea(mySysArea.getParent().getId()));
				List<MySysArea> list = mySysAreaService.findList(mySysArea); 
				if (list.size() > 0){
					mySysArea.setSort(list.get(list.size()-1).getSort());
					if (mySysArea.getSort() != null){
						mySysArea.setSort(mySysArea.getSort() + 30);
					}
				}
			}
		}
		if (mySysArea.getSort() == null){
			mySysArea.setSort(30);
		}
		model.addAttribute("mySysArea", mySysArea);
		return "modules/tp/road/mySysAreaForm";
	}

	/**
	 * 保存道路
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public AjaxJson save(MySysArea mySysArea, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(mySysArea);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}

		//新增或编辑表单保存
		mySysAreaService.save(mySysArea);//保存
		j.setSuccess(true);
		j.put("mySysArea", mySysArea);
		j.setMsg("保存道路成功");
		return j;
	}
	
	@ResponseBody
	@RequestMapping(value = "getChildren")
	public List<MySysArea> getChildren(String parentId){
		if("-1".equals(parentId)){//如果是-1，没指定任何父节点，就从根节点开始查找
			parentId = "0";
		}
		return mySysAreaService.getChildren(parentId);
	}
	
	/**
	 * 删除道路
	 */
	@ResponseBody
	@RequestMapping(value = "delete")
	public AjaxJson delete(MySysArea mySysArea) {
		AjaxJson j = new AjaxJson();
		mySysAreaService.delete(mySysArea);
		j.setSuccess(true);
		j.setMsg("删除道路成功");
		return j;
	}

	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<MySysArea> list = mySysAreaService.findList(new MySysArea());
		for (int i=0; i<list.size(); i++){
			MySysArea e = list.get(i);
			if (StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("text", e.getName());
				if(StringUtils.isBlank(e.getParentId()) || "0".equals(e.getParentId())){
					map.put("parent", "#");
					Map<String, Object> state = Maps.newHashMap();
					state.put("opened", true);
					map.put("state", state);
				}else{
					map.put("parent", e.getParentId());
				}
				mapList.add(map);
			}
		}
		return mapList;
	}
	
}