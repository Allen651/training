/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.exam.web.examquestions;

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
import com.jeeplus.modules.exam.entity.examquestions.ExamQuestions;
import com.jeeplus.modules.exam.service.examquestions.ExamQuestionsService;

/**
 * 试题管理Controller
 * @author zhaol
 * @version 2018-03-14
 */
@Controller
@RequestMapping(value = "${adminPath}/exam/examquestions/examQuestions")
public class ExamQuestionsController extends BaseController {

	@Autowired
	private ExamQuestionsService examQuestionsService;
	
	@ModelAttribute
	public ExamQuestions get(@RequestParam(required=false) String id) {
		ExamQuestions entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = examQuestionsService.get(id);
		}
		if (entity == null){
			entity = new ExamQuestions();
		}
		return entity;
	}
	
	/**
	 * 试题管理列表页面
	 */
	@RequiresPermissions("exam:examquestions:examQuestions:list")
	@RequestMapping(value = {"list", ""})
	public String list(ExamQuestions examQuestions, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ExamQuestions> page = examQuestionsService.findPage(new Page<ExamQuestions>(request, response), examQuestions); 
		model.addAttribute("page", page);
		return "modules/exam/examquestions/examQuestionsList";
	}

	/**
	 * 查看，增加，编辑试题管理表单页面
	 */
	@RequiresPermissions(value={"exam:examquestions:examQuestions:view","exam:examquestions:examQuestions:add","exam:examquestions:examQuestions:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(ExamQuestions examQuestions, Model model) {
		model.addAttribute("examQuestions", examQuestions);
		return "modules/exam/examquestions/examQuestionsForm";
	}

	/**
	 * 保存试题管理
	 */
	@RequiresPermissions(value={"exam:examquestions:examQuestions:add","exam:examquestions:examQuestions:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(ExamQuestions examQuestions, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, examQuestions)){
			return form(examQuestions, model);
		}
		if(!examQuestions.getIsNewRecord()){//编辑表单保存
			ExamQuestions t = examQuestionsService.get(examQuestions.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(examQuestions, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			examQuestionsService.save(t);//保存
		}else{//新增表单保存
			examQuestionsService.save(examQuestions);//保存
		}
		addMessage(redirectAttributes, "保存试题管理成功");
		return "redirect:"+Global.getAdminPath()+"/exam/examquestions/examQuestions/?repage";
	}
	
	/**
	 * 删除试题管理
	 */
	@RequiresPermissions("exam:examquestions:examQuestions:del")
	@RequestMapping(value = "delete")
	public String delete(ExamQuestions examQuestions, RedirectAttributes redirectAttributes) {
		examQuestionsService.delete(examQuestions);
		addMessage(redirectAttributes, "删除试题管理成功");
		return "redirect:"+Global.getAdminPath()+"/exam/examquestions/examQuestions/?repage";
	}
	
	/**
	 * 批量删除试题管理
	 */
	@RequiresPermissions("exam:examquestions:examQuestions:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			examQuestionsService.delete(examQuestionsService.get(id));
		}
		addMessage(redirectAttributes, "删除试题管理成功");
		return "redirect:"+Global.getAdminPath()+"/exam/examquestions/examQuestions/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("exam:examquestions:examQuestions:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(ExamQuestions examQuestions, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "试题管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ExamQuestions> page = examQuestionsService.findPage(new Page<ExamQuestions>(request, response, -1), examQuestions);
    		new ExportExcel("试题管理", ExamQuestions.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出试题管理记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/exam/examquestions/examQuestions/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("exam:examquestions:examQuestions:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<ExamQuestions> list = ei.getDataList(ExamQuestions.class);
			for (ExamQuestions examQuestions : list){
				try{
					examQuestionsService.save(examQuestions);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条试题管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条试题管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入试题管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/exam/examquestions/examQuestions/?repage";
    }
	
	/**
	 * 下载导入试题管理数据模板
	 */
	@RequiresPermissions("exam:examquestions:examQuestions:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "试题管理数据导入模板.xlsx";
    		List<ExamQuestions> list = Lists.newArrayList(); 
    		new ExportExcel("试题管理数据", ExamQuestions.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/exam/examquestions/examQuestions/?repage";
    }
	
	
	

}