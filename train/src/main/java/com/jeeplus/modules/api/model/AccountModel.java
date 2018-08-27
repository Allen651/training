package com.jeeplus.modules.api.model;


import com.jeeplus.modules.api.entity.UserApi;
import com.jeeplus.modules.sys.entity.Office;

public class AccountModel extends UserApi{
	private static final long serialVersionUID = 1L;
	private Double income; //收益
	private String month;//月份
	private String name; //星探姓名
	private Double countIncome; //星探总收益
	private Double totalSettlement; //累计已结算
	private Double notSettlement; //未结算
	private Double directIncome; //直接收益
	private String sex;				//星探性别
	private String headPicture;		//星探头像
	private String talentlevel; //星探等级
	private String referee; //星探表中的推荐编码
	private Office company; //所属公会
	
	
	public Office getCompany() {
		return company;
	}
	public void setCompany(Office company) {
		this.company = company;
	}
	public String getReferee() {
		return referee;
	}
	public void setReferee(String referee) {
		this.referee = referee;
	}
	public String getTalentlevel() {
		return talentlevel;
	}
	public void setTalentlevel(String talentlevel) {
		this.talentlevel = talentlevel;
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
	public Double getDirectIncome() {
		return directIncome;
	}
	public void setDirectIncome(Double directIncome) {
		this.directIncome = directIncome;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getTotalSettlement() {
		return totalSettlement;
	}
	public void setTotalSettlement(Double totalSettlement) {
		this.totalSettlement = totalSettlement;
	}
	public Double getNotSettlement() {
		return notSettlement;
	}
	public void setNotSettlement(Double notSettlement) {
		this.notSettlement = notSettlement;
	}
	public Double getCountIncome() {
		return countIncome;
	}
	public void setCountIncome(Double countIncome) {
		this.countIncome = countIncome;
	}
	public Double getIncome() {
		return income;
	}
	public void setIncome(Double income) {
		this.income = income;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	
}
