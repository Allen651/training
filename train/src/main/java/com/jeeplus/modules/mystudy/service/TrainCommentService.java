/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.mystudy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.mystudy.entity.TrainComment;
import com.jeeplus.modules.mystudy.dao.TrainCommentDao;

/**
 * 评论Service
 * @author yangyy
 * @version 2018-04-04
 */
@Service
@Transactional(readOnly = true)
public class TrainCommentService extends CrudService<TrainCommentDao, TrainComment> {
	@Autowired
	private TrainCommentDao trainCommentDao;

	public TrainComment get(String id) {
		return super.get(id);
	}
	
	public List<TrainComment> findList(TrainComment trainComment) {
		return super.findList(trainComment);
	}
	
	public Page<TrainComment> findPage(Page<TrainComment> page, TrainComment trainComment) {
		return super.findPage(page, trainComment);
	}
	
	@Transactional(readOnly = false)
	public void save(TrainComment trainComment) {
		super.save(trainComment);
	}
	
	@Transactional(readOnly = false)
	public void delete(TrainComment trainComment) {
		super.delete(trainComment);
	}

	public List<TrainComment> findComment(String lessonid, int type) {
		return trainCommentDao.findComment(lessonid,type);
	}

	public List<TrainComment> findmyComMentList(String userId, int givelikeStaus) {
		return trainCommentDao.findmyComMentList(userId,givelikeStaus);
	}

	public List<TrainComment> findComme(String lessonid) {
		return trainCommentDao.findComme(lessonid);
	}

	public List<TrainComment> findChirenComment(String parentid) {
		return trainCommentDao.findChirenComment(parentid);
	}

	public double commentsum(String id) {
		return trainCommentDao.commentsum(id);
	}
	@Transactional(readOnly = false)
	public void insertZan(TrainComment trainComment) {
		trainCommentDao.insertZan(trainComment);
	}
	@Transactional(readOnly = false)
	public void updateZan(TrainComment trainComment) {
		trainCommentDao.updateZan(trainComment);
	}

	public int findCommentNumByLesson(String id) {
		return trainCommentDao.findCommentNumByLesson(id);
	}

	public List<TrainComment> findTalentComment(String lessonid, int type) {
		return trainCommentDao.findTalentComment(lessonid,type);
	}

	public List<TrainComment> findTalentChirenComment(String parentid) {
		return trainCommentDao.findTalentChirenComment(parentid);
	}
	
}