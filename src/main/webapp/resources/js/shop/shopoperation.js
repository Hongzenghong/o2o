/**
 * 
 */
$(function() {
	var shopId=getQueryString('shopId');
	var  isEdit=shopId?true:false;
	var initUrl = '/o2o/shopadmin/getshopinitinfo'
	var registerShopUrl = '/o2o/shopadmin/registershop';
	var shopInfoUrl="/o2o/shopadmin/getshopbyid?shopId="+shopId;
	var editShopUrl='/o2o/shopadmin/modifyshop';
	// alert(initUrl);
	if(!isEdit){
		getShopInitInfo();
	}else{
		getShopInfo(shopId);
	}
	
	
	function getShopInfo(shopId) {
		$.getJSON(shopInfoUrl, function(data) {
			if (data.success) {
			var shop=data.shop;
			$('#shop-name').val(shop.shopName);
			$('#shop-addr').val(shop.shopAddr);
			$('#shop-phone').val(shop.phone);
			$('#shop-desc').val(shop.shopDesc);
				var shopCategory= '<option data-id="'
							+ shop.shopCategory.shopCategoryId + '">' + shop.shopCategory.shopCategoryName
							+ '</option>';
				var tempShopAreaHtml ="";
				data.areaList.map(function(item, index) {
					tempShopAreaHtml += '<option data-id="' + item.areaId
							+ '">' + item.areaName + '</option>';
				});
				// 获取html中对应标签的id 赋值
				$('#shop-category').html(shopCategory);
				$('#shop-category').attr('disabled','disabled');
				$('#area').html(tempShopAreaHtml)
				//初始设置为后台对应的区域 
				$("#shop-area option[data-id='"+shop.area.areaId+"']").attr("selected","selected");
			}else{
				$.toast(data.errMsg);
			}
		});
	}
	/**
	 * 获取区域信息和店铺类别信息
	 */
	function getShopInitInfo() {
		$.getJSON(initUrl, function(data) {
			if (data.success) {
				var tempShopCategoryHtml = '';
				var tempShopAreaHtml = '';
				data.shopCategoryList.map(function(item, index) {
					tempShopCategoryHtml += '<option data-id="'
							+ item.shopCategoryId + '">' + item.shopCategoryName
							+ '</option>';
				});

				data.areaList.map(function(item, index) {
					tempShopAreaHtml += '<option data-id="' + item.areaId
							+ '">' + item.areaName + '</option>';
				});
				// 获取html中对应标签的id 赋值
				$('#shop-category').html(tempShopCategoryHtml);
				$('#area').html(tempShopAreaHtml)

			}else{
				$.toast(data.errMsg);
			}
		});
	}
		$('#submit').click(
				function() {
					alert(22222222);
					var shop = {};
					// 如果是编辑，需要传入shopId
					if(isEdit){
			               shop.shopId=shopId;
			            }
					shop.shopName = $('#shop-name').val();
					shop.shopAddr = $('#shop-addr').val();
					shop.phone = $('#shop-phone').val();
					shop.shopDesc = $('#shop-desc').val();
					// 选择id,双重否定=肯定
					shop.shopCategory = {
						// 这里定义的变量要和ShopCategory.shopCategoryId保持一致，否则使用databind转换会抛出异常
						shopCategoryId : $('#shop-category').find('option')
								.not(function() {
									return !this.selected;
								}).data('id')
					};
					shop.area = {
						areaId : $('#area').find('option').not(function() {
							return !this.selected;
						}).data('id')
					};
					var shopImg = $('#shop-img')[0].files[0];
					var formData = new FormData();
					formData.append('shopImg', shopImg);
					formData.append('shopStr', JSON.stringify(shop));
					var verifyCodeActual = $('#j_captcha').val();
					if (!verifyCodeActual) {
						$.toast("请输入验证码");
						return;
					}
					formData.append('verifyCodeActual', verifyCodeActual);
					$.ajax({
						url : (isEdit?editShopUrl:registerShopUrl),
						type : 'POST',
						data : formData,
						contentType : false,
						processData : false,
						cache : false,
						success : function(data) {
							if (data.success) {
								$.toast('提交成功！');
							} else {
								$.toast('提交失败！' + data.errMsg);
							}
							$('#captcha_img').click();
						}

					});
				});
	
	
})
