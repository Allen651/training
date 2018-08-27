/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.mystudy.web;

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
import com.jeeplus.modules.mystudy.entity.TrainComment;
import com.jeeplus.modules.mystudy.service.TrainCommentService;

/**
 * 评论Controller
 * @author yangyy
 * @version 2018-04-04
 */
@Controller
@RequestMapping(value = "${adminPath}/mystudy/trainComment")
public class TrainCommentController extends BaseController {

	@Autowired
	private TrainCommentService trainCommentService;
	
	@ModelAttribute
	public TrainComment get(@RequestParam(required=false) String id) {
		TrainComment entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = trainCommentService.get(id);
		}
		if (entity == null){
			entity = new TrainComment();
		}
		return entity;
	}
	
	/**
	 * 评论列表页面
	 */
	@RequiresPermissions("mystudy:trainComment:list")
	@RequestMapping(value = {"list", ""})
	public String list(TrainComment trainComment, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TrainComment> page = trainCommentService.findPage(new Page<TrainComment>(request, response), trainComment); 
		model.addAttribute("page", page);
		return "modules/mystudy/trainCommentList";
	}

	/**
	 * 查看，增加，编辑评论表单页面
	 */
	@RequiresPermissions(value={"mystudy:trainComment:view","mystudy:trainComment:add","mystudy:trainComment:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(TrainComment trainComment, Model model) {
		model.addAttribute("trainComment", trainComment);
		return "modules/mystudy/trainCommentForm";
	}

	/**
	 * 保存评论
	 */
	@RequiresPermissions(value={"mystudy:trainComment:add","mystudy:trainComment:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(TrainComment trainComment, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, trainComment)){
			return form(trainComment, model);
		}
		if(!trainComment.getIsNewRecord()){//编辑表单保存
			TrainComment t = trainCommentService.get(trainComment.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(trainComment, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			trainCommentService.save(t);//保存
		}else{//新增表单保存
			trainCommentService.save(trainComment);//保存
		}
		addMessage(redirectAttributes, "保存评论成功");
		return "redirect:"+Global.getAdminPath()+"/mystudy/trainComment/?repage";
	}
	
	/**
	 * 删除评论
	 */
	@RequiresPermissions("mystudy:trainComment:del")
	@RequestMapping(value = "delete")
	public String delete(TrainComment trainComment, RedirectAttributes redirectAttributes) {
		trainCommentService.delete(trainComment);
		addMessage(redirectAttributes, "删除评论成功");
		return "redirect:"+Global.getAdminPath()+"/mystudy/trainComment/?repage";
	}
	
	/**
	 * 批量删除评论
	 */
	@RequiresPermissions("mystudy:trainComment:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			trainCommentService.delete(trainCommentService.get(id));
		}
		addMessage(redirectAttributes, "删除评论成功");
		return "redirect:"+Global.getAdminPath()+"/mystudy/trainComment/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("mystudy:trainComment:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(TrainComment trainComment, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "评论"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TrainComment> page = trainCommentService.findPage(new Page<TrainComment>(request, response, -1), trainComment);
    		new ExportExcel("评论", TrainComment.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出评论记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/mystudy/trainComment/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("mystudy:trainComment:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TrainComment> list = ei.getDataList(TrainComment.class);
			for (TrainComment trainComment : list){
				try{
					trainCommentService.save(trainComment);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条评论记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条评论记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入评论失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/mystudy/trainComment/?repage";
    }
	
	/**
	 * 下载导入评论数据模板
	 */
	@RequiresPermissions("mystudy:trainComment:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "评论数据导入模板.xlsx";
    		List<TrainComment> list = Lists.newArrayList(); 
    		new ExportExcel("评论数据", TrainComment.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/mystudy/trainComment/?repage";
    }
	
	
	

}