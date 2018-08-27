<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>学习管理</title>
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
		<h5>学习列表 </h5>
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
	<form:form id="searchForm" modelAttribute="trainstudy" action="${ctx}/mystudy/trainstudy/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>学习人姓名：</span>
				<form:input path="anchorname" htmlEscape="false" maxlength="24"  class=" form-control input-sm"/>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<%-- <shiro:hasPermission name="mystudy:trainstudy:add">
				<table:addRow url="${ctx}/mystudy/trainstudy/form" title="学习"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="mystudy:trainstudy:edit">
			    <table:editRow url="${ctx}/mystudy/trainstudy/form" title="学习" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="mystudy:trainstudy:del">
				<table:delRow url="${ctx}/mystudy/trainstudy/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="mystudy:trainstudy:import">
				<table:importExcel url="${ctx}/mystudy/trainstudy/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="mystudy:trainstudy:export">
	       		<table:exportExcel url="${ctx}/mystudy/trainstudy/export"></table:exportExcel><!-- 导出按钮 -->
	       	</shiro:hasPermission> --%>
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
				<th  class="sort-column anchorname">学习人名称</th>
				<th  class="sort-column coursename">当前课程名称</th>
				<th  class="sort-column lessonname">当前章节名称</th>
				<th  class="sort-column studyprogress">当前章节学习进度</th>
				<th  class="sort-column studyprog">课程总进度</th>
				<th  class="sort-column examresult">课程总得分</th>
				<th  class="sort-column studytime">学习时长(秒)</th>
				<th  class="sort-column studyplace">剩余学习次数</th>
				<th  class="sort-column studytotalnum">学习次数</th>
				<th  class="sort-column remarks">备注信息</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="trainstudy">
			<tr>
				<td> <input type="checkbox" id="${trainstudy.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看学习', '${ctx}/mystudy/trainstudy/form?id=${trainstudy.id}','800px', '500px')">
					${trainstudy.anchorname}
				</a>
				</td>
				<td>
					${trainstudy.coursename}
				</td>
				<td>
					${trainstudy.lessonname}
				</td>
				<td>
					${trainstudy.studyprogress}
				</td>
				<td>
					${trainstudy.yixue}/${standardCount}
				</td>
				<td>
					${trainstudy.examresult}
				</td>
				<td>
					${trainstudy.studytime}
				</td>
				<td>
					${trainstudy.studyplace}
				</td>
				<td>
					${trainstudy.studytotalnum}
				</td>
				<td>
					${trainstudy.remarks}
				</td>
				<td>
					<shiro:hasPermission name="mystudy:trainstudy:view">
						<a href="#" onclick="openDialogView('查看学习', '${ctx}/mystudy/trainstudy/form?id=${trainstudy.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<%-- <shiro:hasPermission name="mystudy:trainstudy:edit">
    					<a href="#" onclick="openDialog('修改学习', '${ctx}/mystudy/trainstudy/form?id=${trainstudy.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission> --%>
    				<shiro:hasPermission name="mystudy:trainstudy:del">
						<a href="${ctx}/mystudy/trainstudy/delete?id=${trainstudy.id}" onclick="return confirmx('确认要删除该学习吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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