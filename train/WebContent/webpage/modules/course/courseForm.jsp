<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>课程管理</title>
	<meta name="decorator" content="default"/>
	 <link href="${ctxStatic}/summernote/summernote.css" rel="stylesheet">
	 <link href="${ctxStatic}/summernote/summernote-bs3.css" rel="stylesheet">
	 <script src="${ctxStatic}/summernote/summernote.min.js"></script>
	 <script src="${ctxStatic}/summernote/summernote-zh-CN.js"></script>
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
		   	  $("#details").val($("#rich_details").next().find(".note-editable").html());//取富文本的值
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
			
					//富文本初始化
			$('#rich_details').summernote({
                lang: 'zh-CN'
            });

			$("#rich_details").next().find(".note-editable").html(  $("#details").val());

			$("#rich_details").next().find(".note-editable").html(  $("#rich_details").next().find(".note-editable").text());
				
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
	<form:form id="inputForm" modelAttribute="course" action="${ctx}/course/course/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>课程名：</label></td>
					<td class="width-35">
						<form:input path="courseName" htmlEscape="false" maxlength="100"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>讲师：</label></td>
					<td class="width-35">
						 <sys:gridselect url="${ctx}/course/course/selectteacher" id="teacher" name="teacher.id"  value="${course.teacher.id}"  title="选择讲师" labelName="teacher.teacherName" 
							labelValue="${course.teacher.teacherName}" cssClass="form-control required" fieldLabels="姓名|简介" fieldKeys="teacherName|note" searchLabel="讲师姓名：" searchKey="teacherName" ></sys:gridselect>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>课程章节数：</label></td>
					<td class="width-35">
						<form:input path="lessonNum" htmlEscape="false" maxlength="9"    class="form-control required number"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>课程分类：</label></td>
					<td class="width-35">
					<sys:treeselect id="courseType" name="courseType.id" value="${course.courseType.id}" labelName="courseType.name" labelValue="${course.courseType.name}"
						title="课程分类" url="/course/courseType/treeData"  cssClass="form-control required" allowClear="true"/>
					</td>
				</tr>
				
				<tr>
					
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>课程难度：</label></td>
					<td class="width-35">
						<form:select path="difficultyLevel" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('difficulty_level')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>学习路径：</label></td>
					<td class="width-35">
						<form:input path="sort" htmlEscape="false" maxlength="9"    class="form-control required number"/>
					</td>
				</tr>
				<tr>
					
					<td class="width-15 active"><label class="pull-right">授课对象：</td>
					<td class="width-35">
						
						<form:checkboxes path="roleList" items="${fns:getDictList('course_roles')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks "/>
					</td>
					<td colspan=2>（标准课程与延展课程的授课对象默认包含主播）</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>课程简介：</label></td>
					<td class="width-35">
						<form:textarea path="courseBrief" htmlEscape="false" rows="4" maxlength="200"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
					
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">课程详情：</label></td>
					<td colspan="3">
						<form:hidden path="details"/>
						<div id="rich_details">
                           

                        </div>
                    </td>
				</tr>
		 	</tbody>
		</table>
		
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">课程章节：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane active">
			<a class="btn btn-white btn-sm" onclick="addRow('#lessonList', lessonRowIdx, lessonTpl);lessonRowIdx = lessonRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<table id="contentTable" class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>章节名</th>
						<th>排序</th>
						<th>学习限制次数</th>
						<th>备注信息</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="lessonList">
				</tbody>
			</table>
			<script type="text/template" id="lessonTpl">//<!--
				<tr id="lessonList{{idx}}">
					<td class="hide">
						<input id="lessonList{{idx}}_id" name="lessonList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="lessonList{{idx}}_delFlag" name="lessonList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					<td>
						<input id="lessonList{{idx}}_lessonName" name="lessonList[{{idx}}].lessonName" type="text" value="{{row.lessonName}}" maxlength="100"    class="form-control required"/>
					</td>
					<td>
						<input id="lessonList{{idx}}_sort" name="lessonList[{{idx}}].sort" type="text" value="{{row.sort}}" maxlength="9"    class="form-control required number"/>
					</td>
					<td>
						<input id="lessonList{{idx}}_studyLimitNum" name="lessonList[{{idx}}].studyLimitNum" type="text" value="{{row.studyLimitNum}}" maxlength="9"    class="form-control required number"/>
					</td>
					<td>
						<input id="lessonList{{idx}}_remarks" name="lessonList[{{idx}}].remarks" rows="4"    class="form-control " value="{{row.remarks}}" />
					</td>
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#lessonList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var lessonRowIdx = 0, lessonTpl = $("#lessonTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(course.lessonList)};
					for (var i=0; i<data.length; i++){
						addRow('#lessonList', lessonRowIdx, lessonTpl, data[i]);
						lessonRowIdx = lessonRowIdx + 1;
					}
				});
			</script>
			</div>
		</div>
		</div>
	</form:form>
</body>
</html>