<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>学习管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  $("#inputForm").submit();
			  return true;
		  }
	
		  return false;
		}
		$(document).ready(function() {
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
	
	<script type="text/javascript">
		function eaxmRecoard(a) {
			$.ajax({  
		            url:"${ctx}/mystudy/trainstudy/examRecord",  
		            type:"post",  
		            data:{userid:a},  
		            dataType:"JSON",
		            success:function(res){ 
		            	//data = $.parseJSON(res);
		            	var ress = eval(res.body.course);
		            	 $('#neirong').empty();
		            	 if (ress==0) {
								$("#neirong").append("您还没有任何考试!");
						}
		            	for(var i=0;i<ress.length;i++){  
		                     //访问每一个的属性，根据属性拿到值  
		                         //将拿到的值显示到jsp页面  neirong
		                        $('#neirong').append(ress[i].coursename+":"+ress[i].examResult+"分"+"<br/>");
		                    }
		            } 
		        })
		}
		
	</script>
</head>
<body class="hideScroll">
		<form:form id="inputForm" modelAttribute="trainstudy" action="${ctx}/mystudy/trainstudy/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">学习人姓名：</label></td>
					<td class="width-35">
						<form:input path="anchorname" htmlEscape="false"  readonly="true"  class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">课程总得分：</label></td>
					<td class="width-35">
						<form:input path="examresult" htmlEscape="false" readonly="true"   class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">当前课程名称：</label></td>
					<td class="width-35">
						<form:input path="coursename" htmlEscape="false" readonly="true" class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">当前章节名称：</label></td>
					<td class="width-35">
						<form:input path="lessonname" htmlEscape="false" readonly="true"  class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">当前章节学习进度：</label></td>
					<td class="width-35">
						<form:input path="studyprogress" htmlEscape="false" readonly="true"   class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">学习时长(秒)：</label></td>
					<td class="width-35">
						<form:input path="studytime" htmlEscape="false"  readonly="true"  class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">剩余学习次数：</label></td>
					<td class="width-35">
						<form:input path="studyplace" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">学习次数：</label></td>
					<td class="width-35">
						<form:input path="studytotalnum" htmlEscape="false" readonly="true"   class="form-control "/>
					</td>
		  		</tr>
		  		
		  		<tr>
					<td class="width-15 active"><label class="pull-right">课程总进度：</label></td>
					<td class="width-35">
						${trainstudy.yixue}/${standardCount}
					</td>
					<td class="width-15 active"></td>
		   			<td class="width-35" ></td>
		  		</tr>
		  		<tr>
				
		  		</tr>
		 	</tbody>
		</table>
	</form:form>
	
<%-- 	<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
	<tr>
		 <td class="width-15 active"><button value="${trainstudy.userid}" onclick="eaxmRecoard(this.value)">查看考试记录</button></td>
	</tr>
	<tr>
		 <td class="width-15" style="height: 85px"></td>
		</td> 
	</tr>
	</table> --%>
	<div class="panel panel-default">
		<div class="panel-heading">
			<h4 class="panel-title">
				<a data-toggle="collapse" data-parent="#accordion" 
				   href="#collapseTwo" >
					<!-- 查看考试记录(已学完) -->
					<button value="${trainstudy.userid}" onclick="eaxmRecoard(this.value)" >查看考试记录</button>
				</a>
			</h4>
		</div>
		<div id="collapseTwo" class="panel-collapse collapse">
			<div class="panel-body" id="neirong">
			</div>
		</div>
	</div>
	
	
</body>
</html>