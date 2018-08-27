/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.course.dao;

import com.jeeplus.modules.course.entity.Course;
import java.util.List;
import java.util.Map;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.course.entity.Lesson;

/**
 * 课程DAO接口
 * @author jiangjl
 * @version 2018-03-12
 */
@MyBatisDao
public interface LessonDao extends CrudDao<Lesson> {

	public List<Course> findListBycourse(Course course);
	
	public Lesson getByCourseAndSort(Lesson lesson);

	public Integer getMaxSort(Lesson le);

	public Integer getLessonCountByType(String type);

	public Integer getLessonCountByTypeId(String id);

	public List<Lesson> findcourse(String courseid);

	public void updatelesson(Integer givelikeNum, String lessonid);

	public void updateplaynum(int playnum, String lessonid);

	//获取标准课程目录
	public List<Map<String, Object>> listStandardDir(String standard, String id);

	//根据类型获取每个阶段章节数量
	public List<Integer> getStageLessonsNumByType(String standard);

}