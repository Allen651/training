<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>讲师管理</title>
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
</head>
<body class="hideScroll">
		<form:form id="inputForm" modelAttribute="teacher" action="${ctx}/course/teacher/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					
					<td class="width-15 active"><label class="pull-right">讲师姓名：</label></td>
					<td class="width-35">
						<form:input path="teacherName" htmlEscape="false" maxlength="30"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">讲师标签：</label></td>
					<td class="width-35">
						<form:input path="tags" htmlEscape="false" maxlength="200"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">讲师头像：</label></td>
					<td class="width-35">
						<form:hidden id="headImage" path="headImage" htmlEscape="false" maxlength="255" class="input-xlarge"/>
						<sys:ckfinder input="headImage" type="images" uploadPath="/photo" selectMultiple="false" maxWidth="100" maxHeight="100"/>
					</td>
					<td class="width-15 active"><label class="pull-right">讲师简介：</label></td>
					<td class="width-35">
						<form:textarea path="note" htmlEscape="false" rows="4" maxlength="1000"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
					<td class="width-15 active"></td>
		   			<td class="width-35" ></td>
		  		</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>