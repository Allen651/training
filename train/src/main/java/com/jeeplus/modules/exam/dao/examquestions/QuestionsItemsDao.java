/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.exam.dao.examquestions;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.exam.entity.examquestions.QuestionsItems;

/**
 * 试题管理DAO接口
 * @author zhaol
 * @version 2018-03-14
 */
@MyBatisDao
public interface QuestionsItemsDao extends CrudDao<QuestionsItems> {

	
}