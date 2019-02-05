package com.hzh.o2o.service;

import java.io.InputStream;
import java.security.PrivilegedActionException;
import java.util.List;

import com.hzh.o2o.dto.ImageHolder;
import com.hzh.o2o.dto.ProductExecution;
import com.hzh.o2o.entity.Product;
import com.hzh.o2o.exceptions.ProductOperationException;
import com.hzh.o2o.util.ImageUtil;

public interface ProductService {
	/**
	 * 添加商品信息以及图片处理
	 * 
	 * @param product
	 * @param thumbnail
	 * @param productImgList
	 * @return
	 * @throws ProductOperationException
	 */
	ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgList)
			throws ProductOperationException;

	/**
	 * 查询商品列表并分页：可输入条件有：商品名，商品状态，店铺ID,商品类别
	 * 
	 * @param productCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize);

	/**
	 * 通过商品Id查询唯一商品信息
	 * 
	 * @param productId
	 * @return
	 */
	Product getProductById(long productId);

	/**
	 * 修改商品信息以及图片处理
	 * 
	 * @param product
	 * @param thumbnail
	 * @param ProductImgHolderList
	 * @return
	 * @throws ProductOperationException
	 */
	ProductExecution modifyProduct(Product product, ImageHolder thumbnail, List<ImageHolder> ProductImgHolderList)
			throws ProductOperationException;
	
}
