/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.mystudy.web;

import java.util.ArrayList;
import java.util.List;

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
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.api.model.CourseModel;
import com.jeeplus.modules.course.dao.LessonDao;
import com.jeeplus.modules.course.entity.Lesson;
import com.jeeplus.modules.course.entity.Video;
import com.jeeplus.modules.course.service.CourseService;
import com.jeeplus.modules.course.service.VideoService;
import com.jeeplus.modules.exam.entity.examresult.Examine;
import com.jeeplus.modules.exam.service.examresult.ExamineService;
import com.jeeplus.modules.mystudy.entity.Trainstudy;
import com.jeeplus.modules.mystudy.service.TrainstudyService;

/**
 * 我的学习Controller
 * @author yangyy
 * @version 2018-03-22
 */
@Controller
@RequestMapping(value = "${adminPath}/mystudy/trainstudy")
public class TrainstudyController extends BaseController {

	@Autowired
	private TrainstudyService trainstudyService;
	
	@Autowired
	private LessonDao lessonDao;
	@Autowired
	private CourseService courseService;

	@Autowired
	private VideoService videoService;
	
	@Autowired
	private ExamineService examineService;
	
	@ModelAttribute
	public Trainstudy get(@RequestParam(required=false) String id) {
		Trainstudy entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = trainstudyService.get(id);
		}
		if (entity == null){
			entity = new Trainstudy();
		}
		return entity;
	}
	
	/**
	 * 学习列表页面
	 */
	@RequiresPermissions("mystudy:trainstudy:list")
	@RequestMapping(value = {"list", ""})
	public String list(Trainstudy trainstudy, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		Page<Trainstudy> page = trainstudyService.findStuList(new Page<Trainstudy>(request, response), trainstudy); 
		List<Trainstudy> Trainstudy = new ArrayList<Trainstudy>();
		for (Trainstudy study : page.getList()) {
			//查询用户标准课程学习记录
			double studyed = trainstudyService.findStudying("1",study.getUserid());
			Double examScore = examineService.findExamUser(study.getUserid());
			//已学标准课程章节数量
			Trainstudy study1 = new Trainstudy();
			study1.setYixue(studyed);
			study1.setId(study.getId());
			study1.setAnchorname(study.getAnchorname());
			study1.setCoursename(study.getCoursename());
			study1.setLessonname(study.getLessonname());
			study1.setStudyprogress(study.getStudyprogress());
			study1.setExamresult(examScore+"");
			study1.setStudytime(study.getStudytime());
			study1.setStudyplace(study.getStudyplace());
			study1.setStudytotalnum(study.getStudytotalnum());
			Trainstudy.add(study1);
			page.setList(Trainstudy);
			System.out.println(study1.getYixue()+":111");
			System.out.println("用户id:"+study.getUserid());
		}
		
		//查询标准课程章节总数
		double standardCount  = (double)lessonDao.getLessonCountByType("1");
		
		//查询每个人 的考试所考成绩
		/*examineService.findExamUser(trainstudy.getUserid());*/
		model.addAttribute("standardCount", standardCount);
		model.addAttribute("page", page);
		
		return "modules/mystudy/trainstudyList";
	}
	/**
	 * 查看，增加，编辑学习表单页面
	 */
	@RequiresPermissions(value={"mystudy:trainstudy:view","mystudy:trainstudy:add","mystudy:trainstudy:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Trainstudy trainstudy, Model model) {
		/*//查询用户标准课程学习记录
		List<Trainstudy> studyed = trainstudyService.findStudyList(trainstudy.getUserid());
		System.out.println(studyed);
		//已学标准课程章节数量
		double studyedCount = (double)studyed.size();
		model.addAttribute("studyedCount", studyedCount);
		//查询标准课程章节总数
		double standardCount  = (double)lessonDao.getLessonCountByType("1");
		model.addAttribute("standardCount", standardCount);*/
		System.out.println(trainstudy.getId()+"1111");
		Trainstudy trainstudy2 = trainstudyService.get(trainstudy.getId()); 
		//查询用户标准课程学习记录
		double studyed = trainstudyService.findStudying("1",trainstudy.getUserid());
		trainstudy2.setYixue(studyed);
		System.out.println(trainstudy2.getYixue());
		//查询标准课程章节总数
		double standardCount  = (double)lessonDao.getLessonCountByType("1");
		model.addAttribute("standardCount", standardCount);
		model.addAttribute("trainstudy", trainstudy2);
		return "modules/mystudy/trainstudyForm";
	}

	/**
	 * 保存学习
	 */
	@RequiresPermissions(value={"mystudy:trainstudy:add","mystudy:trainstudy:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Trainstudy trainstudy, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, trainstudy)){
			return form(trainstudy, model);
		}
		if(!trainstudy.getIsNewRecord()){//编辑表单保存
			Trainstudy t = trainstudyService.get(trainstudy.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(trainstudy, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			trainstudyService.save(t);//保存
		}else{//新增表单保存
			trainstudyService.save(trainstudy);//保存
		}
		addMessage(redirectAttributes, "保存学习成功");
		return "redirect:"+Global.getAdminPath()+"/mystudy/trainstudy/?repage";
	}
	
	/**
	 * 删除学习
	 */
	@RequiresPermissions("mystudy:trainstudy:del")
	@RequestMapping(value = "delete")
	public String delete(Trainstudy trainstudy, RedirectAttributes redirectAttributes) {
		trainstudyService.delete(trainstudy);
		addMessage(redirectAttributes, "删除学习成功");
		return "redirect:"+Global.getAdminPath()+"/mystudy/trainstudy/?repage";
	}
	
	/**
	 * 考试记录
	 */
	@RequestMapping(value = "examRecord")
	@ResponseBody
	public AjaxJson examRecord(Trainstudy trainstudy, Model model) {
		AjaxJson ajaxJson = new AjaxJson();
		List<Examine> courselist = examineService.findExamRecord(trainstudy.getUserid());
		/*for (CourseModel  course : courselist) {
			int itemNum = courseService.findItemNum(course.getId());
			course.setItemNum(itemNum);
			Lesson lesson = trainstudyService.findLastLesson(course.getId());
			Trainstudy study = trainstudyService.findCourseProgress(lesson.getId(),trainstudy.getUserid());
			if(study != null){
				 course.setProgress(study.getStudyprogress());
				 Video video = videoService.findVideoByLesson(study.getLessonid());
				 course.setCourseImg(video.getVideoImage());
			}else{
				course.setProgress(0.00);
			}   
		}*/
		ajaxJson.setSuccess(true);
	    ajaxJson.setMsg("课程列表");
		ajaxJson.put("course", courselist);
		System.out.println(trainstudy.getUserid()+"!!!!!");
		return ajaxJson;
	}
	
	
	/**
	 * 批量删除学习
	 */
	@RequiresPermissions("mystudy:trainstudy:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			trainstudyService.delete(trainstudyService.get(id));
		}
		addMessage(redirectAttributes, "删除学习成功");
		return "redirect:"+Global.getAdminPath()+"/mystudy/trainstudy/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("mystudy:trainstudy:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Trainstudy trainstudy, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "学习"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Trainstudy> page = trainstudyService.findPage(new Page<Trainstudy>(request, response, -1), trainstudy);
    		new ExportExcel("学习", Trainstudy.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出学习记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/mystudy/trainstudy/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("mystudy:trainstudy:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Trainstudy> list = ei.getDataList(Trainstudy.class);
			for (Trainstudy trainstudy : list){
				try{
					trainstudyService.save(trainstudy);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条学习记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条学习记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入学习失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/mystudy/trainstudy/?repage";
    }
	
	/**
	 * 下载导入学习数据模板
	 */
	@RequiresPermissions("mystudy:trainstudy:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "学习数据导入模板.xlsx";
    		List<Trainstudy> list = Lists.newArrayList(); 
    		new ExportExcel("学习数据", Trainstudy.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/mystudy/trainstudy/?repage";
    }
	
	
	

}