package com.hzh.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.hzh.o2o.BaseTest;
import com.hzh.o2o.entity.ProductCategory;
//按照方法名执行（ＡＢＣ）
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class productCategoryDaoTest extends BaseTest {
	@Autowired
	private ProductCategoryDao productCategoryDao;

	@Test
	@Ignore
	public void testBqueryproductcategory() throws Exception {
		long ShopId=1;
      List<ProductCategory> productCategory=productCategoryDao.queryProductCategoryList(ShopId);
     System.out.println(productCategory.size());
	}
	@Test
	public void testABatchInsertProductCategory() {

		ProductCategory productCategory1 = new ProductCategory();
		productCategory1.setProductCategoryName("product1");
		productCategory1.setPriority(99);
		productCategory1.setCreateTime(new Date());
		productCategory1.setShopId(5L);

		ProductCategory productCategory2 = new ProductCategory();
		productCategory2.setProductCategoryName("product2");
		productCategory2.setPriority(98);
		productCategory2.setCreateTime(new Date());
		productCategory2.setShopId(5L);

		List<ProductCategory> productCategoryList = new ArrayList<ProductCategory>();
		productCategoryList.add(productCategory1);
		productCategoryList.add(productCategory2);

		int effectNum = productCategoryDao.batchInsertProductCategory(productCategoryList);
		assertEquals(2, effectNum);
	}
	@Test
	public void testCDeleteProductCategory()throws Exception{
		long shopId=5;
		List<ProductCategory> productCategoryList=productCategoryDao.queryProductCategoryList(shopId);
		// 遍历循环删除
				for (ProductCategory productCategory : productCategoryList) {
					if ("product1".equals(productCategory.getProductCategoryName()) || "product2".equals(productCategory.getProductCategoryName())) {
						int effectNum = productCategoryDao.deleteProductCategory(productCategory.getProductCategoryId(), 5L);
						assertEquals(1, effectNum);
					}
				}
	}
}
