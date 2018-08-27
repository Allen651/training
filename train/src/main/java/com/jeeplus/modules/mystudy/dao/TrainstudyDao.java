/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.mystudy.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.api.model.AnchorModel;
import com.jeeplus.modules.api.model.CourseModel;
import com.jeeplus.modules.course.entity.Lesson;
import com.jeeplus.modules.mystudy.entity.Trainstudy;

/**
 * 我的学习DAO接口
 * @author yangyy
 * @version 2018-03-22
 */
@MyBatisDao
public interface TrainstudyDao extends CrudDao<Trainstudy> {

	Trainstudy findUserLesson(String userId, String lessonid);

	List<Trainstudy> findStudyList(String userid);
	
	List<Trainstudy> findStuList(Trainstudy trainstudy);

	List<Trainstudy> findStudyingLessons(String type, String id);

	Integer getStudyedCourseNum(String type, String id);
	
	//查询课程最后章节是否学完
	public Trainstudy findCourseProgress(@Param("lessonId")String lessonId,@Param("userId")String userId);

	List<Trainstudy> findListBy(Trainstudy trainstudy);
	
	//查询课程的最后一个章节
	public Lesson findLastLesson(@Param("courseId")String courseId);

	double findStudying(String type, String userid);

	void updateSyudyPlace(Integer studyLimitNum, String userid, String lessonid);

	List<Trainstudy> getProgress(@Param("typeId")String typeId,@Param("id") String id);
	
	//获取学习排行榜
    public List<AnchorModel> getStudyRank(@Param("company")String company);

	public void setIsLing(String id);

	void updatele(int givelikeStaus, String lessonid, String userId);

	int findStudyNumByLesson(String id);

	Trainstudy getMaxCourseId(@Param("userId")String userId,@Param("courseId")String courseId);
}