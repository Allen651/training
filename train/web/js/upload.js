/**
 * author:gouguoyin
 * qq:245629560
 * doc:http://www.gouguoyin.cn/js/141.html
 */
(function($){
    $.fn.ajaxImageUpload = function(options){
        var defaults = {
            data: null,
            url: '',
            zoom: true,
            allowType: ["gif", "jpeg", "jpg", "bmp",'png'],
            maxNum: 10,
            hidenInputName: '', // 上传成功后追加的隐藏input名，注意不要带[]，会自动带[]，不写默认和上传按钮的name相同
            maxSize: 5, //设置允许上传图片的最大尺寸，单位M
            success: $.noop, //上传成功时的回调函数
            error: $.noop //上传失败时的回调函数
        };
        var thisObj = $(this);
        var config  = $.extend(defaults, options);

        var uploadBox = $(".upload-box");
        var imageBox  = $(".image-box");
        var inputName = thisObj.attr('name');
        // 设置是否在上传中全局变量
        isUploading  = false;

        thisObj.each(function(i){
            thisObj.change(function(event){
                handleFileSelect();
                event.target.value='';
            });
        });
        var handleFileSelect = function(){
            if (typeof FileReader == "undefined") {
                return false;
            }
            // 获取最新的section数量
            var imageNum  = $('.image-section').length;
            var postUrl   = config.url;
            var maxNum    = config.maxNum;
            var maxSize   = config.maxSize;
            var allowType = config.allowType;
            if(!postUrl){
                alert('请设置要上传的服务端地址');
                return false;
            }
            var files    = thisObj[0].files;
            if(imageNum + files.length > maxNum ){
                alert("上传图片数目不可以超过"+maxNum+"个");
                return;
            }
            for(var i = 0; i<files.length; i++){
            	var fileObj  = files[i];
	            if(!fileObj){
	                return false;
	            }
	            var fileName = fileObj.name;
	            var fileSize = (fileObj.size)/(1024*1024);
	            if (!isAllowFile(fileName, allowType)) {
	                alert("图片类型必须是" + allowType.join("，") + "中的一种");
	                return false;
	            }
	            if(fileSize > maxSize){
	                alert('上传图片不能超过' + maxSize + 'M，当前上传图片的大小为'+fileSize.toFixed(2) + 'M');
	                return false;
	            }
	            // 执行前置函数
	            var callback = config.before;
	            if(callback && callback() === false){
	                return false;
	            }
	            createImageSection();
	
	            ajaxUpload(fileObj,fileSize);
            }
        };
        var ajaxUpload = function (fileObj,fileSize) {
            // 获取最新的
            var imageSection = $('.image-section:first');
            var imageShow    = $('.image-show:first'); 
            var reader = new FileReader();
        	reader.readAsDataURL(fileObj);
        	reader.onload = function (e) { 
        		//formData.append('imageBase64',  encodeURIComponent(this.result)); 
        		dataURL = this.result;
        		if(fileSize > 1){
        			canvasDataURL(dataURL,{quality: 0.9,width : 500},upload)
        		}else{
        			//upload(dataURL)
        			canvasDataURL(dataURL,{quality: 1,width : 500},upload)
        		}
        	}
        	 var upload = function(dataURL){
				// ajax提交表单对象
			    $.ajax({
			        url: config.url,
			        type: "post",
			        data: {imageBase64: encodeURIComponent(dataURL),token:globel.token},
			        //processData: false,
			        //contentType: false,
					async:false,
			        success:function(json){
						if(json.success){
			                imageSection.removeClass("image-loading");
			                imageShow.removeClass("image-opcity");
			
			                imageShow.attr('src',globel.ctx+json.body.imageUrl);
			                imageShow.siblings('input').val(json.body.imageUrl);
			                /*图片自适应*/
			               setTimeout(function(){
			                   	var imgS=document.getElementsByClassName("image-show");
								for(var i=0;i<imgS.length;i++){
									var imgW=imgS[i].width;
									var imgH=imgS[i].height;			
									if(imgW>=imgH){
										imgS[i].style.width="auto";
										imgS[i].style.height="80px";
									}
									if(imgW<imgH){
										imgS[i].style.width="100%";
										imgS[i].style.height="auto";
									}
									imgS[i].style.marginLeft=-imgW/2+"px";
									imgS[i].style.marginTop=-imgH/2+"px";
								};
			               },200)
						}else{
							if(json.errorCode==1000){
								mui.alert('登录超时，请重新登录', '', function() {  
									location=htmlconfig.anchor_login;
						        });  
							}
						}
			        },
			        error:function(e){
			            imageSection.remove();
			            // 执行失败回调函数
			            var callback = config.error;
			            callback(e);
			            mui.toast('网络不给力，上传失败');
			        }
			    });
			}
        };
        var createDeleteModal = function () {
    	 imageBox.delegate(".image-delete","click",function(){
                // 声明全局变量
             	deleteImageSection = $(this).parent();
             	deleteImageSection.remove();
           });
        };
        var createImageSection = function () {
            var hidenInputName = config.hidenInputName;
            if(!hidenInputName){
                hidenInputName = inputName;
            }
            var imageSection = $("<section class='image-section image-loading'></section>");
//          var imageShade   = $("<div class='image-shade'></div>");
            var imageShow    = $("<img class='image-show image-opcity' />");
            var imageInput   = $("<input class='" + inputName + "' name='" + hidenInputName + "[]' value='' type='hidden'>");
//          var imageZoom    = $("<div class='image-zoom'></div>");
            var imageDelete  = $("<img class='image-delete' src='img/icon_shut@3x.png'/>");
            var showBox=$("<div class='show-box'></div>");
            imageBox.prepend(showBox);
            showBox.prepend(imageSection);
//          imageShade.appendTo(imageSection);
            imageDelete.appendTo(showBox);
            // 判断是否开启缩放功能
//          if(config.zoom && config.zoom === true ){
//              imageZoom.appendTo(imageSection);
//          }
            imageShow.appendTo(imageSection);
            imageInput.appendTo(imageSection);

            return imageSection;
        };
        var createImageZoom = function () {
            var zoomShade   = $("<div id='zoom-shade'></div>");
            var zoomBox = $("<div id='zoom-box'></div>");
            var zoomContent = $("<div id='zoom-content'><img src='http://www.jq22.com/demo/jqueryfancybox201707292345/example/4_b.jpg'></div>");
            uploadBox.append(zoomShade);
            uploadBox.append(zoomBox);
            zoomContent.appendTo(zoomBox);
            // 显示弹框
            imageBox.delegate(".image-show","click",function(){
				var vh=screen.height;				
                var src = $(this).attr('src');
                zoomBox.find('img').attr('src', src);
				zoomShade.css("height",vh);
                zoomShade.show();
                zoomBox.show();

            });
            // 关闭弹窗
            uploadBox.delegate("#zoom-shade","click",function(){
                zoomShade.hide();
                zoomBox.hide();
            }); 
            uploadBox.delegate("#zoom-box","click",function(){
                zoomShade.hide();
                zoomBox.hide();
            });
        };
        //获取上传文件的后缀名
        var getFileExt = function(fileName){
            if (!fileName) {
                return '';
            }
            var _index = fileName.lastIndexOf('.');
            if (_index < 1) {
                return '';
            }
            return fileName.substr(_index+1);
        };
        //是否是允许上传文件格式
        var isAllowFile = function(fileName, allowType){
            var fileExt = getFileExt(fileName).toLowerCase();
            if (!allowType) {
                allowType = ['jpg', 'jpeg', 'png', 'gif', 'bmp'];
            }
            if ($.inArray(fileExt, allowType) != -1) {
                return true;
            }
            return false;
        };
        // 判断是否开启缩放功能
        if(config.zoom && config.zoom === true ){
            createImageZoom();
        }
        createDeleteModal();
		//图片适应
//		var imgW=$(".image-show").width();
//		var imgH=$(".image-show").height();
//		if(imgW>imgH){
//			this.css({"width":"100%","height":"auto",})
//		}else{
//			this.css({"width":"auto","height":"100%",})
//		}
    };
})(jQuery);

function canvasDataURL(path, obj, callback){
    var img = new Image();
    img.src = path;
    img.onload = function(){
        var that = this;
        // 默认按比例压缩
        var w = that.width,
            h = that.height,
            scale = w / h;
        w = (w>obj.width?obj.width : w)|| w;
        h = w / scale;
        var quality = 0.7;  // 默认图片质量为0.7
        //生成canvas
        var canvas = document.createElement('canvas');
        var ctx = canvas.getContext('2d');
        // 创建属性节点
        var anw = document.createAttribute("width");
        anw.nodeValue = w;
        var anh = document.createAttribute("height");
        anh.nodeValue = h;
        canvas.setAttributeNode(anw);
        canvas.setAttributeNode(anh);
        ctx.drawImage(that, 0, 0, w, h);
        // 图像质量
        if(obj.quality && obj.quality <= 1 && obj.quality > 0){
            quality = obj.quality;
        }
        // quality值越小，所绘制出的图像越模糊
        var base64 = canvas.toDataURL('image/jpeg', quality);
        // 回调函数返回base64的值
        callback(base64);
    }
}


