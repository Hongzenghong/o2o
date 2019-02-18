package com.hzh.o2o.web.frontend;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hzh.o2o.entity.PersonInfo;
import com.hzh.o2o.entity.Product;
import com.hzh.o2o.service.ProductService;
import com.hzh.o2o.util.HttpServletRequestUtil;



@Controller
@RequestMapping("/frontend")
public class ProductDetailController {
	@Autowired
	private ProductService productService;

	private static String URLPREFIX = "https://open.weixin.qq.com/connect/oauth2/authorize?"
			+ "appid=wxd7f6c5b8899fba83&"
			+ "redirect_uri=115.28.159.6/myo2o/shop/adduserproductmap&"
			+ "response_type=code&scope=snsapi_userinfo&state=";
	private static String URLSUFFIX = "#wechat_redirect";

	@RequestMapping(value = "/listproductdetailpageinfo", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listProductDetailPageInfo(
			HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		long productId = HttpServletRequestUtil.getLong(request, "productId");
		Product product = null;
		if (productId != -1) {
			product = productService.getProductById(productId);
			modelMap.put("product", product);
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty productId");
		}
		return modelMap;
	}

	
}
