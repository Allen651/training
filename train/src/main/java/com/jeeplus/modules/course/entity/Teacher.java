/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.course.entity;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 讲师Entity
 * @author jiangjl
 * @version 2018-03-12
 */
public class Teacher extends DataEntity<Teacher> {
	
	private static final long serialVersionUID = 1L;
	private String teacherName;		// 讲师姓名
	private String note;		// 讲师简介
	private String headImage;		// 讲师头像
	private String tags;		// 讲师标签
	
	public Teacher() {
		super();
	}

	public Teacher(String id){
		super(id);
	}

	@ExcelField(title="讲师姓名", align=2, sort=7)
	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	
	@ExcelField(title="讲师简介", align=2, sort=8)
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	@ExcelField(title="讲师头像", align=2, sort=9)
	public String getHeadImage() {
		return headImage;
	}

	public void setHeadImage(String headImage) {
		this.headImage = headImage;
	}
	
	@ExcelField(title="讲师标签", align=2, sort=10)
	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}
	
}