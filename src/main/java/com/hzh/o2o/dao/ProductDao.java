package com.hzh.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hzh.o2o.entity.Product;

public interface ProductDao {
	/**
	 * 通过productId查询唯一的商品信息
	 * 
	 * @param productId
	 * @return
	 */
	Product queryProductById(long productId);

	/**
	 * 查询商品列表分页，可输入条件有：商品名（模糊），商品状态，店铺Id,商品类别
	 * 
	 * @param productCondition
	 * @param pageSize
	 * @return
	 */
	List<Product> queryProductList(@Param("productCondition") Product productCondition, @Param("rowIndex") int rowIndex,
			@Param("pageSize") int pageSize);

	/**
	 * 查询对映的商品总数
	 * 
	 * @param productContidition
	 * @return
	 */
	int queryProductCount(@Param("productCondition") Product productCondidition);

	/**
	 * 插入商品
	 * 
	 * @param product
	 * @return
	 */
	int insertProduct(Product product);

	/**
	 * 修改商品
	 * 
	 * @param product
	 * @return
	 */
	int updateProduct(Product product);

	/**
	 * 删除商品类别之前，将商品类别ID置为空
	 * 
	 * @param productCategoryId
	 * @return
	 */
	int updateProductCategoryToNull(long productCategoryId);

	/**
	 * 删除商品
	 * 
	 * @param productId
	 * @param shopId
	 * @return
	 */
	int deleteProduct(@Param("productId") long productId, @Param("shopId") long shopId);

}
