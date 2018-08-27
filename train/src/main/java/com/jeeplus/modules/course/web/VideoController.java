/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.course.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.CreateUploadImageResponse;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoResponse;
import com.google.common.collect.Lists;
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
import com.jeeplus.modules.course.entity.Course;
import com.jeeplus.modules.course.entity.Lesson;
import com.jeeplus.modules.course.entity.Video;
import com.jeeplus.modules.course.service.VideoService;
import com.jeeplus.modules.lesson.VodUtils;

/**
 * 视频管理Controller
 * @author jiangjl
 * @version 2018-03-12
 */
@Controller
@RequestMapping(value = "${adminPath}/course/video")
public class VideoController extends BaseController {

	@Autowired
	private VideoService videoService;
	
	@ModelAttribute
	public Video get(@RequestParam(required=false) String id) {
		Video entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = videoService.get(id);
		}
		if (entity == null){
			entity = new Video();
		}
		return entity;
	}
	/**
	 * 视频管理列表页面
	 */
	@RequiresPermissions("course:video:index")
	@RequestMapping(value = {"index", ""})
	public String index(Course course, Model model) {
		return "modules/course/videoIndex";
	}
	/**
	 * 视频管理列表页面
	 */
	@RequiresPermissions("course:video:index")
	@RequestMapping(value = "list")
	public String list(String jspDecode,Video video, HttpServletRequest request, HttpServletResponse response, Model model) {
		if("true".equals(jspDecode)){
			//处理乱码
			String cname = Encodes.jspDecode(Encodes.unescapeHtml(video.getLesson().getCourse().getCourseName()));
			video.getLesson().getCourse().setCourseName(cname);
			model.addAttribute("jspDecode", "true");
		}
		Page<Video> page = videoService.findPage(new Page<Video>(request, response), video); 
		model.addAttribute("page", page);
		return "modules/course/videoList";
	}

	/**
	 * 查看，增加，编辑视频管理表单页面
	 */
	@RequiresPermissions(value={"course:video:view","course:video:add","course:video:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Video video,String jspDecode, Model model) {
		if("true".equals(jspDecode)){
			//处理乱码
			String cname = Encodes.jspDecode(Encodes.unescapeHtml(video.getLesson().getCourse().getCourseName()));
			video.getLesson().getCourse().setCourseName(cname);
		}
		model.addAttribute("video", video);
		return "modules/course/videoForm";
	}

	/**
	 * 保存视频管理
	 */
	@RequiresPermissions(value={"course:video:add","course:video:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Video video, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, video)){
			return form(video,null, model);
		}
		if(!video.getIsNewRecord()){//编辑表单保存
			Video t = videoService.get(video.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(video, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			videoService.save(t);//保存
		}else{//新增表单保存
			videoService.save(video);//保存
		}
		addMessage(redirectAttributes, "保存视频管理成功");
		return "redirect:"+Global.getAdminPath()+"/course/video/?repage";
	}
	
	/**
	 * 删除视频管理
	 */
	@RequiresPermissions("course:video:del")
	@RequestMapping(value = "delete")
	public String delete(Video video, RedirectAttributes redirectAttributes) {
		videoService.delete(video);
		addMessage(redirectAttributes, "删除视频管理成功");
		return "redirect:"+Global.getAdminPath()+"/course/video/?repage";
	}
	
	/**
	 * 批量删除视频管理
	 */
	@RequiresPermissions("course:video:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			videoService.delete(videoService.get(id));
		}
		addMessage(redirectAttributes, "删除视频管理成功");
		return "redirect:"+Global.getAdminPath()+"/course/video/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("course:video:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Video video, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "视频管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Video> page = videoService.findPage(new Page<Video>(request, response, -1), video);
    		new ExportExcel("视频管理", Video.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出视频管理记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/course/video/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("course:video:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Video> list = ei.getDataList(Video.class);
			for (Video video : list){
				try{
					videoService.save(video);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条视频管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条视频管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入视频管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/course/video/?repage";
    }
	
	/**
	 * 下载导入视频管理数据模板
	 */
	@RequiresPermissions("course:video:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "视频管理数据导入模板.xlsx";
    		List<Video> list = Lists.newArrayList(); 
    		new ExportExcel("视频管理数据", Video.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/course/video/?repage";
    }
	
	
	/**
	 * 选择所属章节
	 */
	@RequestMapping(value = "selectlesson")
	public String selectlesson(Lesson lesson, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Lesson> page = videoService.findPageBylesson(new Page<Lesson>(request, response),  lesson);
		try {
			fieldLabels = Encodes.jspDecode(Encodes.unescapeHtml(URLDecoder.decode(fieldLabels, "UTF-8")));
			fieldKeys = URLDecoder.decode(fieldKeys, "UTF-8");
			searchLabel = Encodes.jspDecode(Encodes.unescapeHtml(URLDecoder.decode(searchLabel, "UTF-8")));
			searchKey = URLDecoder.decode(searchKey, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		model.addAttribute("labelNames", fieldLabels.split("\\|"));
		model.addAttribute("labelValues", fieldKeys.split("\\|"));
		model.addAttribute("fieldLabels", fieldLabels);
		model.addAttribute("fieldKeys", fieldKeys);
		model.addAttribute("url", url);
		model.addAttribute("searchLabel", searchLabel);
		model.addAttribute("searchKey", searchKey);
		model.addAttribute("obj", lesson);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}
	
	/**
	 * 获取视频点播上传凭证和地址
	 * @param lesson
	 * @param url
	 * @return
	 * @author jiangjl
	 */
	@ResponseBody
	@RequestMapping(value = "getVideoUploadAuth")
	public AjaxJson getUploadAuth(HttpServletRequest request,String filename) {
		AjaxJson ajaxJson = new AjaxJson();
		DefaultAcsClient aliyunClient= new DefaultAcsClient(DefaultProfile.getProfile("cn-shanghai",VodUtils.accessKeyId,VodUtils.accessKeySecret));
		CreateUploadVideoResponse response = VodUtils.createUploadVideo(aliyunClient,filename);
		
		ajaxJson.setSuccess(true);
		ajaxJson.put("RequestId", response.getRequestId());
		ajaxJson.put("UploadAuth", response.getUploadAuth());
		ajaxJson.put("UploadAddress", response.getUploadAddress());
		ajaxJson.put("VideoId", response.getVideoId());
		
		return ajaxJson;
	}
	
	/**
	 * 获取视频封面上传凭证和地址
	 * @param lesson
	 * @param url
	 * @return
	 * @author jiangjl
	 */
	@ResponseBody
	@RequestMapping(value = "getImageUploadAuth")
	public AjaxJson getImageUploadAuth(HttpServletRequest request) {
		AjaxJson ajaxJson = new AjaxJson();
		DefaultAcsClient aliyunClient= new DefaultAcsClient(DefaultProfile.getProfile("cn-shanghai",VodUtils.accessKeyId,VodUtils.accessKeySecret));
		CreateUploadImageResponse response = VodUtils.createUploadImage(aliyunClient);
		
		ajaxJson.setSuccess(true);
		ajaxJson.put("RequestId", response.getRequestId());
		ajaxJson.put("UploadAuth", response.getUploadAuth());
		ajaxJson.put("UploadAddress", response.getUploadAddress());
		ajaxJson.put("ImageURL", response.getImageURL());
		ajaxJson.put("ImageId", response.getImageId());
		
		return ajaxJson;
	}
	

	
}