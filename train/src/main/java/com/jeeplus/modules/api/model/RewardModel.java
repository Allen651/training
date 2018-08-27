/**
 * 
 */
package com.jeeplus.modules.api.model;

import com.jeeplus.modules.reward.entity.Reward;

/**
 * @author zhaol
 *
 * 2018年4月14日
 */
public class RewardModel extends Reward{
	
	private static final long serialVersionUID = 1L;
	private String typeName;		//奖励类型
	private String typeIndex;       //类型
	
	
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getTypeIndex() {
		return typeIndex;
	}

	public void setTypeIndex(String typeIndex) {
		this.typeIndex = typeIndex;
	}

}
