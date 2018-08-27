package com.jeeplus.modules.api.model;

import com.jeeplus.modules.anchor.entity.AnchorInfo;

public class AnchorPersonModel extends AnchorInfo{
	private Integer isDisplayMobile;		// 是否显示手机号
	private Integer isDisplayWenXin;		// 是否显示微信号
	private Integer isDisplayLiveData;		// 是否显示直播数据
	private String weixinNumber;      //微信号
    private Integer fansAmount;   //粉丝量
    private String personalDesc; //个人描述
    private String nickName;   //主播昵称
    private String roomurl; //直播地址
    
    
	public String getRoomurl() {
		return roomurl;
	}
	public void setRoomurl(String roomurl) {
		this.roomurl = roomurl;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public Integer getIsDisplayMobile() {
		return isDisplayMobile;
	}
	public void setIsDisplayMobile(Integer isDisplayMobile) {
		this.isDisplayMobile = isDisplayMobile;
	}
	public Integer getIsDisplayWenXin() {
		return isDisplayWenXin;
	}
	public void setIsDisplayWenXin(Integer isDisplayWenXin) {
		this.isDisplayWenXin = isDisplayWenXin;
	}
	public Integer getIsDisplayLiveData() {
		return isDisplayLiveData;
	}
	public void setIsDisplayLiveData(Integer isDisplayLiveData) {
		this.isDisplayLiveData = isDisplayLiveData;
	}
	public String getWeixinNumber() {
		return weixinNumber;
	}
	public void setWeixinNumber(String weixinNumber) {
		this.weixinNumber = weixinNumber;
	}
	public String getPersonalDesc() {
		return personalDesc;
	}
	public void setPersonalDesc(String personalDesc) {
		this.personalDesc = personalDesc;
	}
	public Integer getFansAmount() {
		return fansAmount;
	}
	public void setFansAmount(Integer fansAmount) {
		this.fansAmount = fansAmount;
	}
    

}
