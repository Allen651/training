/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.api.dao;


import java.util.List;


import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.anchor.entity.AnchorInfo;
import com.jeeplus.modules.api.entity.UserApi;
import com.jeeplus.modules.api.model.AccountModel;
import com.jeeplus.modules.fenrun.entity.anchoraccount.ZhaomuAnchorAccount;
import com.jeeplus.modules.fenrun.entity.bili.BiLI;

/**
 * 招募用户账户DAO接口
 * @author wangqy
 * @version 2018-01-08
 */
@MyBatisDao
public interface UserApiDao extends CrudDao<UserApi> {

	/**
	 * 根据openid查询询用户
	 */
	
	public UserApi findByOpenId(@Param("openid")String openid);
    
	/**
	 * 根据手机号查询用户
	 */
	
	public UserApi findBymobile(@Param("mobile")String mobile);

	
	/*
	 * 查询当前工会下的所有主播
	 */
	public List<AnchorInfo> finByAnchor(String companyid);

	/*
	 * 根据推荐人编码查上级
	 */
	public AccountModel finByshangji(String refercode);

	
	public ZhaomuAnchorAccount findAnchorAccount(String id,String month, String year);
	
	/**
	 * 通过OfficeId获取用户列表，仅返回用户id和name（树查询用户时用）
	 * @param officeId
	 * @return
	 */
	public List<UserApi> findAnchorUserByOfficeId(@Param("officeId")String officeId);
	
	
	/**
	 * 通过OfficeId获取用户列表，仅返回用户id和name（树查询用户时用）
	 * @param officeId
	 * @return
	 */
	public List<UserApi> findTalentUserByOfficeId(@Param("officeId")String officeId);
	
	
	/**
	 * 根据推荐编码查询用户
	 */
	public UserApi findByrefercode(@Param("refercode")String refercode);

	/*
	 * 查星探
	 */
	/*public UserApi findByTalent(String id, Integer istalent);*/

	public AccountModel findByTalent(@Param("mobile")String moblie, @Param("istalent")String istalent);

	/*
	 * 根据当前星探编码查询一级星探
	 */
	public List<AccountModel> findBylower(@Param("refercode")String refercode, @Param("istalent")String istalent);
	
	/*
	 *当前工会没有主播的星探
	 */
	public List<AccountModel> finByTalent(String companyid, String year, String month);

	public ZhaomuAnchorAccount findAnchorAccountType(String userId, String month, String year);

	public String findTotalTime(String userId);

}