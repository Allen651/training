/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.course.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.modules.course.entity.Lesson;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.course.entity.Video;
import com.jeeplus.modules.course.dao.LessonDao;
import com.jeeplus.modules.course.dao.VideoDao;

/**
 * 视频管理Service
 * @author jiangjl
 * @version 2018-03-12
 */
@Service
@Transactional(readOnly = true)
public class VideoService extends CrudService<VideoDao, Video> {

	@Autowired
	private VideoDao videoDao;
	
	public Video get(String id) {
		return super.get(id);
	}
	
	public List<Video> findList(Video video) {
		return super.findList(video);
	}
	
	public Page<Video> findPage(Page<Video> page, Video video) {
		return super.findPage(page, video);
	}
	
	@Transactional(readOnly = false)
	public void save(Video video) {
		super.save(video);
	}
	
	@Transactional(readOnly = false)
	public void delete(Video video) {
		super.delete(video);
	}
	
	public Page<Lesson> findPageBylesson(Page<Lesson> page, Lesson lesson) {
		lesson.setPage(page);
		page.setList(dao.findListBylesson(lesson));
		return page;
	}
	
	//通过lessonId查找视频
	public Video findVideoByLesson(String lessonId){
		return videoDao.findVideoByLesson(lessonId);
	}
	
	
}