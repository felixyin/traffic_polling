<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>施工管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="/webpage/include/treeview.jsp" %>
    <%@ include file="/webpage/include/echarts.jsp"%>
    <%@include file="tpTongJiList_.jsp" %>
    <style type="text/css">

        /*解决横向滚动条阻挡内容问题*/
        .fixed-table-body{
            padding-bottom: 14px!important;
        }
    </style>
    <script type="text/javascript">
        $(function(){
            // 双击行，自动展开明细
            $(document).on('dblclick','#tpMaintenanceTable>tbody>tr:not(".detail-view")',function(){
                $(this).find('.detail-icon').click();
            });
        });
    </script>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <%--<div class="panel-heading">--%>
            <%--<h3 class="panel-title">施工列表</h3>--%>
        <%--</div>--%>
        <div class="panel-body">

            <!-- 搜索 -->
            <div id="search-collapse" class="collapse">
                <div class="accordion-inner">
                    <form:form id="searchForm" modelAttribute="tpMaintenance"
                               class="form form-horizontal well clearfix">
                        <div class="col-xs-12 col-sm-6 col-md-4">
                            <label class="label-item single-overflow pull-left" title="任务编号：">任务编号：</label>
                            <form:input path="num" htmlEscape="false" maxlength="64" class=" form-control"/>
                        </div>
                        <div class="col-xs-12 col-sm-6 col-md-4">
                            <label class="label-item single-overflow pull-left" title="任务类型：">任务类型：</label>
                            <form:select path="jobType" class="form-control m-b">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('job_type')}" itemLabel="label" itemValue="value"
                                              htmlEscape="false"/>
                            </form:select>
                        </div>
                        <div class="col-xs-12 col-sm-6 col-md-4">
                            <label class="label-item single-overflow pull-left" title="任务来源：">任务来源：</label>
                            <form:select path="source" class="form-control m-b">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('job_source')}" itemLabel="label"
                                              itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </div>
                        <div class="col-xs-12 col-sm-6 col-md-4">
                            <label class="label-item single-overflow pull-left" title="所属区域：">所属区域：</label>
                            <sys:treeselect id="area" name="area.id" value="${tpMaintenance.area.id}"
                                            labelName="area.name" labelValue="${tpMaintenance.area.name}"
                                            title="区域" url="/sys/area/treeData" cssClass="form-control"
                                            allowClear="true" notAllowSelectParent="true"/>
                        </div>
                        <div class="col-xs-12 col-sm-6 col-md-4">
                            <label class="label-item single-overflow pull-left" title="所属路口：">所属路口：</label>
                            <sys:gridselect url="${ctx}/tp/roadcross/tpRoadCrossing/data" id="roadcross"
                                            name="roadcross.id" value="${tpMaintenance.roadcross.id}"
                                            labelName="roadcross.name" labelValue="${tpMaintenance.roadcross.name}"
                                            title="选择所属路口" cssClass="form-control required" fieldLabels="路口名称|所属区域|所属街道"
                                            fieldKeys="name|sarea.name|township" searchLabels="路口名称|所属区域"
                                            searchKeys="name|sarea.name"></sys:gridselect>
                        </div>
                        <div class="col-xs-12 col-sm-6 col-md-4">
                            <label class="label-item single-overflow pull-left" title="所属道路：">所属道路：</label>
                            <sys:gridselect url="${ctx}/tp/road/tpRoad/data" id="road" name="road.id"
                                            value="${tpMaintenance.road.id}" labelName="road.name"
                                            labelValue="${tpMaintenance.road.name}"
                                            title="选择所属道路" cssClass="form-control " fieldLabels="道路名称|所属区域"
                                            fieldKeys="name|area.name" searchLabels="道路名称|所属区域"
                                            searchKeys="name|area.name"></sys:gridselect>
                        </div>
                        <div class="col-xs-12 col-sm-6 col-md-4">
                            <label class="label-item single-overflow pull-left" title="搜索用地址：">搜索用地址：</label>
                            <form:input path="address" htmlEscape="false" maxlength="100" class=" form-control"/>
                        </div>
                        <div class="col-xs-12 col-sm-6 col-md-4">
                            <label class="label-item single-overflow pull-left" title="派单人：">派单人：</label>
                            <sys:userselect id="sendBy" name="sendBy.id" value="${tpMaintenance.sendBy.id}"
                                            labelName="sendBy.name" labelValue="${tpMaintenance.sendBy.name}"
                                            cssClass="form-control "/>
                        </div>
                        <div class="col-xs-12 col-sm-6 col-md-4">
                            <div class="form-group">
                                <label class="label-item single-overflow pull-left" title="派单时间：">&nbsp;派单时间：</label>
                                <div class="col-xs-12">
                                    <div class="col-xs-12 col-sm-5">
                                        <div class='input-group date' id='beginSendDate' style="left: -10px;">
                                            <input type='text' name="beginSendDate" class="form-control"/>
                                            <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
                                        </div>
                                    </div>
                                    <div class="col-xs-12 col-sm-1">
                                        ~
                                    </div>
                                    <div class="col-xs-12 col-sm-5">
                                        <div class='input-group date' id='endSendDate' style="left: -10px;">
                                            <input type='text' name="endSendDate" class="form-control"/>
                                            <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-6 col-md-4">
                            <label class="label-item single-overflow pull-left" title="施工单位：">施工单位：</label>
                            <sys:treeselect id="office" name="office.id" value="${tpMaintenance.office.id}"
                                            labelName="office.name" labelValue="${tpMaintenance.office.name}"
                                            title="部门" url="/sys/office/treeData?type=2" cssClass="form-control"
                                            allowClear="true" notAllowSelectParent="true"/>
                        </div>
                        <div class="col-xs-12 col-sm-6 col-md-4">
                            <label class="label-item single-overflow pull-left" title="施工负责人：">施工负责人：</label>
                            <sys:userselect id="leaderBy" name="leaderBy.id" value="${tpMaintenance.leaderBy.id}"
                                            labelName="leaderBy.name" labelValue="${tpMaintenance.leaderBy.name}"
                                            cssClass="form-control required" officeIptId="officeId"/>
                        </div>
                        <div class="col-xs-12 col-sm-6 col-md-4">
                            <div class="form-group">
                                <label class="label-item single-overflow pull-left"
                                       title="施工开始时间：">&nbsp;施工开始时间：</label>
                                <div class="col-xs-12">
                                    <div class="col-xs-12 col-sm-5">
                                        <div class='input-group date' id='beginJobBeginDate' style="left: -10px;">
                                            <input type='text' name="beginJobBeginDate" class="form-control"/>
                                            <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
                                        </div>
                                    </div>
                                    <div class="col-xs-12 col-sm-1">
                                        ~
                                    </div>
                                    <div class="col-xs-12 col-sm-5">
                                        <div class='input-group date' id='endJobBeginDate' style="left: -10px;">
                                            <input type='text' name="endJobBeginDate" class="form-control"/>
                                            <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-6 col-md-4">
                            <label class="label-item single-overflow pull-left" title="分组方式：">分组方式：</label>
                            <form:select path="group" class="form-control m-b">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('group_report')}" itemLabel="label"
                                              itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </div>
                        <div class="col-xs-12 col-sm-6 col-md-4">
                            <div class="form-group">
                                <label class="label-item single-overflow pull-left" title="任务状态：">&nbsp;任务状态：</label>
                                <div class="col-xs-12">
                                    <form:radiobuttons class="i-checks" path="status"
                                                       items="${fns:getDictList('job_status')}" itemLabel="label"
                                                       itemValue="value" htmlEscape="false"/>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-6 col-md-4">
                            <div style="margin-top:26px">
                                <a id="search" class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i
                                        class="fa fa-search"></i> 查询</a>
                                <a id="reset" class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i
                                        class="fa fa-refresh"></i> 重置</a>
                            </div>
                        </div>
                    </form:form>
                </div>
            </div>

            <!-- 工具栏 -->
            <div id="toolbar" >
                <shiro:hasPermission name="tp:maintenance:tpMaintenance:export">
                    <button id="export" class="btn btn-warning" title="请先在派单时间中选择要导出的月份！">
                        <i class="fa fa-file-excel-o"></i> 导出
                    </button>
                </shiro:hasPermission>
            </div>

            <!-- 表格 套用overflow，是为了解决chrome下滚动条，和图片弹框一起存在，滚在上的bug-->
            <div style="overflow: hidden">
                <table id="tpMaintenanceTable" data-toolbar="#toolbar" style="display: inline-block;"></table>
                <div id="main" style="width: 100%;height: 500px"></div>
            </div>

            <!-- context menu -->
            <ul id="context-menu" class="dropdown-menu">
                <shiro:hasPermission name="tp:maintenance:tpMaintenance:view">
                    <li data-item="view"><a>查看</a></li>
                </shiro:hasPermission>
                <shiro:hasPermission name="tp:maintenance:tpMaintenance:edit">
                    <li data-item="edit"><a>编辑</a></li>
                </shiro:hasPermission>
                <shiro:hasPermission name="tp:maintenance:tpMaintenance:del">
                    <li data-item="delete"><a>删除</a></li>
                </shiro:hasPermission>
                <li data-item="action1"><a>取消</a></li>
            </ul>
        </div>
    </div>
</div>
</body>
</html>