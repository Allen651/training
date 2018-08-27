/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.api.entity;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.anchor.entity.AnchorInfo;
import com.jeeplus.modules.fenrun.entity.anchoraccount.ZhaomuAnchorAccount;
/**
 * 招募用户账户Entity
 * @author wangqy
 * @version 2018-01-08
 */
public class UserApi extends DataEntity<UserApi> {
	
	private static final long serialVersionUID = 1L;
	private Integer usercode;		// 用户编码
	private String mobile;		// 手机号
	private String name;      //姓名
	private String nickname;		// 昵称
	private String password;		// 密码
	private String weixin_operid;		// 微信id
	private String refercode;		// 推荐人编码
	private Integer istalent;   //是否星探
	private Integer isanchor;   //是否主播
	private String talent_level; //星探等级
	private AnchorInfo anchorInfo;// 主播信息
	
	public AnchorInfo getAnchorInfo() {
		return anchorInfo;
	}

	public void setAnchorInfo(AnchorInfo anchorInfo) {
		this.anchorInfo = anchorInfo;
	}

	public String getTalentlevel() {
		return talent_level;
	}

	public void setTalentlevel(String talentlevel) {
		this.talent_level = talentlevel;
	}

	public UserApi() {
		super();
	}

	public UserApi(String id){
		super(id);
	}

	@NotNull(message="用户编码不能为空")
	@ExcelField(title="用户编码", align=2, sort=1)
	public Integer getUsercode() {
		return usercode;
	}

	public void setUsercode(Integer usercode) {
		this.usercode = usercode;
	}
	
	@ExcelField(title="手机号", align=2, sort=2)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@ExcelField(title="昵称", align=2, sort=3)
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	@ExcelField(title="密码", align=2, sort=4)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@ExcelField(title="微信id", align=2, sort=5)
	public String getWeixinOperid() {
		return weixin_operid;
	}

	public void setWeixinOperid(String weixin_operid) {
		this.weixin_operid = weixin_operid;
	}
	
	@NotNull(message="推荐人编码不能为空")
	@ExcelField(title="推荐人编码", dictType="del_flag", align=2, sort=6)
	public String getRefercode() {
		return refercode;
	}

	public void setRefercode(String refercode) {
		this.refercode = refercode;
	}

	public Integer getIstalent() {
		return istalent;
	}

	public void setIstalent(Integer istalent) {
		this.istalent = istalent;
	}

	public Integer getIsanchor() {
		return isanchor;
	}

	public void setIsanchor(Integer isanchor) {
		this.isanchor = isanchor;
	}


	
}