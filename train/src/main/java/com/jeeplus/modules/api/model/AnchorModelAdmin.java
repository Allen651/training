package com.jeeplus.modules.api.model;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.modules.anchor.entity.AnchorInfo;

public class AnchorModelAdmin {

	private String agentName;		//经纪人
	private String talentName;      //上级星探
	private AnchorInfo anchorInfo = new AnchorInfo();
	private Page<AnchorModelAdmin> page;
	
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public String getTalentName() {
		return talentName;
	}
	public void setTalentName(String talentName) {
		this.talentName = talentName;
	}
	public AnchorInfo getAnchorInfo() {
		return anchorInfo;
	}
	public void setAnchorInfo(AnchorInfo anchorInfo) {
		this.anchorInfo = anchorInfo;
	}
	public Page<AnchorModelAdmin> getPage() {
		return page;
	}
	public void setPage(Page<AnchorModelAdmin> page) {
		this.page = page;
	}
	
	
}
