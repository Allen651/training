/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.exam.entity.examresult;


import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.course.entity.Course;
import com.jeeplus.modules.course.entity.Lesson;

/**
 * 考试管理Entity
 * @author zhaol
 * @version 2018-03-14
 */
public class Examine extends DataEntity<Examine> {
	
	private static final long serialVersionUID = 1L;
	private String userId;		// 考试人id
	private String usedTime;		// 所用时长
	private Double examResult;		// 考试成绩
	private Date examTime;		// 考试时间
	private String courseId;		// 所属课程id
	private String coursename; // 课程名称
	
	public String getCoursename() {
		return coursename;
	}

	public void setCoursename(String coursename) {
		this.coursename = coursename;
	}

	public Examine() {
		super();
	}

	public Examine(String id){
		super(id);
	}

	@ExcelField(title="考试人id", align=2, sort=7)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@ExcelField(title="所用时长", align=2, sort=8)
	public String getUsedTime() {
		return usedTime;
	}

	public void setUsedTime(String usedTime) {
		this.usedTime = usedTime;
	}
	
	@ExcelField(title="考试成绩", align=2, sort=9)
	public Double getExamResult() {
		return examResult;
	}

	public void setExamResult(Double examResult) {
		this.examResult = examResult;
	}
	
	@ExcelField(title="考试时间", align=2, sort=10)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
	public Date getExamTime() {
		return examTime;
	}

	public void setExamTime(Date examTime) {
		this.examTime = examTime;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	
	
	
}