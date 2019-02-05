/**
 *  因为商品的添加和编辑复用同一个页面，所以需要根据url中的商品Id来判断
 */
$(function(){
	//通过url是否含有productId来判断是添加商品还是编辑
	var productId = getQueryString('productId');
   //通过productID获取商品信息的URL
	var infoUrl='/o2o/shopadmin/getproductbyid?productId='+productId;
	//获取当前店铺设定的商品类别列表的URl
	var categoryUrl='/o2o/shopadmin/getproductcategorylist';
	//更新商品信息URl
	var productPostUrl='/o2o/shopadmin/modifyproduct';
	//由于商品添加湖人编辑使用的是同一页面
	// 通过标示符，确定调用的添加还是编辑方法
	var isEdit=false;
	if(productId){
		// 则根据productId调用获取product信息的方法  
         getInfo(productId);
         isEdit=true;
	}else{
		getCategory();
		productPostUrl='/o2o/shopadmin/addproduct';
	}
	
	/**
	 * 为商品添加操作提供该店铺下所有商品类别列表
	 */
	function getCategory(){
		$.getJSON(categoryUrl,
				function(data){
					if(data.success){
						// 设置product_category
						var productCategoryList = data.data;
						var productCategoryTempHtml = '';
						productCategoryList.map(function(item, index) {
							productCategoryTempHtml += '<option data-value="'
								+ item.productCategoryId + '">'
								+ item.productCategoryName + '</option>';
						});
						$('#product-category').html(productCategoryTempHtml);
					}else{
						$.toast(data.errMsg)
					}
		});
	};
	
	/**
	 * 针对商品详情图控件组，若该控件组的最后一个元素发生变化（上传了图片）
	 * 点击控件的最后一个且图片数量小于6个的时候，生成一个选择框
	 */
	$('.detail-img-div').on('change', '.detail-img:last-child', function() {
		if ($('.detail-img').length < 6) {
			$('#detail-img').append('<input type="file" class="detail-img">');
		}
	});
	
	
	/**
	 * 编辑页面调用的function
	 */
	function getInfo(id){
		$.getJSON(
				infoUrl,
				function(data) {
					if (data.success) {
						//从返回的JSON当中获取product对象的信息，并赋值给表单
						var product = data.product;
						$('#product-name').val(product.productName);
						$('#product-desc').val(product.productDesc);
						$('#priority').val(product.priority);
						$('#normal-price').val(product.normalPrice);
						$('#promotion-price').val(
								product.promotionPrice);
                       //获取原本的商品类别以及该店铺的所有商品类别列表
						var optionHtml = '';
						var optionArr = data.productCategoryList;
						var optionSelected = product.productCategory.productCategoryId;
						//生成前端的HTML商品类别列表，并默认选择编辑前的商品类别
						optionArr.map(function(item, index) {
									var isSelect = optionSelected === item.productCategoryId ? 'selected'
											: '';
									optionHtml += '<option data-value="'
											+ item.productCategoryId
											+ '"'
											+ isSelect
											+ '>'
											+ item.productCategoryName
											+ '</option>';
								});
						$('#product-category').html(optionHtml);
					}
				});
	};
	
	/**
	 * 提交按钮的响应时间,分别对商品添加和商品编辑做不同的相应
	 */
	$('#submit').click(
			function(){
				// 创建商品Json对象，并从表单对象中获取对应的属性值
				var product = {};
				
				// 如果是编辑操作,需要传入productId
				if(isEdit){
					product.productId = productId;
				}
				
				product.productName = $('#product-name').val();
				product.productDesc = $('#product-desc').val();
				
				// 获取商品的特定目录值
				product.productCategory = {
						productCategoryId : $('#product-category').find('option').not(
								function() {
									return !this.selected;
								}).data('value')
					};
				
				product.priority = $('#priority').val();
				product.normalPrice = $('#normal-price').val();
				product.promotionPrice = $('#promotion-price').val();
				
				// 生成表单对象用于接收参数并传递给后台
				var formData = new FormData();
				
				// 缩略图 （只有一张），获取缩略图的文件流
				var thumbnail = $('#small-img')[0].files[0];
				formData.append('thumbnail',thumbnail);
				
				// 遍历商品详情图控件，获取里面的文件流
				$('.detail-img').map(
						function(index, item) {
							// 判断该控件是否已经选择了文件	
							if ($('.detail-img')[index].files.length > 0) {
								// 将第i个文件流赋值给key为productImgi的表单键值对里
								formData.append('productImg' + index,
										$('.detail-img')[index].files[0]);
							}
						});
				// 将product 转换为json ,添加到forData
				formData.append('productStr', JSON.stringify(product));
				
				// 获取表单中的验证码
				var verifyCodeActual = $('#j_captcha').val();
				if (!verifyCodeActual) {
					$.toast('请输入验证码！');
					return;
				}
				formData.append("verifyCodeActual", verifyCodeActual);
				
				
				// 使用ajax异步提交
				$.ajax({
					url: productPostUrl,
					type: 'POST' ,
					data : formData,
					contentType : false,
					processData : false,
					cache : false,
					success: function(data){
						if (data.success) {
							$.toast('提交成功！');
							$('#captcha_img').click();
						} else {
							$.toast('提交失败！');
							$('#captcha_img').click();
						}
					}
				});
			});
});