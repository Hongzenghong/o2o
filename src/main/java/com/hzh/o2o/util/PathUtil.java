package com.hzh.o2o.util;

public class PathUtil {
 private static String seperator=System.getProperty("file.separator");
 public static String getImgBasePath() {
	 String os=System.getProperty("os.name");
	 String basePath="";
	 if(os.toLowerCase().startsWith("win")) {
		 basePath="D:/eclipse-workspace2/image";
		 
	 }else {
		 basePath="/home/hzh/imgea";
	 }
	 basePath=basePath.replace("/", seperator);
	 return basePath;
 }
 /**
	 * 返回项目图片子路径
	* @Title: getShopImagePath 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param shopId
	* @param @return  参数说明 
	* @return String    返回类型 
	* @throws
	 */
 public static String getShopImagePath(long shopId) {
	 String imagePath="/upload/item/shop/"+shopId+"/";
	 return imagePath.replace("/", seperator);
 }
}
