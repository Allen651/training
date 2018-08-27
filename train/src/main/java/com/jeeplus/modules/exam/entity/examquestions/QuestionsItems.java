/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.exam.entity.examquestions;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 试题管理Entity
 * @author zhaol
 * @version 2018-03-14
 */
public class QuestionsItems extends DataEntity<QuestionsItems> {
	
	private static final long serialVersionUID = 1L;
	private String itemContent;		// 选项内容
	private String isAnswer;		// 是否是正确答案
	private Integer sort;            // 选项排序
	private ExamQuestions ExamQuestions;		// 试题id 父类
	
	public QuestionsItems() {
		super();
	}

	public QuestionsItems(String id){
		super(id);
	}

	public QuestionsItems(ExamQuestions ExamQuestions){
		this.ExamQuestions = ExamQuestions;
	}

	@ExcelField(title="选项内容", align=2, sort=7)
	public String getItemContent() {
		return itemContent;
	}

	public void setItemContent(String itemContent) {
		this.itemContent = itemContent;
	}
	
	@ExcelField(title="是否是正确答案", dictType="yes_no", align=2, sort=8)
	public String getIsAnswer() {
		return isAnswer;
	}

	public void setIsAnswer(String isAnswer) {
		this.isAnswer = isAnswer;
	}
	
	public ExamQuestions getExamQuestions() {
		return ExamQuestions;
	}

	public void setExamQuestions(ExamQuestions ExamQuestions) {
		this.ExamQuestions = ExamQuestions;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	
	
	
}