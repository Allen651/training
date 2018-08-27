/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.course.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.course.entity.Course;
import com.jeeplus.modules.api.model.CourseModel;
import com.jeeplus.modules.course.dao.CourseDao;

import com.jeeplus.modules.course.entity.Teacher;
import com.jeeplus.modules.exam.entity.examresult.Examine;
import com.jeeplus.modules.course.entity.Lesson;
import com.jeeplus.modules.course.dao.LessonDao;

/**
 * 课程Service
 * @author jiangjl
 * @version 2018-03-12
 */
@Service
@Transactional(readOnly = true)
public class CourseService extends CrudService<CourseDao, Course> {

	@Autowired
	private LessonDao lessonDao;
	
	@Autowired
	private CourseDao courseDao;
	
	public Course get(String id) {
		Course course = super.get(id);
		course.setLessonList(lessonDao.findList(new Lesson(course)));
		return course;
	}
	
	public List<Course> findList(Course course) {
		return super.findList(course);
	}
	
	public Page<Course> findPage(Page<Course> page, Course course) {
		return super.findPage(page, course);
	}
	
	@Transactional(readOnly = false)
	public void save(Course course) {
		super.save(course);
		for (Lesson lesson : course.getLessonList()){
			if (lesson.getId() == null){
				continue;
			}
			if (Lesson.DEL_FLAG_NORMAL.equals(lesson.getDelFlag())){
				if (StringUtils.isBlank(lesson.getId())){
					lesson.setCourse(course);
					lesson.setPlayNum(0);//播放次数默认0
					lesson.setGivelikeNum(0);//点赞次数默认0
					lesson.preInsert();
					lessonDao.insert(lesson);
				}else{
					lesson.preUpdate();
					lessonDao.update(lesson);
				}
			}else{
				lessonDao.delete(lesson);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(Course course) {
		super.delete(course);
		lessonDao.delete(new Lesson(course));
	}
	
	public Page<Teacher> findPageByteacher(Page<Teacher> page, Teacher teacher) {
		teacher.setPage(page);
		page.setList(dao.findListByteacher(teacher));
		return page;
	}
	
	//查询每人的课程列表
	public List<CourseModel> findCourseList(String userId) {
		return courseDao.findCourseList(userId);
	}
	
	//查询课程的试题个数
	public Integer findItemNum(String courseId){
		return courseDao.findItemNum(courseId);
	}
	/**
	 * 根据type(1 标准课程  2 扩展课程)获取课程总数
	 * @param string
	 * @return
	 */
	public Integer getCourseCountByType(String type) {
		return courseDao.getCourseCountByType(type);
	}

	//根据type查最后一门课程
	public Course getLastCourse(String type){
		return courseDao.getLastCourse(type);
	}
	
	//根据授课对象分页查询课程
	public List<Course> listCoursesByRole(String role, int start, int num) {
		List<Course> courses = courseDao.listCoursesByRole(role, start, num);
		return courses;
	}
	//根据授课对象获取课程总数
	public Integer getCoursesCountByRole(String role) {
		return courseDao.getCoursesCountByRole(role);
	}

	public Course getById(String courseId) {
		return super.get(courseId);
	}
	
}