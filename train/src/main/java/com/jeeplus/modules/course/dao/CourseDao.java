/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.course.dao;

import com.jeeplus.modules.course.entity.Teacher;
import com.jeeplus.modules.exam.entity.examresult.Examine;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.api.model.CourseModel;
import com.jeeplus.modules.course.entity.Course;

/**
 * 课程DAO接口
 * @author jiangjl
 * @version 2018-03-12
 */
@MyBatisDao
public interface CourseDao extends CrudDao<Course> {

	public List<Teacher> findListByteacher(Teacher teacher);
	
	//查询每人的课程列表
	public List<CourseModel> findCourseList(@Param("userId")String userId);
	
	//查询课程的试题个数
	public Integer findItemNum(@Param("courseId")String courseId);
	
	//根据type查课程数量
	public Integer getCourseCountByType(String type);
	
	//根据type查最后一门课程
	public Course getLastCourse(@Param("type")String type);

	//根据授课对象分页查询课程
	public List<Course> listCoursesByRole(String role, int start, int num);

	//根据授课对象获取课程总数
	public Integer getCoursesCountByRole(String role);

	
	
}