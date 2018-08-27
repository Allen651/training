/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.mystudy.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.mystudy.entity.Trainstudy;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.talent.entity.TalentInfo;
import com.jeeplus.modules.api.model.AnchorModel;
import com.jeeplus.modules.api.model.AnchorModelAdmin;
import com.jeeplus.modules.api.model.CourseModel;
import com.jeeplus.modules.course.entity.Lesson;
import com.jeeplus.modules.mystudy.dao.TrainstudyDao;

/**
 * 我的学习Service
 * @author yangyy
 * @version 2018-03-22
 */
@Service
@Transactional(readOnly = true)
public class TrainstudyService extends CrudService<TrainstudyDao, Trainstudy> {
	@Autowired
	private TrainstudyDao trainstudyDao;
	
	public Trainstudy get(String id) {
		return super.get(id);
	}
	
	public List<Trainstudy> findList(Trainstudy trainstudy) {
		return super.findList(trainstudy);
	}
	
	public Page<Trainstudy> findPage(Page<Trainstudy> page, Trainstudy trainstudy) {
		return super.findPage(page, trainstudy);
	}
	
	public Page<Trainstudy> findStuList(Page<Trainstudy> page, Trainstudy trainstudy) {
		User user = trainstudy.getCurrentUser();
		String sql = "";
		if (!user.isAdmin()){
			//登录用户是经纪人
			if(Global.AGENT.equals(user.getUserType())){
				sql = "anchor.agent = "+"'"+user.getId()+"'";
			}else{
				//其它用户根据部门和权限范围进行数据过滤
				trainstudy.getSqlMap().put("dsf", dataScopeFilter(trainstudy.getCurrentUser(), "office", ""));
				trainstudy.setPage(page);
				page.setList(trainstudyDao.findStuList(trainstudy));
				return page;
			}
		}
		if ( !sql.equals("")){
			sql = " AND (" + sql + ")";
		}
		trainstudy.getSqlMap().put("dsf", sql);
		// 设置分页参数
		trainstudy.setPage(page);
		// 执行分页查询
		page.setList(trainstudyDao.findStuList(trainstudy));
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(Trainstudy trainstudy) {
		super.save(trainstudy);
	}
	
	@Transactional(readOnly = false)
	public void delete(Trainstudy trainstudy) {
		super.delete(trainstudy);
	}

	public Trainstudy findUserLesson(String userId, String lessonid) {
		return trainstudyDao.findUserLesson(userId,lessonid);
	}

	public List<Trainstudy> findStudyList(String userid) {
		return trainstudyDao.findStudyList(userid);
	}

	public List<Trainstudy> findStudyingLessons(String type, String id) {
		return trainstudyDao.findStudyingLessons(type,id);
	}

	public Integer getStudyedCourseNum(String type, String id) {
		// TODO Auto-generated method stub
		return trainstudyDao.getStudyedCourseNum(type, id);
	}
	
	//查询课程最后一个章节的学习进度
	public Trainstudy findCourseProgress(String lessonId , String userId){
		return trainstudyDao.findCourseProgress(lessonId,userId);
	}

	public List<Trainstudy> findListBy(Trainstudy trainstudy) {
		return trainstudyDao.findListBy(trainstudy);
	}
	
	public List<Trainstudy> getProgress(String typeId, String id) {
		// TODO Auto-generated method stub
		return trainstudyDao.getProgress(typeId,id);
	}
	
	//查询课程的最后一个章节
	public Lesson findLastLesson(String courseId){
		return trainstudyDao.findLastLesson(courseId);
	}

	public double findStudying(String type, String userid) {
		return trainstudyDao.findStudying(type,userid);
	}

	@Transactional(readOnly = false)
	public void updateSyudyPlace(Integer studyLimitNum, String userid, String lessonid) {
		trainstudyDao.updateSyudyPlace(studyLimitNum,userid,lessonid);
	}

	//获取学习排行榜
    public List<AnchorModel> getStudyRank(String company){
    	return trainstudyDao.getStudyRank(company);
    }

    @Transactional(readOnly = false)
	public void setIsLing(String id) {
		// TODO Auto-generated method stub
		trainstudyDao.setIsLing(id);
	}
	
	@Transactional(readOnly = false)
	public void updatele(int givelikeStaus, String lessonid, String userId) {
		trainstudyDao.updatele(givelikeStaus,lessonid,userId);
	}

	public int findStudyNumByLesson(String id) {
		// TODO Auto-generated method stub
		return trainstudyDao.findStudyNumByLesson(id);
	}

	public Trainstudy getMaxCourseId(String userId,String courseId) {
		return trainstudyDao.getMaxCourseId(userId,courseId);
	}
}