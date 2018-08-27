/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.course.entity;

import com.jeeplus.modules.course.entity.Course;
import javax.validation.constraints.NotNull;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 课程Entity
 * @author jiangjl
 * @version 2018-03-12
 */
public class Lesson extends DataEntity<Lesson> {
	
	private static final long serialVersionUID = 1L;
	private String lessonName;		// 章节名
	private Course course;		// 所属课程 父类
	private Integer sort;       // 排序
	private Video video;       // 视频
	private Integer studyLimitNum;  //学习限制次数
	private Integer givelikeNum;//点赞次数
	private Integer playNum; //播放次数
	

	public Integer getPlayNum() {
		return playNum;
	}

	public void setPlayNum(Integer playNum) {
		this.playNum = playNum;
	}

	public Integer getGivelikeNum() {
		return givelikeNum;
	}

	public void setGivelikeNum(Integer givelikeNum) {
		this.givelikeNum = givelikeNum;
	}

	public Lesson() {
		super();
	}

	public Lesson(String id){
		super(id);
	}

	public Lesson(Course course){
		this.course = course;
	}

	@ExcelField(title="章节名", align=2, sort=7)
	public String getLessonName() {
		return lessonName;
	}

	public void setLessonName(String lessonName) {
		this.lessonName = lessonName;
	}
	
	@NotNull(message="所属课程不能为空")
	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Video getVideo() {
		return video;
	}

	public void setVideo(Video video) {
		this.video = video;
	}

	public Integer getStudyLimitNum() {
		return studyLimitNum;
	}

	public void setStudyLimitNum(Integer studyLimitNum) {
		this.studyLimitNum = studyLimitNum;
	}
	
}