/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.course.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.jeeplus.common.persistence.TreeEntity;

/**
 * 课程分类Entity
 * @author jiangjl
 * @version 2018-03-12
 */
public class CourseType extends TreeEntity<CourseType> {
	
	private static final long serialVersionUID = 1L;
	private CourseType parent;		// 上级分类
	private String parentIds;		// 所有上级分类
	private String name;		// 分类名称
	private Integer sort;		// 排序
	private String type;        //课程类型   1 标准课程   2 延展课程
	private String stage;       //阶段
	

	public CourseType() {
		super();
	}

	public CourseType(String id){
		super(id);
	}

	@JsonBackReference
	@NotNull(message="上级分类不能为空")
	public CourseType getParent() {
		return parent;
	}

	public void setParent(CourseType parent) {
		this.parent = parent;
	}
	
	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@NotNull(message="排序不能为空")
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	public String getParentId() {
		return parent != null && parent.getId() != null ? parent.getId() : "0";
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}
}