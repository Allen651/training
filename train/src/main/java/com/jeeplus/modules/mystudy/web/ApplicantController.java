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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.course.dao.LessonDao;
import com.jeeplus.modules.course.entity.Lesson;
import com.jeeplus.modules.mystudy.entity.Applicant;
import com.jeeplus.modules.mystudy.entity.Trainstudy;
import com.jeeplus.modules.mystudy.service.ApplicantService;
import com.jeeplus.modules.mystudy.service.TrainstudyService;

/**
 * 申请重学记录Controller
 * @author yangyy
 * @version 2018-04-02
 */
@Controller
@RequestMapping(value = "${adminPath}/mystudy/applicant")
public class ApplicantController extends BaseController {

	@Autowired
	private ApplicantService applicantService;
	@Autowired
	private LessonDao lessonDao;
	@Autowired
	private TrainstudyService trainstudyService;
	
	
	@ModelAttribute
	public Applicant get(@RequestParam(required=false) String id) {
		Applicant entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = applicantService.get(id);
		}
		if (entity == null){
			entity = new Applicant();
		}
		return entity;
	}
	
	/**
	 * 申请重学记录列表页面
	 */
	@RequiresPermissions("mystudy:applicant:list")
	@RequestMapping(value = {"list", ""})
	public String list(Applicant applicant, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Applicant> page = applicantService.findApplicaList(new Page<Applicant>(request, response), applicant); 
		List<Applicant> applicants = new ArrayList<Applicant>();
		for (Applicant apply : page.getList()) {
			//查询用户标准课程学习记录
			List<Trainstudy> studyed = trainstudyService.findStudyingLessons("1",apply.getUserid());///每个章节所在的进度位置
			Applicant applicant2 = new Applicant();
			applicant2.setId(apply.getId());
			applicant2.setAgent(apply.getAgent());
			applicant2.setLessonname(apply.getLessonname());
			applicant2.setName(apply.getName());
			applicant2.setStudio(apply.getStudio());
			applicant2.setCurrentprogress(apply.getCurrentprogress()+"");
			applicant2.setApplicationtime(apply.getApplicationtime());
			applicant2.setCurrentprogress(apply.getCurrentprogress());
			applicants.add(applicant2);
			page.setList(applicants);
		}
		//查询标准课程章节总数
		double standardCount  = (double)lessonDao.getLessonCountByType("1");
		model.addAttribute("standardCount", standardCount);
		model.addAttribute("page", page);
		return "modules/mystudy/applicantList";
	}

	/**
	 * 查看，增加，编辑申请重学记录表单页面
	 */
	@RequiresPermissions(value={"mystudy:applicant:view","mystudy:applicant:add","mystudy:applicant:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Applicant applicant, Model model) {
		model.addAttribute("applicant", applicant);
		return "modules/mystudy/applicantForm";
	}

	/**
	 * 保存申请重学记录
	 */
	@RequiresPermissions(value={"mystudy:applicant:add","mystudy:applicant:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Applicant applicant, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, applicant)){
			return form(applicant, model);
		}
		if(!applicant.getIsNewRecord()){//编辑表单保存
			Applicant t = applicantService.get(applicant.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(applicant, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			applicantService.save(t);//保存
		}else{//新增表单保存
			applicantService.save(applicant);//保存
		}
		addMessage(redirectAttributes, "保存申请重学记录成功");
		return "redirect:"+Global.getAdminPath()+"/mystudy/applicant/?repage";
	}
	
	/**
	 * 删除申请重学记录
	 */
	@RequiresPermissions("mystudy:applicant:del")
	@RequestMapping(value = "delete")
	public String delete(Applicant applicant, RedirectAttributes redirectAttributes) {
		applicantService.delete(applicant);
		addMessage(redirectAttributes, "删除申请重学记录成功");
		return "redirect:"+Global.getAdminPath()+"/mystudy/applicant/?repage";
	}
	
	/*
	 * 同意重学
	 */
	@RequiresPermissions(value = {"mystudy:applicant:agree"},logical = Logical.OR)
	@RequestMapping(value = "agreeaa")
	public String agree(Applicant applicant, Model model) {
		/*List<Lesson> lesson = lessonDao.findlesson(applicant.getLessonid());*/
		Lesson lesso = lessonDao.findUniqueByProperty("id", applicant.getLessonid());
		trainstudyService.updateSyudyPlace(lesso.getStudyLimitNum(),applicant.getUserid(),applicant.getLessonid());
		model.addAttribute("applicant", applicant);
		applicantService.delete(applicant);
		return "redirect:"+Global.getAdminPath()+"/mystudy/applicant/?repage";
	}
	
	/*
	 * 拒绝重学
	 */
	@RequiresPermissions(value = {"mystudy:applicant:refuse"},logical = Logical.OR)
	@RequestMapping(value = "refuse")
	public String refuse(Applicant applicant, Model model) {
		model.addAttribute("applicant", applicant);
		return "redirect:"+Global.getAdminPath()+"/mystudy/applicant/?repage";
	}
	
	
	/**
	 * 批量删除申请重学记录
	 */
	@RequiresPermissions("mystudy:applicant:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			applicantService.delete(applicantService.get(id));
		}
		addMessage(redirectAttributes, "删除申请重学记录成功");
		return "redirect:"+Global.getAdminPath()+"/mystudy/applicant/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("mystudy:applicant:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Applicant applicant, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "申请重学记录"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Applicant> page = applicantService.findPage(new Page<Applicant>(request, response, -1), applicant);
    		new ExportExcel("申请重学记录", Applicant.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出申请重学记录记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/mystudy/applicant/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("mystudy:applicant:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Applicant> list = ei.getDataList(Applicant.class);
			for (Applicant applicant : list){
				try{
					applicantService.save(applicant);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条申请重学记录记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条申请重学记录记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入申请重学记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/mystudy/applicant/?repage";
    }
	
	/**
	 * 下载导入申请重学记录数据模板
	 */
	@RequiresPermissions("mystudy:applicant:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "申请重学记录数据导入模板.xlsx";
    		List<Applicant> list = Lists.newArrayList(); 
    		new ExportExcel("申请重学记录数据", Applicant.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/mystudy/applicant/?repage";
    }
	
	
	

}