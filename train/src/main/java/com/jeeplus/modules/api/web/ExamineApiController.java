/**
 * Copyright &copy; 2015-2020 怒醒文化传媒 All rights reserved.
 */
package com.jeeplus.modules.api.web;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.api.gateway.Response;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.Encodes;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.anchor.entity.AnchorInfo;
import com.jeeplus.modules.anchor.service.AnchorInfoService;
import com.jeeplus.modules.api.bestsign.BestsignUtil;
import com.jeeplus.modules.api.bestsign.PDFElement;
import com.jeeplus.modules.api.entity.UserApi;
import com.jeeplus.modules.api.model.AnchorModel;
import com.jeeplus.modules.api.model.CourseModel;
import com.jeeplus.modules.api.model.RewardModel;
import com.jeeplus.modules.api.model.TalentModel;
import com.jeeplus.modules.api.service.UserApiService;
import com.jeeplus.modules.contract.entity.ContractInfo;
import com.jeeplus.modules.contract.entity.ContractTemplet;
import com.jeeplus.modules.contract.entity.ContractTempletSetting;
import com.jeeplus.modules.contract.service.ContractInfoService;
import com.jeeplus.modules.contract.service.ContractTempletService;
import com.jeeplus.modules.course.entity.Course;
import com.jeeplus.modules.course.entity.Lesson;
import com.jeeplus.modules.course.entity.Video;
import com.jeeplus.modules.course.service.CourseService;
import com.jeeplus.modules.course.service.VideoService;
import com.jeeplus.modules.exam.entity.examdetail.ExamineDetail;
import com.jeeplus.modules.exam.entity.examquestions.ExamQuestions;
import com.jeeplus.modules.exam.entity.examquestions.QuestionsItems;
import com.jeeplus.modules.exam.entity.examresult.Examine;
import com.jeeplus.modules.exam.service.examdetail.ExamineDetailService;
import com.jeeplus.modules.exam.service.examquestions.ExamQuestionsService;
import com.jeeplus.modules.exam.service.examresult.ExamineService;
import com.jeeplus.modules.messages.entity.NotifyMessages;
import com.jeeplus.modules.messages.service.NotifyMessagesService;
import com.jeeplus.modules.mystudy.entity.Trainstudy;
import com.jeeplus.modules.mystudy.service.TrainstudyService;
import com.jeeplus.modules.reward.entity.CourseRewardSet;
import com.jeeplus.modules.reward.entity.MyReward;
import com.jeeplus.modules.reward.entity.Reward;
import com.jeeplus.modules.reward.service.CourseRewardSetService;
import com.jeeplus.modules.reward.service.MyRewardService;
import com.jeeplus.modules.talent.entity.TalentInfo;
import com.jeeplus.modules.talent.service.TalentInfoService;


/**
 * 培训管理Controller
 * @author zhaol
 * @version 2018-03-15
 */
@Controller
@RequestMapping(value = "${frontPath}/api/")
public class ExamineApiController extends BaseController {

	@Autowired
	private UserApiService userApiService;
	
	@Autowired
	private AnchorInfoService anchorInfoService;
	
	@Autowired
	private ExamQuestionsService examQuestionsService;
	
	@Autowired
	private ExamineDetailService examineDetailService;
	
	@Autowired
	private ExamineService examineService;
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private TrainstudyService trainstudyService;
	
	@Autowired
	private VideoService videoService;
	
	@Autowired
	private CourseRewardSetService courseRewardSetService;
	
	@Autowired
	private MyRewardService myRewardService;
	
	/**
     * 查询试题
     * */
	@ResponseBody
	@RequestMapping(value = "examlist")
	public AjaxJson getExamList(String courseId,String token,HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		AjaxJson ajaxJson = new AjaxJson();
		ExamQuestions examQuestions = new ExamQuestions();
		QuestionsItems questionsItems = new QuestionsItems();
		//查询试题列表
		examQuestions.setCourseId(courseId);
		List<ExamQuestions> examlist = examQuestionsService.findList(examQuestions);
		List<QuestionsItems> questionsItemsList = null;
		if(examlist.size()!=0){
			for (ExamQuestions list : examlist) {
				//每一道试题的id
				examQuestions.setId(list.getId());
				questionsItems.setExamQuestions(examQuestions);
				//根据试题查找选项列表
				questionsItemsList = examQuestionsService.findItemList(questionsItems);
				list.setQuestionsItemsList(questionsItemsList);
			}
			ajaxJson.setSuccess(true);
	     	ajaxJson.setMsg("试题列表");
	     	ajaxJson.put("examlist", examlist);
		}else{
			ajaxJson.setSuccess(false);
        	ajaxJson.setMsg("还没有试题");	
		}
		return ajaxJson;
	}
	
	/**
     * 保存考试明细，并计算考试成绩
     * */
	@ResponseBody
	@RequestMapping(value = "saveAnswer")
	public AjaxJson saveAnswer(String courseId,String token,String time,String testData,HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		UserApi userApi=(UserApi)CacheUtils.get(CacheUtils.USER_API_CACHE, token);
		String  userId = userApi.getId();
		AjaxJson ajaxJson = new AjaxJson();
		Examine examine = new Examine();
		examine.setUserId(userId);
		examine.setCourseId(courseId);
		List<Examine> myExamList = examineService.findList(examine);
		if(myExamList.size()!= 0){
			ajaxJson.setSuccess(false);
		    ajaxJson.setMsg("你已经参加过本课程的考试!");
		}else{
			ExamQuestions examQuestions = new ExamQuestions();
			QuestionsItems questionsItems = new QuestionsItems();
			//String testData = "1:0|2:0,1,2,3|3:2|4:2|5:3|6:2|"; //测试       1:|2:null|3:null|
			String test[] = testData.split("\\|");//分割字符串，得试题组
			double result = 0; //初始化成绩
			//查询试题列表
			examQuestions.setCourseId(courseId);
		    List<ExamQuestions> examlist = examQuestionsService.findList(examQuestions);
		    List<QuestionsItems> questionsItemsList = null;
		    Examine exam = new Examine();
		    exam.setUserId(userId);
		    exam.setCourseId(courseId);
		    exam.setUsedTime(time);
		    //遍历获得每个试题组
			 for (int i = 0 ; i <test.length ; i++ ) {
				 //new一个考试明细对象
				 ExamineDetail examineDetail = new ExamineDetail();
				 System.out.println("每一个试题组："+test[i]);
				 //把题号和答案分割开
				 String answer[]=test[i].split(":");
				 String ansContent = ""; //所填答案
				 if(answer.length==2){
					 if(answer[1].equals("null")){
						 ansContent=""; 
					 }else{
						 ansContent=answer[1];
					 } 
				 }
				 //保存考试明细
				 examineDetail.setTestContent(ansContent);//答题内容
				 examineDetail.setUserId(userId);
				 examineDetail.setQuestionId(examlist.get(i).getId());//试题id
				 examQuestions.setId(examlist.get(i).getId());
				 questionsItems.setExamQuestions(examQuestions);
				 //根据试题查找正确选项列表
				 questionsItems.setIsAnswer(Global.YES);
				 questionsItemsList = examQuestionsService.findItemList(questionsItems);//正确选项列表
				 String rightAns = "";//正确答案
				 for (int k =0; k<questionsItemsList.size();k++) {
					 if(k<(questionsItemsList.size()-1)){
						 rightAns += String.valueOf((questionsItemsList.get(k).getSort()-1))+",";
					 }else{
						 rightAns += String.valueOf((questionsItemsList.get(k).getSort()-1)); 
					 } 
				 }
				 //System.out.println("正确答案是："+rightAns);
				 
				 if(rightAns.equals(ansContent)){
					 examineDetail.setIsRight(Global.YES);//答案正确
					 result += examlist.get(i).getPoint();//计算成绩
					 System.out.println("成绩是："+result);
				 }else{
					 examineDetail.setIsRight(Global.NO);//答案错误
				 }
				 examineDetailService.save(examineDetail);//保存考试明细
				 }
			 exam.setExamResult(result);
			 exam.setExamTime(new Date());
			 examineService.save(exam);//保存我的考试
			 //查询是否有奖励
			 List<RewardModel>  rewardList = courseRewardSetService.getRewardByCourse(courseId);
			 if(rewardList.size() != 0){
				for (RewardModel reward : rewardList) {
					//获得奖励，向我的奖励表添加数据,保存我的奖励
					MyReward myReward = new MyReward();
					myReward.setUserId(userId);
					myReward.setCourseId(courseId);
					myReward.setReceiveStatus("0");
					myReward.setRewardId(reward.getId());
					Date gettime = new Date();
					myReward.setGettime(gettime);
					myReward.setUserMode("0");
					myReward.setTypeName(reward.getTypeName());
					if (reward.getTypeIndex().equals("1")) {
						myReward.setRewardValue(Integer.parseInt(reward.getMoney()));
					}
					myRewardService.save(myReward);	
				}    
			 }
			 ajaxJson.setSuccess(true);
		     ajaxJson.setMsg("考试成绩");
			 ajaxJson.put("result", result);	
		}
		 return ajaxJson;
	}
	
	/**
	 * 获取所有课程目录
	 * @return
	 * @author zhaol
	 */
	@ResponseBody
	@RequestMapping(value = "getMyCourse")
	public AjaxJson getAllCourseDir(String token,HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		UserApi userApi=(UserApi)CacheUtils.get(CacheUtils.USER_API_CACHE, token);
		String  userId = userApi.getId();
		AjaxJson ajaxJson = new AjaxJson();
		List<CourseModel> courselist = courseService.findCourseList(userId);
		for (CourseModel  course : courselist) {
			int itemNum = courseService.findItemNum(course.getId());
			//查询试题列表
			ExamQuestions examQuestions = new ExamQuestions();
			examQuestions.setCourseId(course.getId());
		    List<ExamQuestions> examlist = examQuestionsService.findList(examQuestions);
		    double sumPoint = 0; //初始化总分数
		    for (ExamQuestions examQues : examlist) {
		    	sumPoint+=examQues.getPoint();//计算总分数	
			}
		    //计算正确率
			int rightNum = examineService.findRightNum(course.getId(),userId);
			NumberFormat numberFormat = NumberFormat.getInstance();   
	        // 设置精确到小数点后0位  
	        numberFormat.setMaximumFractionDigits(0);
	        String rightRate = numberFormat.format((float) rightNum / (float) itemNum * 100)+"%";//正确率
			course.setItemNum(itemNum);
			course.setSumPoint(sumPoint);
			course.setRightRate(rightRate);
			course.setCourseBrief(Encodes.unescapeHtml(course.getCourseBrief()));
			Lesson lesson = trainstudyService.findLastLesson(course.getId());
			Trainstudy study = trainstudyService.findCourseProgress(lesson.getId(),userId);
			if(study != null){
				 course.setIsFinish(study.getIsstudy());
				 Video video = videoService.findVideoByLesson(study.getLessonid());
				 course.setCourseImg(video.getVideoImage());
			}else{
				course.setIsFinish("0");
			}   
		}
		ajaxJson.setSuccess(true);
	    ajaxJson.setMsg("课程列表");
		ajaxJson.put("course", courselist);
	    return ajaxJson;
	}
	
	/**
	 * 判断课程是否有奖励
	 * @return
	 * @author zhaol
	 */
	@ResponseBody
    @RequestMapping(value = "isReward")
	public AjaxJson isReward(String courseId,String token,HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		UserApi userApi=(UserApi)CacheUtils.get(CacheUtils.USER_API_CACHE, token);
		String  userId = userApi.getId();
		AjaxJson ajaxJson = new AjaxJson();
		//查询主播
		AnchorInfo anchor = anchorInfoService.findUniqueByProperty("user_id", userId);
		//计算正确率
		int rightNum = examineService.findRightNum(courseId,userId);
		int itemNum = courseService.findItemNum(courseId);
		NumberFormat numberFormat = NumberFormat.getInstance();   
        // 设置精确到小数点后0位  
        numberFormat.setMaximumFractionDigits(0);
        String score = numberFormat.format((float) rightNum / (float) itemNum * 100);//按正确率显示成绩
        //String rightRate = score+"%";
        //查询是否有奖励
		List<RewardModel>  rewardList = courseRewardSetService.getRewardByCourse(courseId);
		Course course = courseService.get(courseId);
		if(rewardList.size() != 0){
		    ajaxJson.setMsg("有奖励");
			ajaxJson.put("reward", "1");
		}else{
		    ajaxJson.setMsg("没有奖励信息");
			ajaxJson.put("reward","0");	
		}
		ajaxJson.setSuccess(true);
		ajaxJson.put("anchor", anchor);
		ajaxJson.put("course", course);
		ajaxJson.put("score", score);
	    return ajaxJson;
	}
	
	/**
	 * 获取课程奖励
	 * @return
	 * @author zhaol
	 */
	@ResponseBody
	@RequestMapping(value = "getReward")
	public AjaxJson getReward(String courseId,String token,HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		UserApi userApi=(UserApi)CacheUtils.get(CacheUtils.USER_API_CACHE, token);
		String  userId = userApi.getId();
		AjaxJson ajaxJson = new AjaxJson();
		List<RewardModel>  rewardList = courseRewardSetService.getRewardByCourse(courseId);
		int rightNum = examineService.findRightNum(courseId,userId);
		int itemNum = courseService.findItemNum(courseId);
		NumberFormat numberFormat = NumberFormat.getInstance();   
        // 设置精确到小数点后0位  
        numberFormat.setMaximumFractionDigits(0);
        String score = numberFormat.format((float) rightNum / (float) itemNum * 100);//按正确率显示成绩
        String rightRate = score+"%";  
        Course course = courseService.get(courseId);
		if(rewardList.size() != 0){
			AnchorInfo anchor = anchorInfoService.findUniqueByProperty("user_id", userId);
			for (RewardModel reward : rewardList) {
				//红包
				if(reward.getTypeIndex().equals(Global.REDPACKET)){
					String redpacket = reward.getRewardName();
					ajaxJson.put("redpacket", redpacket);
				//奖章
				}else if(reward.getTypeIndex().equals(Global.MEDAL)){
					String medal = reward.getRewardImg();
					String rewardName = reward.getRewardName();
					String medalId = reward.getRewardNameIndex();
					ajaxJson.put("medal", medal);
					ajaxJson.put("rewardName", rewardName);
					ajaxJson.put("medalId", medalId);
				//称号
				}else if(reward.getTypeIndex().equals(Global.TITLE)){
					String title = reward.getRewardName();
					ajaxJson.put("title", title);
				}
			}
			String type = "1";
			String graduation = "0";
			Course lastCourse = courseService.getLastCourse(type);//最后一门课
			if(course.getId().equals(lastCourse.getId())){
				graduation = "1";
			}
			ajaxJson.setSuccess(true);
		    ajaxJson.setMsg("奖励信息");
		    ajaxJson.put("anchor", anchor);
		    ajaxJson.put("course", course);
			ajaxJson.put("rewardList", rewardList);
			ajaxJson.put("rightRate", rightRate);
			ajaxJson.put("graduation", graduation);
		}else{
			ajaxJson.setSuccess(false);
		    ajaxJson.setMsg("系统出错，请重试！");
		}
	    return ajaxJson;
	}

	/**
	 * 成绩排行榜
	 * @author zhaol
	 */
	@ResponseBody
	@RequestMapping(value = "scoreRank")
	public AjaxJson getScoreRank(String token,HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		UserApi userApi=(UserApi)CacheUtils.get(CacheUtils.USER_API_CACHE, token);
		AnchorInfo anchor = anchorInfoService.findBymobile(userApi.getMobile());
		String company = anchor.getCompany().getId();
		AjaxJson ajaxJson = new AjaxJson();
		List<AnchorModel> anchorList = examineService.getResultRank(company);
		ajaxJson.setSuccess(true);
	    ajaxJson.setMsg("成绩排行榜");
	    ajaxJson.put("resultRank", anchorList);
		return ajaxJson;
	}

	
}

