package com.jeeplus.modules.api.web;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.common.utils.Encodes;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.api.entity.UserApi;
import com.jeeplus.modules.course.dao.LessonDao;
import com.jeeplus.modules.course.entity.Course;
import com.jeeplus.modules.course.entity.CourseType;
import com.jeeplus.modules.course.entity.Lesson;
import com.jeeplus.modules.course.service.CourseService;
import com.jeeplus.modules.course.service.CourseTypeService;
import com.jeeplus.modules.course.service.VideoService;
import com.jeeplus.modules.exam.entity.examresult.Examine;
import com.jeeplus.modules.exam.service.examquestions.ExamQuestionsService;
import com.jeeplus.modules.exam.service.examresult.ExamineService;
import com.jeeplus.modules.lesson.VodUtils;
import com.jeeplus.modules.mystudy.entity.Trainstudy;
import com.jeeplus.modules.mystudy.service.TrainCommentService;
import com.jeeplus.modules.mystudy.service.TrainstudyService;
import com.jeeplus.modules.reward.entity.CourseRewardSet;
import com.jeeplus.modules.reward.entity.Reward;
import com.jeeplus.modules.reward.service.CourseRewardSetService;
import com.jeeplus.modules.reward.service.RewardService;

@Controller
@RequestMapping(value = "${frontPath}/api/")
public class CourseApiController   extends BaseController{

	@Autowired
	private CourseService courseService;
	@Autowired
	private CourseTypeService courseTypeService;
	@Autowired
	private LessonDao lessonDao;
	@Autowired
	private VideoService videoService;
	@Autowired
	private ExamQuestionsService examQuestionsService;
	@Autowired
	private TrainstudyService trainstudyService;
	@Autowired
	private ExamineService examineService;
	@Autowired
	private CourseRewardSetService courseRewardSetService;
	@Autowired
	private RewardService rewardService;
	@Autowired
	private TrainCommentService traincommentService;
	
	/**
	 * 获取标准课程正在学习的课程信息(培训首页)
	 * @param token 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getStandardStudyingCourse")
	public AjaxJson getStandardStudyingCourse(String token,HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		UserApi userApi=(UserApi)CacheUtils.get(CacheUtils.USER_API_CACHE, token);
		AjaxJson ajaxJson=new AjaxJson();
		ajaxJson.put("userid", userApi.getId());
		DecimalFormat df = new DecimalFormat("##.00");
		//查询用户标准课程学习记录
		List<Trainstudy> studyed = trainstudyService.findStudyingLessons("1",userApi.getId());
		if(studyed ==null ||studyed.isEmpty()){
			getTrainIndexInfo(1, 1, "1", ajaxJson);
			ajaxJson.put("standardProgress", 0);//标准课程总学习进度
			return ajaxJson;
		}
		//查询标准课程章节总数
		double standardCount  = (double)lessonDao.getLessonCountByType("1");
		//查询所有标准课程包括章节信息
		Course courseModel = new Course();
		CourseType ct = new CourseType();
		ct.setType("1");//扩展课程
		courseModel.setCourseType(ct);
		List<Course> list = courseService.findList(courseModel);
		int index = 0;
		for (int j = 0; j < list.size(); j++) {
			Course course = list.get(j);
			course.setLessonList(lessonDao.findList(new Lesson(course)));
			List<Lesson> lessons = course.getLessonList();
			for (int i = 0; i < lessons.size(); i++) {
				Lesson lesson = lessons.get(i);
				int commentNum = traincommentService.findCommentNumByLesson(lesson.getId());//评论次数
				int studyNum = trainstudyService.findStudyNumByLesson(lesson.getId());//学习人数
				List<Object> isStudy = new ArrayList<Object>();
				if(i == lessons.size()-1){
					isStudy = isStudyComplete(true,course,lessons.get(i),userApi);
				}else{
					isStudy = isStudyComplete(false,course,lessons.get(i),userApi);
				}
				if((Integer)isStudy.get(0)==0){
					//未学完，返回
					ajaxJson.put("lessonName", lesson.getLessonName());
					ajaxJson.put("courseBrief", Encodes.unescapeHtml(course.getCourseBrief()));
					ajaxJson.put("videoImage", lesson.getVideo().getVideoImage());
					ajaxJson.put("courseSort", course.getSort());
					ajaxJson.put("lessonSort", lesson.getSort());
					ajaxJson.put("courseId", course.getId());
					ajaxJson.put("lessonId", lesson.getId());
					//ajaxJson.put("progress", studyUncomplete.getStudyprogress());
					ajaxJson.put("standardProgress", df.format(index/standardCount));//标准课程总学习进度
					ajaxJson.put("playNum", lesson.getPlayNum());
					ajaxJson.put("giveLikeNum", lesson.getGivelikeNum());
					ajaxJson.put("commentNum", commentNum);
					ajaxJson.put("studyNum", studyNum);
					return ajaxJson;
				}else{
					//本章节已学完
					if(j==list.size()-1 && i==lessons.size()-1){
						//全部学完，显示最后一节课信息
						ajaxJson.put("lessonName", lesson.getLessonName());
						ajaxJson.put("courseBrief", Encodes.unescapeHtml(course.getCourseBrief()));
						ajaxJson.put("videoImage", lesson.getVideo().getVideoImage());
						ajaxJson.put("courseSort", course.getSort());
						ajaxJson.put("lessonSort", lesson.getSort());
						ajaxJson.put("courseId", course.getId());
						ajaxJson.put("lessonId", lesson.getId());
						//ajaxJson.put("progress", studyUncomplete.getStudyprogress());
						ajaxJson.put("standardProgress", 1);//标准课程总学习进度
						ajaxJson.put("playNum", lesson.getPlayNum());
						ajaxJson.put("giveLikeNum", lesson.getGivelikeNum());
						ajaxJson.put("commentNum", commentNum);
						ajaxJson.put("studyNum", studyNum);
						return ajaxJson;
					}else{
						index ++;
						continue;
					}
				}
			}
		}
		return ajaxJson;
		
		//该用户没有学习记录，显示第一节课
		/*if(studyed ==null ||studyed.isEmpty()){
			getTrainIndexInfo(1, 1, "1", ajaxJson);
			ajaxJson.put("standardProgress", 0);//标准课程总学习进度
			return ajaxJson;
		}
		else{
			
			//已学标准课程章节数量
			double studyedCount = (double)studyed.size();
			boolean studyComplete = true;//该章节已学完
			int index1 = 0;
			for (int i=0; i<studyed.size(); i++) {
				if( 1 !=studyed.get(i).getStudyprogress()){
					studyComplete = false;
					index = i;
					break;
				}
			}
			//某章节未学完，返回该章节信息
			if( !studyComplete){
				Trainstudy studyUncomplete = studyed.get(index);
				Course course = courseService.get(studyUncomplete.getCourseid());
				Lesson lesson = lessonDao.get(studyUncomplete.getLessonid());
				ajaxJson.put("lessonName", lesson.getLessonName());
				ajaxJson.put("courseBrief", course.getCourseBrief());
				ajaxJson.put("videoImage", lesson.getVideo().getVideoImage());
				ajaxJson.put("courseSort", course.getSort());
				ajaxJson.put("lessonSort", lesson.getSort());
				ajaxJson.put("progress", studyUncomplete.getStudyprogress());
				ajaxJson.put("standardProgress", df.format((studyedCount-1)/standardCount));//标准课程总学习进度
				return ajaxJson;
			}else{				
				//学习记录全部已学完,查询最近学习记录(章节)是否是该课程最后一个章节
				Course course = courseService.get(studyed.get(0).getCourseid());
				Lesson lesson = lessonDao.get(studyed.get(0).getLessonid());
				Integer max = lessonDao.getMaxSort(lesson);
				if(lesson.getSort() == max){
					//是最后一个章节，查询该课程是否有考试题
					Integer questionsNum = examQuestionsService.getQuestionsNumOfCourse(course.getId());
					if(questionsNum!= null && questionsNum>0){
						//有考试题，查询是否已考试
						Examine exam = new Examine();
						exam.setCourseId(course.getId());
						exam.setUserId(userApi.getId());
						List<Examine> examList = examineService.findList(exam);
						if(examList==null || examList.isEmpty()){
							//未考试
							ajaxJson.put("needToExam", true);
							ajaxJson.put("lessonName", lesson.getLessonName());
							ajaxJson.put("courseBrief", course.getCourseBrief());
							ajaxJson.put("videoImage", lesson.getVideo().getVideoImage());
							ajaxJson.put("courseSort", course.getSort());
							ajaxJson.put("lessonSort", lesson.getSort());
							ajaxJson.put("progress", studyed.get(0).getStudyprogress());
							ajaxJson.put("hasStudyNum", studyed.get(0).getStudyplace());//剩余学习次数
							ajaxJson.put("standardProgress",  df.format((studyedCount-1)/standardCount));//标准课程总学习进度
							return ajaxJson;
						}else{
							//已考试，返回下一课程信息							
							getTrainIndexInfo(course.getSort()+1, 1, "1", ajaxJson);
							ajaxJson.put("standardProgress",  df.format(studyedCount/standardCount));//标准课程总学习进度
							return ajaxJson;
						}
						
					}else{
						//课程最后一个章节并且没有考试，进入下一课
						getTrainIndexInfo(course.getSort()+1, 1, "1", ajaxJson);
						ajaxJson.put("standardProgress",  df.format(studyedCount/standardCount));//标准课程总学习进度
						return ajaxJson;
					}
				}else{
					//不是该课程最后一个章节，进入下一章节学习
					getTrainIndexInfo(course.getSort(),lesson.getSort()+1,"1",ajaxJson);
					ajaxJson.put("standardProgress",  df.format(studyedCount/standardCount));//标准课程总学习进度
					return ajaxJson;
				}
				
			}
		}*/
	}
	
	/**
	 * 查询章节是否学完   
	 * @param isLastLesson 是否是该课程最后一个章节
	 * @param course 章节所属课程
	 * @param lesson 章节
	 * @param userApi
	 * @return list(0): 0 未学   1 已学
	 *         list(1): 剩余学习次数
	 */
	private List<Object> isStudyComplete(Boolean isLastLesson,Course course,Lesson lesson,UserApi userApi){
		Trainstudy trainstudy = new Trainstudy();
		trainstudy.setLessonid(lesson.getId());
		trainstudy.setUserid(userApi.getId());
		List<Trainstudy> study = trainstudyService.findListBy(trainstudy);
		List<Object> result = new ArrayList<Object>();
		if(study != null && study.size()>0){
			if("1".equals(study.get(0).getIsstudy())){
				//查询是否有考试
				if(isLastLesson){
					Integer questionsNum = examQuestionsService.getQuestionsNumOfCourse(course.getId());
					if(questionsNum!= null && questionsNum>0){
						//查询是否已考试
						Examine exam = new Examine();
						exam.setCourseId(course.getId());
						exam.setUserId(userApi.getId());
						List<Examine> examList = examineService.findList(exam);
						if(examList==null || examList.isEmpty()){
							result.add(0);
							result.add(study.get(0).getStudyplace());
							return result;
						}else{
							result.add(1);
							result.add(study.get(0).getStudyplace());
							return result;
						}
					}else{
						result.add(1);
						result.add(study.get(0).getStudyplace());
						return result;
					}
				}else{
					result.add(1);
					result.add(study.get(0).getStudyplace());
					return result;
				}
			}else{
				result.add(0);
				result.add(study.get(0).getStudyplace());
				return result;
			}
		}else{
			result.add(0);
			result.add(lesson.getStudyLimitNum());
			return result;
		}
	}
	
	
	/**
	 * 获取标准课程正在学习的课程信息
	 * @param token 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getStandardStudyingCourse1")
	public AjaxJson getStandardStudyingCourse1(String token,HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		UserApi userApi=(UserApi)CacheUtils.get(CacheUtils.USER_API_CACHE, token);
		AjaxJson ajaxJson=new AjaxJson();
		DecimalFormat df = new DecimalFormat("#.00");
		//查询用户标准课程学习记录
		List<Trainstudy> studyed = trainstudyService.findStudyingLessons("1",userApi.getId());
		//该用户没有学习记录，显示第一节课
		if(studyed ==null ||studyed.isEmpty()){
			getTrainIndexInfo(1, 1, "1", ajaxJson);
			ajaxJson.put("standardProgress", 0);//标准课程总学习进度
			return ajaxJson;
		}else{
			//查询标准课程章节总数
			double standardCount  = (double)lessonDao.getLessonCountByType("1");
			//已学标准课程章节数量
			double studyedCount = (double)studyed.size();
			boolean studyComplete = true;//该章节已学完
			int index = 0;
			for (int i=0; i<studyed.size(); i++) {
				if( 1 !=studyed.get(i).getStudyprogress()){
					studyComplete = false;
					index = i;
					break;
				}
			}
			//某章节未学完，返回该章节信息
			if( !studyComplete){
				Trainstudy studyUncomplete = studyed.get(index);
				Course course = courseService.get(studyUncomplete.getCourseid());
				Lesson lesson = lessonDao.get(studyUncomplete.getLessonid());
				ajaxJson.put("lessonName", lesson.getLessonName());
				ajaxJson.put("courseBrief", Encodes.unescapeHtml(course.getCourseBrief()));
				ajaxJson.put("videoImage", lesson.getVideo().getVideoImage());
				ajaxJson.put("courseSort", course.getSort());
				ajaxJson.put("lessonSort", lesson.getSort());
				ajaxJson.put("progress", studyUncomplete.getStudyprogress());
				ajaxJson.put("standardProgress", df.format((studyedCount-1)/standardCount));//标准课程总学习进度
				return ajaxJson;
			}else{				
				//学习记录全部已学完,查询最近学习记录(章节)是否是该课程最后一个章节
				Course course = courseService.get(studyed.get(0).getCourseid());
				Lesson lesson = lessonDao.get(studyed.get(0).getLessonid());
				Integer max = lessonDao.getMaxSort(lesson);
				if(lesson.getSort() == max){
					//是最后一个章节，查询该课程是否有考试题
					Integer questionsNum = examQuestionsService.getQuestionsNumOfCourse(course.getId());
					if(questionsNum!= null && questionsNum>0){
						//有考试题，查询是否已考试
						Examine exam = new Examine();
						exam.setCourseId(course.getId());
						exam.setUserId(userApi.getId());
						List<Examine> examList = examineService.findList(exam);
						if(examList==null || examList.isEmpty()){
							//未考试
							ajaxJson.put("needToExam", true);
							ajaxJson.put("lessonName", lesson.getLessonName());
							ajaxJson.put("courseBrief", Encodes.unescapeHtml(course.getCourseBrief()));
							ajaxJson.put("videoImage", lesson.getVideo().getVideoImage());
							ajaxJson.put("courseSort", course.getSort());
							ajaxJson.put("lessonSort", lesson.getSort());
							ajaxJson.put("progress", studyed.get(0).getStudyprogress());
							ajaxJson.put("hasStudyNum", studyed.get(0).getStudyplace());//剩余学习次数
							ajaxJson.put("standardProgress",  df.format((studyedCount-1)/standardCount));//标准课程总学习进度
							return ajaxJson;
						}else{
							//已考试，返回下一课程信息							
							getTrainIndexInfo(course.getSort()+1, 1, "1", ajaxJson);
							ajaxJson.put("standardProgress",  df.format(studyedCount/standardCount));//标准课程总学习进度
							return ajaxJson;
						}
						
					}else{
						//课程最后一个章节并且没有考试，进入下一课
						getTrainIndexInfo(course.getSort()+1, 1, "1", ajaxJson);
						ajaxJson.put("standardProgress",  df.format(studyedCount/standardCount));//标准课程总学习进度
						return ajaxJson;
					}
				}else{
					//不是该课程最后一个章节，进入下一章节学习
					getTrainIndexInfo(course.getSort(),lesson.getSort()+1,"1",ajaxJson);
					ajaxJson.put("standardProgress",  df.format(studyedCount/standardCount));//标准课程总学习进度
					return ajaxJson;
				}
				
			}
		}
	}
	
	
	
	/**
	 * 获取扩展课程列表信息
	 * @param token 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "listAdvancedCourse")
	public AjaxJson listAdvancedCourse(String token,HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		UserApi userApi=(UserApi)CacheUtils.get(CacheUtils.USER_API_CACHE, token);
		AjaxJson ajaxJson=new AjaxJson();
		//查询所有扩展课程包括章节信息
		Course courseModel = new Course();
		CourseType ct = new CourseType();
		ct.setType("2");//扩展课程
		courseModel.setCourseType(ct);
		List<Course> list = courseService.findList(courseModel);
		for (Course course : list) {
			course.setLessonList(lessonDao.findList(new Lesson(course)));
		}
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		//遍历所有扩展章节，查询学习表中是否有记录
		for (int i = 0; i < list.size(); i++) {
			Course course = list.get(i);
			List<Lesson> lessonList = course.getLessonList();
//			boolean hasRecord = false;//有学习记录
//			boolean isStudying = false;//学习进度不为1
//			boolean hasNotStudy = false;//多个章节，且存在未学习章节
//			Lesson nextLesson = new Lesson();
			for (int j = 0; j < lessonList.size(); j++) {
				Lesson lesson = lessonList.get(j);
				int commentNum = traincommentService.findCommentNumByLesson(lesson.getId());//评论次数
				int studyNum = trainstudyService.findStudyNumByLesson(lesson.getId());//学习人数
				Trainstudy trainstudy = new Trainstudy();
				trainstudy.setLessonid(lessonList.get(j).getId());
				trainstudy.setUserid(userApi.getId());
				List<Trainstudy> studyLesson = trainstudyService.findListBy(trainstudy);
				if(studyLesson !=null && studyLesson.size()>0){
					//hasRecord = true;
					if(!"1".equals(studyLesson.get(0).getIsstudy())){
						//isStudying = true;
						Map<String,Object> map = new HashMap<String,Object>();
						map.put("lessonName", lesson.getLessonName());
						map.put("courseBrief", Encodes.unescapeHtml(course.getCourseBrief()));
						map.put("videoImage", lesson.getVideo().getVideoImage());
						map.put("courseSort", course.getSort());
						map.put("lessonSort", lesson.getSort());
						map.put("courseId", course.getId());
						map.put("lessonId", lesson.getId());
						map.put("progress", studyLesson.get(0).getStudyprogress());
						map.put("playNum", lesson.getPlayNum());
						map.put("giveLikeNum", lesson.getGivelikeNum());
						map.put("commentNum", commentNum);
						map.put("studyNum", studyNum);
						result.add(map);
						break;
					}else{
						//若本门课程所有章节全部完成，就显示最后一章节信息
						if(j==lessonList.size()-1){
							Map<String,Object> map = new HashMap<String,Object>();
							map.put("lessonName", lesson.getLessonName());
							map.put("courseBrief", Encodes.unescapeHtml(course.getCourseBrief()));
							map.put("videoImage", lesson.getVideo().getVideoImage());
							map.put("courseSort", course.getSort());
							map.put("lessonSort", lesson.getSort());
							map.put("courseId", course.getId());
							map.put("lessonId", lesson.getId());
							map.put("playNum", lesson.getPlayNum());
							map.put("giveLikeNum", lesson.getGivelikeNum());
							map.put("commentNum", commentNum);
							map.put("studyNum", studyNum);
							result.add(map);
						}
					}
				}else{
//					if(j>0){
//						//本课程存在多个章节，且存在未学习章节
//						hasNotStudy = true;
//						nextLesson = lesson;
//						break;
//					}
					//无此章节学习记录，显示此章节信息
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("lessonName", lesson.getLessonName());
					map.put("courseBrief", Encodes.unescapeHtml(course.getCourseBrief()));
					map.put("videoImage", lesson.getVideo().getVideoImage());
					map.put("courseSort", course.getSort());
					map.put("lessonSort", lesson.getSort());
					map.put("courseId", course.getId());
					map.put("lessonId", lesson.getId());
					map.put("playNum", lesson.getPlayNum());
					map.put("giveLikeNum", lesson.getGivelikeNum());
					map.put("commentNum", commentNum);
					map.put("studyNum", studyNum);
					result.add(map);
					break;
				}
			}
//			if( !hasRecord){
//				//本门课程未发现学习记录，显示第一章节信息
//				Map<String,Object> map = new HashMap<String,Object>();
//				map.put("lessonName", lessonList.get(0).getLessonName());
//				map.put("courseBrief", course.getCourseBrief());
//				map.put("videoImage", lessonList.get(0).getVideo().getVideoImage());
//				map.put("courseSort", course.getSort());
//				map.put("lessonSort", lessonList.get(0).getSort());
//				result.add(map);
//			}else{
//				if(!isStudying && hasNotStudy){
//					//本课程有学习记录，且记录进度已完成，存在未学习章节，显示下一章节信息
//					Map<String,Object> map = new HashMap<String,Object>();
//					map.put("lessonName", nextLesson.getLessonName());
//					map.put("courseBrief", course.getCourseBrief());
//					map.put("videoImage", nextLesson.getVideo().getVideoImage());
//					map.put("courseSort", course.getSort());
//					map.put("lessonSort", nextLesson.getSort());
//					result.add(map);
//				}
//			}
		}
		ajaxJson.put("course", result);
		return ajaxJson;
	}
	
	public void getTrainIndexInfo(Integer courseSort,Integer lessonSort,String type,AjaxJson ajaxJson){
		Course courseModel = new Course();
		courseModel.setSort(courseSort);
		CourseType ct = new CourseType();
		ct.setType(type);
		courseModel.setCourseType(ct);
		List<Course> list = courseService.findList(courseModel);
		Course course = list.get(0);
		Lesson le = new Lesson(course);
		le.setSort(lessonSort);
		Lesson lesson = lessonDao.getByCourseAndSort(le);
		int commentNum = traincommentService.findCommentNumByLesson(lesson.getId());//评论次数
		int studyNum = trainstudyService.findStudyNumByLesson(lesson.getId());//学习人数
		
		ajaxJson.put("lessonName", lesson.getLessonName());
		ajaxJson.put("courseBrief", Encodes.unescapeHtml(course.getCourseBrief()));
		ajaxJson.put("videoImage", lesson.getVideo().getVideoImage());
		ajaxJson.put("courseSort", course.getSort());
		ajaxJson.put("lessonSort", lesson.getSort());
		ajaxJson.put("courseId", course.getId());
		ajaxJson.put("lessonId", lesson.getId());
		ajaxJson.put("playNum", lesson.getPlayNum());
		ajaxJson.put("giveLikeNum", lesson.getGivelikeNum());
		ajaxJson.put("commentNum", commentNum);
		ajaxJson.put("studyNum", studyNum);
	}
	
	/**
	 * 获取某课程某章节全部信息
	 * @param type 1 标准课程   2 进阶课程
	 * @param courseSort 课程排序
	 * @param lessonSort 章节排序
	 * @param response
	 * @return
	 * @author jiangjl
	 */
	@ResponseBody
	@RequestMapping(value = "getCourseInfo")
	public AjaxJson getCourseInfo(String token,String type, Integer courseSort,Integer lessonSort,HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		UserApi userApi=(UserApi)CacheUtils.get(CacheUtils.USER_API_CACHE, token);
		AjaxJson ajaxJson=new AjaxJson();
		Course courseModel = new Course();
		courseModel.setSort(courseSort);
		CourseType ct = new CourseType();
		ct.setType(type);
		courseModel.setCourseType(ct);
		List<Course> list = courseService.findList(courseModel);
		Course course = list.get(0);
		Lesson le = new Lesson(course);
		le.setSort(lessonSort);
		Lesson lesson = lessonDao.getByCourseAndSort(le);
		List<Lesson> lessons = new ArrayList<Lesson>();
		lessons.add(lesson);
		course.setLessonList(lessons);
		course.setDetails(Encodes.unescapeHtml(course.getDetails()));
		course.setCourseBrief(Encodes.unescapeHtml(course.getCourseBrief()));
		course.getTeacher().setNote(Encodes.unescapeHtml(course.getTeacher().getNote()));
		
		Trainstudy trainstudy = new Trainstudy();
		trainstudy.setLessonid(lesson.getId());
		trainstudy.setUserid(userApi.getId());
		List<Trainstudy> studyed = trainstudyService.findListBy(trainstudy);
		if("1".equals(type)){
			//标准课程查询此章节剩余学习次数
			if(studyed!=null && studyed.size()>0){
				ajaxJson.put("studyPlace",studyed.get(0).getStudyplace());
			}else{
				ajaxJson.put("studyPlace",lesson.getStudyLimitNum());
			}
			//查询是否是最后一个课程
			boolean isLastCourse = false;
			Course lastCourse = courseService.getLastCourse("1");
			if(course.getId().equals(lastCourse.getId())){
				isLastCourse = true;
			}
			//查询该章节是否是该课程最后一个章节
			Integer max = lessonDao.getMaxSort(le);
			if(lesson.getSort() == max){
				ajaxJson.put("isLast",true);
				if(isLastCourse){
					ajaxJson.put("end",true);
				}
			}
			//查询该课程是否有考试
			Integer num = examQuestionsService.getQuestionsNumOfCourse(course.getId());
			if(num != null && num > 0){
				ajaxJson.put("hasExam",true);
				//查询是否已考试
				Examine exam = new Examine();
				exam.setCourseId(course.getId());
				exam.setUserId(userApi.getId());
				List<Examine> examine = examineService.findList(exam);
				if(examine!=null && examine.size()>0){
					ajaxJson.put("examed",true);
				}
			}
		}
		//查询该章节学习进度
		if(studyed!=null && studyed.size()>0){
			ajaxJson.put("progress",studyed.get(0).getStudyprogress());
			ajaxJson.put("isStudy",studyed.get(0).getIsstudy());
		}
		
		ajaxJson.setSuccess(true);
		ajaxJson.put("course",course);
		
	    return ajaxJson;
	}
	
	/**
	 * 获取播放凭证
	 * @param response
	 * @return
	 * @author jiangjl
	 */
	@ResponseBody
	@RequestMapping(value = "getPlayauth")
	public AjaxJson getPlayauth(String videoId,HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		AjaxJson ajaxJson=new AjaxJson();
		DefaultAcsClient aliyunClient= new DefaultAcsClient(DefaultProfile.getProfile("cn-shanghai",VodUtils.accessKeyId,VodUtils.accessKeySecret));
		GetVideoPlayAuthResponse respone= VodUtils.getVideoPlayAuth(aliyunClient,videoId);
		ajaxJson.setSuccess(true);
		ajaxJson.put("playAuth", respone.getPlayAuth());
		
	    return ajaxJson;
	}
	
	/**
	 * 按类型获取所有章节目录
	 * @param type
	 * @param stage
	 * @param response
	 * @return
	 * @author jiangjl
	 */
	@ResponseBody
	@RequestMapping(value = "getAllLessonDir1")
	public AjaxJson getAllLessonDir1(String type,HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		AjaxJson ajaxJson=new AjaxJson();
		Course courseModel = new Course();
		//查询共有几个阶段
		Integer stagesCount = courseTypeService.getStagesCountByType(type);
		List<Map<String,Object>> ajaxlist = new ArrayList<Map<String,Object>>();
		//查询某阶段分类
		CourseType ct = new CourseType();
		for (int i = 0; i < stagesCount; i++) {
			ct.setType(type);
			ct.setStage(i+1+"");
			CourseType stage = courseTypeService.getByTypeAndStage(ct);
			
			CourseType ct2 = new CourseType();
			ct2.setId(stage.getId());
			ct2.setParentIds(stage.getParentIds());
			courseModel.setCourseType(ct2);
			List<Lesson> lessons = new ArrayList<Lesson>();
			List<Course> list = courseService.findList(courseModel);
			for (Course course : list) {
				Course c = new Course();
				c.setId(course.getId());
				lessons.addAll(lessonDao.findList(new Lesson(c)));
			}
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("stageName",stage.getName());
			map.put("stageLessons",lessons);
			
			ajaxlist.add(map);
		}
		ajaxJson.put("dir", ajaxlist);
	    return ajaxJson;
	}
	
	/**
	 * 获取标准课程所有章节目录
	 * @param type
	 * @param stage
	 * @param response
	 * @return
	 * @author jiangjl
	 */
	@ResponseBody
	@RequestMapping(value = "listStandardDir")
	public AjaxJson listStandardDir(String token,String type,String lessonId,HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		UserApi userApi=(UserApi)CacheUtils.get(CacheUtils.USER_API_CACHE, token);
		AjaxJson ajaxJson=new AjaxJson();
		List<List<Map<String,Object>>> ajaxlist = new ArrayList<List<Map<String,Object>>>();
		//查询标准课程所有章节学习信息
		List<Map<String,Object>> allLessons = lessonDao.listStandardDir(Global.STANDARD,userApi.getId());
		//查询每个阶段章节数量
		List<Integer> stageLessonsNum = lessonDao.getStageLessonsNumByType(Global.STANDARD);
		for (int i = 0; i < allLessons.size(); i++) {
			Map<String,Object> map = allLessons.get(i);
			map.put("index",i+1);
			if(map.get("lessonId").equals(lessonId)){
				map.put("active", true);
				map.put("label", "学习中");
				ajaxJson.put("currentIndex", i+1);
			}else{
				if("1".equals(map.get("isStudy"))){
					if(map.get("lessonSort").equals(map.get("lessonNum"))){
						if((Long)(map.get("questionsNum")) > 0){
							if(StringUtils.isNotBlank((String)(map.get("examineId")))){
								map.put("label", "已学完");
								map.put("canClick", true);
							}else{
								map.put("label", "未学习");
							}
						}else{
							map.put("label", "已学完");
							map.put("canClick", true);
						}
					}else{
						map.put("label", "已学完");
						map.put("canClick", true);
					}
				}else{
					map.put("label", "未学习");
				}
			}
		}
		int num = 0;
		for (Integer count : stageLessonsNum) {
			List<Map<String,Object>> stageList = new ArrayList<Map<String,Object>>();
			stageList.addAll(allLessons.subList(num, num + count));
			num += count;
			ajaxlist.add(stageList);
		}
		ajaxJson.put("dir", ajaxlist);
	    return ajaxJson;
	}
	
	/**
	 * 按类型获取所有章节目录
	 * @param type
	 * @param stage
	 * @param response
	 * @return
	 * @author jiangjl
	 */
	@ResponseBody
	@RequestMapping(value = "getAllLessonDir")
	public AjaxJson getAllLessonDir(String token,String type,String lessonId,HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		UserApi userApi=(UserApi)CacheUtils.get(CacheUtils.USER_API_CACHE, token);
		AjaxJson ajaxJson=new AjaxJson();
		Course courseModel = new Course();
		//查询共有几个阶段
		Integer stagesCount = courseTypeService.getStagesCountByType(type);
		List<Map<String,Object>> ajaxlist = new ArrayList<Map<String,Object>>();
		
		//查询某阶段分类
		CourseType ct = new CourseType();
		int index = 1;
		for (int i = 0; i < stagesCount; i++) {
			ct.setType(type);
			ct.setStage(i+1+"");
			CourseType stage = courseTypeService.getByTypeAndStage(ct);
			
			CourseType ct2 = new CourseType();
			ct2.setId(stage.getId());
			ct2.setParentIds(stage.getParentIds());
			courseModel.setCourseType(ct2);
			List<Course> list = courseService.findList(courseModel);
			//List<Lesson> lessons = new ArrayList<Lesson>();
			List<Map<String,Object>> lessonInfoList = new ArrayList<Map<String,Object>>();
			for (Course course : list) {
				Course c = new Course();
				c.setId(course.getId());
				//lessons.addAll(lessonDao.findList(new Lesson(c)));
				List<Lesson> lessons = lessonDao.findList(new Lesson(c));
				for (int j=0; j<lessons.size(); j++) {
					Lesson lesson = lessons.get(j);
					Map<String,Object> lessonInfoMap = new HashMap<String,Object>();
					lessonInfoMap.put("index", index);
					lessonInfoMap.put("lessonName", lesson.getLessonName());
					lessonInfoMap.put("lessonSort", lesson.getSort());
					lessonInfoMap.put("courseSort", course.getSort());
					lessonInfoMap.put("lessonId", lesson.getId());
					lessonInfoMap.put("courseId", course.getId());
					lessonInfoMap.put("videoDuration", lesson.getVideo().getVideoDuration());
					//标准课程显示标签：未学习，已学习
					if(lessonId.equals(lesson.getId())){
						lessonInfoMap.put("label", "学习中");
						lessonInfoMap.put("active", true);
						ajaxJson.put("currentIndex", index);
					}else{
						//查询是否有学习记录
						Trainstudy trainstudy = new Trainstudy();
						trainstudy.setLessonid(lesson.getId());
						trainstudy.setUserid(userApi.getId());
						List<Trainstudy> studyed = trainstudyService.findListBy(trainstudy);
						if(studyed==null || studyed.isEmpty()){
							lessonInfoMap.put("label", "未学习");
						}else{
							if(!"1".equals(studyed.get(0).getIsstudy())){
								lessonInfoMap.put("label", "未学习");
								lessonInfoMap.put("progress", studyed.get(0).getStudyprogress());
							}else{
								if(j==(lessons.size()-1)){
									Integer num = examQuestionsService.getQuestionsNumOfCourse(course.getId());
									if(num == 0){
										lessonInfoMap.put("label", "已学完");
										lessonInfoMap.put("canClick", true);
									}else{
										Examine exam = new Examine();
										exam.setCourseId(course.getId());
										exam.setUserId(userApi.getId());
										List<Examine> examine = examineService.findList(exam);
										if(examine==null || examine.isEmpty()){
											lessonInfoMap.put("label", "未学习");
										}else{
											lessonInfoMap.put("label", "已学完");
											lessonInfoMap.put("canClick", true);
										}
									}
								}else{
									lessonInfoMap.put("label", "已学完");
									lessonInfoMap.put("canClick", true);
								}
								
							}
						}
					}
					lessonInfoList.add(lessonInfoMap);
					index++;
				}
			}
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("stageName",stage.getName());
			map.put("stageLessons",lessonInfoList);
			
			ajaxlist.add(map);
		}
		ajaxJson.put("dir", ajaxlist);
	    return ajaxJson;
	}
	
	/**
	 * 获取扩展课程某课程下的章节目录
	 * @param token
	 * @param type
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getAdvancedLessonDir")
	public AjaxJson getAdvancedLessonDir(String courseId,String lessonId,String token,HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		UserApi userApi=(UserApi)CacheUtils.get(CacheUtils.USER_API_CACHE, token);
		AjaxJson ajaxJson = new AjaxJson();
		Course course = courseService.get(courseId);
		List<Lesson> lessons = lessonDao.findList(new Lesson(new Course(courseId)));
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		for (Lesson lesson : lessons) {
			Map<String,Object> map = new HashMap<String,Object>();
			if(lesson.getId().equals(lessonId)){
				map.put("active", true);
			}
			map.put("lessonName", lesson.getLessonName());
			map.put("lessonSort", lesson.getSort());
			map.put("courseSort", course.getSort());
			map.put("lessonId", lesson.getId());
			map.put("courseId", course.getId());
			map.put("videoDuration", lesson.getVideo().getVideoDuration());
			Trainstudy trainstudy = new Trainstudy();
			trainstudy.setLessonid(lesson.getId());
			trainstudy.setUserid(userApi.getId());
			List<Trainstudy> studyed = trainstudyService.findListBy(trainstudy);
			if(studyed!=null && !studyed.isEmpty()){
				map.put("progress", studyed.get(0).getStudyprogress());
			}
			result.add(map);
		}
		ajaxJson.put("dir", result);
		return ajaxJson;
	}
	
	
	/**
	 * 获取标准课程路径页信息
	 * @param token
	 * @param type
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getStandardPathInfo")
	public AjaxJson getStandardPathInfo(String token,HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		UserApi userApi=(UserApi)CacheUtils.get(CacheUtils.USER_API_CACHE, token);
		AjaxJson ajaxJson = new AjaxJson();
		Course courseModel = new Course();
		//查询共有几个阶段
		Integer stagesCount = courseTypeService.getStagesCountByType("1");
		List<Map<String,Object>> ajaxlist = new ArrayList<Map<String,Object>>();
		
		//查询某阶段
		CourseType ct = new CourseType();
		int index = 0;
		int maxPath = 0;
		int totalPath = lessonDao.getLessonCountByType("1");
		boolean breakOut = false;
		for (int i = 0; i < stagesCount; i++) {
			ct.setType("1");
			ct.setStage(i+1+"");
			CourseType stage = courseTypeService.getByTypeAndStage(ct);
			
			CourseType ct2 = new CourseType();
			ct2.setId(stage.getId());
			ct2.setParentIds(stage.getParentIds());
			courseModel.setCourseType(ct2);
			List<Course> list = courseService.findList(courseModel);
			List<Map<String,Object>> lessonInfoList = new ArrayList<Map<String,Object>>();
			int stageCount = 0;
			for (Course course : list) {
				stageCount += course.getLessonNum();
			}
			for (Course course : list) {
				if(breakOut){
					break;
				}
				List<Lesson> lessons = lessonDao.findList(new Lesson(course));
				for (int j=0; j<lessons.size(); j++) {
					index ++;
					Lesson lesson = lessons.get(j);
					List<Object> isStudy = new ArrayList<Object>();
					Map<String,Object> map = new HashMap<String, Object>();
					if(j==lessons.size()-1){
						//查询奖励
						List<CourseRewardSet> courseRewardList = courseRewardSetService.findListByCourse(course.getId());
						List<Map<String,Object>> rewards = new ArrayList<Map<String,Object>>();
						for (CourseRewardSet courseRewardSet : courseRewardList) {
							Map<String,Object> rewardMap = new HashMap<String,Object>();
							Reward reward = rewardService.get(courseRewardSet.getRewardId());
							if(reward!=null){
								rewardMap.put("name", reward.getRewardName());
								rewardMap.put("image", reward.getRewardImg());
								rewards.add(rewardMap);
							}
						}
						map.put("rewards", rewards);
						isStudy = isStudyComplete(true, course, lesson, userApi);
					}else{
						isStudy = isStudyComplete(false, course, lesson, userApi);
					}
					map.put("index", index);
					map.put("lessonName", lesson.getLessonName());
					map.put("lessonSort", lesson.getSort());
					map.put("courseSort", course.getSort());
					map.put("lessonId", lesson.getId());
					map.put("courseId", course.getId());
					map.put("playNum", lesson.getPlayNum());
					map.put("giveLikeNum", lesson.getGivelikeNum());
					//map.put("courseBrief", course.getCourseBrief());
					map.put("studyPlace", isStudy.get(1));
					//lessonInfoList.add(map);
					ajaxlist.add(map);
					if((Integer)isStudy.get(0) == 0){
						breakOut = true;
						break;
					}else{
						continue;
					}
				}
				
			}
			maxPath += stageCount;
//			Map<String,Object> map = new HashMap<String,Object>();
//			map.put("stageCount",stageCount);
//			if(lessonInfoList.isEmpty()){
//				map.put("stageLessons",null);
//			}else{
//				map.put("stageLessons",lessonInfoList);
//				ajaxJson.put("maxPath", maxPath);
//				ajaxJson.put("index", index);
//			}
			
			//ajaxlist.add(map);
			if(breakOut){
				break;
			}
		}
		ajaxJson.put("totalPath", totalPath);
		ajaxJson.put("maxPath", maxPath);
		ajaxJson.put("index", index);
		ajaxJson.put("path", ajaxlist);
		return ajaxJson;
	}
	
	/**
	 * 根据授课对象获取课程（主播课程除外）
	 * @param token
	 * @param start 分页查询 起始记录index
	 * @param num   查询记录个数
	 * @param role  talent:星探     agent:经纪人
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "listAllCoursesByRole")
	public AjaxJson listAllCoursesByRole(String token,String role,int start,int num,HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		AjaxJson ajaxJson = new AjaxJson();
		//根据类型查询课程总数
		Integer total = courseService.getCoursesCountByRole(role);
		//查询课程信息
		List<Course> courses = courseService.listCoursesByRole(role,start,num);
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		for (Course course : courses) {
			Map<String,Object> map = new HashMap<String,Object>();
			Lesson le = new Lesson();
			le.setCourse(course);
			le.setSort(1);
			Lesson lesson = lessonDao.getByCourseAndSort(le);
			map.put("courseName", course.getCourseName());
			map.put("courseBrief", Encodes.unescapeHtml(course.getCourseBrief()));
			map.put("courseId", course.getId());
			map.put("videoImage", lesson.getVideo().getVideoImage());
			map.put("lessonId", lesson.getId());
			result.add(map);
		}
		ajaxJson.setSuccess(true);
		ajaxJson.put("course", result);
		ajaxJson.put("total", total);
		return ajaxJson;
	}
	
	/**
	 * 根据courseId和lessonId获取课程信息
	 * @param token
	 * @param courseId 课程id
	 * @param lessonId 章节id
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getCourseById")
	public AjaxJson getCourseById(String token,String courseId,String lessonId,HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		AjaxJson ajaxJson = new AjaxJson();
		Course course = courseService.getById(courseId);
		Lesson lesson = lessonDao.get(lessonId);
		
		ajaxJson.put("course", course);
		ajaxJson.put("lesson", lesson);
		return ajaxJson;
	}
	
}
