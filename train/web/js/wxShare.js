window.share_config = {
	"share": {
		"imgUrl": "http://localhost/share.png", //分享图
		"desc": "你对页面的描述", //摘要
		"title": '分享卡片的标题', //标题
		"link": window.location.href, //分享后链接
		"success": function() { //分享成功后的回调函数
		},
		 'cancel': function() { // 用户取消分享后执行的回调函数
		}
	}
};
wx.ready(function() {
	wx.onMenuShareAppMessage(share_config.share); //分享给好友
	wx.onMenuShareTimeline(share_config.share); //分享到朋友圈
	wx.onMenuShareQQ(share_config.share); //分享给手机QQ
	wx.onMenuShareWeibo(share_config.share);//分享到微博
    wx.onMenuShareQZone(share_config.share);//分享到QQ空间
});