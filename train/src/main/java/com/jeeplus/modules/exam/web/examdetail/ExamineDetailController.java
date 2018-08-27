/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.exam.web.examdetail;

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
import com.jeeplus.modules.exam.entity.examdetail.ExamineDetail;
import com.jeeplus.modules.exam.service.examdetail.ExamineDetailService;

/**
 * 考试明细Controller
 * @author zhaol
 * @version 2018-03-14
 */
@Controller
@RequestMapping(value = "${adminPath}/exam/examdetail/examineDetail")
public class ExamineDetailController extends BaseController {

	@Autowired
	private ExamineDetailService examineDetailService;
	
	@ModelAttribute
	public ExamineDetail get(@RequestParam(required=false) String id) {
		ExamineDetail entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = examineDetailService.get(id);
		}
		if (entity == null){
			entity = new ExamineDetail();
		}
		return entity;
	}
	
	/**
	 * 考试明细列表页面
	 */
	@RequiresPermissions("exam:examdetail:examineDetail:list")
	@RequestMapping(value = {"list", ""})
	public String list(ExamineDetail examineDetail, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ExamineDetail> page = examineDetailService.findPage(new Page<ExamineDetail>(request, response), examineDetail); 
		model.addAttribute("page", page);
		return "modules/exam/examdetail/examineDetailList";
	}

	/**
	 * 查看，增加，编辑考试明细表单页面
	 */
	@RequiresPermissions(value={"exam:examdetail:examineDetail:view","exam:examdetail:examineDetail:add","exam:examdetail:examineDetail:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(ExamineDetail examineDetail, Model model) {
		model.addAttribute("examineDetail", examineDetail);
		return "modules/exam/examdetail/examineDetailForm";
	}

	/**
	 * 保存考试明细
	 */
	@RequiresPermissions(value={"exam:examdetail:examineDetail:add","exam:examdetail:examineDetail:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(ExamineDetail examineDetail, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, examineDetail)){
			return form(examineDetail, model);
		}
		if(!examineDetail.getIsNewRecord()){//编辑表单保存
			ExamineDetail t = examineDetailService.get(examineDetail.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(examineDetail, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			examineDetailService.save(t);//保存
		}else{//新增表单保存
			examineDetailService.save(examineDetail);//保存
		}
		addMessage(redirectAttributes, "保存考试明细成功");
		return "redirect:"+Global.getAdminPath()+"/exam/examdetail/examineDetail/?repage";
	}
	
	/**
	 * 删除考试明细
	 */
	@RequiresPermissions("exam:examdetail:examineDetail:del")
	@RequestMapping(value = "delete")
	public String delete(ExamineDetail examineDetail, RedirectAttributes redirectAttributes) {
		examineDetailService.delete(examineDetail);
		addMessage(redirectAttributes, "删除考试明细成功");
		return "redirect:"+Global.getAdminPath()+"/exam/examdetail/examineDetail/?repage";
	}
	
	/**
	 * 批量删除考试明细
	 */
	@RequiresPermissions("exam:examdetail:examineDetail:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			examineDetailService.delete(examineDetailService.get(id));
		}
		addMessage(redirectAttributes, "删除考试明细成功");
		return "redirect:"+Global.getAdminPath()+"/exam/examdetail/examineDetail/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("exam:examdetail:examineDetail:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(ExamineDetail examineDetail, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "考试明细"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ExamineDetail> page = examineDetailService.findPage(new Page<ExamineDetail>(request, response, -1), examineDetail);
    		new ExportExcel("考试明细", ExamineDetail.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出考试明细记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/exam/examdetail/examineDetail/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("exam:examdetail:examineDetail:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<ExamineDetail> list = ei.getDataList(ExamineDetail.class);
			for (ExamineDetail examineDetail : list){
				try{
					examineDetailService.save(examineDetail);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条考试明细记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条考试明细记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入考试明细失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/exam/examdetail/examineDetail/?repage";
    }
	
	/**
	 * 下载导入考试明细数据模板
	 */
	@RequiresPermissions("exam:examdetail:examineDetail:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "考试明细数据导入模板.xlsx";
    		List<ExamineDetail> list = Lists.newArrayList(); 
    		new ExportExcel("考试明细数据", ExamineDetail.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/exam/examdetail/examineDetail/?repage";
    }
	
	
	

}