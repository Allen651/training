/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.exam.service.examdetail;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.exam.entity.examdetail.ExamineDetail;
import com.jeeplus.modules.exam.dao.examdetail.ExamineDetailDao;

/**
 * 考试明细Service
 * @author zhaol
 * @version 2018-03-14
 */
@Service
@Transactional(readOnly = true)
public class ExamineDetailService extends CrudService<ExamineDetailDao, ExamineDetail> {

	public ExamineDetail get(String id) {
		return super.get(id);
	}
	
	public List<ExamineDetail> findList(ExamineDetail examineDetail) {
		return super.findList(examineDetail);
	}
	
	public Page<ExamineDetail> findPage(Page<ExamineDetail> page, ExamineDetail examineDetail) {
		return super.findPage(page, examineDetail);
	}
	
	@Transactional(readOnly = false)
	public void save(ExamineDetail examineDetail) {
		super.save(examineDetail);
	}
	
	@Transactional(readOnly = false)
	public void delete(ExamineDetail examineDetail) {
		super.delete(examineDetail);
	}
	
	
	
	
}