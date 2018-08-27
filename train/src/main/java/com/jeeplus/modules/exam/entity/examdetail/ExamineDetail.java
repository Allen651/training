/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.exam.entity.examdetail;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 考试明细Entity
 * @author zhaol
 * @version 2018-03-14
 */
public class ExamineDetail extends DataEntity<ExamineDetail> {
	
	private static final long serialVersionUID = 1L;
	private String userId;		// 考试人id
	private String questionId;		// 试题id
	private String testContent;		// 答题内容
	private String isRight;		// 是否正确
	
	public ExamineDetail() {
		super();
	}

	public ExamineDetail(String id){
		super(id);
	}

	@ExcelField(title="考试人id", align=2, sort=7)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@ExcelField(title="试题id", align=2, sort=8)
	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}
	
	@ExcelField(title="答题内容", align=2, sort=9)
	public String getTestContent() {
		return testContent;
	}

	public void setTestContent(String testContent) {
		this.testContent = testContent;
	}
	
	@ExcelField(title="是否正确", dictType="yes_no", align=2, sort=10)
	public String getIsRight() {
		return isRight;
	}

	public void setIsRight(String isRight) {
		this.isRight = isRight;
	}
	
}