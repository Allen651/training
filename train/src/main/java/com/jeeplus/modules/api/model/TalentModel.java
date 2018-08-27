/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.api.model;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.modules.sys.entity.Area;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.talent.entity.TalentInfo;

import javax.validation.constraints.NotNull;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 星探关联字段
 * @author zhaol
 * @version 2018-01-08
 */
public class TalentModel extends TalentInfo{
	
	private static final long serialVersionUID = 1L;
	private Integer anchorNum;      //主播数量
	private String talentName;      //所属星探
	private Double income;          //星探月收益
	private Page<TalentModel> modelPage;
	private String status; 
	private String deptname;
	
	
	
	public String getDeptname() {
		return deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTalentName() {
		return talentName;
	}

	public void setTalentName(String talentName) {
		this.talentName = talentName;
	}

	public Integer getAnchorNum() {
		return anchorNum;
	}

	public void setAnchorNum(Integer anchorNum) {
		this.anchorNum = anchorNum;
	}

	public Double getIncome() {
		return income;
	}

	public void setIncome(Double income) {
		this.income = income;
	}

	public Page<TalentModel> getModelPage() {
		return modelPage;
	}

	public void setModelPage(Page<TalentModel> modelPage) {
		this.modelPage = modelPage;
	}


}