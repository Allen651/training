/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.course.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.service.TreeService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.course.entity.CourseType;
import com.jeeplus.modules.course.dao.CourseTypeDao;

/**
 * 课程分类Service
 * @author jiangjl
 * @version 2018-03-12
 */
@Service
@Transactional(readOnly = true)
public class CourseTypeService extends TreeService<CourseTypeDao, CourseType> {
	@Autowired
	private CourseTypeDao courseTypeDao;

	public CourseType get(String id) {
		return super.get(id);
	}
	/**
	 * 查询标准课程或进阶课程 的阶段数量
	 * @return
	 * @author jiangjl
	 */
	public Integer getStagesCountByType(String type) {
		return courseTypeDao.getStagesCountByType(type);
	}
	
	public CourseType getByTypeAndStage(CourseType courseType) {
		return courseTypeDao.getByTypeAndStage(courseType);
	}
	
	public List<CourseType> findList(CourseType courseType) {
		if (StringUtils.isNotBlank(courseType.getParentIds())){
			courseType.setParentIds(","+courseType.getParentIds()+",");
		}
		return super.findList(courseType);
	}
	
	@Transactional(readOnly = false)
	public void save(CourseType courseType) {
		super.save(courseType);
	}
	
	@Transactional(readOnly = false)
	public void delete(CourseType courseType) {
		super.delete(courseType);
	}
	
	
}