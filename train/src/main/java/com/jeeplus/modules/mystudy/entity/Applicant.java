/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.mystudy.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 申请重学记录Entity
 * @author yangyy
 * @version 2018-04-02
 */
public class Applicant extends DataEntity<Applicant> {
	
	private static final long serialVersionUID = 1L;
	private String userid;		// 申请人id
	private String name; //申请人姓名
	private String currentprogress;		// 当前进度
	private String studio;		// 工作室
	private String agent;		// 经纪人
	private Date applicationtime;		// 申请时间
	private String lessonid; //申请人ID
	private String lessonname;		// 申请章节名称
	
	
	public String getLessonid() {
		return lessonid;
	}

	public void setLessonid(String lessonid) {
		this.lessonid = lessonid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Applicant() {
		super();
	}

	public Applicant(String id){
		super(id);
	}

	@ExcelField(title="申请人", align=2, sort=7)
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	@ExcelField(title="申请章节名称", align=2, sort=8)
	public String getLessonname() {
		return lessonname;
	}

	public void setLessonname(String lessonname) {
		this.lessonname = lessonname;
	}
	
	@ExcelField(title="当前进度", align=2, sort=9)
	public String getCurrentprogress() {
		return currentprogress;
	}

	public void setCurrentprogress(String currentprogress) {
		this.currentprogress = currentprogress;
	}
	
	@ExcelField(title="工作室", align=2, sort=10)
	public String getStudio() {
		return studio;
	}

	public void setStudio(String studio) {
		this.studio = studio;
	}
	
	@ExcelField(title="经纪人", align=2, sort=11)
	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="申请时间", align=2, sort=12)
	public Date getApplicationtime() {
		return applicationtime;
	}

	public void setApplicationtime(Date applicationtime) {
		this.applicationtime = applicationtime;
	}
	
}