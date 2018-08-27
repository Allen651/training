package com.jeeplus.modules.api.model;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.modules.talent.entity.TalentInfo;

public class TalentModelAdmin {
	private String talentName;      //所属星探
	private TalentInfo talentInfo = new TalentModel();
	private Page<TalentModelAdmin> page;
	
	public String getTalentName() {
		return talentName;
	}
	public void setTalentName(String talentName) {
		this.talentName = talentName;
	}
	public TalentInfo getTalentInfo() {
		return talentInfo;
	}
	public void setTalentInfo(TalentInfo talentInfo) {
		this.talentInfo = talentInfo;
	}
	public Page<TalentModelAdmin> getPage() {
		return page;
	}
	public void setPage(Page<TalentModelAdmin> page) {
		this.page = page;
	}
	
	

}
