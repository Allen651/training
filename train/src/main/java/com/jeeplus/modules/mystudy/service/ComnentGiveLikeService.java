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
import com.jeeplus.modules.mystudy.entity.ComnentGiveLike;
import com.jeeplus.modules.mystudy.dao.ComnentGiveLikeDao;

/**
 * 评论的点赞功能Service
 * @author yangyy
 * @version 2018-04-11
 */
@Service
@Transactional(readOnly = true)
public class ComnentGiveLikeService extends CrudService<ComnentGiveLikeDao, ComnentGiveLike> {
	
	@Autowired
     private ComnentGiveLikeDao comnentGiveLikeDao;

	public ComnentGiveLike get(String id) {
		return super.get(id);
	}
	
	public List<ComnentGiveLike> findList(ComnentGiveLike comnentGiveLike) {
		return super.findList(comnentGiveLike);
	}
	
	public Page<ComnentGiveLike> findPage(Page<ComnentGiveLike> page, ComnentGiveLike comnentGiveLike) {
		return super.findPage(page, comnentGiveLike);
	}
	
	@Transactional(readOnly = false)
	public void save(ComnentGiveLike comnentGiveLike) {
		super.save(comnentGiveLike);
	}
	
	@Transactional(readOnly = false)
	public void delete(ComnentGiveLike comnentGiveLike) {
		super.delete(comnentGiveLike);
	}

	@Transactional(readOnly = false)
	public void deletezan(String beidianren, String dianzanren) {
		comnentGiveLikeDao.deletezan(beidianren,dianzanren);
	}

	public List<ComnentGiveLike> finddianzan(String userId, String commentid) {
		return comnentGiveLikeDao.finddianzan(userId,commentid);
	}
	
	
	
	
}