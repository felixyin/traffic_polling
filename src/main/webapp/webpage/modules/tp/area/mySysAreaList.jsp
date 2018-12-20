<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>区域管理</title>
	<meta name="decorator" content="ani"/>
	<%@include file="mySysAreaList.js" %>
</head>
<body>

	<div class="wrapper wrapper-content">
		<div class="panel panel-primary">
			<div class="panel-heading">
				<h3 class="panel-title">区域列表  </h3>
			</div>
			
			<div class="panel-body">
	
			<!-- 工具栏 -->
			<div class="row">
			<div class="col-sm-12">
				<div class="pull-left treetable-bar">
					<shiro:hasPermission name="tp:area:mySysArea:add">
						<a id="add" href="javascript: return false" class="btn btn-primary" onclick="add()"><i class="glyphicon glyphicon-plus"></i> 新建</a><!-- 增加按钮 -->
					</shiro:hasPermission>
			       <button class="btn btn-default" data-toggle="tooltip" data-placement="left" onclick="refresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
				
				</div>
			</div>
			</div>
			<table id="mySysAreaTreeTable" class="table table-hover">
				<thead>
					<tr>
						<th>编号</th>
						<th>父级编号</th>
						<th>名称</th>
						<th>区域编码</th>
						<th>区域类型</th>
						<th>更新时间</th>
						<th>备注信息</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody id="mySysAreaTreeTableList"></tbody>
			</table>
			<br/>
			</div>
			</div>
	</div>
</body>
</html>