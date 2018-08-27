/**
 * 
 */
package com.jeeplus.modules.api.model;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.modules.exam.entity.examresult.Examine;

/**
 * @author zhaol
 *
 * 2018年5月7日
 */
public class ExamineModel{

	private String anchorName;   //主播姓名
	private String courseName;   //课程名称
	private Examine examine = new Examine();
	private Page<ExamineModel> page;

	public String getAnchorName() {
		return anchorName;
	}

	public void setAnchorName(String anchorName) {
		this.anchorName = anchorName;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public Page<ExamineModel> getPage() {
		return page;
	}
	public void setPage(Page<ExamineModel> page) {
		this.page = page;
	}

	public Examine getExamine() {
		return examine;
	}

	public void setExamine(Examine examine) {
		this.examine = examine;
	}
	
	
}
