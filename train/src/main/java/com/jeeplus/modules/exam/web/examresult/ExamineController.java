/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.exam.web.examresult;

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
import com.jeeplus.modules.api.model.ExamineModel;
import com.jeeplus.modules.exam.entity.examresult.Examine;
import com.jeeplus.modules.exam.service.examresult.ExamineService;

/**
 * 考试管理Controller
 * @author zhaol
 * @version 2018-03-14
 */
@Controller
@RequestMapping(value = "${adminPath}/exam/examresult/examine")
public class ExamineController extends BaseController {

	@Autowired
	private ExamineService examineService;
	
	@ModelAttribute
	public Examine get(@RequestParam(required=false) String id) {
		Examine entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = examineService.get(id);
		}
		if (entity == null){
			entity = new Examine();
		}
		return entity;
	}
	
	/**
	 * 考试管理列表页面
	 */
	@RequiresPermissions("exam:examresult:examine:list")
	@RequestMapping(value = {"list", ""})
	public String list(ExamineModel examineModel, HttpServletRequest request, HttpServletResponse response, Model model) {
		//Page<Examine> page = examineService.findPage(new Page<Examine>(request, response), examine); 
		Page<ExamineModel> page = examineService.findExamList(new Page<ExamineModel>(request, response), examineModel); 
		model.addAttribute("page", page);
		return "modules/exam/examresult/examineList";
	}

	/**
	 * 查看，增加，编辑考试管理表单页面
	 */
	@RequiresPermissions(value={"exam:examresult:examine:view","exam:examresult:examine:add","exam:examresult:examine:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Examine examine, Model model) {
		model.addAttribute("examine", examine);
		return "modules/exam/examresult/examineForm";
	}

	/**
	 * 保存考试管理
	 */
	@RequiresPermissions(value={"exam:examresult:examine:add","exam:examresult:examine:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Examine examine, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, examine)){
			return form(examine, model);
		}
		if(!examine.getIsNewRecord()){//编辑表单保存
			Examine t = examineService.get(examine.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(examine, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			examineService.save(t);//保存
		}else{//新增表单保存
			examineService.save(examine);//保存
		}
		addMessage(redirectAttributes, "保存考试管理成功");
		return "redirect:"+Global.getAdminPath()+"/exam/examresult/examine/?repage";
	}
	
	/**
	 * 删除考试管理
	 */
	@RequiresPermissions("exam:examresult:examine:del")
	@RequestMapping(value = "delete")
	public String delete(Examine examine, RedirectAttributes redirectAttributes) {
		examineService.delete(examine);
		addMessage(redirectAttributes, "删除考试管理成功");
		return "redirect:"+Global.getAdminPath()+"/exam/examresult/examine/?repage";
	}
	
	/**
	 * 批量删除考试管理
	 */
	@RequiresPermissions("exam:examresult:examine:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			examineService.delete(examineService.get(id));
		}
		addMessage(redirectAttributes, "删除考试管理成功");
		return "redirect:"+Global.getAdminPath()+"/exam/examresult/examine/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("exam:examresult:examine:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Examine examine, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "考试管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Examine> page = examineService.findPage(new Page<Examine>(request, response, -1), examine);
    		new ExportExcel("考试管理", Examine.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出考试管理记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/exam/examresult/examine/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("exam:examresult:examine:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Examine> list = ei.getDataList(Examine.class);
			for (Examine examine : list){
				try{
					examineService.save(examine);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条考试管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条考试管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入考试管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/exam/examresult/examine/?repage";
    }
	
	/**
	 * 下载导入考试管理数据模板
	 */
	@RequiresPermissions("exam:examresult:examine:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "考试管理数据导入模板.xlsx";
    		List<Examine> list = Lists.newArrayList(); 
    		new ExportExcel("考试管理数据", Examine.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/exam/examresult/examine/?repage";
    }
	
	
	

}