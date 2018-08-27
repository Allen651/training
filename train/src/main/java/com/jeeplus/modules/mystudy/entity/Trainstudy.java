/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.mystudy.entity;



import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 我的学习Entity
 * @author yangyy
 * @version 2018-03-22
 */
public class Trainstudy extends DataEntity<Trainstudy> {
	
	private static final long serialVersionUID = 1L;
	private String userid;		// 学习人id
	private String courseid;		// 课程id
	private String lessonid;		// 章节id
	private Double studyprogress;		// 学习进度
	private Integer studytime;		// 学习时长(秒)
	private Integer studyplace;		// 剩余学习次数
	private Integer studytotalnum;  //学习总次数
	private String isstudy;   //是否学完 0:未学完,1:已学完
	private Integer givelikeStaus; // 点赞状态 0:取消赞 1:已点赞 
	private Integer roles; // 0:主播 1:星探 
	
	private Integer courseSort; //课程序号
	private Integer lessonSort; //章节序号
	private String videoImage;	//视频图片
	private String lessonname; //章节名称
	private String coursetypename; //阶段名称
	private String type;  //1:标准课程，2:进阶课程
	private String coursename; //课程名称
	private String anchorname; //学习人姓名
	private String examresult; //考试成绩
	
	private Double yixue; //已学章节
	private Integer isLing;//是否领取
	
	
	public Integer getRoles() {
		return roles;
	}

	public void setRoles(Integer roles) {
		this.roles = roles;
	}

	public Integer getGivelikeStaus() {
		return givelikeStaus;
	}

	public void setGivelikeStaus(Integer givelikeStaus) {
		this.givelikeStaus = givelikeStaus;
	}

	public String getIsstudy() {
		return isstudy;
	}

	public void setIsstudy(String isstudy) {
		this.isstudy = isstudy;
	}

	public Double getYixue() {
		return yixue;
	}

	public void setYixue(Double yixue) {
		this.yixue = yixue;
	}

	public Integer getCourseSort() {
		return courseSort;
	}

	public void setCourseSort(Integer courseSort) {
		this.courseSort = courseSort;
	}

	public Integer getLessonSort() {
		return lessonSort;
	}

	public void setLessonSort(Integer lessonSort) {
		this.lessonSort = lessonSort;
	}

	public String getExamresult() {
		return examresult;
	}

	public void setExamresult(String examresult) {
		this.examresult = examresult;
	}

	public String getAnchorname() {
		return anchorname;
	}

	public void setAnchorname(String anchorname) {
		this.anchorname = anchorname;
	}

	public String getCoursename() {
		return coursename;
	}

	public void setCoursename(String coursename) {
		this.coursename = coursename;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCoursetypename() {
		return coursetypename;
	}

	public void setCoursetypename(String coursetypename) {
		this.coursetypename = coursetypename;
	}

	public String getVideoImage() {
		return videoImage;
	}

	public void setVideoImage(String videoImage) {
		this.videoImage = videoImage;
	}

	public String getLessonname() {
		return lessonname;
	}

	public void setLessonname(String lessonname) {
		this.lessonname = lessonname;
	}

	public Trainstudy() {
		super();
	}

	public Trainstudy(String id){
		super(id);
	}

	public Integer getStudytotalnum() {
		return studytotalnum;
	}

	public void setStudytotalnum(Integer studytotalnum) {
		this.studytotalnum = studytotalnum;
	}

	@ExcelField(title="学习人id", align=2, sort=7)
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	@ExcelField(title="课程id", align=2, sort=8)
	public String getCourseid() {
		return courseid;
	}

	public void setCourseid(String courseid) {
		this.courseid = courseid;
	}
	
	@ExcelField(title="章节id", align=2, sort=9)
	public String getLessonid() {
		return lessonid;
	}

	public void setLessonid(String lessonid) {
		this.lessonid = lessonid;
	}
	
	@ExcelField(title="学习进度", align=2, sort=10)
	public Double getStudyprogress() {
		return studyprogress;
	}

	public void setStudyprogress(Double studyprogress) {
		this.studyprogress = studyprogress;
	}
	
	@ExcelField(title="学习时长(秒)", align=2, sort=11)
	public Integer getStudytime() {
		return studytime;
	}

	public void setStudytime(Integer studytime) {
		this.studytime = studytime;
	}
	
	@ExcelField(title="剩余学习次数", align=2, sort=12)
	public Integer getStudyplace() {
		return studyplace;
	}

	public void setStudyplace(Integer studyplace) {
		this.studyplace = studyplace;
	}

	public Integer getIsLing() {
		return isLing;
	}

	public void setIsLing(Integer isLing) {
		this.isLing = isLing;
	}
	
	
}