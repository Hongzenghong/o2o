package com.hzh.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hzh.o2o.entity.ShopCategory;

public interface ShopCategoryDao {
	/**
	 * 
	 * @param shopCategoryCondition
	 * @return
	 */
	List<ShopCategory> queryShopCategoryList(@Param("shopCategoryCondition") ShopCategory shopCategoryCondition);
}
