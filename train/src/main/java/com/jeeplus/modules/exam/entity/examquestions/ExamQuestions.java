/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.exam.entity.examquestions;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Max;
import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.course.entity.Course;
import com.jeeplus.modules.course.entity.Lesson;

/**
 * 试题管理Entity
 * @author zhaol
 * @version 2018-03-14
 */
public class ExamQuestions extends DataEntity<ExamQuestions> {
	
	private static final long serialVersionUID = 1L;
	private String title;		// 试题标题
	private Double point;		// 试题分数
	private String type;		// 试题类型（单选|多选）
	private Integer sort;		// 排序
	private String courseId;    // 课程id
	private Course course;		// 所属课程
	private List<QuestionsItems> questionsItemsList = Lists.newArrayList();		// 子表列表
	
	public ExamQuestions() {
		super();
	}

	public ExamQuestions(String id){
		super(id);
	}

	@ExcelField(title="试题标题", align=2, sort=7)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@NotNull(message="试题分数不能为空")
	@ExcelField(title="试题分数", align=2, sort=8)
	public Double getPoint() {
		return point;
	}

	public void setPoint(Double point) {
		this.point = point;
	}
	
	@ExcelField(title="试题类型（单选|多选）", dictType="testType", align=2, sort=9)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@NotNull(message="排序不能为空")
	@ExcelField(title="排序", align=2, sort=10)
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public List<QuestionsItems> getQuestionsItemsList() {
		return questionsItemsList;
	}

	public void setQuestionsItemsList(List<QuestionsItems> questionsItemsList) {
		this.questionsItemsList = questionsItemsList;
	}

	@NotNull(message="所属课程不能为空")
	@ExcelField(title="所属课程", align=2, sort=11)
	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}
	
	
}