package com.hzh.o2o.web.shopadmin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hzh.o2o.dto.ShopExecution;
import com.hzh.o2o.entity.Area;
import com.hzh.o2o.entity.PersonInfo;
import com.hzh.o2o.entity.Shop;
import com.hzh.o2o.entity.ShopCategory;
import com.hzh.o2o.enums.ShopStateEnum;
import com.hzh.o2o.exceptions.ShopOperationException;
import com.hzh.o2o.service.AreaService;
import com.hzh.o2o.service.ShopCategoryService;
import com.hzh.o2o.service.ShopService;
import com.hzh.o2o.util.CodeUtil;
import com.hzh.o2o.util.HttpServletRequestUtil;
import com.hzh.o2o.util.ImageUtil;
import com.hzh.o2o.util.PathUtil;

@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {
	@Autowired
	private ShopService shopService;
	@Autowired
	private ShopCategoryService shopCategoryService;
	
	@Autowired
	private AreaService areaService;
	
	@RequestMapping(value="/getshopbyid",method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopById(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<>();
		Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if(shopId > -1){
			try{
				Shop shop = shopService.getByShopId(shopId);
				List<Area> areaList = areaService.getAreaList();
				modelMap.put("shop", shop);
				modelMap.put("areaList", areaList);
				modelMap.put("success", true);
			}catch(Exception e){
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
			}
		}else{
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty shopId");
		}
		return modelMap;
	}
/**
 * 获取区域和商铺分类
 * @return
 */
	
	@RequestMapping(value="/getshopinitinfo",method=RequestMethod.GET)
	@ResponseBody
	private Map<String,Object> getShopInitInfo(){
		Map<String, Object> modelMap = new HashMap<>();
		List<ShopCategory> shopCategoryList = new ArrayList<>();
		List<Area> areaList = new ArrayList<>();
		try{
			shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
			areaList = areaService.getAreaList();
			modelMap.put("shopCategoryList", shopCategoryList);
			modelMap.put("areaList", areaList);
			modelMap.put("success", true);
		}catch(Exception e){
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}
/**
 * 注册
 * @param request
 * @return
 */
	@RequestMapping(value = "/registershop", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> registerShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<>();
		if(!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入验证码错误");
			return modelMap;
		}
		// 1.接收并转化相应的参数,包括店铺信息及图片信息
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try {
			shop = mapper.readValue(shopStr, Shop.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}

		CommonsMultipartFile shopImg = null;
		// 获取当前servert对话中的多部件解析器
		CommonsMultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		// 判断解析器中是否包含文件流
		if (resolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "上传图片不能为空");
			return modelMap;
		}

		// 2.注册店铺
		if (shop != null && shopImg != null) {
			PersonInfo owner = (PersonInfo)request.getSession().getAttribute("user");
			shop.setOwner(owner);

			// service中传入的参数是file,方便于测试
			// 这里的是CommonsMultiPartFile,所以我们需要将CommonsMultiPartFile转为File
			// File shopImgFile = new File(PathUtil.getImgBasePath()+
			// ImageUtil.getRandomFileName());
			// try {
			// shopImgFile.createNewFile();
			// } catch (IOException e) {
			// modelMap.put("success", false);
			// modelMap.put("errMsg",e.getMessage());
			// return modelMap;
			// }
			//
			ShopExecution se;
			try {
				se = shopService.addShop(shop, shopImg.getInputStream(), shopImg.getOriginalFilename());
				if (se.getState() == ShopStateEnum.CHECK.getState()) {
					modelMap.put("success", true);
					//该用户可以操作的店铺列表
					@SuppressWarnings("unchecked")
					List<Shop> shopList=(List<Shop>)request.getSession().getAttribute("shopList");
					if(shopList==null||shopList.size()==0) {
						shopList=new ArrayList<Shop>();
					}
						shopList.add(se.getShop());
						request.getSession().setAttribute("shopList", shopList);
					
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", "请输入店铺信息");
				}
			} catch (ShopOperationException e) {
				// TODO Auto-generated catch block
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				;
			}
			return modelMap;
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入店铺信息");
			return modelMap;
		}
		// 3.返回结果

	}
	// private static void inputStreamToFile(InputStream ins,File file){
	// FileOutputStream os = null;
	// try{
	// os = new FileOutputStream(file);
	// int bytesRead = 0;
	// byte[] buffer = new byte[1024];
	// while((bytesRead = ins.read(buffer))!=-1){
	// os.write(buffer, 0, bytesRead);
	// }
	// }catch(Exception e){
	// throw new RuntimeException("调用inputStreamToFile异常:" + e.getMessage());
	// }finally{
	// try{
	// if(os != null){
	// os.close();
	// }
	// if(ins != null){
	// ins.close();
	// }
	// }catch(IOException e){
	// throw new RuntimeException("inputStreamToFile关闭IO异常:" + e.getMessage());
	// }
	// }
	// }
	//
	/**
	 * 修改店铺信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/modifyshop",method=RequestMethod.POST)
	@ResponseBody
	private Map<String,Object> modifyShop(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<>();
		if(!CodeUtil.checkVerifyCode(request)){
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		//1.接收并转化相应的参数,包括店铺信息及图片信息
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try{
			shop = mapper.readValue(shopStr, Shop.class);
		}catch(Exception e){
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		
		CommonsMultipartFile shopImg = null;
		//获取当前servert对话中的多部件解析器
		CommonsMultipartResolver resolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		//判断解析器中是否包含文件流
		if(resolver.isMultipart(request)){
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)request;
			shopImg = (CommonsMultipartFile)multipartHttpServletRequest.getFile("shopImg");
		}
		//2.修改店铺
		if(shop != null && shop.getShopId() != null){
			ShopExecution se;
			try {
				if(shopImg == null){
					se = shopService.modifyShop(shop,null,null);
				}else{
					se = shopService.modifyShop(shop, shopImg.getInputStream(),shopImg.getOriginalFilename());
				}
				if(se.getState() == ShopStateEnum.SUCCESS.getState()){
					modelMap.put("success", true);
				}else{
					modelMap.put("success", false);
					modelMap.put("errMsg", "请输入店铺信息");
				}
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
			
			return modelMap;
		}else{
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入店铺id");
			return modelMap;
		}
		//3.返回结果
	}
}