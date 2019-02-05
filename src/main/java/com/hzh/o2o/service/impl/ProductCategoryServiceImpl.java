package com.hzh.o2o.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hzh.o2o.dao.ProductCategoryDao;
import com.hzh.o2o.dao.ProductDao;
import com.hzh.o2o.dto.ProductCategoryExecution;
import com.hzh.o2o.entity.ProductCategory;
import com.hzh.o2o.enums.ProductCategoryStateEnum;
import com.hzh.o2o.exceptions.ProductCategoryOperationException;
import com.hzh.o2o.service.ProductCategoryService;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
	@Autowired
	private ProductCategoryDao productCategoryDao;

	@Autowired
	private ProductDao productDao;

	@Override
	public List<ProductCategory> getProductCategoryList(long shopId) {

		return productCategoryDao.queryProductCategoryList(shopId);

	}

	/**
	 * 批量增加商品类别
	 */
	@Override
	@Transactional
	public ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList)
			throws ProductCategoryOperationException {
		if (productCategoryList != null && productCategoryList.size() > 0) {
			try {
				// 批量增加ProductCategory
				int effectedNum = productCategoryDao.batchInsertProductCategory(productCategoryList);
				if (effectedNum <= 0) {
					throw new ProductCategoryOperationException("商品类别创建失败");
				} else {
					return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
				}
			} catch (Exception e) {
				throw new ProductCategoryOperationException("batchAddProductCategory error" + e.getMessage());
			}
		} else {
			return new ProductCategoryExecution(ProductCategoryStateEnum.EMPETY_LIST);
		}
	}

	/**
	 * 删除商品类别
	 */
	@Override
	@Transactional
	public ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId)
			throws ProductCategoryOperationException {
		// 解除tb_product里商品玉该productcategoryId的关联，将此类别下商品里的类别id置空
		try {
			int effctedNum = productDao.updateProductCategoryToNull(productCategoryId);
			if (effctedNum < 0) {
				throw new RuntimeException("商品类别更新失败");
			}
		} catch (Exception e) {
			throw new ProductCategoryOperationException("deldeteProductCategory error:" + e.getMessage());
		}
    //删除该productCategory
		try {
			int effectedNum = productCategoryDao.deleteProductCategory(productCategoryId, shopId);
			if (effectedNum <= 0) {
				throw new ProductCategoryOperationException("商品类别删除失败");
			} else {
				return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
			}
		} catch (Exception e) {
			throw new ProductCategoryOperationException("deleteProductCategory error:" + e.getMessage());
		}

	}

}
