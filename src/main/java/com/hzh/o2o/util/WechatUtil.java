package com.hzh.o2o.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hzh.o2o.dto.UserAccessToken;
import com.hzh.o2o.dto.WechatUser;

public class WechatUtil {

	private static Logger log = LoggerFactory.getLogger(WechatUtil.class);
	public static UserAccessToken getUserAccessToken(String code)
			throws IOException {
	    String appId="wx25df44b6a934fc9e";
	    log.debug("appId:" + appId);
		String appsecret = "f5d27e5c54720dd12ec8a46a9cbcf2ca";
		log.debug("secret:" + appsecret);
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
				+ appId + "&secret=" + appsecret + "&code=" + code
				+ "&grant_type=authorization_code";
		String tokenStr= httpRequest(url, "GET", null);
		log.debug("userAccessToken:" + tokenStr);
		UserAccessToken token=new UserAccessToken();
		ObjectMapper objectMapper=new ObjectMapper();
		try {
			token=objectMapper.readValue(tokenStr, UserAccessToken.class);
		}catch(JsonParseException e) {
			e.printStackTrace();
		}
		catch(JsonMappingException e) {
			e.printStackTrace();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		if(token==null) {
			return null;
		}
		return token;
	}

	public static String httpRequest(String requestUrl,String requestMethod,String outputStr) {
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();

			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			log.debug("https buffer:"+buffer.toString());
		} catch (ConnectException ce) {
			log.error("Weixin server connection timed out.");
		} catch (Exception e) {
			log.error("https request error:{}", e);
		}
		return buffer.toString();
	}
	public static WechatUser getUserInfo(String accessToken, String openId) {
		String url = "https://api.weixin.qq.com/sns/userinfo?access_token="
				+ accessToken + "&openid=" + openId + "&lang=zh_CN";
		String jsonObject = httpRequest(url, "GET", null);
		WechatUser user = new WechatUser();
		String openid = jsonObject("openid");
		if (openid == null) {
			log.debug("获取用户信息失败。");
			return null;
		}
		user.setOpenId(openid);
		user.setNickName(jsonObject("nickname"));
		//user.setSex(jsonObject("sex"));
		user.setProvince(jsonObject("province"));
		user.setCity(jsonObject("city"));
		user.setCountry(jsonObject("country"));
		user.setHeadimgurl(jsonObject("headimgurl"));
		user.setPrivilege(null);
		// user.setUnionid(jsonObject.getString("unionid"));
		return user;
	}

	private static String jsonObject(String string) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
