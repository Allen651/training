/*登录注册切换*/
window.onload=function(){
	document.getElementsByClassName("regbtn")[0].onclick=function(){
		document.getElementsByClassName("mregister")[0].style.display="block";
		document.getElementsByClassName("mload")[0].style.display="none";
	}
	document.getElementsByClassName("loadbtn")[1].onclick=function(){
		document.getElementsByClassName("mregister")[0].style.display="none";
		document.getElementsByClassName("mload")[0].style.display="block";
	}	
	document.getElementsByClassName("load-btn3")[0].onclick=function(){
		document.getElementsByClassName("mregister")[0].style.display="block";
		document.getElementsByClassName("mload")[0].style.display="none";
	}
/*主体高度自适应屏幕*/
	autodivheight();
	function autodivheight(){ //函数：获取尺寸
    //获取浏览器窗口高度
    var winHeight=0;
    if (window.innerHeight)
        winHeight = window.innerHeight;
    else if ((document.body) && (document.body.clientHeight))
        winHeight = document.body.clientHeight;
    //通过深入Document内部对body进行检测，获取浏览器窗口高度
    if (document.documentElement && document.documentElement.clientHeight)
        winHeight = document.documentElement.clientHeight;
    //DIV高度为浏览器窗口的高度
    //document.getElementById("test").style.height= winHeight +"px";
    //DIV高度为浏览器窗口高度的一半
    /*document.getElementById("test").style.height= winHeight/2 +"px";*/
    document.getElementsByClassName("load-main")[0].style.height=winHeight*0.8+"px";
    document.getElementsByClassName("register-main")[0].style.height=winHeight*0.8+"px";
}
window.onresize=autodivheight; //浏览器窗口发生变化时同时变化DIV高度
}
/*登录注册表单验证*/
/*$.validator.setDefaults({
    submitHandler: function() {
      alert("提交事件!");
    }
});*/
//$().ready(function() {
//// 在键盘按下并释放及提交后验证提交表单
//  $("#load-form").validate({
//    rules: {
//      firstname: "required",
//      lastname: "required",
//      phone:"required",
//      username: {
//        required: true,
//        minlength: 2
//      },
//      password: {
//        required: true,
//        minlength: 5
//      },
//      confirm_password: {
//        required: true,
//        minlength: 5,
//        equalTo: "#password"
//      },
//      email: {
//        required: true,
//        email: true
//      },
//      topic: {
//        required: "#newsletter:checked",
//        minlength: 2
//      },
//      agree: "required"
//    },
//    errorPlacement: function(error, element) {
//			// Append error within linked label
//			$( element )
//				.closest( "form" )
//					.find( "#error-message" )
//						.append( error );
//		},
//    errorElement:"span",
//    messages: {
//      phone:'<svg class="iconfont" aria-hidden="true" style="width: 0.7em; height: 0.7em;vertical-align: -0.08em;margin-right:10px ;fill: currentColor;overflow: hidden;"><use xlink:href="#icon-gantanhao"></use></svg>用户名或密码不正确',
//      psd:"",
//      firstname: "请输入您的名字",
//      lastname: "请输入您的姓氏",
//      username: {
//        required: "请输入用户名",
//        minlength: "用户名必需由两个字母组成"
//      },
//      password: {
//        required: "请输入密码",
//        minlength: "密码长度不能小于 5 个字母"
//      },
//      confirm_password: {
//        required: "请输入密码",
//        minlength: "密码长度不能小于 5 个字母",
//        equalTo: "两次密码输入不一致"
//      },
//      email: "请输入一个正确的邮箱",
//      agree: "请接受我们的声明",
//      topic: "请选择两个主题"
//     }
//   });
//  $("#reg-form").validate({
//    rules: {
//      firstname: "required",
//      lastname: "required",
//      username: {
//        required: true,
//        minlength: 2
//      },
//      pnumber:"required",
//      yanzhengma:"required",
//      password: {
//        required: true,
//        minlength: 5
//      },
//      confirm_password: {
//        required: true,
//        minlength: 5,
//        equalTo: "#password"
//      },
//      email: {
//        required: true,
//        email: true
//      },
//      topic: {
//        required: "#newsletter:checked",
//        minlength: 2
//      },
//      agree: "required"
//    },
//    errorPlacement: function(error, element) {
//			// Append error within linked label
//			$( element ).parent().after(error);
//		},
//    errorElement:"span",
//    messages: {
//      firstname: "请输入您的名字",
//      lastname: "请输入您的姓氏",
//      username: {
//        required: "请输入用户名",
//        minlength: "用户名必需由两个字母组成"
//      },
//      pnumber:"请输入您的手机号",
//      yanzhengma:"请输入验证码",
//      password: {
//        required: "请输入密码",
//        minlength: "密码长度不能小于 5 个字母"
//      },
//      confirm_password: {
//        required: "请输入密码",
//        minlength: "密码长度不能小于 5 个字母",
//        equalTo: "两次密码输入不一致"
//      },
//      email: "请输入一个正确的邮箱",
//      agree: "请接受我们的声明",
//      topic: "请选择两个主题"
//     }
//    })
//});