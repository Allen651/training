/*身份选择主播选择*/
var zb = document.getElementById("imzhubo");
var xt = document.getElementById("imxingtan");
var dh1 = document.getElementsByClassName("duihao1")[0];
var dh2 = document.getElementsByClassName("duihao2")[0];
var sub = document.getElementsByClassName("sfxzSubmit")[0];
//var state =document.getElementById("state");
var zhubo = true;

function wszb() {
	zb.style.backgroundImage = "url(img/woshizhubo1.png)";
	zb.classList.add("active");
	dh1.style.display = "block";
	xt.style.backgroundImage = "url(img/woshixingtan.png)";
	xt.classList.remove("active");
	dh2.style.display = "none";
	//state.innerHTML='<input id="state" name="state" type="hidden" value="1" />';
	zhubo = true;
	return zhubo;
};

function wsxt() {
	xt.style.backgroundImage = "url(img/woshixingtan1.png)";
	xt.classList.add("active");
	dh2.style.display = "block";
	zb.style.backgroundImage = "url(img/woshizhubo.png)";
	zb.classList.remove("active");
	dh1.style.display = "none";
	//state.innerHTML='<input id="state" name="state" type="hidden" value="2" />';
	zhubo = false;
	return zhubo;
};

//function sfxz() {
//	if(zhubo) {
//		window.location.href = "zhuboindex.html";
//	} else {
//		window.location.href = "xingtanindex.html";
//	}
//}
/*主播首页tab切换*/
var tableft = document.getElementsByClassName("tableft");
var tabright = document.getElementsByClassName("tabright");
var tabcontentl = document.getElementsByClassName("tabcontentl");
var tabcontentr = document.getElementsByClassName("tabcontentr");
tl = function() {
	tabcontentl[0].style.display = "block";
	tabcontentr[0].style.display = "none";
	tabright[0].style.color = "#c0c0c0";
	tableft[0].style.color = "#333";
	tableft[0].classList.add("active");
	tabright[0].classList.remove("active");
	imgCenter();
};
tr = function() {
	tabcontentr[0].style.display = "block";
	tabcontentl[0].style.display = "none";
	tabright[0].style.color = "#333";
	tableft[0].style.color = "#c0c0c0";
	tabright[0].classList.add("active");
	tableft[0].classList.remove("active");
	imgCenter();
};
tl1 = function() {
	tabcontentl[1].style.display = "block";
	tabcontentr[1].style.display = "none";
	tabright[1].style.color = "#c0c0c0";
	tableft[1].style.color = "#333";
	tableft[1].classList.add("active");
	tabright[1].classList.remove("active");
	imgCenter();
};
tr1 = function() {
	tabcontentr[1].style.display = "block";
	tabcontentl[1].style.display = "none";
	tabright[1].style.color = "#333";
	tableft[1].style.color = "#c0c0c0";
	tabright[1].classList.add("active");
	tableft[1].classList.remove("active");
	imgCenter();
};
/*排行榜tab切换*/
var first = document.getElementsByClassName("first")[0];
var second = document.getElementsByClassName("second")[0];
var third = document.getElementsByClassName("third")[0];
var firstlist = document.getElementsByClassName("first-list")[0];
var secondlist = document.getElementsByClassName("second-list")[0];
var thirdlist = document.getElementsByClassName("third-list")[0];
f1 = function() {
	first.classList.add("active");
	second.classList.remove("active");
	third.classList.remove("active");
	firstlist.style.display = "block";
	secondlist.style.display = "none";
	thirdlist.style.display = "none";
}
f2 = function() {
	second.classList.add("active");
	first.classList.remove("active");
	third.classList.remove("active");
	secondlist.style.display = "block";
	firstlist.style.display = "none";
	thirdlist.style.display = "none";
}
f3 = function() {
	third.classList.add("active");
	first.classList.remove("active");
	second.classList.remove("active");
	thirdlist.style.display = "block";
	firstlist.style.display = "none";
	secondlist.style.display = "none";
};
/*后退一步*/
back = function() {
	window.history.back(-1);
}
/*招募令*/
/*屏幕高度自适应*/
/*mui底部选项卡修复*/
mui('body').on('tap', 'a', function() {
	document.location.href = this.href;
});
mui('body').on('click', 'a', function() {
	document.location.href = this.href;
});
/*轮播*/
//var gallery = mui('.mui-slider');
//gallery.slider({
//	interval: 5000 //自动轮播周期，若为0则不自动播放，默认为0；
//});
//var gallery = mui('.mui-slider');
//gallery.slider().gotoItem(index);//跳转到第index张图片，index从0开始；\n
function autodivheight(box) { //函数：获取尺寸
	//获取浏览器窗口高度
	var winHeight = window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight;
	box.style.height = winHeight + "px";
}

function mmark() {
	var rili = document.getElementsByClassName("rili")[0];
	var box = document.getElementById("markbox");
	var xzk = document.getElementById("xuezekuang");
	window.onresize = autodivheight(box);
	rili.onclick = function() {
		box.style.display = "inline-block";
		$(document).on("touchmove", function(e) {
			e.preventDefault();
		})
		var ua = window.navigator.userAgent.toLowerCase();
		if(ua.match(/MicroMessenger/i) == 'micromessenger') {
			$("#xuanzekuang").css("top", "36px");
			$("#markbox .sanjiao").css("top", "25px");
		}
	}
	var markbox = document.getElementById("markbox");
	window.onclick = function(event) {
		if(event.target == markbox) {
			markbox.style.display = "none";
		}
	}
	/*$("body,html").css({"overflow":"hidden"});*/

}

function getIndex() {
	document.getElementById("markbox").style.display = "none";
	var body = document.getElementsByTagName("body")[0];
	body.style.overflow = "visible";
	return this.index;
}

function paiHangBang() {
	$("ul li:first-child>span:first-child").after("<svg class='icon mui-col-xs-1' aria-hidden='true'><use xlink:href='#icon-paihang_guanjun'></use></svg>");
	$("ul li:first-child>span:first-child ").addClass("diyi");
	$("ul li:nth-child(2)>span:first-child").after("<svg class='icon mui-col-xs-1' aria-hidden='true'><use xlink:href='#icon-paihang_yajun'></use></svg>");
	$("ul li:nth-child(2)>span:first-child").addClass("dier");
	$("ul li:nth-child(3)>span:first-child").after("<svg class='icon mui-col-xs-1' aria-hidden='true'><use xlink:href='#icon-paihang_jijun'></use></svg>");
	$("ul li:nth-child(3)>span:first-child").addClass("dier");
}

function mymark() {
	var mark = document.getElementById("mymark");
	window.onresize = autodivheight(mark);
	mark.style.display = "inline-block";
	mark.onclick = function() {
		mark.style.display = "none";
	}
}
/*判断微信浏览器*/
function is_weixin() {
	var ua = window.navigator.userAgent.toLowerCase();
	if(ua.match(/MicroMessenger/i) == 'micromessenger') {
		$("header").hide();
		$(".mui-content").css("padding-top", "0");
	}
}
//window.onload=is_weixin();
/*图片视频适应居中*/
function imgCenter() {
	/*图片循环*/
	for(var i = 0; i < $("img").length; i++) {
		var img = $("img").eq(i);
		var imgp = img.parent();
		var boxh = imgp.height();
		var boxw = imgp.width();
		var imgh = img.height();
		var imgw = img.width();
		if(imgp.hasClass("imgbox") || imgp.hasClass("videobox") || imgp.hasClass("touxiang")) {
			if(imgh / imgw > boxh / boxw) {
				img.width("100%");
				img.height("auto");
			} else {
				img.width("auto");
				img.height("100%");
			}
		}

	}
}
function imgCenterByObj(obj) {
	var img = $(obj);
	var imgp = img.parent();
	var boxh = imgp.height();
	var boxw = imgp.width();
	var imgh = img.height();
	var imgw = img.width();
	if(imgh / imgw > boxh / boxw) {
		img.width("100%");
		img.height("auto");
	} else {
		img.width("auto");
		img.height("100%");
	}
}
function mytab(){
	$(".my-tab div").on("click", function() {
	$(this).addClass("active").siblings().removeClass("active");
	$(".content").eq($(this).index()).show().siblings().hide();
})
}
function toptab(){
	$(".toptab div").on("click", function() {
	$(this).addClass("active").siblings().removeClass("active");
	$(".content").eq($(this).index()).show().siblings().hide();
})
}
