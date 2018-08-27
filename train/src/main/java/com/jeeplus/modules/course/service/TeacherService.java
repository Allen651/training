/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.course.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.course.entity.Teacher;
import com.jeeplus.modules.course.dao.TeacherDao;

/**
 * 讲师Service
 * @author jiangjl
 * @version 2018-03-12
 */
@Service
@Transactional(readOnly = true)
public class TeacherService extends CrudService<TeacherDao, Teacher> {

	public Teacher get(String id) {
		return super.get(id);
	}
	
	public List<Teacher> findList(Teacher teacher) {
		return super.findList(teacher);
	}
	
	public Page<Teacher> findPage(Page<Teacher> page, Teacher teacher) {
		return super.findPage(page, teacher);
	}
	
	@Transactional(readOnly = false)
	public void save(Teacher teacher) {
		super.save(teacher);
	}
	
	@Transactional(readOnly = false)
	public void delete(Teacher teacher) {
		super.delete(teacher);
	}
	
	
	
	
}