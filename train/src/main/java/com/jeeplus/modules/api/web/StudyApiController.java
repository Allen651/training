package com.jeeplus.modules.api.web;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.anchor.entity.AnchorInfo;
import com.jeeplus.modules.anchor.service.AnchorInfoService;
import com.jeeplus.modules.api.entity.UserApi;
import com.jeeplus.modules.api.model.AnchorModel;
import com.jeeplus.modules.course.dao.LessonDao;
import com.jeeplus.modules.course.entity.Lesson;
import com.jeeplus.modules.course.service.CourseService;
import com.jeeplus.modules.course.service.CourseTypeService;
import com.jeeplus.modules.mystudy.entity.Applicant;
import com.jeeplus.modules.mystudy.entity.ComnentGiveLike;
import com.jeeplus.modules.mystudy.entity.TrainComment;
import com.jeeplus.modules.mystudy.entity.Trainstudy;
import com.jeeplus.modules.mystudy.service.ApplicantService;
import com.jeeplus.modules.mystudy.service.ComnentGiveLikeService;
import com.jeeplus.modules.mystudy.service.TrainCommentService;
import com.jeeplus.modules.mystudy.service.TrainstudyService;
@Controller
@RequestMapping(value = "${frontPath}/api/")
public class StudyApiController extends BaseController{
	
	@Autowired
	private CourseService courseService;
	@Autowired
	private CourseTypeService courseTypeService;
	@Autowired
	private LessonDao lessonDao;
	@Autowired
	private TrainstudyService trainstudyService;
	@Autowired
	private AnchorInfoService anchorInfoService;
	@Autowired
	private ApplicantService applicantService;
	@Autowired
	private TrainCommentService trainCommentService;
	
	@Autowired
	private ComnentGiveLikeService comnentGiveLikeService;
	
	
	/*
	 * 保存已学课程
	 */
	@ResponseBody
	@RequestMapping(value = "saveHasCourse")
	public AjaxJson getSaveHasCourse(String token, String courseid,String lessonid,
			Double studytime,Double studyhaving,HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		UserApi userApi=(UserApi)CacheUtils.get(CacheUtils.USER_API_CACHE, token);
		AnchorInfo anchorInfo = anchorInfoService.findByanchor(userApi.getUsercode());
		List<Lesson> lesson = lessonDao.findcourse(courseid);
		AjaxJson ajaxJson=new AjaxJson();
		DecimalFormat df = new DecimalFormat("######0.00"); // 进度保留2位小数
		Trainstudy trainstudy = new Trainstudy();
		trainstudy.setUserid(anchorInfo.getUserId());
		trainstudy.setCourseid(courseid);
		trainstudy.setLessonid(lessonid);
		trainstudy.setStudytime(studytime.intValue());
		trainstudy.setRoles(0);
		String format = df.format(studytime/studyhaving);
		trainstudy.setStudyprogress(Double.parseDouble(format));
		trainstudy.setIsstudy("0");
		
		trainstudy.setGivelikeStaus(0);
		
		for (Lesson lesson2 : lesson) {
			trainstudy.setStudyplace(lesson2.getStudyLimitNum());
			trainstudy.setStudytotalnum(lesson2.getStudyLimitNum());
		}
		
		//查询有没有这条记录
		Trainstudy trainss = trainstudyService.findUserLesson(anchorInfo.getUserId(),lessonid);
		if (trainss==null) {
			//System.out.println("为空时保存");
			trainstudyService.save(trainstudy);
		}else {
			trainstudy.setIsNewRecord(false);
			System.out.println("");
			//不为空时更新
			trainss.setStudyprogress(Double.parseDouble(format));
			trainss.setStudytime(studytime.intValue());
			trainstudyService.save(trainss);
		}
		
		ajaxJson.put("trainstudy", trainstudy);
	    return ajaxJson;
	}
	
	/*
	 * //播放结束后,观看次数减一
	 */
	@ResponseBody
	@RequestMapping(value = "reduceNum")
	public AjaxJson getreduceNum(String token,String lessonid, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		AjaxJson ajaxJson = new AjaxJson();
		UserApi userApi = (UserApi) CacheUtils.get(CacheUtils.USER_API_CACHE, token);
		Trainstudy trainss = trainstudyService.findUserLesson(userApi.getId(),lessonid);
		if (trainss.getStudyplace() !=0) {
			trainss.setStudyplace(trainss.getStudyplace()-1);
		}
		
		trainss.setStudyprogress(1.0);
		trainss.setIsstudy("1");
		trainstudyService.save(trainss);
		
		Lesson lesson = lessonDao.findUniqueByProperty("id",lessonid);
		if (null == lesson.getPlayNum()) {
			lesson.setPlayNum(0);
		}
		lessonDao.updateplaynum(lesson.getPlayNum()+1,lessonid);
		ajaxJson.put("playsum", lesson);
		return ajaxJson;
	}
	
	
	/*
	 * 我的学习
	 */
	@ResponseBody
	@RequestMapping(value = "myStudy")
	public AjaxJson getmyStudy(String token,HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		UserApi userApi=(UserApi)CacheUtils.get(CacheUtils.USER_API_CACHE, token);
		AjaxJson ajaxJson=new AjaxJson();
		AnchorInfo anchorInfo = anchorInfoService.findByanchor(userApi.getUsercode());
		List<Trainstudy> trainstudy = trainstudyService.findStudyList(anchorInfo.getUserId());
		ajaxJson.put("studylist", trainstudy);
	    return ajaxJson;
	}
	
	//申请重学记录
	@ResponseBody
	@RequestMapping(value = "applicationStudy")
	public AjaxJson applicationStudy(String token,String lessonName,Integer index,HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		UserApi userApi=(UserApi)CacheUtils.get(CacheUtils.USER_API_CACHE, token);
		AjaxJson ajaxJson=new AjaxJson();
		AnchorModel anchorInfo =anchorInfoService.findAnchorInfo(userApi.getUsercode());
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		String format = dateFormat.format(cal.getTime());
		System.out.println(format+":当前时间");
		//System.out.println("主播:"+anchorInfo.getName()+"他的经纪人为:"+anchorInfo.getAgentName()+"申请时间为:"+""+"工作室为:"+anchorInfo.getOfficeName());
		Applicant applicant = new Applicant();
		applicant.setAgent(anchorInfo.getAgentName());
		applicant.setApplicationtime(cal.getTime());
		//applicant.setLessonname(lessonName);
		applicant.setLessonid(lessonName);
		applicant.setStudio(anchorInfo.getOfficeName());
		applicant.setUserid(anchorInfo.getUserId());
		applicant.setName(anchorInfo.getName());
		applicant.setCurrentprogress(index+"");
		/*applicant.setCurrentprogress(studyed+"");*/
		//查询数据库有没有这条信息,没有添加,有了更新
		Applicant appli = applicantService.findUserLesson(anchorInfo.getUserId(),lessonName);
		if (appli == null) {
			applicantService.save(applicant);
		}else{
			applicant.setIsNewRecord(false);
			applicantService.save(appli);
		}
	    return ajaxJson;
	}
	
	/*
	 * 学习进度排行榜
	 */
	@ResponseBody
	@RequestMapping(value = "studyRank")
	public AjaxJson getScoreRank(String token,HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		UserApi userApi=(UserApi)CacheUtils.get(CacheUtils.USER_API_CACHE, token);
		AnchorInfo anchorInfo = anchorInfoService.findBymobile(userApi.getMobile());
		String company = anchorInfo.getCompany().getId();
		AjaxJson ajaxJson = new AjaxJson();
		List<AnchorModel> studyList = trainstudyService.getStudyRank(company);
		int lessonNum = lessonDao.getLessonCountByType("1");
		for (AnchorModel anchor : studyList) {
			int studyNum = anchor.getStudyNum();
			NumberFormat numberFormat = NumberFormat.getInstance();   
	        // 设置精确到小数点后2位  
	        numberFormat.setMaximumFractionDigits(0);  
	        String stuProgress = numberFormat.format((float) studyNum / (float) lessonNum * 100)+"%";  
			anchor.setStuProgress(stuProgress);	
		}
		ajaxJson.setSuccess(true);
	    ajaxJson.setMsg("学习排行榜");
	    ajaxJson.put("studyRank", studyList);
		return ajaxJson;
	}
	
	
	/*
	 * 保存点赞次数
	 */
	@ResponseBody
	@RequestMapping(value = "giveLikeNum")
	public AjaxJson saveGiveLikeNum(String token,String lessonid, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		UserApi userApi=(UserApi)CacheUtils.get(CacheUtils.USER_API_CACHE, token);
		AjaxJson ajaxJson=new AjaxJson(); 
		AnchorInfo anchorInfo = anchorInfoService.findByanchor(userApi.getUsercode());
		Lesson lesson = lessonDao.findUniqueByProperty("id",lessonid);
		Trainstudy trainss = trainstudyService.findUserLesson(anchorInfo.getUserId(),lessonid);
		if (null ==lesson.getGivelikeNum()) {
			lesson.setGivelikeNum(0);
			/*lesson.setGivelikeStaus(1);*/
			trainss.setGivelikeStaus(1);
		}
		trainstudyService.updatele(1,lessonid,anchorInfo.getUserId());
		lessonDao.updatelesson(lesson.getGivelikeNum()+1,lessonid);
		ajaxJson.put("giveLikeNum", lesson);
		ajaxJson.put("giveLikeStaus", trainss);
	    return ajaxJson;
	}
	
	
	/*
	 * 取消点赞
	 */
	@ResponseBody
	@RequestMapping(value = "delGiveLikeNum")
	public AjaxJson delGiveLikeNum(String token,String lessonid, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		UserApi userApi=(UserApi)CacheUtils.get(CacheUtils.USER_API_CACHE, token);
		AjaxJson ajaxJson=new AjaxJson(); 
		AnchorInfo anchorInfo = anchorInfoService.findByanchor(userApi.getUsercode());
		Lesson lesson = lessonDao.findUniqueByProperty("id",lessonid);
		Trainstudy trainss = trainstudyService.findUserLesson(anchorInfo.getUserId(),lessonid);
		/*lesson.setGivelikeNum(lesson.getGivelikeNum()+1);*/
		if (null ==lesson.getGivelikeNum()) {
			lesson.setGivelikeNum(0);
			trainss.setGivelikeStaus(0);
		}
		trainstudyService.updatele(0,lessonid,anchorInfo.getUserId());
		lessonDao.updatelesson(lesson.getGivelikeNum()-1,lessonid);
		ajaxJson.put("giveLikeNum", lesson);
		ajaxJson.put("giveLikeStaus", trainss);
	    return ajaxJson;
	}
	
	/*
	 * 查询点赞状态
	 */
	@ResponseBody
	@RequestMapping(value = "findGiveLikeStaus")
	public AjaxJson findGiveLikeStaus(String token,String lessonid, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		UserApi userApi=(UserApi)CacheUtils.get(CacheUtils.USER_API_CACHE, token);
		AjaxJson ajaxJson=new AjaxJson(); 
		AnchorInfo anchorInfo = anchorInfoService.findByanchor(userApi.getUsercode());
		Trainstudy trainss = trainstudyService.findUserLesson(anchorInfo.getUserId(),lessonid);
		ajaxJson.put("giveLikeStaus", trainss);
	    return ajaxJson;
	}
	
	
	
	
	
	
	
	
	
	
	
	/*
	 * 添加评论
	 */
	@ResponseBody
	@RequestMapping(value = "comMent")
	public AjaxJson saveComMent(String token,String lessonid,String data, String type,HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		UserApi userApi=(UserApi)CacheUtils.get(CacheUtils.USER_API_CACHE, token);
		AjaxJson ajaxJson=new AjaxJson(); 
		AnchorInfo anchorInfo = anchorInfoService.findByanchor(userApi.getUsercode());
		TrainComment trainComment = new TrainComment();
		trainComment.setContent(data);
		trainComment.setUserid(anchorInfo.getUserId());
		trainComment.setType("0");
		trainComment.setTopicid(lessonid);
		trainComment.setGivelikeStaus(0);
		trainComment.setGivelikeNum(0);
		trainCommentService.save(trainComment);
		List<TrainComment> trainComments = trainCommentService.findComme(lessonid);
		if (trainComments.size() !=0) {
			ajaxJson.put("trainComment", trainComments);
		}
	    return ajaxJson;
	}
	
	/*
	 * 回复评论
	 */
	@ResponseBody
	@RequestMapping(value = "huifucomMent")
	public AjaxJson savehuifucomMent(String token,String lessonid,String data, String type,String userid,String commentid,HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		UserApi userApi=(UserApi)CacheUtils.get(CacheUtils.USER_API_CACHE, token);
		AjaxJson ajaxJson=new AjaxJson(); 
		AnchorInfo anchorInfo = anchorInfoService.findByanchor(userApi.getUsercode());
		/*TrainComment trainComment = trainCommentService.findUniqueByProperty("id", commentid);*/
		TrainComment trainComment = new TrainComment();
		trainComment.setContent(data);
		trainComment.setUserid(userid);
		trainComment.setType("1");
		trainComment.setTopicid(lessonid);
		trainComment.setRelatid(anchorInfo.getUserId());
		trainComment.setParentid(commentid);
		trainCommentService.save(trainComment);
		//System.out.println("保存回复评论成功"+"-------------------");
		List<TrainComment> trainComments = trainCommentService.findComme(lessonid);
		if (trainComments.size() !=0) {
			ajaxJson.put("huifucomMent", trainComments);
		}
	    return ajaxJson;
	}
	
	
	/*
	 * 评论列表
	 */
	@ResponseBody
	@RequestMapping(value = "comMentList")
	public AjaxJson findComMent(String token,String lessonid,String data, String type,HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		UserApi userApi=(UserApi)CacheUtils.get(CacheUtils.USER_API_CACHE, token);
		AjaxJson ajaxJson=new AjaxJson(); 
		AnchorInfo anchorInfo = anchorInfoService.findByanchor(userApi.getUsercode());
		List trainComments2 = new ArrayList();
		List zan = new ArrayList();
		List<TrainComment> trainComments = trainCommentService.findComment(lessonid,0);
		for (TrainComment trainComment : trainComments) {
			//回复评论
			List<TrainComment> trainComments1 = trainCommentService.findChirenComment(trainComment.getId());
			double commentsum = trainCommentService.commentsum(trainComment.getId());
			trainComment.setCommentsum(commentsum);
			if (trainComments1.size()!=0) {
				trainComments2.add(trainComments1);
				ajaxJson.put("huifucomnent", trainComments2);
			}
			
			List<ComnentGiveLike> comnentGiveLikes = comnentGiveLikeService.finddianzan(anchorInfo.getUserId(),trainComment.getId());
			if (comnentGiveLikes != null && comnentGiveLikes.size() != 0) {
				zan.add(1);
			}else{
				zan.add(0);
			}
			System.out.println(comnentGiveLikes);
			ajaxJson.put("comnentGiveLikes", zan);
		}
		if (trainComments.size() !=0) {
			ajaxJson.put("trainComment", trainComments);
		}
	    return ajaxJson;
	}
	
	
	/*
	 * 自己评论列表
	 */
	@ResponseBody
	@RequestMapping(value = "myComMentList")
	public AjaxJson findmyComMentList(String token,HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		UserApi userApi=(UserApi)CacheUtils.get(CacheUtils.USER_API_CACHE, token);
		AjaxJson ajaxJson=new AjaxJson(); 
		AnchorInfo anchorInfo = anchorInfoService.findByanchor(userApi.getUsercode());
		List<TrainComment> trainComments = trainCommentService.findmyComMentList(anchorInfo.getUserId(),1);
		ajaxJson.put("myComMentList", trainComments);
		//System.out.println("查询自己评论成功"+"-------------------");
	    return ajaxJson;
	}
	
	
	
	
	/*
	 * 评论页点赞
	 */
	
	@ResponseBody
	@RequestMapping(value = "commentGiveLikeNum")
	public AjaxJson saveCommentGiveLikeNum(String token,String givelikeNum,String conmentid,String dianzanren,String beidianren,HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		UserApi userApi=(UserApi)CacheUtils.get(CacheUtils.USER_API_CACHE, token);
		AjaxJson ajaxJson=new AjaxJson(); 
		ComnentGiveLike comnentGiveLike = new ComnentGiveLike();
		TrainComment trainComment = trainCommentService.get(conmentid);
		if (beidianren.equals(dianzanren)) {
			if(Integer.parseInt(givelikeNum)>-1){
				trainComment.setGivelikeNum(Integer.parseInt(givelikeNum)+1);
			}
			trainComment.setGivelikeStaus(1);
			//System.out.println("给自己赞");
		} else {
			comnentGiveLike.setCommentid(conmentid);
			comnentGiveLike.setUserid(dianzanren);
			comnentGiveLike.setRelatid(beidianren);
			comnentGiveLike.setType(1);
			trainComment.setGivelikeNum(Integer.parseInt(givelikeNum)+1);
			comnentGiveLikeService.save(comnentGiveLike);
			//System.out.println("给别人赞");
		}
		trainCommentService.save(trainComment);
		ajaxJson.put("commentGiveLikeNum", trainComment);
		ajaxJson.put("comnentGiveLike", comnentGiveLike);
		System.out.println("评论页点赞成功"+"+++++++++++++++++++++");
	    return ajaxJson;
	}
	
	/*
	 * 评论页取消点赞
	 */
	@ResponseBody
	@RequestMapping(value = "delCommentGiveLikeNum")
	public AjaxJson delCommentGiveLikeNum(String token,String givelikeNum,String conmentid,String dianzanren,String beidianren,HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		UserApi userApi=(UserApi)CacheUtils.get(CacheUtils.USER_API_CACHE, token);
		AjaxJson ajaxJson=new AjaxJson(); 
		ComnentGiveLike comnentGiveLike = new ComnentGiveLike();
		TrainComment trainComment = trainCommentService.get(conmentid);
		/*trainComment.setGivelikeNum(Integer.parseInt(givelikeNum)-1);
		trainComment.setGivelikeStaus(0);
		trainCommentService.save(trainComment);*/
		if ( beidianren.equals(dianzanren) ) {
			if(Integer.parseInt(givelikeNum)>-1){
				trainComment.setGivelikeNum(Integer.parseInt(givelikeNum)-1);
			}
			trainComment.setGivelikeStaus(0);
			//System.out.println("取消自己赞");
		} else {
			comnentGiveLike.setCommentid(conmentid);
			comnentGiveLike.setUserid(dianzanren);
			comnentGiveLike.setRelatid(beidianren);
			comnentGiveLike.setType(0);
			trainComment.setGivelikeNum(Integer.parseInt(givelikeNum)-1);
			comnentGiveLikeService.deletezan(beidianren,dianzanren);
		//	System.out.println("取消给别人赞");
		}
		trainCommentService.save(trainComment);
		ajaxJson.put("delcomnentGiveLike", comnentGiveLike);
		ajaxJson.put("delCommentGiveLikeNum", trainComment);
		//System.out.println("评论页点赞取消"+"+++++++++++++++++++++");
	    return ajaxJson;
	}
	
	
	
}
