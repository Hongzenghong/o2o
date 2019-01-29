package com.hzh.o2o.service;

import java.util.List;

import com.hzh.o2o.entity.ShopCategory;

public interface ShopCategoryService {
	List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition);
}