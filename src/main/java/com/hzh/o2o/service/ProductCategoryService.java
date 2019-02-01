package com.hzh.o2o.service;

import java.util.List;

import com.hzh.o2o.dto.ProductCategoryExecution;
import com.hzh.o2o.entity.ProductCategory;
import com.hzh.o2o.exceptions.ProductCategoryOperationException;

public interface ProductCategoryService {
	/**
	 * 查询指定某个店铺下所有商品类别信息
	 * 
	 * @param shop
	 * @return
	 */
	List<ProductCategory> getProductCategoryList(long shop);

	/**
	 * 批量添加方法
	 * 
	 * @param productCategoryList
	 * @return
	 * @throws ProductCategoryOperationException
	 */
	ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList)
			throws ProductCategoryOperationException;
	/**
	 * 将此类别下商品里的类别id置空，再删除掉该商品的类别
	 * @param productCategoryId
	 * @param shopId
	 * @return
	 */
	ProductCategoryExecution deleteProductCategory(long productCategoryId,long shopId)
	throws ProductCategoryOperationException;
}
