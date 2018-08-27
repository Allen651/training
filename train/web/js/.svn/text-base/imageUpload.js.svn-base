isIDCardOK = {
	cardface: false,
	cardback: false,
	cardperson: false
};
var token = globel.token;
var role = $('#role').val();
var faceFailNum = 0;//人脸识别失败次数
var facecheck =0;

//function canNextStep() {
//	checkIsIDCardOK() ? document.getElementById('nextStep').disabled = false : document.getElementById('nextStep').disabled = true;
//}
//
//function checkIsIDCardOK() {
//	return(isIDCardOK.cardface && isIDCardOK.cardback && isIDCardOK.cardperson) ? true : false;
//}

function faceVerify() {
	$.ajax({
		async: false,
		type: 'POST',
		url: globel.ctx + apiconfig.face_url,
		data: {
			token: token,
			face1Path: $('#cardfaceUrl').val(),
			face2Path: $('#cardpersonUrl').val()
		},
		//dataType : 'json',
		success: function(data) {
			if(data.success) {
				facecheck = 1;
				mui.alert('身份核实成功', '', '确定', null, 'div');
				isIDCardOK.cardperson = true;
			} else {
				faceFailNum++;
				if(faceFailNum==1){
					if(data.errorCode == '1') {
						mui.alert('身份对比失败，请确认照片是否为本人', '', '确定', null, 'div');
					} else {
						mui.alert(data.msg, '', '确定', null, 'div');
					}
					isIDCardOK.cardperson = false;
					$('#cardperson').attr('src', 'img/shouchishenfenzheng.png');
				}else{
					var btnArray = ['重新上传', '人工审核'];
					mui.confirm('身份对比失败，是否提交人工审核？','提示',btnArray,function(e){
						if (e.index == 1) {
							facecheck = 0;
							isIDCardOK.cardperson = true;
						}
						if (e.index == 0){
							isIDCardOK.cardperson = false;
							$('#cardperson').attr('src', 'img/shouchishenfenzheng.png');
						}
					});
				}
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			mui.toast('网络不给力，上传失败');
			//$('#cardback').attr('src', "img/shenfenzhengfanmian.png");
			//$('#cardface').attr('src', 'img/shenfenzhengzhengmian.png');
			$('#cardperson').attr('src', 'img/shouchishenfenzheng.png');
		}
	});
}

function ocrRead(side, imgurl, fun) {
	$.ajax({
		async: false,
		type: 'POST',
		url: globel.ctx + apiconfig.ocr_url,
		data: {
			token: token,
			side: side,
			imagePath: imgurl
		},
		//dataType : 'json',
		success: function(data) {
			fun(data);
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			mui.toast('网络不给力，上传失败');
			if("back" == side){
				$('#cardback').attr('src', 'img/shenfenzhengfanmian.png');
			}else if("face" == side){
				$('#cardface').attr('src', 'img/shenfenzhengzhengmian.png');
			}
		}
	});
}

function uploadPic(imageBase64, imageId) {
	$("#error-message").html("");
	$.ajax({
		async: false,
		type: 'POST',
		url: globel.ctx + apiconfig.uploadImage_url,
		data: {
			token: token,
			imageBase64: encodeURIComponent(imageBase64)
		},
		//dataType : 'json',
		success: function(data) {
			if(data.success) {
				$('#' + imageId + 'Url').val(data.body.imageUrl);
				if(imageId == 'cardback') {
					ocrRead("back", $('#cardbackUrl').val(), cardbackReadSuccess);
				} else if(imageId == 'cardface') {
					ocrRead("face", $('#cardfaceUrl').val(), cardfaceReadSuccess);
				} else if(imageId == 'cardperson') {
					faceVerify();
				}
				//canNextStep();
			} else {
				if(data.errorCode==1000){
					mui.alert('登录超时，请重新登录', '', function() {  
			            if("Anchor"==role){
							location=htmlconfig.anchor_login;
						}else{
							location=htmlconfig.talent_login;
						}
			        });  
				}else{
					mui.alert(data.msg, '', '确定', null, 'div');
				}
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			mui.toast('网络不给力，上传失败');
		}
	});
}

function formatBirthday(birthday) {
	if(birthday == null || birthday == '') {
		return '';
	}
	return birthday.substr(0, 4) + "-" + birthday.substr(4, 2) + "-" + birthday.substr(-2, 2);
}

function clearError() {
	$("#error-message").html("");
}

function cardfaceReadSuccess(data) {
	if(data.success) {
		var info = data.body;
		if(info.sex == "女") {
			$('#female').attr("checked", "checked");
		} else if(info.sex == "男") {
			$('#male').attr("checked", "checked");
		}
		$('#birthday').val(formatBirthday(info.birth));
		$('#num').val(info.num);
		$('#name').val(info.name);
		$('#native').val(info.address);
		isIDCardOK.cardface = true;
		//mui.closePopups();
	} else {
		mui.alert(data.msg, '', '确定', null, 'div');
		$('#cardface').attr('src', 'img/shenfenzhengzhengmian.png');
	}
}

function cardbackReadSuccess(data) {
	if(data.success) {
		if(data.body.available) {
			isIDCardOK.cardback = true;
		} else {
			mui.alert('身份证已过期', '', '确定', null, 'div');
			$('#cardback').attr('src', 'img/shenfenzhengfanmian.png');
		}
		//mui.closePopups();
	} else {
		mui.alert('身份证信息识别失败，请重新上传', '', '确定', null, 'div');
		$('#cardback').attr('src', 'img/shenfenzhengfanmian.png');
	}
}
$(function() {
	$('#nextStep').on('click', function() {
		var referee = $('#referee');
		var nickname = $('#nickname');
		var lifePhotoUrls;
		var reg = /^\d{6}$/;		
		if(referee.val().length == 0) {
			mui.toast("邀请人ID不能为空");
			return;
		}		
		if(!reg.test(referee.val())) {
			mui.toast("邀请人ID格式不正确");
			return;
		}
		if(nickname.val().length == 0) {
			mui.toast("昵称不能为空");
			return;
		}
		if(nickname.val().length > 20) {
			mui.toast("昵称不能超过20个字符");
			return;
		}
		if(isIDCardOK.cardface == false ){
			mui.toast("请上传身份证正面");
			return
		}
		if(isIDCardOK.cardback == false){
			mui.toast("请上传身份证反面照片");
			return;
		}
		if(isIDCardOK.cardperson==false){
			mui.toast("请上传手持身份证照片");
			return;
		}
		/*生活照start*/
		if("Anchor" == role){
			var livingPic = document.getElementsByName("file[]");
			if(livingPic.length == 0){
				mui.toast("请上传生活照");
				return;
			}
			for(var i=0; i<livingPic.length; i++){
				if(i == 0){
					lifePhotoUrls = livingPic[i].value;
				}else{
					lifePhotoUrls += "|"+ livingPic[i].value;
				}
			}
			//默认头像是第一张生活照
			if($('#headPicUrl').val() == ""){
				$('#headPicUrl').val(livingPic[livingPic.length-1].value);
			}
		}		/*生活照End*/				
		if($('#birthday').val().length == 0) {
			mui.toast("生日不能为空");
			return;
		}
		if($('#native').val().length == 0) {
			mui.toast("籍贯不能为空");
			return;
		}
		if($('#native').val().length > 50) {
			mui.toast("籍贯不能超过50个字符");
			return;
		}
		if("Anchor" == role){
			if($('#height').val().length == 0) {
				mui.toast("身高不能为空");
				return;
			}
			if($('#height').val().length > 20) {
				mui.toast("身高字符数过长");
				return;
			}
			if($('#weight').val().length == 0) {
				mui.toast("体重不能为空");
				return;
			}
			if($('#weight').val().length > 20) {
				mui.toast("体重字符数过长");
				return;
			}
			if($('#graduate').val().length == 0) {
				mui.toast("毕业院校不能为空");
				return;
			}
			if($('#graduate').val().length > 50) {
				mui.toast("毕业院校字符数过长");
				return;
			}
			if($('#degree').val().length == 0) {
				mui.toast("最高学历不能为空");
				return;
			}
			if($('#degree').val().length > 50) {
				mui.toast("学历字符数过长");
				return;
			}
		}
		if($('#job').val().length == 0) {
			mui.toast("职业不能为空");
			return;
		}
		if($('#job').val().length > 50) {
			mui.toast("职业字符数过长");
			return;
		}
		if($('#address').val().length == 0) {
			mui.toast("现住址不能为空");
			return;
		}
		if($('#address').val().length > 50) {
			mui.toast("现住址字符数过长");
			return;
		}		
		if("Anchor" == role){
			if($('input:radio[name="jingyan"]:checked').val()==0){
				$('#pingtai option:selected').val('');
				$('#fansNum').val('');
			}else{
				var reg2 = /^\d{0,9}$/;
				if($('#fansNum').val().length > 9) {
					mui.toast("粉丝量不能超过9个字符");
					return;
				}
				if(!reg2.test($('#fansNum').val())) {
					mui.toast("粉丝量格式不正确");
					return;
				}
			}
		}
		if("Anchor" == role){
			/*if($('#videoUrl').val().length == 0) {
				mui.toast("才艺视频链接不能为空");
				return;
			}*/
			if($('#videoUrl').val().length > 500) {
				mui.toast("才艺视频链接字符过长");
				return;
			}			
			
		}
 		if($('#personalDisc').val().length > 500) {
			mui.toast("个人描述不能超过500个字符");
			return;
		}
		var api = "save" + role + "Info";
		$.ajax({
			async: false,
			type: 'POST',
			url: globel.ctx + "f/api/" + api,
			data: {
				token: globel.token,
				facecheck:facecheck,
				role: role,
				referee: $('#referee').val(),
				nickname: $('#nickname').val(),
				name: $('#name').val(),
				idcard: $('#num').val(),
				sex: $('input:radio[name="xingbie"]:checked').val(),
				brithday: $('#birthday').val(),
				nativePlace: $('#native').val(),
				address: $('#address').val(),
				profession: $('#job').val(),
				hasExperence: $('input:radio[name="jingyan"]:checked').val(),
				platformcode: $('#pingtai option:selected').val(),
				fansNum: $('#fansNum').val(),
				intention: $('input:radio[name="fenlei"]:checked').val(),
				videoLink: $('#videoUrl').val(),
				individualDes: $('#personalDisc').val(),
				zIdcard: $('#cardfaceUrl').val(),
				fIdcard: $('#cardbackUrl').val(),
				sIdcard: $('#cardpersonUrl').val(),
				headPicture: $('#headPicUrl').val(),
				height: $('#height').val(),
				weight: $('#weight').val(),
				graduateSchool: $('#graduate').val(),
				degree: $('#degree').val(),
				lifePhoto: lifePhotoUrls,
				applyReason : $('#reason option:selected').val()
			},
			//dataType : 'json',
			success: function(data) {
				if(data.success) {
					//mui.alert("报名成功"'','确定',null,'div');
					window.location.assign(encodeURI(htmlconfig.certification+"?role=" + role + "&name=" + $('#name').val() + "&idNo=" + $('#num').val()));
					//window.location.href="shimingrenzheng.html?name="+$('#name').val()+"&idNo="+$('#num').val()+"&id="+10000*Math.random();
				} else {
					if(data.errorCode==1000){
						mui.alert('登录超时，请重新登录', '', function() {  
				            if("Anchor"==role){
								location=htmlconfig.anchor_login;
							}else{
								location=htmlconfig.talent_login;
							}
				        });  
					}else{
						if(data.errorCode == "1") {
							mui.alert("您已报名", '', '确定', null, 'div');
							if(role == "Talent") {
								window.location.href = htmlconfig.talent_index;
							} else {
								window.location.href = htmlconfig.anchor_index;
							}
						} else {
							mui.alert(data.msg, '', '确定', null, 'div');
						}
					}
				}
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				mui.toast('网络不给力，上传失败');
			}
		});
	});

});

function imageOpera(imageId) {
	var imageBase64 = $("#" + imageId).attr("src");
	uploadPic(imageBase64, imageId);
}