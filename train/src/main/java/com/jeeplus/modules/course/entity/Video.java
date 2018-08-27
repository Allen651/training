/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.course.entity;

import javax.validation.constraints.NotNull;
import com.jeeplus.modules.course.entity.Lesson;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 视频管理Entity
 * @author jiangjl
 * @version 2018-03-12
 */
public class Video extends DataEntity<Video> {
	
	private static final long serialVersionUID = 1L;
	private String videoName;		// 视频名
	private Integer videoDuration;		// 时长(秒)
	private String videoId;		// 视频托管id
	private String videoImage;		// 封面图片地址
	private Lesson lesson;		// 所属章节
	
	public Video() {
		super();
	}

	public Video(String id){
		super(id);
	}

	@ExcelField(title="视频名", align=2, sort=7)
	public String getVideoName() {
		return videoName;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}
	
	@NotNull(message="时长(秒)不能为空")
	@ExcelField(title="时长(秒)", align=2, sort=8)
	public Integer getVideoDuration() {
		return videoDuration;
	}

	public void setVideoDuration(Integer videoDuration) {
		this.videoDuration = videoDuration;
	}
	
	@ExcelField(title="视频托管id", align=2, sort=9)
	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}
	
	@ExcelField(title="封面图片地址", align=2, sort=10)
	public String getVideoImage() {
		return videoImage;
	}

	public void setVideoImage(String videoImage) {
		this.videoImage = videoImage;
	}
	
	@NotNull(message="所属章节不能为空")
	@ExcelField(title="所属章节", align=2, sort=11)
	public Lesson getLesson() {
		return lesson;
	}

	public void setLesson(Lesson lesson) {
		this.lesson = lesson;
	}
	
}