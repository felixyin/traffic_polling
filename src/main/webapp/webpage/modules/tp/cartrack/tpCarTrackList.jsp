<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>出车记录管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="tpCarTrackList_.jsp" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<%--<div class="panel-heading">--%>
		<%--<h3 class="panel-title">出车记录列表</h3>--%>
	<%--</div>--%>
	<div class="panel-body">
	
	<!-- 搜索 -->
	<div id="search-collapse" class="collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="tpCarTrack" class="form form-horizontal well clearfix">
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="开始位置地名：">开始位置地名：</label>
				<form:input path="nameBegin" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="结束位置地名：">结束位置地名：</label>
				<form:input path="nameEnd" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="星期几：">星期几：</label>
				 <form:select path="whatDay"  class="form-control m-b">
					 <form:option value="" label=""/>
					 <form:options items="${fns:getDictList('what_day')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				 </form:select>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="开始时间：">大于等于开始时间：</label>
				<form:input path="timeBegin" htmlEscape="false"  class=" form-control" id="beginDate"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="结束时间：">小于等于结束时间：</label>
				<form:input path="timeEnd" htmlEscape="false"  class=" form-control" id="endDate"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="行驶里程：">行驶里程：</label>
				<form:input path="km" htmlEscape="false"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="用车类型：">用车类型：</label>
				<form:select path="driverType"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('driver_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="关联任务：">关联任务：</label>
				 <sys:gridselect url="${ctx}/tp/maintenance/tpMaintenance/data" id="maintenance" name="maintenance.id" value="${tpCarTrack.maintenance.id}" labelName="maintenance.num" labelValue="${tpCarTrack.maintenance.num}"
								 title="选择关联任务" cssClass="form-control " fieldLabels="任务编号|所属路口|所属道路|施工单位|施工负责人" fieldKeys="num|roadcross.name|road.name|office.name|leaderBy.name" searchLabels="任务编号|任务类型|施工负责人" searchKeys="num|jobSource|leaderBy.name" ></sys:gridselect>
				 </div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="任务描述：">任务描述：</label>
				<form:input path="jobDesc" htmlEscape="false" maxlength="255"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="关联驾驶人：">关联驾驶人：</label>
				<sys:userselect id="user" name="user.id" value="${tpCarTrack.user.id}" labelName="user.name" labelValue="${tpCarTrack.user.name}"
							    cssClass="form-control required"/>
			</div>
				<div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="车辆名称：">车辆名称：</label>
					<form:input path="car.name" htmlEscape="false"  class=" form-control"/>
				</div>
		 <div class="col-xs-12 col-sm-6 col-md-4">
			<div style="margin-top:26px">
			  <a  id="search" class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i class="fa fa-search"></i> 查询</a>
			  <a  id="reset" class="btn btn-primary btn-rounded  btn-bordered btn-sm" ><i class="fa fa-refresh"></i> 重置</a>
			 </div>
	    </div>	
	</form:form>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div id="toolbar">
			<shiro:hasPermission name="tp:cartrack:tpCarTrack:add">
				<button id="add" class="btn btn-primary" onclick="add()">
					<i class="glyphicon glyphicon-plus"></i> 新建
				</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="tp:cartrack:tpCarTrack:edit">
			    <button id="edit" class="btn btn-success" disabled onclick="edit()">
	            	<i class="glyphicon glyphicon-edit"></i> 修改
	        	</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="tp:cartrack:tpCarTrack:del">
				<button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
	            	<i class="glyphicon glyphicon-remove"></i> 删除
	        	</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="tp:cartrack:tpCarTrack:import">
				<button id="btnImport" class="btn btn-info"><i class="fa fa-folder-open-o"></i> 导入</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="tp:cartrack:tpCarTrack:export">
	        		<button id="export" class="btn btn-warning">
					<i class="fa fa-file-excel-o"></i> 导出
				</button>
			 </shiro:hasPermission>
	                 <shiro:hasPermission name="tp:cartrack:tpCarTrack:view">
				<button id="view" class="btn btn-default" disabled onclick="view()">
					<i class="fa fa-search-plus"></i> 查看
				</button>
			</shiro:hasPermission>
		    </div>
		
	<!-- 表格 -->
	<table id="tpCarTrackTable"   data-toolbar="#toolbar"></table>

    <!-- context menu -->
    <ul id="context-menu" class="dropdown-menu">
    	<shiro:hasPermission name="tp:cartrack:tpCarTrack:view">
        <li data-item="view"><a>查看</a></li>
        </shiro:hasPermission>
    	<shiro:hasPermission name="tp:cartrack:tpCarTrack:edit">
        <li data-item="edit"><a>编辑</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="tp:cartrack:tpCarTrack:del">
        <li data-item="delete"><a>删除</a></li>
        </shiro:hasPermission>
        <li data-item="action1"><a>取消</a></li>
    </ul>  
	</div>
	</div>
	</div>
</body>
</html>