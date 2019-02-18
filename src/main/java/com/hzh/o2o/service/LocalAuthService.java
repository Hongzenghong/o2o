package com.hzh.o2o.service;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.hzh.o2o.dto.LocalAuthExecution;
import com.hzh.o2o.entity.LocalAuth;

public interface LocalAuthService {

	/**
	 * 
	 * @param userId
	 * @return
	 */
	LocalAuth getLocalAuthByUserId(long userId);

	/**
	 * 
	 * @param localAuth
	 * @param profileImg
	 * @return
	 * @throws RuntimeException
	 */
	//LocalAuthExecution register(LocalAuth localAuth, CommonsMultipartFile profileImg) throws RuntimeException;

	/**
	 * 绑定微信，生成平台专属账号
	 * 
	 * @param localAuth
	 * @return
	 * @throws RuntimeException
	 */
	LocalAuthExecution bindLocalAuth(LocalAuth localAuth) throws RuntimeException;

	/**
	 * 修改密码
	 * 
	 * @param localAuthId
	 * @param userName
	 * @param password
	 * @param newPassword
	 * @param lastEditTime
	 * @return
	 */
	LocalAuthExecution modifyLocalAuth(Long userId, String userName, String password, String newPassword);

	/**
	 * 登陆
	 * @param userName
	 * @param password
	 * @return
	 */
	LocalAuth queryLocalAuthByUserNameAndPwd(String userName, String password);
}