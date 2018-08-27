/*
 * 动态加载JS和CSS头文件
 * Author:wangqy
 * Date:2018-5-05 14:52
 */

var _cdn_web_path='/bbohu_Web';   //'http://cdn.bbohu.com';
var _cdn_admin_path='http://cdn2.bbohu.com';
var rootPath = getRootPath(); //项目路径

/**
 * 动态加载CSS和JS文件
 */
var dynamicLoading = {
    meta : function(){
        document.write('<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">');
    },
    css: function(path){
        if(!path || path.length === 0){
            throw new Error('argument "path" is required!');
        }
        document.write('<link rel="stylesheet" type="text/css" href="' +_cdn_web_path+path + '">');
    },
    js: function(path, charset){
        if(!path || path.length === 0){
            throw new Error('argument "path" is required!');
        }
        document.write('<script charset="' + (charset ? charset : "utf-8") + '" src="' +_cdn_web_path+path + '"></script>');
    }
};

/**
 * 取得项目路径
 */
function getRootPath() {
    //取得当前URL
    var path = window.document.location.href;
    //取得主机地址后的目录
    var pathName = window.document.location.pathname;
    var post = path.indexOf(pathName);
    //取得主机地址
    var hostPath = path.substring(0, post);
    //取得项目名
    var name = pathName.substring(0, pathName.substr(1).indexOf("/") + 1);
    return hostPath + name + "/";
}