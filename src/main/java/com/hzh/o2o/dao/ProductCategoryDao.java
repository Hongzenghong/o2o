package com.hzh.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hzh.o2o.entity.ProductCategory;

public interface ProductCategoryDao {
	/**
	 * 根据ShopId查询商品列表
	 * 
	 * @param ShopId
	 * @return
	 */
	List<ProductCategory> queryProductCategoryList(long ShopId);

	/**
	 * 批量新增商品类别
	 * 
	 * @param producCategoryList
	 * @return  
	 */
	int batchInsertProductCategory(List<ProductCategory> productCategoryList);
	/**
	 * 删除指定商品类别
	 * @param productCategoryId
	 * @param shopId
	 * @return
	 */
	int deleteProductCategory(@Param("productCategoryId")long productCategoryId,
			@Param("shopId")long shopId);
}
