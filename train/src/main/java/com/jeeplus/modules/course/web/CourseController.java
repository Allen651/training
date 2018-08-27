/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.course.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Comparator;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.Encodes;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.course.dao.LessonDao;
import com.jeeplus.modules.course.entity.Course;
import com.jeeplus.modules.course.entity.CourseType;
import com.jeeplus.modules.course.entity.Lesson;
import com.jeeplus.modules.course.entity.Teacher;
import com.jeeplus.modules.course.service.CourseService;
import com.jeeplus.modules.course.service.CourseTypeService;

/**
 * 课程Controller
 * @author jiangjl
 * @version 2018-03-12
 */
@Controller
@RequestMapping(value = "${adminPath}/course/course")
public class CourseController extends BaseController {

	@Autowired
	private CourseService courseService;
	
	@Autowired
	private CourseTypeService courseTypeService;
	
	@Autowired
	private LessonDao lessonDao;
	
	@ModelAttribute
	public Course get(@RequestParam(required=false) String id) {
		Course entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = courseService.get(id);
		}
		if (entity == null){
			entity = new Course();
		}
		return entity;
	}
	
	/**
	 * 课程列表页面
	 */
	@RequiresPermissions("course:course:index")
	@RequestMapping(value = {"index", ""})
	public String index(Course course, Model model) {
		return "modules/course/courseIndex";
	}
	/**
	 * 课程列表页面
	 */
	@RequiresPermissions("course:course:index")
	@RequestMapping(value = "list")
	public String list(String jspDecode,Course course, HttpServletRequest request, HttpServletResponse response, Model model) {
		if("true".equals(jspDecode)){
			//处理乱码
			String cname = Encodes.jspDecode(Encodes.unescapeHtml(course.getCourseType().getName()));
			course.getCourseType().setName(cname);
			model.addAttribute("jspDecode", "true");
		}
		Page<Course> page = courseService.findPage(new Page<Course>(request, response), course); 
		model.addAttribute("page", page);
		return "modules/course/courseList";
	}

	/**
	 * 查看，增加，编辑课程表单页面
	 */
	@RequiresPermissions(value={"course:course:view","course:course:add","course:course:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Course course, String jspDecode, Model model) {
		if("true".equals(jspDecode)){
			String cname = Encodes.jspDecode(Encodes.unescapeHtml(course.getCourseType().getName()));
			course.getCourseType().setName(cname);
		}
		model.addAttribute("course", course);
		return "modules/course/courseForm";
	}

	/**
	 * 保存课程
	 */
	@RequiresPermissions(value={"course:course:add","course:course:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Course course, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, course)){
			return form(course,null, model);
		}
		if(!course.getIsNewRecord()){//编辑表单保存
			Course t = courseService.get(course.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(course, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			courseService.save(t);//保存
		}else{//新增表单保存
			courseService.save(course);//保存
		}
		addMessage(redirectAttributes, "保存课程成功");
		return "redirect:"+Global.getAdminPath()+"/course/course/?repage";
	}
	
	/**
	 * 删除课程
	 */
	@RequiresPermissions("course:course:del")
	@RequestMapping(value = "delete")
	public String delete(Course course, RedirectAttributes redirectAttributes) {
		courseService.delete(course);
		addMessage(redirectAttributes, "删除课程成功");
		return "redirect:"+Global.getAdminPath()+"/course/course/?repage";
	}
	
	/**
	 * 批量删除课程
	 */
	@RequiresPermissions("course:course:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			courseService.delete(courseService.get(id));
		}
		addMessage(redirectAttributes, "删除课程成功");
		return "redirect:"+Global.getAdminPath()+"/course/course/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("course:course:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Course course, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "课程"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Course> page = courseService.findPage(new Page<Course>(request, response, -1), course);
    		new ExportExcel("课程", Course.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出课程记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/course/course/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("course:course:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Course> list = ei.getDataList(Course.class);
			for (Course course : list){
				try{
					courseService.save(course);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条课程记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条课程记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入课程失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/course/course/?repage";
    }
	
	/**
	 * 下载导入课程数据模板
	 */
	@RequiresPermissions("course:course:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "课程数据导入模板.xlsx";
    		List<Course> list = Lists.newArrayList(); 
    		new ExportExcel("课程数据", Course.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/course/course/?repage";
    }
	
	
	/**
	 * 选择讲师
	 */
	@RequestMapping(value = "selectteacher")
	public String selectteacher(Teacher teacher,String jspDecode, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Teacher> page = courseService.findPageByteacher(new Page<Teacher>(request, response),  teacher);
		if( !"false".equals(jspDecode)){
			try {
				fieldLabels = Encodes.jspDecode(Encodes.unescapeHtml(URLDecoder.decode(fieldLabels, "UTF-8")));
				fieldKeys = URLDecoder.decode(fieldKeys, "UTF-8");
				searchLabel = Encodes.jspDecode(Encodes.unescapeHtml(URLDecoder.decode(searchLabel, "UTF-8")));
				searchKey = URLDecoder.decode(searchKey, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
//		String[] fieldLabelss = fieldLabels.split("\\|");
//		for (int i = 0; i < fieldLabelss.length; i++) {
//			fieldLabelss[i] = Encodes.urlEncode(fieldLabelss[i]);
//		}
		model.addAttribute("labelNames", fieldLabels.split("\\|"));
		model.addAttribute("labelValues", fieldKeys.split("\\|"));
		model.addAttribute("fieldLabels", fieldLabels);
		model.addAttribute("fieldKeys", fieldKeys);
		model.addAttribute("url", url);
		model.addAttribute("searchLabel", searchLabel);
		model.addAttribute("searchKey", searchKey);
		model.addAttribute("obj", teacher);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}
	

	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeDataLesson")
	public List<Map<String, Object>> treeDataLesson(@RequestParam(required=false) String extId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<CourseType> listSort = Lists.newArrayList();
		List<Course> list = courseService.findList(new Course());
		for (int i=0; i<list.size(); i++){
			Course course = list.get(i);
			Map<String, Object> mapC = Maps.newHashMap();
			mapC.put("id", course.getId());
			mapC.put("pId", course.getCourseType().getId());
			mapC.put("name", course.getCourseName());
			mapList.add(mapC);
			
			CourseType cType = courseTypeService.get(course.getCourseType());
			if(!listSort.isEmpty()){
				boolean exist = false;
				for (CourseType ct : listSort) {
					if(ct.getId().equals(cType.getId())){
						exist = true;
					}
				}
				if( !exist){
					listSort.add(cType);
				}
			}else{
				listSort.add(cType);
			}
			while(!"0".equals((cType = courseTypeService.get(cType.getParent())).getParentId())){
				if(!listSort.isEmpty()){
					boolean exist = false;
					for (CourseType ct : listSort) {
						if(ct.getId().equals(cType.getId())){
							exist = true;
						}
					}
					if( !exist){
						listSort.add(cType);
					}
				}else{
					listSort.add(cType);
				}
			}
			course.setLessonList(lessonDao.findList(new Lesson(course)));
			List<Lesson> lessons = course.getLessonList();
			if(lessons != null && !lessons.isEmpty()){
				for (Lesson lesson : lessons) {
					Map<String, Object> map = Maps.newHashMap();
					map.put("id", lesson.getId());
					map.put("pId", course.getId());
					map.put("name", lesson.getLessonName());
					mapList.add(map);
				}
			}
		}
		listSort.sort(new Comparator<CourseType>(){  
	        @Override  
	        public int compare(CourseType c1, CourseType c2) {  
	            if(c1.getSort()>c2.getSort()){  
	                return 1;  
	            }else if(c1.getSort()==c2.getSort()){  
	                return 0;  
	            }else{  
	                return -1;  
	            }  
	        }
	        });
		
		for (CourseType cType : listSort) {
			Map<String, Object> mapType = Maps.newHashMap();
			mapType.put("id", cType.getId());
			mapType.put("pId", cType.getParentId());
			mapType.put("name", cType.getName());
			mapList.add(mapType);
		}
		return mapList;
		
		
	}
	
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeDataCourse")
	public List<Map<String, Object>> treeDataCourse(@RequestParam(required=false) String extId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<CourseType> list = courseTypeService.findList(new CourseType());
		for (int i=0; i<list.size(); i++){
			CourseType e = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			map.put("pId", e.getParentId());
			map.put("name", e.getName());
			mapList.add(map);
		}
		
		List<Course> lists = courseService.findList(new Course());
		for (int i=0; i<lists.size(); i++){
			Course course = lists.get(i);
			Map<String, Object> mapC = Maps.newHashMap();
			mapC.put("id", course.getId());
			mapC.put("pId", course.getCourseType().getId());
			mapC.put("name", course.getCourseName());
			mapList.add(mapC);
			
		}
		return mapList;
	}
	
	/**
	 * 根据course_id获取课程名字
	 */
	@ResponseBody
	@RequestMapping(value = "getCourse")
	public AjaxJson getName(HttpServletResponse response, String courseId) {
		AjaxJson ajaxJson = new AjaxJson();
		Course course = courseService.get(courseId);
		String courseName = course.getCourseName();
		ajaxJson.put("courseName",courseName);
		return ajaxJson;
	}
	
}