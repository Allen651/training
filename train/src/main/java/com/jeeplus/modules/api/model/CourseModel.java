/**
 * 
 */
package com.jeeplus.modules.api.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.modules.course.entity.Course;

/**
 * @author zhaol
 *
 * 2018年3月22日
 */
public class CourseModel extends Course{
	
	private static final long serialVersionUID = 1L;
	private String result;		//成绩
	private Double progress;	//学习进度
    private Integer itemNum;    //试题个数
    private String courseImg;   //课程封面
    private String isFinish;    //课程是否学完
    private String isExam;      //课程是否考试
    private String redpacket;   //红包奖励
    private String isReceive;   //是否领取
    private Date rewardGetTime;     //奖励获取时间
    private String rewardId;    //奖励id
    private String rightRate;   //正确率
    private Double sumPoint;    //总分
    private String courseBrief; //课程简介
    private String remarks;  //课程奖励备注信息
    
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getResult() {
		return result;
	}
	
	public void setResult(String result) {
		this.result = result;
	}

	public Double getProgress() {
		return progress;
	}

	public void setProgress(Double progress) {
		this.progress = progress;
	}

	public Integer getItemNum() {
		return itemNum;
	}

	public void setItemNum(Integer itemNum) {
		this.itemNum = itemNum;
	}

	public String getCourseImg() {
		return courseImg;
	}

	public void setCourseImg(String courseImg) {
		this.courseImg = courseImg;
	}

	public String getIsFinish() {
		return isFinish;
	}

	public void setIsFinish(String isFinish) {
		this.isFinish = isFinish;
	}

	public String getRedpacket() {
		return redpacket;
	}

	public void setRedpacket(String redpacket) {
		this.redpacket = redpacket;
	}

	public String getIsExam() {
		return isExam;
	}

	public void setIsExam(String isExam) {
		this.isExam = isExam;
	}

	public String getIsReceive() {
		return isReceive;
	}

	public void setIsReceive(String isReceive) {
		this.isReceive = isReceive;
	}

	public String getRewardId() {
		return rewardId;
	}

	public void setRewardId(String rewardId) {
		this.rewardId = rewardId;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
	public Date getRewardGetTime() {
		return rewardGetTime;
	}

	public void setRewardGetTime(Date rewardGetTime) {
		this.rewardGetTime = rewardGetTime;
	}

	public String getRightRate() {
		return rightRate;
	}

	public void setRightRate(String rightRate) {
		this.rightRate = rightRate;
	}

	public Double getSumPoint() {
		return sumPoint;
	}

	public void setSumPoint(Double sumPoint) {
		this.sumPoint = sumPoint;
	}

	public String getCourseBrief() {
		return courseBrief;
	}

	public void setCourseBrief(String courseBrief) {
		this.courseBrief = courseBrief;
	}
	
	
}
