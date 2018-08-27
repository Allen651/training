/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.mystudy.dao;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.mystudy.entity.Applicant;
import com.jeeplus.modules.mystudy.entity.Trainstudy;

/**
 * 申请重学记录DAO接口
 * @author yangyy
 * @version 2018-04-02
 */
@MyBatisDao
public interface ApplicantDao extends CrudDao<Applicant> {

	Applicant findUserLesson(String userId, String lessonName);
	
	List<Applicant> findApplicaList(Applicant applicant);

	
}