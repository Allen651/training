/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.mystudy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.mystudy.entity.Applicant;
import com.jeeplus.modules.mystudy.entity.Trainstudy;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.mystudy.dao.ApplicantDao;

/**
 * 申请重学记录Service
 * @author yangyy
 * @version 2018-04-02
 */
@Service
@Transactional(readOnly = true)
public class ApplicantService extends CrudService<ApplicantDao, Applicant> {
	@Autowired
	private ApplicantDao applicantDao;

	public Applicant get(String id) {
		return super.get(id);
	}
	
	public List<Applicant> findList(Applicant applicant) {
		return super.findList(applicant);
	}
	
	public Page<Applicant> findPage(Page<Applicant> page, Applicant applicant) {
		return super.findPage(page, applicant);
	}
	
	@Transactional(readOnly = false)
	public void save(Applicant applicant) {
		super.save(applicant);
	}
	
	@Transactional(readOnly = false)
	public void delete(Applicant applicant) {
		super.delete(applicant);
	}

	public Applicant findUserLesson(String userId, String lessonName) {
		return applicantDao.findUserLesson(userId,lessonName);
	}
	
	public Page<Applicant> findApplicaList(Page<Applicant> page, Applicant applicant) {
		User user = applicant.getCurrentUser();
		String sql = "";
		if (!user.isAdmin()){
			//登录用户是经纪人
			if(Global.AGENT.equals(user.getUserType())){
				sql = "z.agent = "+"'"+user.getId()+"'";
			}else{
				//其它用户根据部门和权限范围进行数据过滤
				applicant.getSqlMap().put("dsf", dataScopeFilter(applicant.getCurrentUser(), "office", ""));
				applicant.setPage(page);
				page.setList(applicantDao.findApplicaList(applicant));
				return page;
			}
		}
		if ( !sql.equals("")){
			sql = " AND (" + sql + ")";
		}
		applicant.getSqlMap().put("dsf", sql);
		// 设置分页参数
		applicant.setPage(page);
		// 执行分页查询
		page.setList(applicantDao.findApplicaList(applicant));
		return page;
	}
	
	
}