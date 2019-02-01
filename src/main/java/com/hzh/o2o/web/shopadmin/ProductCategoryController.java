package com.hzh.o2o.web.shopadmin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hzh.o2o.dto.ProductCategoryExecution;
import com.hzh.o2o.dto.Result;
import com.hzh.o2o.entity.ProductCategory;
import com.hzh.o2o.entity.Shop;
import com.hzh.o2o.enums.ProductCategoryStateEnum;
import com.hzh.o2o.exceptions.ProductCategoryOperationException;
import com.hzh.o2o.service.ProductCategoryService;

@Controller
@RequestMapping("/shopadmin")
public class ProductCategoryController {
	@Autowired
	private ProductCategoryService productCategoryService;

	@RequestMapping(value = "/getproductcategorylist", method = RequestMethod.GET)
	@ResponseBody
	private Result<List<ProductCategory>> getProductcategoryList(HttpServletRequest request) {
		// Shop shop = new Shop();
		// shop.setShopId(1L);
		// 把信息保存到currentShop
		// request.getSession().setAttribute("currentShop", shop);
		// 在进入到
		// shop管理页面（即调用getShopManageInfo方法时）,如果shopId合法，便将该shop信息放在了session中，key为currentShop
		// 这里我们不依赖前端的传入，因为不安全。 我们在后端通过session来做
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		List<ProductCategory> productCategoryList;
		ProductCategoryStateEnum ps;
		if (currentShop != null && currentShop.getShopId() != null) {
			try {
				productCategoryList = productCategoryService.getProductCategoryList(currentShop.getShopId());
				return new Result<List<ProductCategory>>(true, productCategoryList);
			} catch (Exception e) {
				e.printStackTrace();
				ps = ProductCategoryStateEnum.INNER_ERROR;
				return new Result<List<ProductCategory>>(false, ps.getState(), ps.getStateInfo());
			}
		} else {
			ps = ProductCategoryStateEnum.NULL_SHOP;
			return new Result<List<ProductCategory>>(false, ps.getState(), ps.getStateInfo());
		}
	}

	@RequestMapping(value = "/addproductcategory", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addProductCategorys(@RequestBody List<ProductCategory> productCategoryList,
			HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		for (ProductCategory pc : productCategoryList) {
			pc.setShopId(currentShop.getShopId());
		}
		if (productCategoryList != null && productCategoryList.size() > 0) {
			try {
				// 批量插入
				ProductCategoryExecution pce = productCategoryService.batchAddProductCategory(productCategoryList);
				if (pce.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
					
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", pce.getStateInfo());
				}
			} catch (ProductCategoryOperationException e) {
				e.printStackTrace();
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "至少输入一个店铺目录信息");
		}
		return modelMap;
	}
	@RequestMapping(value = "/removeproductcategory", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addProductCategorys(Long productCategoryId,
			HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (productCategoryId != null && productCategoryId> 0) {
			try {
				Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
				ProductCategoryExecution pce = productCategoryService.deleteProductCategory(productCategoryId, currentShop.getShopId());
				if (pce.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
					
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", pce.getStateInfo());
				}
			} catch (ProductCategoryOperationException e) {
				e.printStackTrace();
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "至少选择一个店铺目录类别");
		}
		return modelMap;
	}

}
