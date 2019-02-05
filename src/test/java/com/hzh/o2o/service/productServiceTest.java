package com.hzh.o2o.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hzh.o2o.BaseTest;
import com.hzh.o2o.dto.ImageHolder;
import com.hzh.o2o.dto.ProductExecution;
import com.hzh.o2o.entity.Product;
import com.hzh.o2o.entity.ProductCategory;
import com.hzh.o2o.entity.Shop;
import com.hzh.o2o.enums.ProductStateEnum;
import com.hzh.o2o.exceptions.ShopOperationException;

public class productServiceTest extends BaseTest {
	@Autowired
	private ProductService productService;
	@Test
	@Ignore
	public void testAddProduct()throws ShopOperationException,FileNotFoundException{
		// 创建shopId为1且productCategoryId为1的商品实例并给其他变量赋值
				ProductCategory productCategory = new ProductCategory();
				productCategory.setProductCategoryId(1L);

				// 注意表中的外键关系，确保这些数据在对应的表中的存在
				Shop shop = new Shop();
				shop.setShopId(1L);

				// 构造Product
				Product product = new Product();
				product.setProductName("test_product");
				product.setProductDesc("product desc");

				product.setNormalPrice("10");
				product.setPromotionPrice("8");
				product.setPriority(66);
				product.setCreateTime(new Date());
				product.setLastEditTime(new Date());
				product.setEnableStatus(1);
				product.setProductCategory(productCategory);
				product.setShop(shop);

				// 构造 商品图片
				File shopImg=new File("D:\\eclipse-workspace2\\image\\2.jpg");
				InputStream ins = new FileInputStream(shopImg);
				ImageHolder imageHolder = new ImageHolder(shopImg.getName(),ins);

				// 构造商品详情图片
				List<ImageHolder> prodImgDetailList = new ArrayList<ImageHolder>();

				File productDetailFile1 = new File("D:\\eclipse-workspace2\\image\\2.jpg");
				InputStream ins1 = new FileInputStream(productDetailFile1);
				ImageHolder imageHolder1 = new ImageHolder( productDetailFile1.getName(),ins1);

				File productDetailFile2 = new File("D:\\eclipse-workspace2\\image\\3.jpg");
				InputStream ins2 = new FileInputStream(productDetailFile2);
				ImageHolder imageHolder2 = new ImageHolder(productDetailFile2.getName(),ins2);

				prodImgDetailList.add(imageHolder1);
				prodImgDetailList.add(imageHolder2);

				// 调用服务
				ProductExecution pe = productService.addProduct(product, imageHolder, prodImgDetailList);
				assertEquals(ProductStateEnum.SUCCESS.getState(), pe.getState());
	}
	@Test
	public void testModifyProduct() throws Exception {

		// 创建shopId为1且productIdCategoryId为1的商品实例并给其成员变量赋值
		ProductCategory productCategory = new ProductCategory();
		productCategory.setProductCategoryId(1L);

		// 注意表中的外键关系，确保这些数据在对应的表中的存在
		Shop shop = new Shop();
		shop.setShopId(5L);

		// 构造Product
		Product product = new Product();
		product.setProductName("正式商品");
		product.setProductDesc("正式商品");

		product.setNormalPrice("100");
		product.setPromotionPrice("80");
		product.setPriority(66);
		product.setLastEditTime(new Date());
		product.setProductCategory(productCategory);
		product.setShop(shop);

		product.setProductId(1L);
		// 构造 商品图片
		File productFile = new File("C:\\Users\\admin\\Pictures\\Camera Roll\\1.jpg");
		InputStream ins = new FileInputStream(productFile);
		ImageHolder imageHolder = new ImageHolder(productFile.getName(),ins );

		// 构造商品详情图片
		List<ImageHolder> prodImgDetailList = new ArrayList<ImageHolder>();

		File productDetailFile1 = new File("C:\\Users\\admin\\Pictures\\Camera Roll\\2.jpg");
		InputStream ins1 = new FileInputStream(productDetailFile1);
		ImageHolder imageHolder1 = new ImageHolder(productDetailFile1.getName(),ins1);

		File productDetailFile2 = new File("C:\\Users\\admin\\Pictures\\Camera Roll\\3.jpg");
		InputStream ins2 = new FileInputStream(productDetailFile2);
		ImageHolder imageHolder2 = new ImageHolder( productDetailFile2.getName(),ins2);

		prodImgDetailList.add(imageHolder1);
		prodImgDetailList.add(imageHolder2);

		// 调用服务
		ProductExecution pe = productService.modifyProduct(product, imageHolder, prodImgDetailList);
       assertEquals(ProductStateEnum.SUCCESS.getState(), pe.getState());

	}
}
