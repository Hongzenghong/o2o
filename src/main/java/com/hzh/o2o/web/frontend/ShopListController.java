package com.hzh.o2o.web.frontend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hzh.o2o.dto.ShopExecution;
import com.hzh.o2o.entity.Area;
import com.hzh.o2o.entity.Shop;
import com.hzh.o2o.entity.ShopCategory;
import com.hzh.o2o.service.AreaService;
import com.hzh.o2o.service.ShopCategoryService;
import com.hzh.o2o.service.ShopService;
import com.hzh.o2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/frontend")
public class ShopListController {

	@Autowired
	private ShopService shopService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private ShopCategoryService shopCategoryService;

	/**
	 * 返回商品列表页里ShopCategory列表（二级或者一级），以及区域信息列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listshopspageinfo", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listShopsPageInfo(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 试着从前端请求获取praentId
		long parentId = HttpServletRequestUtil.getLong(request, "parentId");
		List<ShopCategory> shopCategoryList = null;
		// parentId不为空，查询出对应parentId目录下的全部商品目录
		if (parentId != -1) {
			try {
				// 如果parentId存在，则取出一级shopCategory下的二级ShopCategory列表
				ShopCategory childCategory = new ShopCategory();
				ShopCategory parentCategory = new ShopCategory();
				parentCategory.setShopCategoryId(parentId);
				childCategory.setParent(parentCategory);
				shopCategoryList = shopCategoryService.getShopCategoryList(childCategory);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
			}
		} else {
			// parentId为空，查询出一级的shopCategory
			try {
				shopCategoryList = shopCategoryService.getShopCategoryList(null);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
			}
		}
		modelMap.put("shopCategoryList", shopCategoryList);
		List<Area> areaList = null;
		try {
			areaList = areaService.getAreaList();
			modelMap.put("areaList", areaList);
			modelMap.put("success", true);
			return modelMap;
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
		}
		return modelMap;
	}

	/**
	 * 获取指定查询条件下的店铺列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listshops", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listShops(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//获取页码
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		//获取一页的数据条数
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		//非空判断
		if ((pageIndex > -1) && (pageSize > -1)) {
			//获取一级分类Id
			long parentId = HttpServletRequestUtil.getLong(request, "parentId");
			//试着获取特定二级类别Id
			long shopCategoryId = HttpServletRequestUtil.getLong(request, "shopCategoryId");
			//获取区域Id
			int areaId = HttpServletRequestUtil.getInt(request, "areaId");
			//获取模糊查询的姓名
			String shopName = HttpServletRequestUtil.getString(request, "shopName");
			// 封装查询条件
			Shop shopCondition = compactShopCondition4Search(parentId, shopCategoryId, areaId, shopName);
			// 调用service层提供的方法
			ShopExecution se = shopService.getShopList(shopCondition, pageIndex, pageSize);
			modelMap.put("shopList", se.getShopList());
			modelMap.put("count", se.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex");
		}

		return modelMap;
	}

	private Shop compactShopCondition4Search(long parentId, long shopCategoryId, int areaId, String shopName) {
		Shop shopCondition = new Shop();
		if (parentId != -1L) {
			//查询某个一级shopcategory下面的所有shopcategory里面的店铺列表
			ShopCategory childCategory = new ShopCategory();
			ShopCategory parentCategory = new ShopCategory();
			parentCategory.setShopCategoryId(parentId);
			childCategory.setParent(parentCategory);
			shopCondition.setShopCategory(childCategory);
		}
		if (shopCategoryId != -1L) {
			//查询二级shopcategory下的店铺列表
			ShopCategory shopCategory = new ShopCategory();
			shopCategory.setShopCategoryId(shopCategoryId);
			shopCondition.setShopCategory(shopCategory);
		}
		if (areaId != -1L) {
			//查询位于某个区域ID下店铺列表
			Area area = new Area();
			area.setAreaId(areaId);
			shopCondition.setArea(area);
		}

		if (shopName != null) {
			//查询名字里包含shopName的店铺列表
			shopCondition.setShopName(shopName);
		}
		// 查询状态为审核通过的商铺
		shopCondition.setEnableStatus(1);
		return shopCondition;
	}

}
