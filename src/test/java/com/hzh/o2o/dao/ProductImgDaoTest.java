package com.hzh.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.hzh.o2o.BaseTest;
import com.hzh.o2o.entity.ProductImg;
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductImgDaoTest extends BaseTest{
	@Autowired
	private ProductImgDao productImgDao;
	@Test
	public void testABatchInsertProductImg() throws Exception{
		ProductImg productImg1 = new ProductImg();
		productImg1.setImgAddr("/xiaogongjiang/xxxx");
		productImg1.setImgDesc("商品详情图片1");
		productImg1.setPriority(99);
		productImg1.setCreateTime(new Date());
		productImg1.setProductId(2L);
		
		ProductImg productImg2 = new ProductImg();
		productImg2.setImgAddr("/artisan/xxxx");
		productImg2.setImgDesc("商品详情图片2");
		productImg2.setPriority(98);
		productImg2.setCreateTime(new Date());
		productImg2.setProductId(2L);

		// 添加到productImgList中
		List<ProductImg> productImgList = new ArrayList<ProductImg>();
		productImgList.add(productImg1);
		productImgList.add(productImg2);

		// 调用接口批量新增商品详情图片
		int effectNum = productImgDao.batchInsertProductImg(productImgList);
		System.out.println(effectNum);
	}
//	@Test
//	public void testBQueryProductImg() {
//		List<ProductImg> producctImgList=productImgDao.queryProductImgList(2L);
//		System.out.println(producctImgList);
//	}
	@Test
	public void testCDeleteProductImgByProductId()throws Exception {
		//删除新增的两条商品详情图片记录
		long productId=2L;
		int effctedNum=productImgDao.deleteProductImgByProductId(productId);
		assertEquals(2, effctedNum);
	}
}
