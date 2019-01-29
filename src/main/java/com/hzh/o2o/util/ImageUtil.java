package com.hzh.o2o.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.mysql.fabric.xmlrpc.base.Data;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Position;
import net.coobird.thumbnailator.geometry.Positions;

public class ImageUtil {
	private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final Random r = new Random();
	private static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);
	/**
	 * 
	* @Title: generateThumbnail 
	* @Description: TODO(生成图片缩略图) 
	* @param @param thumbnail 用户上传的文件
	* @param @param targetAddr 图片要保存的路径
	* @param @return  参数说明 
	* @return String    返回类型 
	* @throws
	 */
	public static String generateThumbnail(InputStream thumbnailInputStream,String fileName,String targetAddr){
		String realFileName = getRandomFileName();
		String extension = getFileExtension(fileName);
		makeDirPath(targetAddr);
		String relativeAddr = targetAddr + realFileName + extension;
		logger.debug("current relativeAddr is:"+relativeAddr);
		File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
		logger.debug("current complete addr is:" + PathUtil.getImgBasePath() + relativeAddr);
		try{
			Thumbnails.of(thumbnailInputStream).size(200, 200)
			.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/watermark.jpg")), 0.25f)
			.outputQuality(0.8f).toFile(dest);
		}catch(IOException e){
			logger.error(e.toString());
			e.printStackTrace();
		}
		return relativeAddr;
	}
	
	/**
	 * 
	* @Title: makeDirPath 
	* @Description: TODO(创建目标路径所涉及的目录) 
	* @param @param targetAddr  参数说明 
	* @return void    返回类型 
	* @throws
	 */
	private static void makeDirPath(String targetAddr) {
		//获取绝对路径
		String realFileParentPath = PathUtil.getImgBasePath() + targetAddr;
		File dirPath = new File(realFileParentPath);
		if(!dirPath.exists()){
			dirPath.mkdirs();
		}
	}

	/**
	 * 
	* @Title: getFileExtension 
	* @Description: TODO(获取输入文件流的扩展名) 
	* @param @param thumbnail
	* @param @return  参数说明 
	* @return String    返回类型 
	* @throws
	 */
	private static String getFileExtension(String fileName ) {
		return fileName.substring(fileName.lastIndexOf("."));
	}

	/**
	 * 
	* @Title: getRandomFileName 
	* @Description: TODO(生成随机文件名) 
	* @param @return  参数说明 
	* @return String    返回类型 
	* @throws
	 */
	public static String getRandomFileName() {
		//获取随机五位数
		int rannum = r.nextInt(89999)+10000;
		String nowTimeStr = sdf.format(new Date());
		return nowTimeStr + rannum;
	}
	public static void main(String[] args) throws IOException {
		//1.通过当前线程找到当前的classLoader,并得到当前的resource路径作为水印图片的路径
		String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		//读取文件并设置大小
		Thumbnails.of(new File("D:\\eclipse-workspace2\\image\\avatar-defualt.jpg")).size(200, 200)
				//添加水印到右下角,设置图片透明度为25%
				.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/watermark.jpg")), 0.25f)
				//设置图片品质为80%并输出到新的文件中
				.outputQuality(0.8f).toFile("D:\\eclipse-workspace2\\image\\avatar-defualt2.jpg");
	}
	/**
	 * storepath 是文件夹的路劲还是目录路径
	 * 如果是store PAth是文件路径则删除该文件
	 *  如果是store PAth目录路径则删除该目录下所有文件
	 * @param storePath
	 */
	public static void deleteFileOrPath(String storePath){
		File fileOrPath = new File(PathUtil.getImgBasePath()+storePath);
		if(fileOrPath.exists()){
			if(fileOrPath.isDirectory()){
				File files[] = fileOrPath.listFiles();
				for(int i=0;i<files.length;i++){
					files[i].delete();
				}
			}
			fileOrPath.delete();
		}
	}
}
