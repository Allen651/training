/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.exam.service.examquestions;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.exam.entity.examquestions.ExamQuestions;
import com.jeeplus.modules.exam.dao.examquestions.ExamQuestionsDao;


import com.jeeplus.modules.exam.entity.examquestions.QuestionsItems;
import com.jeeplus.modules.exam.dao.examquestions.QuestionsItemsDao;

/**
 * 试题管理Service
 * @author zhaol
 * @version 2018-03-14
 */
@Service
@Transactional(readOnly = true)
public class ExamQuestionsService extends CrudService<ExamQuestionsDao, ExamQuestions> {

	@Autowired
	private QuestionsItemsDao questionsItemsDao;
	@Autowired
	private ExamQuestionsDao examQuestionsDao;
	
	public ExamQuestions get(String id) {
		ExamQuestions examQuestions = super.get(id);
		examQuestions.setQuestionsItemsList(questionsItemsDao.findList(new QuestionsItems(examQuestions)));
		return examQuestions;
	}
	
	public List<ExamQuestions> findList(ExamQuestions examQuestions) {
		return super.findList(examQuestions);
	}
	
	//查询试题选项
	public List<QuestionsItems> findItemList(QuestionsItems questionsItems) {
		return questionsItemsDao.findList(questionsItems);
	}
	
	public Page<ExamQuestions> findPage(Page<ExamQuestions> page, ExamQuestions examQuestions) {
		return super.findPage(page, examQuestions);
	}
	
	@Transactional(readOnly = false)
	public void save(ExamQuestions examQuestions) {
		super.save(examQuestions);
		for (QuestionsItems questionsItems : examQuestions.getQuestionsItemsList()){
			if (questionsItems.getId() == null){
				continue;
			}
			if (QuestionsItems.DEL_FLAG_NORMAL.equals(questionsItems.getDelFlag())){
				if (StringUtils.isBlank(questionsItems.getId())){
					questionsItems.setExamQuestions(examQuestions);
					questionsItems.preInsert();
					questionsItemsDao.insert(questionsItems);
				}else{
					questionsItems.preUpdate();
					questionsItemsDao.update(questionsItems);
				}
			}else{
				questionsItemsDao.delete(questionsItems);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(ExamQuestions examQuestions) {
		super.delete(examQuestions);
		questionsItemsDao.delete(new QuestionsItems(examQuestions));
	}

	public Integer getQuestionsNumOfCourse(String id) {
		return examQuestionsDao.getQuestionsNumOfCourse(id);
	}
	
	
}