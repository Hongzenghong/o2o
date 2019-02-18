package com.hzh.o2o.web.wechat;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hzh.o2o.dto.UserAccessToken;
import com.hzh.o2o.dto.WechatUser;
import com.hzh.o2o.entity.WechatAuth;
import com.hzh.o2o.util.WechatUtil;
//https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx25df44b6a934fc9e&redirect_uri=http://o2o.hzh123.xyz/o2o/wechatlogin/logincheck&role_type=1&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect
@Controller
@RequestMapping("wechatlogin")

public class WechatLoginController {

	private static Logger log = LoggerFactory
			.getLogger(WechatLoginController.class);
@RequestMapping(value="/logincheck", method = { RequestMethod.GET })
	public String doGet(HttpServletRequest request, HttpServletResponse response) {
		log.debug("weixin login get...");
		String code = request.getParameter("code");
		String roleType = request.getParameter("state");
		log.debug("weixin login code:" + code);
		WechatUser user = null;
		String openId = null;
		if (null != code) {
			UserAccessToken token;
			try {
				token = WechatUtil.getUserAccessToken(code);
				log.debug("weixin login token:" + token.toString());
				String accessToken = token.getAccessToken();
				openId = token.getOpenId();
				user = WechatUtil.getUserInfo(accessToken, openId);
				log.debug("weixin login user:" + user.toString());
				request.getSession().setAttribute("openId", openId);
			} catch (IOException e) {
				log.error("error in getUserAccessToken or getUserInfo or findByOpenId: "
						+ e.toString());
				e.printStackTrace();
			}
		}
		if(user!=null) {
			return "frontend/index";
		}else {
			return null;
		}
	}
}
