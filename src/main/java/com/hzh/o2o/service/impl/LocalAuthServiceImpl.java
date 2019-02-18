package com.hzh.o2o.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hzh.o2o.dao.LocalAuthDao;
import com.hzh.o2o.dto.LocalAuthExecution;
import com.hzh.o2o.entity.LocalAuth;
import com.hzh.o2o.enums.LocalAuthStateEnum;
import com.hzh.o2o.service.LocalAuthService;
import com.hzh.o2o.util.MD5;
@Service
public class LocalAuthServiceImpl implements LocalAuthService {
	@Autowired
	private LocalAuthDao localAuthDao;


	@Override
	public LocalAuth queryLocalAuthByUserNameAndPwd(String userName, String password) {
		//return localAuthDao.queryLocalByUserNameAndPwd(userName,MD5.getMd5(password));
	return localAuthDao.queryLocalByUserNameAndPwd(userName,password);
	}


	@Override
	public LocalAuth getLocalAuthByUserId(long userId) {
		// TODO Auto-generated method stub
		return localAuthDao.queryLocalByUserId(userId);
	}


	@Override
	@Transactional
	public LocalAuthExecution bindLocalAuth(LocalAuth localAuth) throws RuntimeException {
		if (localAuth == null || localAuth.getPassword() == null
				|| localAuth.getUsername() == null
				|| localAuth.getPersonInfo() == null
				|| localAuth.getPersonInfo().getUserId() == null) {
			return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
		}
		//判断是否绑定过平台账号
		LocalAuth tempAuth = localAuthDao.queryLocalByUserId(localAuth.getPersonInfo().getUserId());
		if (tempAuth != null) {
			//绑定过就直接退出
			return new LocalAuthExecution(LocalAuthStateEnum.ONLY_ONE_ACCOUNT);
		}
		try {
			localAuth.setCreateTime(new Date());
			localAuth.setLastEditTime(new Date());
			localAuth.setPassword(MD5.getMd5(localAuth.getPassword()));
			int effectedNum = localAuthDao.insertLocalAuth(localAuth);
			if (effectedNum <= 0) {
				throw new RuntimeException("帐号绑定失败");
			} else {
				return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS,
						localAuth);
			}
		} catch (Exception e) {
			throw new RuntimeException("insertLocalAuth error: "
					+ e.getMessage());
		}
	}


	@Override
	public LocalAuthExecution modifyLocalAuth(Long userId, String userName, String password, String newPassword) {
		if (userId != null && userName != null && password != null
				&& newPassword != null && !password.equals(newPassword)) {
			try {
				int effectedNum = localAuthDao.updateLocalAuth(userId,
						userName, MD5.getMd5(password),
						//加密
						MD5.getMd5(newPassword), new Date());
				if (effectedNum <= 0) {
					throw new RuntimeException("更新密码失败");
				}
				return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS);
			} catch (Exception e) {
				throw new RuntimeException("更新密码失败:" + e.toString());
			}
		} else {
			return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
		}
	}
}
