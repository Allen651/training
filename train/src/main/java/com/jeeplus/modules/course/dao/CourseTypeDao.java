/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.course.dao;

import com.jeeplus.common.persistence.TreeDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.course.entity.CourseType;

/**
 * 课程分类DAO接口
 * @author jiangjl
 * @version 2018-03-12
 */
@MyBatisDao
public interface CourseTypeDao extends TreeDao<CourseType> {

	CourseType getByTypeAndStage(CourseType courseType);

	Integer getStagesCountByType(String type);
	
}