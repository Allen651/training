/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.exam.dao.examresult;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.api.model.AnchorModel;
import com.jeeplus.modules.api.model.ExamineModel;
import com.jeeplus.modules.exam.entity.examresult.Examine;

/**
 * 考试管理DAO接口
 * @author zhaol
 * @version 2018-03-14
 */
@MyBatisDao
public interface ExamineDao extends CrudDao<Examine> {

	//查询课程考试的答题正确数量
    public Integer findRightNum(@Param("courseId")String courseId,@Param("userId")String userId);
	
    //获取成绩排行榜
    public List<AnchorModel> getResultRank(@Param("company")String company);

	public List<Examine> findExamRecord(String userid);

	public Double findExamUser(String userid); //每个人所考试的成绩
	
	public List<ExamineModel> findExamList(ExamineModel examineModel);

	public Examine getExamByUserId(String userId,String courseId);
    
}