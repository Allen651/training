/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.course.web;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.course.entity.CourseType;
import com.jeeplus.modules.course.service.CourseTypeService;

/**
 * 课程分类Controller
 * @author jiangjl
 * @version 2018-03-12
 */
@Controller
@RequestMapping(value = "${adminPath}/course/courseType")
public class CourseTypeController extends BaseController {

	@Autowired
	private CourseTypeService courseTypeService;
	
	@ModelAttribute
	public CourseType get(@RequestParam(required=false) String id) {
		CourseType entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = courseTypeService.get(id);
		}
		if (entity == null){
			entity = new CourseType();
		}
		return entity;
	}
	
	/**
	 * 课程分类列表页面
	 */
	@RequiresPermissions("course:courseType:list")
	@RequestMapping(value = {"list", ""})
	public String list(CourseType courseType, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<CourseType> list = courseTypeService.findList(courseType); 
		model.addAttribute("list", list);
		return "modules/course/courseTypeList";
	}

	/**
	 * 查看，增加，编辑课程分类表单页面
	 */
	@RequiresPermissions(value={"course:courseType:view","course:courseType:add","course:courseType:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(CourseType courseType, Model model) {
		if (courseType.getParent()!=null && StringUtils.isNotBlank(courseType.getParent().getId())){
			courseType.setParent(courseTypeService.get(courseType.getParent().getId()));
			// 获取排序号，最末节点排序号+30
			if (StringUtils.isBlank(courseType.getId())){
				CourseType courseTypeChild = new CourseType();
				courseTypeChild.setParent(new CourseType(courseType.getParent().getId()));
				List<CourseType> list = courseTypeService.findList(courseType); 
				if (list.size() > 0){
					courseType.setSort(list.get(list.size()-1).getSort());
					if (courseType.getSort() != null){
						courseType.setSort(courseType.getSort() + 30);
					}
				}
			}
		}
		if (courseType.getSort() == null){
			courseType.setSort(30);
		}
		model.addAttribute("courseType", courseType);
		return "modules/course/courseTypeForm";
	}

	/**
	 * 保存课程分类
	 */
	@RequiresPermissions(value={"course:courseType:add","course:courseType:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(CourseType courseType, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, courseType)){
			return form(courseType, model);
		}
		if(!courseType.getIsNewRecord()){//编辑表单保存
			CourseType t = courseTypeService.get(courseType.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(courseType, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			courseTypeService.save(t);//保存
		}else{//新增表单保存
			courseTypeService.save(courseType);//保存
		}
		addMessage(redirectAttributes, "保存课程分类成功");
		return "redirect:"+Global.getAdminPath()+"/course/courseType/?repage";
	}
	
	/**
	 * 删除课程分类
	 */
	@RequiresPermissions("course:courseType:del")
	@RequestMapping(value = "delete")
	public String delete(CourseType courseType, RedirectAttributes redirectAttributes) {
		courseTypeService.delete(courseType);
		addMessage(redirectAttributes, "删除课程分类成功");
		return "redirect:"+Global.getAdminPath()+"/course/courseType/?repage";
	}

	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<CourseType> list = courseTypeService.findList(new CourseType());
		for (int i=0; i<list.size(); i++){
			CourseType e = list.get(i);
			if (StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}
	
}