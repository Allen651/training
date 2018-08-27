var id = GetQueryString('id');
var vm = new Vue({
	el: '.anchorinfo',
	data: {
		anchor: "",
		imgs: [],
		chenghao:"",
		flag:"",
		weixinNumber:"-主人隐藏啦-",
		mobile:"-主人隐藏啦-",
		luyinId:"",
		desc:"-请添加个人描述-"
	},
	created: function() {
		this.zhan();
		this.luyin();
		this.followStatus();
		
	},
	filters: {
		urlFilter: function(value) {
			if(!value) return ''
			value = value.toString()
			return _cdn_admin_path + value
		},
		platformFilter: function(value){
			return getPlatformLabel(value);
		}
	},
	methods: {
		zhan:function(){
			var _self = this;
			$.ajax({
				type: 'get',
				url: globel.ctx + "f/api/findAnchorInfo",
				data: {
					token: globel.token,
					userid:id
				},
				dataType: 'json',
				success: function(json) {
					if(json.success) {
						_self.anchor = json.body.anchorinfo;
						_self.imgs = json.body.imgs;
						_self.chenghao = json.body.chenghao;
						var pic = json.body.anchorinfo.headPicture;
						_self.luyinId = json.body.luyin.soundRecordId;
						/*alert(_self.luyinId);*/
						_self.flag = json.body.flag;
						if(pic){
	            			$("#headPic").attr("src",_cdn_admin_path+pic)
	            		}else{
	            			$("#headPic").attr("src","img/morentouxiang.png")
	            		};
	            		
	            		if (_self.anchor.isDisplayWenXin == null ) {
	            			/*vm.weixinNumber = vm.anchor.weixinNumber;*/ //615xiu
	            			} else{
	            		};
	            		
	            		setTimeout(function(){
	            			if(json.body.flag == 0){ //0为别人
	            			$("#edit1122").hide();
	            			$(".begbring button").show();
	            			$("#lu").hide();
	            		}else{
	            			$("#guan").hide();
	            			vm.weixinNumber = vm.anchor.weixinNumber;
	            			vm.mobile = vm.anchor.mobile;
	            		}
	            		},300);
	            		
	            		setTimeout(function() {
								var winHeight = $(window).height() || $(document).height() || $(document.body).outerHeight(true);
								var winWidth = $(window).width() || $(document).width() || $(document.body).outerWidth(true);
								for(var i = 0; i < $(".smallImg img").length; i++) {
									var imgW = $(".smallImg img").eq(i).css("width");
									var imgH = $(".smallImg img").eq(i).css("height");
									var boxw = $(".smallImg").css("width");
									var boxh = $(".smallImg").css("height");
									var scale = boxw / boxh;
									if(imgW / imgH > scale) {
										$(".smallImg img").eq(i).css({
											"width": "100%",
											"height": "auto"
										});
									} else {
										$(".smallImg img").eq(i).css({
											"width": "auto",
											"height": "100%"
										});
									}
								}
								$(".visual img").css({
									"width": winWidth,
									/*"height":"auto",*/
									"margin": "auto"
								});
								$(".smallImg").on("click", function() {
									var nowpage = $(this).index();
									var mark = document.getElementById("mark");
									var shezhihead= document.getElementById("shezhihead");
									window.onclick = function(event) {
										if(event.target == mark) {
											mark.style.display = "none";
										}
									}
									//图片滑动
									$('#pbSlider').pbTouchSlider({
										slider_Wrap: '#pbSliderWrap',
										slider_Threshold: 25,
										slider_Speed: 200,
										nowpage: nowpage,
										slider_Ease: 'linear',
										slider_Dots: {
											class: '.slider-pagination',
											enabled: true
										},
										slider_Breakpoints: {
											default: {
												height:'auto',
											},
											tablet: {
												height: 'auto',
												media: 1024
											},
											smartphone: {
												height: 'auto',
												media: 768
											}
										}
									});
									$("#mark").show();								
								})
								$("#mark").on("click", function() {
									$("#mark").hide();
								})
							}, 500);       		
	            		
	            		
					}
				}
			});
		},
		shezhihead:function(img){
			$.ajax({
				type: 'post',
				url: globel.ctx + "f/api/saveAnchorHeadPic",
				data: {
					token: globel.token,
					headPicture:img
				},
				dataType: 'json',
				success: function(res) {
						mui.toast("保存头像成功!");
					}
				});
		},
		donghua:function(){
			var duration = 2000; /*延时=录音时长-1000ms*/
			$(".record").on("click", function() {
				$(".play_warp").animate({
					"width": "33px",
					"height": "33px",
					"top": "-1px"
				});
				$(".play_warp").animate({
					"width": "26px",
					"height": "26px",
					"top": "1px"
				}, 400).animate({
					"width": "30px",
					"height": "30px",
					"top": "0px"
				}, 400);
				var recordPlays = setInterval(function recordPlay() {
					$(".play_warp").animate({
						"width": "33px",
						"height": "33px",
						"top": "-1px"
					});
					$(".play_warp").animate({
						"width": "26px",
						"height": "26px",
						"top": "1px"
					}, 400).animate({
						"width": "30px",
						"height": "30px",
						"top": "0px"
					}, 400);
				}, 1000);
				setTimeout(function() {
					/*停止动画*/
					$(".play_warp").animate({
						"width": "33px",
						"height": "33px",
						"top": "-1px"
					});
					clearInterval(recordPlays);
				}, duration);
			});
			vm.play();
		},
		play:function(){
			/*document.getElementById('musicfx').play();*/
			function audioAutoPlay() {
				var audio = document.getElementById('musicfx'),
				play = function() {
					audio.play();
					document.removeEventListener("touchstart", play, false);
				};
				audio.play();
				document.addEventListener("WeixinJSBridgeReady", function() {
					audio.play();
				}, false);
				document.addEventListener('YixinJSBridgeReady', function() {
					audio.play();
				}, false);
				document.addEventListener("touchstart", play, false);
				/*解决ios只播放一次的问题*/
				audio.onended = function () {
		            audio.load();
		            audio.play();
		            audio.pause();
		        }
			}
			audioAutoPlay('musicfx');
		},
		shangchuan:function(){
			$("#upload-input").ajaxImageUpload({
				url: globel.ctx + apiconfig.uploadImage_url, //上传的服务器地址
				maxNum: 6, //允许上传图片数量
				zoom: true, //允许上传图片点击放大
				allowType: ["gif", "jpeg", "jpg", "bmp", 'png'], //允许上传图片的类型
				maxSize: 10, //允许上传图片的最大尺寸，单位M
				before: function() {
					/*alert('上传前回调函数');*/
				},
				success: function(data) {
				/*	alert('上传成功回调函数');*/
					console.log(data);
				},
				error: function(e) {
						/*alert('上传失败回调函数');*/
					console.log(e);
				}
			});
		},
		edit:function(){
			mui.toast("请进行编辑");
			$("#save").show();
			$("#save button").show();
			$(".edit").removeAttr("disabled");
			$('.edit').css('color','#1D1D1D');
			vm.weixinNumber = vm.anchor.weixinNumber;
			$(".upload-box").show();
			/*if (vm.imgs.length>=6) {
				$(".upload-box").hide();
			} else{
				$(".upload-box").show();
			}*/
			
		},
		save:function(){
			/*var data = {}; 
			data.weixin = $("#weixin").val();
			data.fansAmount = $("#fansAmount").val();
			data.personalDesc = $("#desc").val();
			data.roomurl = $("#roomurl").val();
			data.nickname = $("#nickname").val();
			data.address = $("#address").val();
			data.profession = $("#profession").val();
			alert(data.nickname);*/
			var weixin = $("#weixin").val();
			var fansAmount = $("#fansAmount").val();
			if(isNaN(fansAmount)){
				return mui.toast("粉丝数量请输入数字");
			}
			var desc = $("#desc").val();
			var roomurl = $("#roomurl").val();
			var nickname = $("#nickname").val();
			var address = $("#address").val();
			var profession = $("#profession").val();
			
			var lifePhotoUrls;
			var photoUrls;
			/*生活照start*/
			var livingPic = document.getElementsByName("file[]");
			var yuanyou =  document.getElementsByClassName("yuan");
			for(var i=0; i<yuanyou.length; i++){
				if (i==0) {
					/*photoUrls = yuanyou[i].src.substr(yuanyou[i].src.indexOf('bbohu/') + 5)*/
					photoUrls = yuanyou[i].src.substr(yuanyou[i].src.indexOf('com/')+3)
				} else{
					photoUrls += '|'+ yuanyou[i].src.substr(yuanyou[i].src.indexOf('com/')+3)
				}
				
				/*alert(yuanyou[i].src.substr(yuanyou[i].src.indexOf('com/') + 3));*/
			}
			/*console.log(livingPic)
			var photo = photoUrls.split("|");
			if(livingPic.length + photo.length  > 6 ){
				mui.toast("生活照最多6张");
				return;
			}*/
			if(livingPic.length != 0){
				for(var i=0; i<livingPic.length; i++){
					if(i == 0){
						lifePhotoUrls = livingPic[i].value+"|"+photoUrls;
					}else{
						lifePhotoUrls += "|"+ livingPic[i].value;
					}
				}
			}else{
				lifePhotoUrls = photoUrls;
			}
			
		/*生活照End*/	
			
			
			$.ajax({
			type: 'post',
			url: globel.ctx + "f/api/editAnchorInfo111",
			data: {
				token: globel.token,
				weixinNumber:weixin,
				fansAmount:fansAmount,
				personalDesc:desc,
				roomurl:roomurl,
				nickname:nickname,
				address:address,
				profession:profession,
				lifePhoto: lifePhotoUrls
			},
			dataType: 'json',
			success: function(json) {
				if(json.success) {
					vm.anchor = json.body.anchorinfo;
					$(".image-show").hide();
					/*$(".upload-section").hide();*/
					$(".show-box").remove();
					/*$(".upload-section").hide();*/
					$(".upload-box").hide();
					mui.toast("保存成功");
					vm.zhan();
				}
				$('.edit').css('color','#999999')
				$("#save").hide();
				$('.edit').attr("disabled",true); 
				vm.weixinNumber = vm.anchor.weixinNumber;
				}
			});
		},
		guanzhu:function(status){
			
			if(status == 0){
				$("#yiguan").hide();
				$("#guan").show();
			}else{
				$("#yiguan").show();
				$("#guan").hide();
			}
				$.ajax({
					type: 'post',
					url: globel.ctx + "f/api/saveMyFollow",
					data: {
						token: globel.token,
						followeduser:id,
						status:status
					},
					dataType: 'json',
					success: function(res) {
						}
					});
			
		},
		/*关注状态*/
		followStatus:function(){
			$.ajax({
				type: 'post',
				async: false,
				url: globel.ctx + "f/api/isfollowStatus",
				data: {
					token: globel.token,
					followeduser:id
				},
				dataType: 'json',
				success: function(res) {
						setTimeout(function(){
							if (res.body.status == 1) {
								$("#yiguan").show();
								$("#guan").hide();
							} else{
								$("#yiguan").hide();
								$("#guan").show();
							}
						},100)
					}
				});
		},
		gototape:function(){
			window.location.href="anchor_personal_details_tape.html";
		},
		luyin:function(){
			_self = this;
			wx.ready(function(){
				
				//返回音频的本地ID  
				var localId;  
				//返回音频的服务器端ID  
				var serverId;  
				//录音计时,小于指定秒数(minTime = 10)则设置用户未录音  
				var startTime , endTime , minTime = 2;  
				
				//开始录音
				$('.start_btn').on('touchstart', function(event){
			    event.preventDefault();
			    START = new Date().getTime();
			    /*计时*/
			   	var sum=0;
			   	$(".times").html(sum);
				times=setInterval(function(){					
					sum=sum+1;
					$(".times").html(sum);
				},1000)
			    recordTimer = setTimeout(function(){
			        wx.startRecord({
			            success: function(){
			     	           localStorage.rainAllowRecord = 'true';
			            },
			            cancel: function () {
			                alert('用户拒绝授权录音');
			            }
			        });
			    },300);
			    return times;
			});
			//松手结束录音
			$('.start_btn').on('touchend', function(event){
			    event.preventDefault();
			    END = new Date().getTime();
			    /*清除计时*/
			    clearInterval(times);
			    if((END - START) < 300){
			        END = 0;
			        START = 0;
			        //小于300ms，不录音
			        clearTimeout(recordTimer);
			    }else{
			        wx.stopRecord({
			          success: function (res) {
			           localId  = res.localId;
			          //录音结束往后台传送数据 保存到本地
			           /*uploadVoice(localId);*/
			          $(".yylz .foot button").css('background-color','#2e2e2b')
			          mui.toast("录音成功!");
			          },
			          fail: function (res) {
			            alert("录音失败,请重新录制!");
			          }
			        });
			    }
			});
			
			
			//点击重置
			$('#chongzhi').on('click',function(){  
		      clearInterval(times);
		      mui.toast("重置成功!");
		    }); 
			
			
		 //上传语音接口  
		    $('#send_btn').on('click',function(){  
		        if(!localId){  
		            alert('您还未录音，请录音后再保存');  
		            return;  
		        }  
		          
		       /* alert('上传语音,测试，并未提交保存');  
		        return;  */
		          
		        //上传语音接口  
		        uploadVoice(localId);
		    }); 
			function uploadVoice(localId){
				//上传语音接口  
		        wx.uploadVoice({  
		            //需要上传的音频的本地ID，由 stopRecord 或 onVoiceRecordEnd 接口获得  
		            localId: localId,   
		            //默认为1，显示进度提示  
		            isShowProgressTips: 1,  
		            success: function (res) {  
		                //返回音频的服务器端ID  
		                /*alert('微信上传成功')*/
		                serverId = res.serverId;  
			           downloadPic(serverId);
		            }  
		        });
		        
				}
				
				function downloadPic(mediaId) {
					/*alert('下载语音')*/
					/*alert(mediaId);*/
				$('#mediaId').val(mediaId)
				$.ajax({
			        url: globel.ctx + "f/api/saveSoundRecording",
			        type: 'get',
			        data: { 
			        	token: globel.token,
			        	mediaId:mediaId
			        },
			        dataType: "json",
			        success: function (res) {
			        	if(res.success){
			        		 mui.toast('恭喜您!录音保存成功!');
			        		 vm.zhan();
			        		 /* location = "anchor_personal_details.html?id="+id;*/
			           //vm.luyinId = res.body.anchorinfo.soundRecordId;
			            /* alert("从后台拿到的本地id:"+res.body.anchorinfo.soundRecordId+"-----");*/
			        	}else{
			        		mui.alert(res.msg)
			        	}
			        },
			        error: function (xhr, errorType, error) {
			            console.log(error);
			        }
			    	});
			    }
			});
			
			
			
		}
		
		
		
	}
});

Vue.filter('replace', function(input) {
	if(input != null) {
		/*return _cdn_admin_path + input.substr(input.indexOf('/') + 6);*/
		return _cdn_admin_path + input;
	}
});
