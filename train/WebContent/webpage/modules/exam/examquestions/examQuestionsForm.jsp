<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>试题管理管理</title>
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
		function addRow(list, idx, tpl, row){
			$(list).append(Mustache.render(tpl, {
				idx: idx, delBtn: true, row: row
			}));
			$(list+idx).find("select").each(function(){
				$(this).val($(this).attr("data-value"));
			});
			$(list+idx).find("input[type='checkbox'], input[type='radio']").each(function(){
				var ss = $(this).attr("data-value").split(',');
				for (var i=0; i<ss.length; i++){
					if($(this).val() == ss[i]){
						$(this).attr("checked","checked");
					}
				}
			});
		}
		function delRow(obj, prefix){
			var id = $(prefix+"_id");
			var delFlag = $(prefix+"_delFlag");
			if (id.val() == ""){
				$(obj).parent().parent().remove();
			}else if(delFlag.val() == "0"){
				delFlag.val("1");
				$(obj).html("&divide;").attr("title", "撤销删除");
				$(obj).parent().parent().addClass("error");
			}else if(delFlag.val() == "1"){
				delFlag.val("0");
				$(obj).html("&times;").attr("title", "删除");
				$(obj).parent().parent().removeClass("error");
			}
		}
	</script>
</head>
<body class="hideScroll">
	<form:form id="inputForm" modelAttribute="examQuestions" action="${ctx}/exam/examquestions/examQuestions/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<%-- <td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td> --%>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>试题标题：</label></td>
					<td class="width-35">
						<form:input path="title" htmlEscape="false"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>试题类型：</label></td>
					<td class="width-35">
						<form:radiobuttons path="type" items="${fns:getDictList('testType')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks required"/>
					</td>
				</tr>
				<tr>
				    <td class="width-15 active"><label class="pull-right"><font color="red">*</font>所属课程：</label></td>
					<td class="width-35">
					    <sys:treeselect id="course" name="course.id" value="${examQuestions.course.id}" labelName="course.courseName" labelValue="${examQuestions.course.courseName}"
					    title="所属课程" url="/course/course/treeDataCourse" notAllowSelectParent="true" cssClass="form-control " allowClear="true"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>试题分数：</label></td>
					<td class="width-35">
						<form:input path="point" htmlEscape="false"   max="100"  class="form-control required number"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>排序：</label></td>
					<td class="width-35">
						<form:input path="sort" htmlEscape="false"    class="form-control required number"/>
					</td>
				</tr>
		 	</tbody>
		</table>
		
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">试题选项表：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane active">
			<a class="btn btn-white btn-sm" onclick="addRow('#questionsItemsList', questionsItemsRowIdx, questionsItemsTpl);questionsItemsRowIdx = questionsItemsRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<table id="contentTable" class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<!-- <th>备注信息</th> -->
						<th>选项内容</th>
						<th>是否是正确答案</th>
						<th>选项排序</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="questionsItemsList">
				</tbody>
			</table>
			<script type="text/template" id="questionsItemsTpl">//<!--
				<tr id="questionsItemsList{{idx}}">
					<td class="hide">
						<input id="questionsItemsList{{idx}}_id" name="questionsItemsList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="questionsItemsList{{idx}}_delFlag" name="questionsItemsList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					
					<td>
						<input id="questionsItemsList{{idx}}_itemContent" name="questionsItemsList[{{idx}}].itemContent" type="text" value="{{row.itemContent}}"    class="form-control required"/>
					</td>
					
					
					<td>
						<c:forEach items="${fns:getDictList('yes_no')}" var="dict" varStatus="dictStatus">
							<span><input id="questionsItemsList{{idx}}_isAnswer${dictStatus.index}" name="questionsItemsList[{{idx}}].isAnswer" type="radio" class="i-checks" value="${dict.value}" data-value="{{row.isAnswer}}"><label for="questionsItemsList{{idx}}_isAnswer${dictStatus.index}">${dict.label}</label></span>
						</c:forEach>
					</td>
                    
                    
                    <td>
						<input id="questionsItemsList{{idx}}_sort" name="questionsItemsList[{{idx}}].sort" type="text" value="{{row.sort}}"    class="form-control required"/>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#questionsItemsList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var questionsItemsRowIdx = 0, questionsItemsTpl = $("#questionsItemsTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(examQuestions.questionsItemsList)};
					for (var i=0; i<data.length; i++){
						addRow('#questionsItemsList', questionsItemsRowIdx, questionsItemsTpl, data[i]);
						questionsItemsRowIdx = questionsItemsRowIdx + 1;
					}
				});
			</script>
			</div>
		</div>
		</div>
	</form:form>
</body>
</html>