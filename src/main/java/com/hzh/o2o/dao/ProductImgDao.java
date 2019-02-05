package com.hzh.o2o.dao;

import java.util.List;

import com.hzh.o2o.entity.ProductImg;

public interface ProductImgDao {
	/**
	 * 
	 * @param productId
	 * @return
	 */
	List<ProductImg> queryProductImgList(long productId);

	/**
	 * 批量添加商品详情图片
	 * 
	 * @param productImgList
	 * @return
	 */
	int batchInsertProductImg(List<ProductImg> productImgList);

	/**
	 * 删除指定商品的所有详情图
	 * @param productId
	 * @return
	 */
	int deleteProductImgByProductId(long productId);
	
}
