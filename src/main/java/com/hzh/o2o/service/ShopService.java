package com.hzh.o2o.service;

import java.io.File;
import java.io.InputStream;

import com.hzh.o2o.dto.ShopExecution;
import com.hzh.o2o.entity.Shop;
import com.hzh.o2o.exceptions.ShopOperationException;

public interface ShopService {
	/**
	 * 通过店铺ID获取店铺信息
	 * @param shopId
	 * @return
	 */
	Shop getByShopId(Long shopId);
	/**
	 * 更新店铺信息，包括对图片的处理
	 * @param shop
	 * @param shopImgInputStream
	 * @param fileName
	 * @return
	 */
	ShopExecution modifyShop(Shop shop,InputStream shopImgInputStream,String fileName) throws ShopOperationException;
	/**
	 * 注册店铺信息，包括图片处理
	 * @param shop
	 * @param shopInputStream
	 * @param fileName
	 * @return
	 * @throws ShopOperationException
	 */
    ShopExecution addShop(Shop shop,InputStream shopInputStream,String fileName) throws ShopOperationException;
}
