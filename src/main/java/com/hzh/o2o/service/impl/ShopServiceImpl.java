package com.hzh.o2o.service.impl;

import java.io.File;
import java.io.InputStream;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.hzh.o2o.dao.ShopDao;
import com.hzh.o2o.dto.ShopExecution;
import com.hzh.o2o.entity.Shop;
import com.hzh.o2o.enums.ShopStateEnum;
import com.hzh.o2o.exceptions.ShopOperationException;
import com.hzh.o2o.service.ShopService;
import com.hzh.o2o.util.ImageUtil;
import com.hzh.o2o.util.PathUtil;

@Service
public class ShopServiceImpl implements ShopService{

	@Autowired
	private ShopDao shopDao;
	
	@Override
	@Transactional
	public ShopExecution addShop(Shop shop, InputStream shopImgInputStream,String fileName) throws ShopOperationException{
		//1.店铺空值判断
				if(shop == null){
					return new ShopExecution(ShopStateEnum.NULL_SHOP);
				}
				try{
					//2.店铺信息初始化
					shop.setEnableStatus(0);
					shop.setCreateTime(new Date());
					shop.setLastEditTime(new Date());
					//3.添加店铺信息
					int effectedNum = shopDao.insertShop(shop);
					if(effectedNum <= 0){
						throw new ShopOperationException("店铺创建失败");
					}else{
						if(shopImgInputStream != null){
							//存储图片
							try {
								addShopImg(shop,shopImgInputStream,fileName);
							} catch (Exception e) {
								throw new ShopOperationException("addShop error:"+e.getMessage());
							}
							//更新图片的地址
							effectedNum = shopDao.updateShop(shop);
							if(effectedNum <= 0){
								throw new ShopOperationException("更新图片地址失败");
							}
						}
					}
				}catch(Exception e){
					throw new ShopOperationException("addShop error:"+e.getMessage());
				}
				return new ShopExecution(ShopStateEnum.CHECK, shop);
			}

			
			private void addShopImg(Shop shop, InputStream shopImgInputStream,String fileName){
				//1.获取shop图片目录的相对值路径
				String dest = PathUtil.getShopImagePath(shop.getShopId());
				String shopImgAddr = ImageUtil.generateThumbnail(shopImgInputStream, fileName,dest);
				shop.setShopImg(shopImgAddr);
			}


			@Override
			public Shop getByShopId(Long shopId) {
				return shopDao.queryByShopId(shopId);

			}
			@Override
			public ShopExecution modifyShop(Shop shop, InputStream shopImgInputStream, String fileName)
					throws ShopOperationException {
				if(shop==null||shop.getShopId()==null) {
					return new ShopExecution(ShopStateEnum.NULL_SHOP);
				}else {
					// 1.判断是否需要处理图片
					try {
					if(shopImgInputStream!=null&& fileName!=null&&!"".equals(fileName)) {
						Shop tempShop=shopDao.queryByShopId(shop.getShopId());
								if(tempShop.getShopImg()!=null) {
									ImageUtil.deleteFileOrPath(tempShop.getShopImg());
								}
						addShopImg(shop,shopImgInputStream,fileName);
					}
					//2.更行店铺信息
					shop.setLastEditTime(new Date());
					int effectedNum = shopDao.updateShop(shop);
					if(effectedNum <=0 ){
						return new ShopExecution(ShopStateEnum.INNER_ERROR);
					}else{
						shop = shopDao.queryByShopId(shop.getShopId());
						return new ShopExecution(ShopStateEnum.SUCCESS, shop);
					}}catch(Exception e) {
						throw new ShopOperationException("modifyShop error:"+e.getMessage());
					}
				}
				
			}

	}