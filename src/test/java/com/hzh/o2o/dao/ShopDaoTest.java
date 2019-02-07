package com.hzh.o2o.dao;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hzh.o2o.BaseTest;
import com.hzh.o2o.entity.Area;
import com.hzh.o2o.entity.PersonInfo;
import com.hzh.o2o.entity.Shop;
import com.hzh.o2o.entity.ShopCategory;

public class ShopDaoTest extends BaseTest {
	@Autowired
	private ShopDao shopDao;

	@Test
	public void testQueryShopList() {
		Shop shopCondition = new Shop();
		ShopCategory childCategory = new ShopCategory();
		ShopCategory parentCategory = new ShopCategory();
		parentCategory.setShopCategoryId(5L);
		childCategory.setParent(parentCategory);
		shopCondition.setShopCategory(childCategory);
		List<Shop> shopList = shopDao.queryShopList(shopCondition, 0, 9);
		int count = shopDao.queryShopCount(shopCondition);
		System.out.println("店铺列表的大小" + shopList.size());
		System.out.println("店铺总数：" + count);

	}

	@Test
	@Ignore
	public void testQueryByShopId() {
		long shopId = 1;
		Shop shop = shopDao.queryByShopId(shopId);
		System.out.println("areaId:" + shop.getArea().getAreaId());
		System.out.println("areaName:" + shop.getArea().getAreaName());
	}

	@Test
	@Ignore
	public void testInsertShop() {
		Shop shop = new Shop();
		PersonInfo personInfo = new PersonInfo();
		Area area = new Area();
		ShopCategory shopCategory = new ShopCategory();

		// 因为tb_shop表中有外键约束,因此务必确保 设置的这几个id在对应的表中存在.
		// 我们提前在tb_person_inf tb_area
		// tb_shop_category分别添加了如下id的数据,以避免插入tb_shop时抛出如下异常
		// com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException:
		// Cannot add or update a child row: a foreign key constraint fails
		// (`o2o`.`tb_shop`, CONSTRAINT `fk_shop_area` FOREIGN KEY (`area_id`)
		// REFERENCES `tb_area` (`area_id`))
		personInfo.setUserId(1L);
		area.setAreaId(1);
		shopCategory.setShopCategoryId(1L);

		shop.setOwner(personInfo);
		shop.setArea(area);
		shop.setShopCategory(shopCategory);
		shop.setShopName("Artisan");
		shop.setShopDesc("ArtisanDesc");
		shop.setShopAddr("NanJing");
		shop.setPhone("123456");
		shop.setShopImg("/xxx/xxx");
		shop.setPriority(99);
		shop.setCreateTime(new Date());
		shop.setLastEditTime(new Date());
		shop.setEnableStatus(0);
		shop.setAdvice("Waring");

		int effectNum = shopDao.insertShop(shop);

		Assert.assertEquals(effectNum, 1);

	}

	@Test
	@Ignore
	public void testupdateShop() {
		Shop shop = new Shop();
		shop.setShopId(1L);
		shop.setShopDesc("test2");
		shop.setShopAddr("test2");
		shop.setLastEditTime(new Date());
		int effectedNum = shopDao.updateShop(shop);
		assertEquals(1, effectedNum);
	}
}
