sessionStorage.gz="1";
		var images = {
			localId: [],
			serverId: []
		};
		isIDCardOK = {cardface: false, cardback: false, cardperson: false};
		var token = GetQueryString('token');
		var role = $('#role').val();
		
		function canNextStep(){
			checkIsIDCardOK() ? document.getElementById('nextStep').disabled =false : document.getElementById('nextStep').disabled =true;
		}
		function checkIsIDCardOK(){
			return (isIDCardOK.cardface && isIDCardOK.cardback && isIDCardOK.cardperson)? true : false;
		}
		wx.ready(function(){
			
			//拍照或从手机相册中选图-正面
			document.getElementById('cardface').onclick = function(){
				wx.chooseImage({
				    count: 1, // 默认9
				    sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
				    sourceType: ['camera','album'], // 可以指定来源是相册还是相机，默认二者都有'album'
					success: function (res) {
				    	images.localId = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
//				    	$(".card_box img").height(img_h1-img_h2);
//				    	$(".watermark0").show();		    	
//				    	$(".watermark").height(img_h1-img_h2);
						document.getElementById("cardface").src=images.localId[0];  //图片预览
				    	//uploadImage();  //上传图片
						var i = 0, len = images.localId.length;
						function wxUpload(){
							wx.uploadImage({
								localId: images.localId[i], // 需要上传的图片的本地ID，由chooseImage接口获得
								isShowProgressTips: 1, // 默认为1，显示进度提示
								success: function (res) {
									i++;
									//将上传成功后的serverId保存到serverid
									images.serverId.push(res.serverId);
									downloadPic(res.serverId, "cardface");
									ocrRead("face",$('#cardfaceUrl').val(),cardfaceReadSuccess);
									canNextStep();
									if(i < len){
										wxUpload();
									}
								}
							});
						}
						wxUpload();
				    }
				});
			};
			document.getElementById('cardback').onclick = function(){
				wx.chooseImage({
				    count: 1, // 默认9
				    sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
				    sourceType: ['camera','album'], // 可以指定来源是相册还是相机，默认二者都有'album'
					success: function (res) {
				    	images.localId = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
//				    	$(".card_box img").height(img_h1-img_h2);
//				    	$(".watermark0").show();		    	
//				    	$(".watermark").height(img_h1-img_h2);
						document.getElementById("cardback").src=images.localId[0];  //图片预览
				    	//uploadImage();  //上传图片
						var i = 0, len = images.localId.length;
						function wxUpload(){
							wx.uploadImage({
								localId: images.localId[i], // 需要上传的图片的本地ID，由chooseImage接口获得
								isShowProgressTips: 1, // 默认为1，显示进度提示
								success: function (res) {
									i++;
									//将上传成功后的serverId保存到serverid
									images.serverId.push(res.serverId);
									downloadPic(res.serverId, "cardback");
									ocrRead("back",$('#cardbackUrl').val(),cardbackReadSuccess);
									canNextStep();
									if(i < len){
										wxUpload();
									}
								}
							});
						}
						wxUpload();
				    }
				});
			};
			document.getElementById('cardperson').onclick = function(){
				if(!(isIDCardOK.cardface && isIDCardOK.cardback)){
					//身份证正面和反面未成功上传
					mui.alert('请先上传身份证照片','','确定',null,'div'); 
					return;
				}
				wx.chooseImage({
				    count: 1, // 默认9
				    sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
				    sourceType: ['camera','album'], // 可以指定来源是相册还是相机，默认二者都有'album'
					success: function (res) {
				    	images.localId = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
//				    	$(".card_box img").height(img_h1-img_h2);
//				    	$(".watermark0").show();		    	
//				    	$(".watermark").height(img_h1-img_h2);
						document.getElementById("cardperson").src=images.localId[0];  //图片预览
				    	//uploadImage();  //上传图片
						var i = 0, len = images.localId.length;
						function wxUpload(){
							wx.uploadImage({
								localId: images.localId[i], // 需要上传的图片的本地ID，由chooseImage接口获得
								isShowProgressTips: 1, // 默认为1，显示进度提示
								success: function (res) {
									i++;
									//将上传成功后的serverId保存到serverid
									images.serverId.push(res.serverId);
									downloadPic(res.serverId, "cardperson");
									faceVerify();
									canNextStep();
									if(i < len){
										wxUpload();
									}
								}
							});
						}
						wxUpload();
				    }
				});
			};
			document.getElementById('headPic').onclick = function(){
				wx.chooseImage({
				    count: 1, // 默认9
				    sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
				    sourceType: ['camera','album'], // 可以指定来源是相册还是相机，默认二者都有'album'
					success: function (res) {
				    	images.localId = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
//				    	$(".card_box img").height(img_h1-img_h2);
//				    	$(".watermark0").show();		    	
//				    	$(".watermark").height(img_h1-img_h2);
						document.getElementById("headPic").src=images.localId[0];  //图片预览
				    	//uploadImage();  //上传图片
						var i = 0, len = images.localId.length;
						function wxUpload(){
							wx.uploadImage({
								localId: images.localId[i], // 需要上传的图片的本地ID，由chooseImage接口获得
								isShowProgressTips: 1, // 默认为1，显示进度提示
								success: function (res) {
									i++;
									//将上传成功后的serverId保存到serverid
									images.serverId.push(res.serverId);
									downloadPic(res.serverId, "headPic");
									canNextStep();
									if(i < len){
										wxUpload();
									}
								}
							});
						}
						wxUpload();
				    }
				});
			};

		});
		function faceVerify(){
			$.ajax({
				async : false,
				type : 'POST',
				url : "http://8e8948f8.ngrok.io/bbohu/f/api/idcard/face",
				data : {
					token     : token,
					face1Path : $('#cardfaceUrl').val(),
					face2Path: $('#cardpersonUrl').val()
				},
				//dataType : 'json',
				success: function(data){
					if(data.success){
						mui.alert('身份核实成功','','确定',null,'div'); 
						isIDCardOK.cardperson=true;
					}else{
						if(data.errorCode == '1'){
							mui.alert('请上传本人身份证','','确定',null,'div');
						}else{
							mui.alert(data.msg,'','确定',null,'div'); 
						}
						$('#cardface').attr('src','img/shenfenzhengzhengmian.png');
						$('#cardback').attr('src','img/shenfenzhengfanmian.png');
						$('#cardperson').attr('src','img/shouchi.png');
						isIDCardOK.cardface = false;
						isIDCardOK.cardback = false;
						isIDCardOK.cardperson = false;
					} 
				},
				error: function(XMLHttpRequest, textStatus, errorThrown){
					mui.alert('出现错误，请重试','','确定',null,'div'); 
					$('#cardperson').attr('src','img/shouchi.png');
				}
			});
		}

		function ocrRead(side,imgurl,fun){
			$.ajax({
				async : false,
				type : 'POST',
				url : "http://8e8948f8.ngrok.io/bbohu/f/api/idcard/ocr",
				data : {
					token: token,
					side : side,
					imagePath: imgurl
				},
				//dataType : 'json',
				success: function(data){
					fun(data);
				},
				error: function(XMLHttpRequest, textStatus, errorThrown){
					mui.alert('出现错误，请重试','','确定',null,'div'); 
				}
			});
		}
		//服务器下载微信拍照的照片
		function downloadPic(mediaId, src) {
			
			$.ajax({
				async : false,
				type : 'POST',
				url : globel.ctx+apiconfig.downloadMedia_url,
				data : {
					token   : token,
					mediaId : mediaId
				},
				//dataType : 'json',
				success: function(data){
					if (data.success) {
						$('#'+src+'Url').val(data.body.imageUrl);
					} else {
						mui.alert(data.msg,'','确定',null,'div');
					}
				},
				error: function(XMLHttpRequest, textStatus, errorThrown){
					 alert(XMLHttpRequest.status);
					 alert(XMLHttpRequest.readyState);
					 alert(textStatus);
				}
			});
		}
		function formatBirthday(birthday){
			if(birthday ==null || birthday ==''){
				return '';
			}
			return birthday.substr(0,4)+"-"+birthday.substr(4,2)+"-"+birthday.substr(-2,2);
		}
		function clearError(){
			$("#error-message").html(""); 
		}
		function cardfaceReadSuccess(data){
			if (data.success) {
				var info = data.body;
				if(info.sex == "女"){
					$('#female').attr("checked","checked");
				}else if(info.sex == "男"){
					$('#male').attr("checked","checked");
				}
				$('#birthday').val(formatBirthday(info.birth));
				$('#num').val(info.num);
				$('#name').val(info.name);
				$('#native').val(info.address);
				isIDCardOK.cardface = true;
				//mui.closePopups();
			} else {
				mui.alert(data.msg,'','确定',null,'div'); 
				$('#cardface').attr('src','img/shenfenzhengzhengmian.png');
			}
		}
		function cardbackReadSuccess(data){
			if (data.success) {
				if(data.body.available){
					isIDCardOK.cardback = true;
				}else{
					mui.alert('身份证已过期','','确定',null,'div'); 
					$('#cardback').attr('src','img/shenfenzhengfanmian.png');
				}
				//mui.closePopups();
			} else {
				mui.alert('身份证信息识别失败，请重新上传','','确定',null,'div'); 
				$('#cardback').attr('src','img/shenfenzhengfanmian.png');
			}
		}
		$('#nextStep').on('click',function(){
			var referee = $('#referee');
			var nickname = $('#nickname');
			if(referee.val().length==0){
				$("#error-message").html("<span class='icon font_family icon-gantanhao'></span>邀请人ID不能为空"); 
				return;
			}
			var reg = /^\d{6}$/;  
			if(!reg.test(referee.val())){
				$("#error-message").html("<span class='icon font_family icon-gantanhao'></span>邀请人ID格式不正确"); 
				return;
			}
			if(nickname.val().length==0){
				$("#error-message").html("<span class='icon font_family icon-gantanhao'></span>昵称不能为空"); 
				return;
			}
			var api = "save" + role +"Info";
			$.ajax({
				async : false,
				type : 'POST',
				url : globel.ctx+"f/api/"+api,
				data : {
					token      : token,
					role       : role,
					referee    : $('#referee').val(),
					nickname   : $('#nickname').val(),
					name       : $('#name').val(),
					idcard     : $('#num').val(),
					sex        : $('input[name="xingbie"]').val(),
					brithday   : $('#birthday').val(),
					nativePlace: $('#native').val(),
					height     : $('#height').val(),   
					weight     : $('#weight').val(),
					graduateSchool:$('#graduate').val(),
					degree     :$('#degree').val(),
					applyReason:$('input[name="reason"]').val(),
					address    : $('#address').val(),
					profession : $('#job').val(),
					hasExperence: $('input[name="jingyan"]').val(),
					platformcode: $('input[name="pingtai"]').val(),
					fansNum     : $('#fansNum').val(),
					intention   : $('input[name="fenlei"]').val(),
					videoLink    : $('#videoUrl').val(),
					individualDes: $('#personalDisc').val(),
					zIdcard : $('#cardfaceUrl').val(),
					fIdcard : $('#cardbackUrl').val(),
					sIdcard : $('#cardpersonUrl').val(),
					headPicture  :$('#headPicUrl').val()
				},
				//dataType : 'json',
				success: function(data){
					if (data.success) {
						//mui.alert("报名成功"'','确定',null,'div');
						window.location.assign(encodeURI("shimingrenzheng.html?role="+role+"&token="+token+"&name="+$('#name').val()+"&idNo="+$('#num').val()+"&id="+10000*Math.random()));
						//window.location.href="shimingrenzheng.html?name="+$('#name').val()+"&idNo="+$('#num').val()+"&id="+10000*Math.random();
					} else {
						if(data.errorCode == "1"){
							mui.alert("您已报名",'','确定',null,'div');
							if(url =="saveTalentInfo"){
								window.location.href="xingtanindex.html?id="+10000*Math.random();
							}else{
								window.location.href="zhuboindex.html?id="+10000*Math.random();
							}
						}else{
							mui.alert(data.msg,'','确定',null,'div');
						}
					}
				},
				error: function(XMLHttpRequest, textStatus, errorThrown){
					 alert(XMLHttpRequest.status);
					 alert(XMLHttpRequest.readyState);
					 alert(textStatus);
				}
			});
		})