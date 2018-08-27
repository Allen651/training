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
import com.jeeplus.modules.mystudy.entity.ComnentGiveLike;
import com.jeeplus.modules.mystudy.service.ComnentGiveLikeService;

/**
 * 评论的点赞功能Controller
 * @author yangyy
 * @version 2018-04-11
 */
@Controller
@RequestMapping(value = "${adminPath}/mystudy/comnentGiveLike")
public class ComnentGiveLikeController extends BaseController {

	@Autowired
	private ComnentGiveLikeService comnentGiveLikeService;
	
	@ModelAttribute
	public ComnentGiveLike get(@RequestParam(required=false) String id) {
		ComnentGiveLike entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = comnentGiveLikeService.get(id);
		}
		if (entity == null){
			entity = new ComnentGiveLike();
		}
		return entity;
	}
	
	/**
	 * 评论点赞列表页面
	 */
	@RequiresPermissions("mystudy:comnentGiveLike:list")
	@RequestMapping(value = {"list", ""})
	public String list(ComnentGiveLike comnentGiveLike, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ComnentGiveLike> page = comnentGiveLikeService.findPage(new Page<ComnentGiveLike>(request, response), comnentGiveLike); 
		model.addAttribute("page", page);
		return "modules/mystudy/comnentGiveLikeList";
	}

	/**
	 * 查看，增加，编辑评论点赞表单页面
	 */
	@RequiresPermissions(value={"mystudy:comnentGiveLike:view","mystudy:comnentGiveLike:add","mystudy:comnentGiveLike:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(ComnentGiveLike comnentGiveLike, Model model) {
		model.addAttribute("comnentGiveLike", comnentGiveLike);
		return "modules/mystudy/comnentGiveLikeForm";
	}

	/**
	 * 保存评论点赞
	 */
	@RequiresPermissions(value={"mystudy:comnentGiveLike:add","mystudy:comnentGiveLike:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(ComnentGiveLike comnentGiveLike, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, comnentGiveLike)){
			return form(comnentGiveLike, model);
		}
		if(!comnentGiveLike.getIsNewRecord()){//编辑表单保存
			ComnentGiveLike t = comnentGiveLikeService.get(comnentGiveLike.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(comnentGiveLike, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			comnentGiveLikeService.save(t);//保存
		}else{//新增表单保存
			comnentGiveLikeService.save(comnentGiveLike);//保存
		}
		addMessage(redirectAttributes, "保存评论点赞成功");
		return "redirect:"+Global.getAdminPath()+"/mystudy/comnentGiveLike/?repage";
	}
	
	/**
	 * 删除评论点赞
	 */
	@RequiresPermissions("mystudy:comnentGiveLike:del")
	@RequestMapping(value = "delete")
	public String delete(ComnentGiveLike comnentGiveLike, RedirectAttributes redirectAttributes) {
		comnentGiveLikeService.delete(comnentGiveLike);
		addMessage(redirectAttributes, "删除评论点赞成功");
		return "redirect:"+Global.getAdminPath()+"/mystudy/comnentGiveLike/?repage";
	}
	
	/**
	 * 批量删除评论点赞
	 */
	@RequiresPermissions("mystudy:comnentGiveLike:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			comnentGiveLikeService.delete(comnentGiveLikeService.get(id));
		}
		addMessage(redirectAttributes, "删除评论点赞成功");
		return "redirect:"+Global.getAdminPath()+"/mystudy/comnentGiveLike/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("mystudy:comnentGiveLike:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(ComnentGiveLike comnentGiveLike, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "评论点赞"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ComnentGiveLike> page = comnentGiveLikeService.findPage(new Page<ComnentGiveLike>(request, response, -1), comnentGiveLike);
    		new ExportExcel("评论点赞", ComnentGiveLike.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出评论点赞记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/mystudy/comnentGiveLike/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("mystudy:comnentGiveLike:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<ComnentGiveLike> list = ei.getDataList(ComnentGiveLike.class);
			for (ComnentGiveLike comnentGiveLike : list){
				try{
					comnentGiveLikeService.save(comnentGiveLike);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条评论点赞记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条评论点赞记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入评论点赞失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/mystudy/comnentGiveLike/?repage";
    }
	
	/**
	 * 下载导入评论点赞数据模板
	 */
	@RequiresPermissions("mystudy:comnentGiveLike:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "评论点赞数据导入模板.xlsx";
    		List<ComnentGiveLike> list = Lists.newArrayList(); 
    		new ExportExcel("评论点赞数据", ComnentGiveLike.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/mystudy/comnentGiveLike/?repage";
    }
	
	
	

}