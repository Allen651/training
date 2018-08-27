/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.api.model;

import com.jeeplus.modules.anchor.entity.AnchorInfo;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.common.persistence.Page;

/**
 * 主播关联字段
 * @author zhaol
 * @version 2018-01-19
 */
public class AnchorModel extends AnchorInfo{
	
	private static final long serialVersionUID = 1L;
	private String agentName;		//经纪人
	private String officeName;		//工作室
	private Double income;          //主播收益
	private String talentName;      //上级星探
	private String nickName;   //主播昵称
	private Double sumResult;  //总成绩
	private Integer studyNum;  //学习的课程数
	private String stuProgress; //学习总进度
	
	
	



	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	private Page<AnchorModel> modelPage;
	
	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public Double getIncome() {
		return income;
	}

	public void setIncome(Double income) {
		this.income = income;
	}

	public String getTalentName() {
		return talentName;
	}

	public void setTalentName(String talentName) {
		this.talentName = talentName;
	}

	public Page<AnchorModel> getModelPage() {
		return modelPage;
	}

	public void setModelPage(Page<AnchorModel> modelPage) {
		this.modelPage = modelPage;
	}

	public Double getSumResult() {
		return sumResult;
	}

	public void setSumResult(Double sumResult) {
		this.sumResult = sumResult;
	}

	public String getStuProgress() {
		return stuProgress;
	}

	public void setStuProgress(String stuProgress) {
		this.stuProgress = stuProgress;
	}

	public Integer getStudyNum() {
		return studyNum;
	}

	public void setStudyNum(Integer studyNum) {
		this.studyNum = studyNum;
	}
	
	
}