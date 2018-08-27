<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>考试明细管理</title>
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
		<form:form id="inputForm" modelAttribute="examineDetail" action="${ctx}/exam/examdetail/examineDetail/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<%-- <td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td> --%>
					<td class="width-15 active"><label class="pull-right">考试人id：</label></td>
					<td class="width-35">
						<form:input path="userId" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">试题id：</label></td>
					<td class="width-35">
						<form:input path="questionId" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					
					<td class="width-15 active"><label class="pull-right">答题内容：</label></td>
					<td class="width-35">
						<form:input path="testContent" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">是否正确：</label></td>
					<td class="width-35">
						<form:radiobuttons path="isRight" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks "/>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>