<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>视频管理管理</title>
	<meta name="decorator" content="default"/>
	<script src="${ctxStatic}/aliyun-video/es6-promise.min.js"></script>
    <script src="${ctxStatic}/aliyun-video/aliyun-oss-sdk.min.js"></script>
    <script src="${ctxStatic}/aliyun-video/aliyun-upload-sdk-1.3.0.min.js"></script>
    <link rel="stylesheet" href="${ctxStatic}/layui-v2.2.5/layui/css/layui.css" />
    <script src="${ctxStatic}/layui-v2.2.5/layui/layui.js"></script>
    <script>
		//注意进度条依赖 element 模块，否则无法进行正常渲染和功能性操作
		
		
	</script>
	<script type="text/javascript">
	function GetQueryString(name){
	     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
	     var r = window.location.search.substr(1).match(reg);
	     if(r!=null)return  decodeURI(r[2]); 
	     return null;
	}
	
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  $("#inputForm").submit();
			  return true;
		  }
	
		  return false;
		}
		
		$(document).ready(function() {
			var up = GetQueryString("up");
			if("false" == up){
				$('#uVideo').hide();
				$('#uImage').hide();
			}
			
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
			
		});
	</script>
	
</head>
<body class="hideScroll">
		
   
    
		<form:form id="inputForm" modelAttribute="video" action="${ctx}/course/video/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		
    	
    	
   		<!--  <button type="button" onclick="stop()">停止上传</button> -->
   		 <!-- <button type="button" onclick="resumeWithToken()">token过期恢复上传</button> -->
		
		
		
		
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		   		<tr id="uVideo">
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>视频上传：</label></td>
					<td class="width-35">
						<input type="file" name="file" id="files"/>
					</td>
					<td class="width-15 active"><label class="pull-right"></label></td>
					<td class="width-35">
						<input type="button" id="start" value="开始上传" />
					</td>
				</tr>
				<tr id="uImage">
					<td class="width-15 active"><label class="pull-right">封面上传：</label></td>
					<td class="width-35">
						<input type="file" name="image" id="image"/>
					</td>
					<td class="width-15 active"><label class="pull-right"></label></td>
					<td class="width-35">
						<input type="button" id="startImage" value="开始上传" />
					</td>
				</tr>
				<tr id="proTr" style="display: none">
					<td class="width-15 active"><label class="pull-right">上传进度：</td>
					<td colspan = 3>
						 <div class="layui-progress" lay-showPercent="true" lay-filter="demo">
						  <div class="layui-progress-bar" id="progress" lay-percent="0" ></div>
						</div>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">视频名：</label></td>
					<td class="width-35">
						<form:input path="videoName" id="videoName" htmlEscape="false" maxlength="100" readonly="true"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right">视频托管id：</label></td>
					<td class="width-35">
						<form:input path="videoId" id="videoId" htmlEscape="false"  readonly="true" class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">封面图片地址：</label></td>
					<td class="width-35">
						<form:input path="videoImage" id="videoImage" htmlEscape="false"  readonly="true"  class="form-control"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>所属章节：</label></td>
					<td class="width-35">
						<sys:treeselect id="lesson" name="lesson" value="${video.lesson.id}" labelName="lesson.lessonName" labelValue="${video.lesson.lessonName}"
						title="所属章节" url="/course/course/treeDataLesson" notAllowSelectParent="true" cssClass="form-control required" allowClear="true"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>时长(秒)：</label></td>
					<td class="width-35">
						<form:input path="videoDuration" id="videoDuration" htmlEscape="false" maxlength="9"    class="form-control required number"/>
					</td>
				</tr>
				<tr>
					
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
	<script>
    layui.use('element', function(){
	 var element = layui.element;
	var uploadType;
    var uploader = new AliyunUpload.Vod({
            // 文件上传失败
            'onUploadFailed': function (uploadInfo, code, message) {
            	console.log("onUploadFailed: file:" + uploadInfo.file.name + ",code:" + code + ", message:" + message);
            },
            // 文件上传完成
            'onUploadSucceed': function (uploadInfo) {
            	console.log("onUploadSucceed: " + uploadInfo.file.name + ", endpoint:" + uploadInfo.endpoint + ", bucket:" + uploadInfo.bucket + ", object:" + uploadInfo.object);
                if("video" == uploadType){
                	$('#videoName').val(uploadInfo.file.name);
                }
            },
            // 文件上传进度
            'onUploadProgress': function (uploadInfo, totalSize, loadedPercent) {
            	console.log("onUploadProgress:file:" + uploadInfo.file.name + ", fileSize:" + totalSize + ", percent:" + Math.ceil(loadedPercent * 100.00) + "%");
                $("#proTr").show();
                //$('#progress').attr('lay-percent',Math.ceil(loadedPercent * 100.00) + "%");
                element.progress('demo', Math.ceil(loadedPercent * 100.00) + "%");
            },
            onUploadCanceled:function(uploadInfo)
            {
            	console.log("onUploadCanceled:file:" + uploadInfo.file.name);
            },
            // 开始上传
            'onUploadstarted': function (uploadInfo) {
         
                if ("video" == uploadType) {
                	$.ajax({
						url: "${ctx}/course/video/getVideoUploadAuth",
						type: 'POST',
						async: false,
						data:{filename:uploadInfo.file.name},
						success: function(res) {
							if(res.success) {
								var uploadAuth = res.body.UploadAuth;
								var uploadAddress = res.body.UploadAddress;
								$('#videoId').val(res.body.VideoId)
								uploader.setUploadAuthAndAddress(uploadInfo, uploadAuth, uploadAddress);
							} else {
								alert('请求上传失败，请重试');
							}
						},
						error: function() {
							alert('请求上传失败，请重试');
						}
					})
                }
                
                if ("image" == uploadType) {
                	$.ajax({
						url: "${ctx}/course/video/getImageUploadAuth",
						type: 'POST',
						async: false,
						success: function(res) {
							if(res.success) {
								var uploadAuth = res.body.UploadAuth;
								var uploadAddress = res.body.UploadAddress;
								$('#videoImage').val(res.body.ImageURL)
								uploader.setUploadAuthAndAddress(uploadInfo, uploadAuth, uploadAddress);
							} else {
								alert('请求上传失败，请重试');
							}
						},
						error: function() {
							alert('请求上传失败，请重试');
						}
					})
                }
               
                console.log("onUploadStarted:" + uploadInfo.file.name + ", endpoint:" + uploadInfo.endpoint + ", bucket:" + uploadInfo.bucket + ", object:" + uploadInfo.object);
            }
        });
   
    // 点播上传。每次上传都是独立的鉴权，所以初始化时，不需要设置鉴权
    // 临时账号过期时，在onUploadTokenExpired事件中，用resumeWithToken更新临时账号，上传会续传。
    var selectFile = function (event) {
            var userData;
                userData = '{"Vod":{"UserData":{"IsShowWaterMark":"false","Priority":"7"}}}';

            for(var i=0; i<event.target.files.length; i++) {
            	console.log("add file: " + event.target.files[i].name);
                    // 点播上传。每次上传都是独立的OSS object，所以添加文件时，不需要设置OSS的属性
                    uploader.addFile(event.target.files[i], null, null, null, userData);
            }
        };

    document.getElementById("files")
        .addEventListener('change', selectFile);
    document.getElementById("image")
    .addEventListener('change', selectFile);
    
    $("#start").click(function(){
    	console.log("start uploadVideo.");
    	uploadType = "video";
    	uploader.startUpload();
    })
    $("#startImage").click(function(){
    	console.log("start uploadImage.");
    	uploadType = "image";
    	uploader.startUpload();
    })
	function start() {
    	console.log("start upload.");
        uploader.startUpload();
    }

    function stop() {
        log("stop upload.");
        uploader.stopUpload();
    }
		    
    function resumeWithToken() {
        log("resume upload with token.");
        var uploadAuth = document.getElementById("uploadAuth").value;

        if (isVodMode()) {
            uploader.resumeUploadWithAuth(uploadAuth);
        } 
    }

    function clearInputFile()
    {
        var ie = (navigator.appVersion.indexOf("MSIE")!=-1);  
        if( ie ){  
            var file = document.getElementById("files");
            var file2= file.cloneNode(false);  
            file2.addEventListener('change', selectFile);
            file.parentNode.replaceChild(file2,file);  
        }
        else
        {
            document.getElementById("files").value = '';
        }

    }

    function clearList() {
        log("clean upload list.");
        uploader.cleanList();
    }

    function getList() {
        log("get upload list.");
        var list = uploader.listFiles();
        for (var i=0; i<list.length; i++) {
            log("file:" + list[i].file.name + ", status:" + list[i].state + ", endpoint:" + list[i].endpoint + ", bucket:" + list[i].bucket + ", object:" + list[i].object);
        }
    }

    function deleteFile() {
        if (document.getElementById("deleteIndex").value) {
            var index = document.getElementById("deleteIndex").value
            log("delete file index:" + index);
            uploader.deleteFile(index);
        }
    }

    function cancelFile() {
        if (document.getElementById("cancelIndex").value) {
            var index = document.getElementById("cancelIndex").value
            log("cancel file index:" + index);
            uploader.cancelFile(index);
        }
    }

    function resumeFile() {
        if (document.getElementById("resumeIndex").value) {
            var index = document.getElementById("resumeIndex").value
            log("resume file index:" + index);
            uploader.resumeFile(index);
        }
    }

    function clearLog() {
        textarea.options.length = 0;
    }

    function log(value) {
        if (!value) {
            return;
        }

    }

    function isVodMode() {
        return true;
    }

    });
</script>
</body>

</html>