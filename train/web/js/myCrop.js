$(function() {
	var screenWidth = $(window).width();
	var screenHeight = $(window).height();
	var Cropper = window.Cropper;
	var image =$('.img-container').find('img')[0];
	var options = {
		minContainerHeight: screenHeight,
		minContainerWidth: screenWidth,
		aspectRatio: 1 / 1, //裁剪框比例 1：1
		viewMode: 1, //显示
		guides: false, //裁剪框虚线 默认true有
		dragMode: "move",
		build: function(e) { //加载开始
			//可以放你的过渡 效果
		},
		built: function(e) { //加载完成
			$("#containerDiv").show();
			$("#imgEdit").show();
		},
		zoom: function(e) {
			console.log(e.type, e.detail.ratio);
		},
		background: true, // 容器是否显示网格背景
		movable: true, //是否能移动图片
		cropBoxMovable: true, //是否允许拖动裁剪框
		cropBoxResizable: true, //是否允许拖动 改变裁剪框大小
	};
	var cropper;

	var data = {
		method: 'getCroppedCanvas',
		option: {
			'width': 320,
			'height': 180
		},
		secondOption: ''
	};
	$('#imgCutConfirm').click(function() {
		var result = cropper[data.method](data.option, data.secondOption);
		if(result) {
			fileImg = result.toDataURL('image/jpg');
			var imageId=$("#imageId").val();
			$("#"+imageId).attr("src", fileImg);
			$("#containerDiv").hide();
			$("#imgEdit").hide();
			imageOpera(imageId);
		}
	});
	
	$('#imgCutCancel').click(function() {
		$("#containerDiv").hide();
		$("#imgEdit").hide();
	});
	
	$('#imgrotate').click(function() {
		//cropper('rotate', 90);
		var result = cropper['rotate'](90, data.secondOption);
	});
	
	$('#talentImgCut').click(function() {
		var result = cropper[data.method](data.option, data.secondOption);
		if(result) {
			fileImg = result.toDataURL('image/jpg');
			var imageId=$("#imageId").val();
			$("#"+imageId).attr("src", fileImg);
			$("#containerDiv").hide();
			$("#imgEdit").hide();
			imageOpera(imageId);
			$.ajax({
				type: 'POST',
				url: globel.ctx + apiconfig.saveTalentHeadPic_url,
				data: {
					token: globel.token,
					headPicture:$('#headPicUrl').val()
				},
				success: function(data) {
					if(data.success) {
						
					}else{
						mui.alert('头像更换失败！');
					}
				},
			});
		}
	});
	
		$('#anchorImgCut').click(function() {
		var result = cropper[data.method](data.option, data.secondOption);
		if(result) {
			fileImg = result.toDataURL('image/jpg');
			var imageId=$("#imageId").val();
			$("#"+imageId).attr("src", fileImg);
			$("#containerDiv").hide();
			$("#imgEdit").hide();
			imageOpera(imageId);
			$.ajax({
				type: 'POST',
				url: globel.ctx + apiconfig.saveAnchorHeadPic_url,
				data: {
					token: globel.token,
					headPicture:$('#headPicUrl').val()
				},
				success: function(data) {
					if(data.success) {
						
					}else{
						mui.alert('头像更换失败！');
					}
				},
			});
		}
	});

	var URL = window.URL || window.webkitURL;
	var blobURL;
	if(URL) {
		$('#inputImage').change(function(event) {
			var files = this.files;
			var file;
			if(cropper && files && files.length) {
				file = files[0];
				if(/^image\/\w+/.test(file.type)) {
					blobURL = URL.createObjectURL(file);
					console.log(blobURL);
					cropper.reset().replace(blobURL);
				} else {
					window.alert('Please choose an image file.');
				}
			}
			event.target.value='';
		});
	}

	 $(".showImg").each(function(){
	 	$(this).click(function(){
		 	 var id=$(this).attr("id");
		 	 $('#imageId').val(id); 
		 	 $('#inputImage').click();
		 	 if(id=='cardback'||id=='cardface'){
             	options.aspectRatio=1.5/1;
             }else if(id=='headPic'){
             	options.aspectRatio=1/1;
             }else if(id=='cardperson'){
             	options.aspectRatio=9/16;
             }
		 	 cropper=new Cropper(image, options);
	 	});
	 });

});