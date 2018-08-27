/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.mystudy.entity;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 评论的点赞功能Entity
 * @author yangyy
 * @version 2018-04-11
 */
public class ComnentGiveLike extends DataEntity<ComnentGiveLike> {
	
	private static final long serialVersionUID = 1L;
	private String commentid;		// 某条评论的id
	private String userid;		// 点赞者id
	private String relatid;		// 被点赞者id
	private Integer type;		// 点赞类型(0:未点 1:已点)
	
	public ComnentGiveLike() {
		super();
	}

	public ComnentGiveLike(String id){
		super(id);
	}

	@ExcelField(title="某条评论的id", align=2, sort=7)
	public String getCommentid() {
		return commentid;
	}

	public void setCommentid(String commentid) {
		this.commentid = commentid;
	}
	
	@ExcelField(title="点赞者id", align=2, sort=8)
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	@ExcelField(title="被点赞者id", align=2, sort=9)
	public String getRelatid() {
		return relatid;
	}

	public void setRelatid(String relatid) {
		this.relatid = relatid;
	}
	
	@ExcelField(title="点赞类型(0:未点 1:已点)", align=2, sort=10)
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
}