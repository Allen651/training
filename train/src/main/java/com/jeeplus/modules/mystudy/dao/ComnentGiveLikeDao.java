/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.mystudy.dao;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.mystudy.entity.ComnentGiveLike;

/**
 * 评论的点赞功能DAO接口
 * @author yangyy
 * @version 2018-04-11
 */
@MyBatisDao
public interface ComnentGiveLikeDao extends CrudDao<ComnentGiveLike> {

	void deletezan(String beidianren, String dianzanren);

	List<ComnentGiveLike> finddianzan(String userId, String commentid);

	
}