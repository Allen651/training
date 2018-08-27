/**
 * 
 */
package com.jeeplus.modules.api.model;

import com.jeeplus.modules.oldnew.entity.takeinfo.TakeInfo;

/**
 * @author zhaol
 *
 * 2018年5月7日
 */
public class TakeInfoModel extends TakeInfo{

	private String anchorName;   //主播姓名
	private Integer ownerFans;   //主播粉丝
	private String sex;      //性别
	private String headPicture;   //头像
	private Integer takeNum;  //可带人次数
	
	public String getAnchorName() {
		return anchorName;
	}

	public void setAnchorName(String anchorName) {
		this.anchorName = anchorName;
	}

	public Integer getOwnerFans() {
		return ownerFans;
	}

	public void setOwnerFans(Integer ownerFans) {
		this.ownerFans = ownerFans;
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

	public Integer getTakeNum() {
		return takeNum;
	}

	public void setTakeNum(Integer takeNum) {
		this.takeNum = takeNum;
	}

	
	
}
