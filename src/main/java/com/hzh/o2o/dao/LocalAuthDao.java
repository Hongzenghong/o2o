package com.hzh.o2o.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.hzh.o2o.entity.LocalAuth;

public interface LocalAuthDao {

	/**
	 * 根据账号密码查询用户
	 * @param userName
	 * @param password
	 * @return
	 */
	LocalAuth queryLocalByUserNameAndPwd(@Param("userName") String userName, @Param("password") String password);
	/**
	 * 通过用户ID查询对应localuth
	 * @param userId
	 * @return
	 */
	LocalAuth queryLocalByUserId(@Param("userId") long userId);

	/**
	 * 添加平台账号
	 * @param localAuth
	 * @return
	 */
	int insertLocalAuth(LocalAuth localAuth);

	/**
	 * 通过userID,username,password 更改密码
	 * @param localAuth
	 * @return
	 */
	int updateLocalAuth(@Param("userId") Long userId,
			@Param("userName") String userName,
			@Param("password") String password,
			@Param("newPassword") String newPassword,
			@Param("lastEditTime") Date lastEditTime);
}
