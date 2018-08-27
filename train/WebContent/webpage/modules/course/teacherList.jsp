<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>讲师管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>讲师列表 </h5>
		<div class="ibox-tools">
			<a class="collapse-link">
				<i class="fa fa-chevron-up"></i>
			</a>
			<a class="dropdown-toggle" data-toggle="dropdown" href="#">
				<i class="fa fa-wrench"></i>
			</a>
			<ul class="dropdown-menu dropdown-user">
				<li><a href="#">选项1</a>
				</li>
				<li><a href="#">选项2</a>
				</li>
			</ul>
			<a class="close-link">
				<i class="fa fa-times"></i>
			</a>
		</div>
	</div>
    
    <div class="ibox-content">
	<sys:message content="${message}"/>
	
	<!--查询条件-->
	<div class="row">
	<div class="col-sm-12">
	<form:form id="searchForm" modelAttribute="teacher" action="${ctx}/course/teacher/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>讲师姓名：</span>
				<form:input path="teacherName" htmlEscape="false" maxlength="8"  class=" form-control input-sm"/>
			<span>讲师标签：</span>
				<form:input path="tags" htmlEscape="false" maxlength="200"  class=" form-control input-sm"/>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="course:teacher:add">
				<table:addRow url="${ctx}/course/teacher/form" title="讲师"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="course:teacher:edit">
			    <table:editRow url="${ctx}/course/teacher/form" title="讲师" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="course:teacher:del">
				<table:delRow url="${ctx}/course/teacher/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="course:teacher:import">
				<table:importExcel url="${ctx}/course/teacher/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="course:teacher:export">
	       		<table:exportExcel url="${ctx}/course/teacher/export"></table:exportExcel><!-- 导出按钮 -->
	       	</shiro:hasPermission>
	       <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
		
			</div>
		<div class="pull-right">
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
		</div>
	</div>
	</div>
	
	<!-- 表格 -->
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th> <input type="checkbox" class="i-checks"></th>
				<th  class="sort-column teacherName">讲师姓名</th>
				<th  class="sort-column note">讲师简介</th>
				<th  class="sort-column headImage">讲师头像</th>
				<th  class="sort-column tags">讲师标签</th>
				<th  class="sort-column remarks">备注信息</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="teacher">
			<tr>
				<td> <input type="checkbox" id="${teacher.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看讲师', '${ctx}/course/teacher/form?id=${teacher.id}','800px', '500px')">
					${teacher.teacherName}
				</a></td>
				
				<td>
					${teacher.note}
				</td>
				<td>
					<c:if test="${not empty teacher.headImage}">
					<img src="${teacher.headImage}" width="50" height="50" />
					</c:if>
				</td>
				<td>
					${teacher.tags}
				</td>
				<td>
					${teacher.remarks}
				</td>
				<td>
					<shiro:hasPermission name="course:teacher:view">
						<a href="#" onclick="openDialogView('查看讲师', '${ctx}/course/teacher/form?id=${teacher.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="course:teacher:edit">
    					<a href="#" onclick="openDialog('修改讲师', '${ctx}/course/teacher/form?id=${teacher.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="course:teacher:del">
						<a href="${ctx}/course/teacher/delete?id=${teacher.id}" onclick="return confirmx('确认要删除该讲师吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
					</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	
		<!-- 分页代码 -->
	<table:page page="${page}"></table:page>
	<br/>
	<br/>
	</div>
	</div>
</div>
</body>
</html>