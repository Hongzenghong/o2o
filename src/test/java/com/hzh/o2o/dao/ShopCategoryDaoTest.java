package com.hzh.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hzh.o2o.BaseTest;
import com.hzh.o2o.entity.ShopCategory;

public class ShopCategoryDaoTest extends BaseTest{
	
	@Autowired
	private ShopCategoryDao shopCategoryDao;
	
	@Test
	public void testQueryShopCategory(){
		List<ShopCategory> shopCategoryList = shopCategoryDao.queryShopCategoryList(null);
		System.out.println(shopCategoryList.size());
//		assertEquals(2,shopCategoryList.size());
//		ShopCategory testCategory=new ShopCategory();
//		ShopCategory parentCategory=new ShopCategory();
//		parentCategory.setShopCategoryId(1L);
//		testCategory.setParent(parentCategory);
//		shopCategoryList=shopCategoryDao.queryShopCategoryList(testCategory);
//		assertEquals(1, shopCategoryList.size());
		//System.out.println(shopCategoryList.get(0).getShopCategoryName());
	}
}
