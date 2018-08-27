/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.course.entity;

import javax.validation.constraints.NotNull;
import com.jeeplus.modules.course.entity.CourseType;
import com.jeeplus.modules.course.entity.Teacher;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 课程Entity
 * @author jiangjl
 * @version 2018-03-12
 */
public class Course extends DataEntity<Course> {
	
	private static final long serialVersionUID = 1L;
	private String courseName;		// 课程名
	private Integer lessonNum;		// 课程章节数
	private CourseType courseType;		// 课程分类
	private String courseBrief;		// 课程简介
	private String details;		// 课程详情
	private Teacher teacher;		// 讲师
	private String difficultyLevel;		// 课程难度
	private Integer sort;           //排序
	private String roles;           //授课对象  星探：talent 经纪人：agent (标准课程和延展课程默认包括 主播)
	private List<Lesson> lessonList = Lists.newArrayList();		// 子表列表
	
	public Course() {
		super();
	}

	public Course(String id){
		super(id);
	}

	@ExcelField(title="课程名", align=2, sort=5)
	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	
	@NotNull(message="课程章节数不能为空")
	@ExcelField(title="课程章节数", align=2, sort=6)
	public Integer getLessonNum() {
		return lessonNum;
	}

	public void setLessonNum(Integer lessonNum) {
		this.lessonNum = lessonNum;
	}
	
	@NotNull(message="课程分类不能为空")
	@ExcelField(title="课程分类", align=2, sort=7)
	public CourseType getCourseType() {
		return courseType;
	}

	public void setCourseType(CourseType courseType) {
		this.courseType = courseType;
	}
	
	@ExcelField(title="课程简介", align=2, sort=8)
	public String getCourseBrief() {
		return courseBrief;
	}

	public void setCourseBrief(String courseBrief) {
		this.courseBrief = courseBrief;
	}
	
	@NotNull(message="讲师不能为空")
	@ExcelField(title="讲师", align=2, sort=10)
	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
	
	@ExcelField(title="课程难度", dictType="difficulty_level", align=2, sort=11)
	public String getDifficultyLevel() {
		return difficultyLevel;
	}

	public void setDifficultyLevel(String difficultyLevel) {
		this.difficultyLevel = difficultyLevel;
	}
	
	public List<Lesson> getLessonList() {
		return lessonList;
	}

	public void setLessonList(List<Lesson> lessonList) {
		this.lessonList = lessonList;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}
	
	@JsonIgnore
	public String[] getRoleList() {
		/*List<String> roleList = Lists.newArrayList();
		String[] array = roles.split(",");
		for (int i = 0; i < array.length; i++) {
			roleList.add(array[i]);
		}
		return roleList;*/
		if(StringUtils.isNotBlank(roles)){
			return roles.split(",");
		}else{
			return null;
		}
	}

	public void setRoleList(String[] roles) {
		this.roles = "";
		for (int i =0; i < roles.length; i++) {
			if(i==roles.length-1){
				this.roles += roles[i];
			}else{
				this.roles += roles[i] +",";
			}
		}
	}
}