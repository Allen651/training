/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.mystudy.dao;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.mystudy.entity.TrainComment;

/**
 * 评论DAO接口
 * @author yangyy
 * @version 2018-04-04
 */
@MyBatisDao
public interface TrainCommentDao extends CrudDao<TrainComment> {
	
	//根据主题 和类型查询评论详情
	List<TrainComment> findComment(String lessonid, int type);

	List<TrainComment> findmyComMentList(String userId, int givelikeStaus);

	List<TrainComment> findComme(String lessonid);

	List<TrainComment> findChirenComment(String parentid);

	double commentsum(String id);
	
	void insertZan(TrainComment trainComment);

	void updateZan(TrainComment trainComment);

	int findCommentNumByLesson(String id);

	List<TrainComment> findTalentComment(String lessonid, int type);

	List<TrainComment> findTalentChirenComment(String parentid);

	
}