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
import com.hzh.o2o.entity.Product;
import com.hzh.o2o.entity.ProductCategory;
import com.hzh.o2o.entity.ProductImg;
import com.hzh.o2o.entity.Shop;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductDaoTest extends BaseTest {
	@Autowired
	ProductDao productDao;
	@Autowired
	ProductImgDao productImgDao;

	@Test
	@Ignore
	public void testAInsertProdcut() {

		// 注意表中的外键关系，确保这些数据在对应的表中的存在
		ProductCategory productCategory = new ProductCategory();
		productCategory.setProductCategoryId(1L);

		// 注意表中的外键关系，确保这些数据在对应的表中的存在
		Shop shop = new Shop();
		shop.setShopId(1L);

		Product product = new Product();
		product.setProductName("糖果");
		product.setProductDesc("product desc");
		product.setImgAddr("/aaa/bbb");
		product.setNormalPrice("10");
		product.setPromotionPrice("8");
		product.setPriority(66);
		product.setCreateTime(new Date());
		product.setLastEditTime(new Date());
		product.setEnableStatus(1);
		product.setProductCategory(productCategory);
		product.setShop(shop);

		int effectNum = productDao.insertProduct(product);
		System.out.println(effectNum);
	}

	@Test
	public void testBQueryProductList() throws Exception {
		Shop shop = new Shop();
		shop.setShopId(1L);
		Product productCondition = new Product();
		productCondition.setShop(shop);
		productCondition.setProductName("糖果");
		List<Product> productList = productDao.queryProductList(productCondition, 0, 2);
		assertEquals(2, productList.size());
		// 查询总数
		int count = productDao.queryProductCount(productCondition);
		assertEquals(2, count);
	}

	@Test
	@Ignore
	public void testCQueryProductById() throws Exception {
		long productId = 1;
		// 初始化两个商品下详情图实例作为productID为1的商品下的详情图片
		// 批量插入到商品详情图中
		ProductImg productImg1 = new ProductImg();
		productImg1.setImgAddr("图片1");
		productImg1.setImgDesc("测试图片1");
		productImg1.setPriority(1);
		productImg1.setCreateTime(new Date());
		productImg1.setProductId(productId);
		ProductImg productImg2 = new ProductImg();
		productImg2.setImgAddr("图片2");
		productImg2.setImgDesc("测试图片2");
		productImg2.setPriority(1);
		productImg2.setCreateTime(new Date());
		productImg2.setProductId(productId);
		List<ProductImg> productImgList = new ArrayList<ProductImg>();
		productImgList.add(productImg1);
		productImgList.add(productImg2);
		int effectedNum = productImgDao.batchInsertProductImg(productImgList);
		System.out.println("==================");
		assertEquals(2, effectedNum);
		// 查询product为1的商品信息并校验返回的详情图实例列表是否为2
		Product product = productDao.queryProductById(productId);
		assertEquals(2, product.getProductImgList().size());
		// 删除新增的这两个商品详情图实例
		effectedNum = productImgDao.deleteProductImgByProductId(productId);
		assertEquals(2, effectedNum);
		System.out.println("===================");
	}

	@Test
	@Ignore
	public void testDUpdateProduct() throws Exception {
		Product product = new Product();
		ProductCategory pc = new ProductCategory();
		Shop shop = new Shop();
		shop.setShopId(1L);
		pc.setProductCategoryId(2L);
		product.setProductId(4L);
		product.setShop(shop);
		product.setProductName("第一个产品");
		product.setProductCategory(pc);
		// 修改productID为1的商品的名称
		// 以及商品类别并校验印象行数是否为1
		int effctedNum = productDao.updateProduct(product);
		assertEquals(1, effctedNum);
	}

	@Test
	public void testUpdateProductCategoryToNull() {

		int effectNum = productDao.updateProductCategoryToNull(5);
		assertEquals(1, effectNum);
	}

}
