/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.modules.tp.roadcross.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeeplus.modules.tp.road.entity.SysArea;
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
import com.jeeplus.core.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.tp.roadcross.service.SysArea2Service;

/**
 * 路口Controller
 * @author 尹彬
 * @version 2018-12-22
 */
@Controller
@RequestMapping(value = "${adminPath}/tp/roadcross/sysArea2")
public class SysArea2Controller extends BaseController {

	@Autowired
	private SysArea2Service sysArea2Service;
	
	@ModelAttribute
	public SysArea get(@RequestParam(required=false) String id) {
		SysArea entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = sysArea2Service.get(id);
		}
		if (entity == null){
			entity = new SysArea();
		}
		return entity;
	}
	
	/**
	 * 路口列表页面
	 */
	@RequestMapping(value = {"list", ""})
	public String list(SysArea sysArea2, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		return "modules/tp/roadcross/sysArea2List";
	}

	/**
	 * 查看，增加，编辑路口表单页面
	 */
	@RequestMapping(value = "form")
	public String form(SysArea sysArea2, Model model) {
		if (sysArea2.getParent()!=null && StringUtils.isNotBlank(sysArea2.getParent().getId())){
			sysArea2.setParent(sysArea2Service.get(sysArea2.getParent().getId()));
			// 获取排序号，最末节点排序号+30
			if (StringUtils.isBlank(sysArea2.getId())){
				SysArea sysArea2Child = new SysArea();
				sysArea2Child.setParent(new SysArea(sysArea2.getParent().getId()));
				List<SysArea> list = sysArea2Service.findList(sysArea2);
				if (list.size() > 0){
					sysArea2.setSort(list.get(list.size()-1).getSort());
					if (sysArea2.getSort() != null){
						sysArea2.setSort(sysArea2.getSort() + 30);
					}
				}
			}
		}
		if (sysArea2.getSort() == null){
			sysArea2.setSort(30);
		}
		model.addAttribute("sysArea2", sysArea2);
		return "modules/tp/roadcross/sysArea2Form";
	}

	/**
	 * 保存路口
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public AjaxJson save(SysArea sysArea2, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(sysArea2);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}

		//新增或编辑表单保存
		sysArea2Service.save(sysArea2);//保存
		j.setSuccess(true);
		j.put("sysArea2", sysArea2);
		j.setMsg("保存路口成功");
		return j;
	}
	
	@ResponseBody
	@RequestMapping(value = "getChildren")
	public List<SysArea> getChildren(String parentId){
		if("-1".equals(parentId)){//如果是-1，没指定任何父节点，就从根节点开始查找
			parentId = "0";
		}
		return sysArea2Service.getChildren(parentId);
	}
	
	/**
	 * 删除路口
	 */
	@ResponseBody
	@RequestMapping(value = "delete")
	public AjaxJson delete(SysArea sysArea2) {
		AjaxJson j = new AjaxJson();
		sysArea2Service.delete(sysArea2);
		j.setSuccess(true);
		j.setMsg("删除路口成功");
		return j;
	}

	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<SysArea> list = sysArea2Service.findList(new SysArea());
		for (int i=0; i<list.size(); i++){
			SysArea e = list.get(i);
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