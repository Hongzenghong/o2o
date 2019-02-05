package com.hzh.o2o.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hzh.o2o.dao.ProductDao;
import com.hzh.o2o.dao.ProductImgDao;
import com.hzh.o2o.dto.ImageHolder;
import com.hzh.o2o.dto.ProductExecution;
import com.hzh.o2o.entity.Product;
import com.hzh.o2o.entity.ProductImg;
import com.hzh.o2o.enums.ProductStateEnum;
import com.hzh.o2o.exceptions.ProductOperationException;
import com.hzh.o2o.service.ProductService;
import com.hzh.o2o.util.ImageUtil;
import com.hzh.o2o.util.PageCalculator;
import com.hzh.o2o.util.PathUtil;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductDao productDao;
	@Autowired
	private ProductImgDao productImgDao;

	@Override
	@Transactional
	// 1、处理缩略图，获取缩略图相应路劲并赋值给product
	// 2、往tb_producr写入商品信息，获取productId
	// 3、结合productId批量处理商品详情图
	// 4、将商品详情列表批量插入tb_product_img中
	public ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgList)
			throws ProductOperationException {
		if (product != null && product.getShop() != null && product.getShop().getShopId() != null
				&& product.getProductCategory().getProductCategoryId() != null) {
			// 设置默认的属性 展示
			product.setCreateTime(new Date());
			product.setLastEditTime(new Date());
			// 默认上架的状态
			product.setEnableStatus(1);
			// 如果文件的输入流和文件名不为空，添加文件到特定目录，并且将相对路径设置给product,这样product就有了ImgAddr，为下一步的插入tb_product提供了数据来源
			if (thumbnail != null) {
				addProductImg(product, thumbnail);
			}
			try {
				// 写入tb_product
				int effectNum = productDao.insertProduct(product);
				if (effectNum <= 0) {
					throw new ProductOperationException("商品创建失败");
				}
				// 如果添加商品成功，继续处理商品详情图片，并写入tb_product_img
				if (productImgList != null && productImgList.size() > 0) {
					addProductImgList(product, productImgList);
				}
				return new ProductExecution(ProductStateEnum.SUCCESS, product);
			} catch (Exception e) {
				throw new ProductOperationException("商品创建失败：" + e.getMessage());
			}

		} else {
			return new ProductExecution(ProductStateEnum.NULL_PARAMETER);
		}
	}

	/**
	 * 将商品的缩略图写到特定的shopId目录，并将imgAddr属性设置给product
	 * 
	 * @param product
	 * @param imageHolder
	 */
	private void addProductImg(Product product, ImageHolder thumbnail) {
		// 根据shopId获取图片存储的相对路径
		String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
		// 添加图片到指定的目录
		String thumbnailAddr = ImageUtil.generateThumbnail(thumbnail, dest);
		// 将relativeAddr设置给product
		product.setImgAddr(thumbnailAddr);
	}

	/**
	 * 批量添加图片
	 * 
	 * @param product
	 * @param productImg
	 */
	private void addProductImgList(Product product, List<ImageHolder> productImgHolderList) {
		// 获取图片储存路径，这里直接存放到相应店铺文件夹下
		String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
		List<ProductImg> productImgList = new ArrayList<ProductImg>();
		// 遍历图片一次去处理，并添加进ProductImg实体类

		for (ImageHolder productImgHolder : productImgHolderList) {
			String imgAddr = ImageUtil.generateNormalImg(productImgHolder, dest);
			ProductImg productImg = new ProductImg();
			productImg.setImgAddr(imgAddr);
			productImg.setProductId(product.getProductId());
			productImg.setCreateTime(new Date());
			productImgList.add(productImg);
		}
		// 如果的确是有图片需要添加，就执行批量操作
		if (productImgList.size() > 0) {
			try {
				int effectedNum = productImgDao.batchInsertProductImg(productImgList);
				if (effectedNum <= 0) {
					throw new ProductOperationException("创建商品详情图片失败");
				}
			} catch (Exception e) {
				throw new ProductOperationException("创建商品详情图片失败:" + e.toString());
			}
		}
	}

	@Override
	public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize) {
		// 页面转换成数据库行码，并调用dao层取回指定页码的商品列表
		int rowIndex=PageCalculator.calculateRownIndex(pageIndex, pageSize);
		List<Product> productList=productDao.queryProductList(productCondition, rowIndex, pageSize);
		//基于同样的条件返回下商品总数
		int count=productDao.queryProductCount(productCondition);
		ProductExecution pe=new ProductExecution();
		pe.setProductList(productList);
		pe.setCount(count);
		return pe;
	}

	@Override
	public Product getProductById(long productId) {
		return productDao.queryProductById(productId);
	}

	@Override
	@Transactional
	// 1、若缩略图参数有值，则处理缩略图
	// 若原先早缩略图则先删除再添加新图，之后获取缩略图相对路径并赋值给product
	// 2、若商品详情图列表参数有值，对商品详情图片列表进行同样的操作
	// 3、将tb_product_img下面的该商品原先的商品详情图记录全部清除
	// 4、更行tb-product-img以及tb_product的信息
	public ProductExecution modifyProduct(Product product, ImageHolder thumbnail,
			List<ImageHolder> ProductImgHolderList) throws ProductOperationException {
		// 空值判断
		if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
			// 设置商品默认的属性
			product.setLastEditTime(new Date());
			// 若不为空则删除再添加
			// Step1. 处理缩略图
			if (thumbnail != null) {
				Product tempProduct = productDao.queryProductById(product.getProductId());
				// 1.1 删除旧的缩略图
				if (tempProduct.getImgAddr() != null) {
					ImageUtil.deleteFileOrPath(tempProduct.getImgAddr());
				}
				// 1.2 添加新的缩略图
				addProductImg(product, thumbnail);
			}

			// Step2. 处理商品详情

			// 如果添加商品成功，继续处理商品详情图片，并写入tb_product_img
			if (ProductImgHolderList != null && ProductImgHolderList.size() > 0) {
				// 2.1 删除库表中productId对应的tb_product_img的信息
				deleteProductImgList(product.getProductId());
				// 2.2 处理商品详情图片，并写入tb_product_img
				addProductImgList(product, ProductImgHolderList);
			}
			try {
				// Step3.更新tb_product
				int effectNum = productDao.updateProduct(product);
				if (effectNum <= 0) {
					throw new ProductOperationException("商品更新失败");
				}
				return new ProductExecution(ProductStateEnum.SUCCESS, product);
			} catch (Exception e) {
				throw new ProductOperationException("商品更新失败：" + e.getMessage());
			}

		} else {
			return new ProductExecution(ProductStateEnum.NULL_PARAMETER);
		}
	}

	/**
	 * 删除某个商品下的所有详情图
	 * 
	 * @param productId
	 */
	private void deleteProductImgList(long productId) {
		// 获取该商铺下对应的productImg信息
		List<ProductImg> productImgList = productImgDao.queryProductImgList(productId);
		// 遍历删除该目录下的全部文件
		for (ProductImg productImg : productImgList) {
			ImageUtil.deleteFileOrPath(productImg.getImgAddr());
		}
		// 删除tb_product_img中该productId对应的记录
		productImgDao.deleteProductImgByProductId(productId);
	}
}
