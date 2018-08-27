/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.mystudy.entity;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 评论Entity
 * @author yangyy
 * @version 2018-04-04
 */
public class TrainComment extends DataEntity<TrainComment> {
	
	private static final long serialVersionUID = 1L;
	private String topicid;		// 要评论的主题id
	private String userid;		// 评论人id
	private String content;		// 评论内容
	private String relatid;		// 回复评论id
	private String parentid;		// 直接评论id
	private String type;		// 评论类型(0:直接评论|1:回复评论)
	private Integer givelikeNum;//点赞次数
	private Integer givelikeStaus; // 点赞状态 0:取消赞 1:已点赞
	private String givezan;//点赞者
	
	private double commentsum; // 评论数
	private String nickname; //评论人名次
	private String sex;				//评论人性别
	private String headPicture;	//评论人头像

	
 
	public String getGivezan() {
		return givezan;
	}

	public void setGivezan(String givezan) {
		this.givezan = givezan;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getHeadPicture() {
		return headPicture;
	}

	public void setHeadPicture(String headPicture) {
		this.headPicture = headPicture;
	}

	public double getCommentsum() {
		return commentsum;
	}

	public void setCommentsum(double commentsum) {
		this.commentsum = commentsum;
	}

	public Integer getGivelikeNum() {
		return givelikeNum;
	}

	public void setGivelikeNum(Integer givelikeNum) {
		this.givelikeNum = givelikeNum;
	}

	public Integer getGivelikeStaus() {
		return givelikeStaus;
	}

	public void setGivelikeStaus(Integer givelikeStaus) {
		this.givelikeStaus = givelikeStaus;
	}

	public TrainComment() {
		super();
	}

	public TrainComment(String id){
		super(id);
	}

	@ExcelField(title="要评论的主题id", align=2, sort=7)
	public String getTopicid() {
		return topicid;
	}

	public void setTopicid(String topicid) {
		this.topicid = topicid;
	}
	
	@ExcelField(title="评论人", align=2, sort=8)
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	@ExcelField(title="评论内容", align=2, sort=9)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@ExcelField(title="回复评论id", align=2, sort=10)
	public String getRelatid() {
		return relatid;
	}

	public void setRelatid(String relatid) {
		this.relatid = relatid;
	}
	
	@ExcelField(title="直接评论id", align=2, sort=11)
	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	
	@ExcelField(title="评论类型(0:直接评论|1:回复评论)", align=2, sort=12)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}