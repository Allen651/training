/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.exam.service.examresult;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.exam.entity.examresult.Examine;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.talent.entity.TalentInfo;
import com.jeeplus.modules.api.model.AnchorModel;
import com.jeeplus.modules.api.model.AnchorModelAdmin;
import com.jeeplus.modules.api.model.ExamineModel;
import com.jeeplus.modules.exam.dao.examresult.ExamineDao;

/**
 * 考试管理Service
 * @author zhaol
 * @version 2018-03-14
 */
@Service
@Transactional(readOnly = true)
public class ExamineService extends CrudService<ExamineDao, Examine> {

	@Autowired
	ExamineDao examinDao;
	
	public Examine get(String id) {
		return super.get(id);
	}
	
	public List<Examine> findList(Examine examine) {
		return super.findList(examine);
	}
	
	public Page<Examine> findPage(Page<Examine> page, Examine examine) {
		return super.findPage(page, examine);
	}
	
	//查询考试列表
	public Page<ExamineModel> findExamList(Page<ExamineModel> page, ExamineModel examineModel) {
		User user = examineModel.getExamine().getCurrentUser();
		String sql = "";
		if (!user.isAdmin()){
			//登录用户是经纪人
			if(Global.AGENT.equals(user.getUserType())){
				sql = "z.agent = "+"'"+user.getId()+"'";
			}else{
				//其它用户根据部门和权限范围进行数据过滤
				examineModel.getExamine().getSqlMap().put("dsf", dataScopeFilter(examineModel.getExamine().getCurrentUser(), "office", ""));
				examineModel.setPage(page);
				page.setList(examinDao.findExamList(examineModel));
				return page;
			}
		}
		if ( !sql.equals("")){
			sql = " AND (" + sql + ")";
		}
		examineModel.getExamine().getSqlMap().put("dsf", sql);
		// 设置分页参数
		examineModel.setPage(page);
		// 执行分页查询
		page.setList(examinDao.findExamList(examineModel));
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(Examine examine) {
		super.save(examine);
	}
	
	@Transactional(readOnly = false)
	public void delete(Examine examine) {
		super.delete(examine);
	}
	
	//查询课程考试的答题正确数量
    public Integer findRightNum(String courseId,String userId){
    	return examinDao.findRightNum(courseId,userId);
    }
	
    //获取成绩排行榜
    public List<AnchorModel> getResultRank(String company){
    	return examinDao.getResultRank(company);
    }

	public List<Examine> findExamRecord(String userid) {
		return examinDao.findExamRecord(userid);
	}

	public Double findExamUser(String userid) {
		Double findExamUser = examinDao.findExamUser(userid);
		if (findExamUser == null) {
			return 0.0;
		}
		return findExamUser;
	}

	public Examine getExamByUserId(String userId, String courseId) {
		// TODO Auto-generated method stub
		return examinDao.getExamByUserId(userId,courseId);
	}
}