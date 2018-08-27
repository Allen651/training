/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.course.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.course.entity.Teacher;

/**
 * 讲师DAO接口
 * @author jiangjl
 * @version 2018-03-12
 */
@MyBatisDao
public interface TeacherDao extends CrudDao<Teacher> {

	
}