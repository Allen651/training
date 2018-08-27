/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.course.dao;

import com.jeeplus.modules.course.entity.Lesson;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.course.entity.Video;

/**
 * 视频管理DAO接口
 * @author jiangjl
 * @version 2018-03-12
 */
@MyBatisDao
public interface VideoDao extends CrudDao<Video> {

	public List<Lesson> findListBylesson(Lesson lesson);
	
	//通过lessonId查找视频
	public Video findVideoByLesson(@Param("lessonId")String lessonId);
	
}