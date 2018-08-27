var mapapikey = "R5LBZ-H73A6-BXGSK-MVENH-26RGJ-SKF4C";
var openId = "";
var title = "";
var des = "";
var link = "";
var imgUrl = "";
$(document).ready(function() {
	var appId = null;
	var timestamp = null;
	var nonceStr = null;
	var signature = null;
	$.ajax({
		type: "POST",
		//dataType : 'json',
		async: false,
		data: {
			url: window.location.href
		},
		url: globel.ctx + apiconfig.config_url + "?token=" + globel.token,
		success: function(data) {
			//alert("config")
			appId = "wx26b549095cfa8917";
			timestamp = data.timestamp;
			nonceStr = data.nonceStr;
			signature = data.signature;
			wx.config({
				debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
				appId: "wx26b549095cfa8917", // 必填，公众号的唯一标识
				timestamp: timestamp, // 必填，生成签名的时间戳
				nonceStr: nonceStr, // 必填，生成签名的随机串
				signature: signature, // 必填，签名，见附录1
				jsApiList: ['onMenuShareTimeline', 'onMenuShareAppMessage', 'getLocation', 'chooseImage', 'uploadImage', 'scanQRCode', 
				'translateVoice', 'hideMenuItems','startRecord','stopRecord','onVoiceRecordEnd','playVoice','stopVoice','onVoicePlayEnd','uploadVoice','chooseWXPay'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
			});
		}
	});

});

function GetQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if(r != null) return decodeURI(r[2]);
	return null;
}

function oauth() {
	var code = GetQueryString("code");
	if(code != "" && code != null && code != undefined) {
		$.ajax({
			type: 'POST',
			dataType: 'json',
			async: true,
			url: "/hk/common/getWxId",
			data: {
				code: code
			},
			success: function(data) {
				openId = data.openId;
				sessionStorage.openid = data.openId;
			}
		});
	}
}