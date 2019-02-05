package com.hzh.o2o.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hzh.o2o.BaseTest;
import com.hzh.o2o.dto.ImageHolder;
import com.hzh.o2o.dto.ShopExecution;
import com.hzh.o2o.entity.Area;
import com.hzh.o2o.entity.PersonInfo;
import com.hzh.o2o.entity.Shop;
import com.hzh.o2o.entity.ShopCategory;
import com.hzh.o2o.enums.ShopStateEnum;
import com.hzh.o2o.exceptions.ShopOperationException;

public class ShopServiceTest extends BaseTest{
@Autowired
private ShopService shopService;

@Test
public void testGetShopList() {
	Shop shopCondition=new Shop();
	ShopCategory sc=new ShopCategory();
	sc.setShopCategoryId(2L);
	shopCondition.setShopCategory(sc);
	ShopExecution se=shopService.getShopList(shopCondition, 2, 2);
	System.out.println("店铺列表数"+se.getShopList().size());
	System.out.println("店铺总数"+se.getCount());
}

@Test
@Ignore
public void testModifyShop()throws ShopOperationException,FileNotFoundException{
	Shop shop=new Shop();
	shop.setShopId(1L);
	shop.setShopName("修改后店铺名称");
	File shopImg=new File("D:\\eclipse-workspace2\\image\\3.jpg");
	InputStream is=new FileInputStream(shopImg);
	ImageHolder imageHolder=new ImageHolder("3.jpg", is);
	ShopExecution shopExecution=shopService.modifyShop(shop,imageHolder);
	System.out.println("新图片地址："+shopExecution.getShop().getShopImg());
}
@Test
@Ignore
public void testAddShop() throws FileNotFoundException {
	Shop shop=new Shop();
	PersonInfo owner=new PersonInfo();
	Area area=new Area();
	ShopCategory shopCategory=new ShopCategory();
	owner.setUserId(1L);
	area.setAreaId(2);
	shopCategory.setShopCategoryId(1L);
	shop.setOwner(owner);
	shop.setArea(area);
	shop.setShopCategory(shopCategory);
	shop.setShopName("测试的店铺3");
	shop.setShopDesc("test3");
	shop.setShopAddr("test3");
	shop.setPhone("test3");
	shop.setCreateTime(new Date());
	shop.setEnableStatus(ShopStateEnum.CHECK.getState());
	shop.setAdvice("审核中");
	File shopImg=new File("D:\\eclipse-workspace2\\image\\2.jpg");
	InputStream Is=new FileInputStream(shopImg);
	ImageHolder imageHolder=new ImageHolder(shopImg.getName(),Is);
	ShopExecution se=shopService.addShop(shop, imageHolder);
	assertEquals(ShopStateEnum.CHECK.getState(), se.getState());
}
}
