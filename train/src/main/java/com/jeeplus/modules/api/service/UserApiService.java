/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.api.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.api.entity.UserApi;
import com.jeeplus.modules.api.model.AccountModel;
import com.jeeplus.modules.fenrun.entity.anchoraccount.ZhaomuAnchorAccount;
import com.jeeplus.modules.fenrun.entity.bili.BiLI;
import com.jeeplus.modules.sys.dao.UserDao;
import com.jeeplus.modules.anchor.entity.AnchorInfo;
import com.jeeplus.modules.api.dao.UserApiDao;

/**
 * 招募用户账户Service
 * @author wangqy
 * @version 2018-01-08
 */
@Service
@Transactional(readOnly = true)
public class UserApiService extends CrudService<UserApiDao, UserApi> {
	@Autowired
	private UserApiDao userApiDao;
	
	public UserApi get(String id) {
		return super.get(id);
	}
	
	public List<UserApi> findList(UserApi userApi) {
		return super.findList(userApi);
	}
	
	public Page<UserApi> findPage(Page<UserApi> page, UserApi userApi) {
		return super.findPage(page, userApi);
	}
	
	@Transactional(readOnly = false)
	public void save(UserApi userApi) {
		super.save(userApi);
	}
	
	@Transactional(readOnly = false)
	public void delete(UserApi userApi) {
		super.delete(userApi);
	}
	
	@Transactional(readOnly = false)
	public void deleteByLogic(UserApi userApi) {
		userApiDao.deleteByLogic(userApi);
	}
	
	public UserApi findBymobile(String mobile) {
	    return userApiDao.findBymobile(mobile);
	}

	public UserApi findByOpenId(String openid) {
	    return userApiDao.findByOpenId(openid);
	}

	public List<AnchorInfo> finByAnchor(String companyid) {
		return userApiDao.finByAnchor(companyid);
	}

	public AccountModel finByshangji(String refercode) {
		return userApiDao.finByshangji(refercode);
	}


	public ZhaomuAnchorAccount findAnchorAccount(String id, String month, String year) {
		return userApiDao.findAnchorAccount(id,month,year);
	}

	

	public List<UserApi> findAnchorUserByOfficeId(String officeId){
		return userApiDao.findAnchorUserByOfficeId(officeId);
	}
    
	public List<UserApi> findTalentUserByOfficeId(String officeId){
		return userApiDao.findTalentUserByOfficeId(officeId);
	}

	public AccountModel findByTalent(String moblie, String istalent) {
		return userApiDao.findByTalent(moblie,istalent);
	}

	public List<AccountModel> findBylower(String refercode, String istalent) {
		return userApiDao.findBylower(refercode,istalent);
	}

	public List<AccountModel> finByTalent(String companyid, String year, String month) {
		return userApiDao.finByTalent(companyid,year,month);
	}

	public ZhaomuAnchorAccount findAnchorAccountType(String userId, String month, String year) {
		return userApiDao.findAnchorAccountType(userId,month,year);
	}

	public String findTotalTime(String userId) {
		return userApiDao.findTotalTime(userId);
	}
	

}