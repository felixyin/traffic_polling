<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="java.util.regex.Pattern" %>
<%@ page import="com.jeeplus.modules.tp.maintenance.entity.TpMaintenance" %>
<%@ page import="java.util.regex.Matcher" %>
<%@ page import="java.net.URLDecoder" %>
<%@ page import="com.jeeplus.common.utils.text.Charsets" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>施工管理</title>
    <meta name="decorator" content="ani"/>
    <!-- SUMMERNOTE -->
    <%@include file="/webpage/include/summernote.jsp" %>
    <link href="${ctxStatic}/plugin/jquery-autocomplete/easy-autocomplete.min.css" rel="stylesheet">

    <style type="text/css">
        .panel-body {
            width: 97% !important;
        }

        .easy-autocomplete {
            display: inline-block;
            z-index: 1000;
        }

        .note-editing-area {
            height: 120px;
        }

        .error > td:last-child {
            background-color: palevioletred !important;
        }
    </style>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="row">
        <div class="col-md-12">
            <div class="panel panel-primary">
                <%--<div class="panel-heading">--%>
                <%--<h3 class="panel-title">--%>
                <a class="panelButton btn btn-primary" href="${ctx}/tp/maintenance/tpMaintenance"><i class="fa fa-reply"></i>
                    返回</a>
                <%--</h3>--%>
                <%--</div>--%>
                <div class="panel-body">
                    <form:form id="inputForm" modelAttribute="tpMaintenance"
                               action="${ctx}/tp/maintenance/tpMaintenance/save" method="post" class="form-horizontal">
                        <form:hidden path="id"/>


                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>任务类型：</label>
                            <div class="col-sm-4">
                                <shiro:hasPermission name="tp:maintenance:tpMaintenance:jiaoJing">
                                    <form:select path="jobType" class="form-control required readonly" multiple="multiple">
                                        <form:option value="" label=""/>
                                        <c:forEach items="${fns:getDictList('job_type')}" var="all">
                                            <c:choose>
                                                <c:when test="${fn:contains(tpMaintenance.jobType,all.value)}">
                                                    <form:option value="${all.value}" selected="selected"> ${all.label} </form:option>
                                                </c:when>
                                                <c:otherwise>
                                                    <form:option value="${all.value}"> ${all.label} </form:option>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </form:select>
                                </shiro:hasPermission>
                                <shiro:lacksPermission name="tp:maintenance:tpMaintenance:jiaoJing">
                                    <input type="hidden" name="jobType" value="${tpMaintenance.jobType}">
                                    <c:forEach items="${fns:getDictList('job_type')}" var="all">
                                        <c:choose>
                                            <c:when test="${fn:contains(tpMaintenance.jobType,all.value)}">
                                                ${fns:getDictLabel(all.value,'job_type','')}<br/>
                                            </c:when>
                                        </c:choose>
                                    </c:forEach>
                                </shiro:lacksPermission>
                            </div>
                            <label class="col-sm-2 control-label"><font color="red">*</font>任务来源：</label>
                            <div class="col-sm-4">
                                <shiro:hasPermission name="tp:maintenance:tpMaintenance:jiaoJing">
                                    <form:select path="source" class="form-control required">
                                        <form:option value="" label=""/>
                                        <form:options items="${fns:getDictList('job_source')}" itemLabel="label"
                                                      itemValue="value" htmlEscape="false"/>
                                    </form:select>
                                </shiro:hasPermission>
                                <shiro:lacksPermission name="tp:maintenance:tpMaintenance:jiaoJing">
                                    <input type="hidden" name="source" value="${tpMaintenance.source}">
                                    ${fns:getDictLabel(tpMaintenance.source,'job_source','')}
                                </shiro:lacksPermission>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">问题描述：</label>
                            <div class="col-sm-10">
                                <shiro:hasPermission name="tp:maintenance:tpMaintenance:jiaoJing">
                                    <input type="hidden" name="jobDescription" class="required" value=" ${tpMaintenance.jobDescription}"/>
                                    <div id="jobDescription">
                                            ${fns:unescapeHtml(tpMaintenance.jobDescription)}
                                    </div>
                                </shiro:hasPermission>
                                <shiro:lacksPermission name="tp:maintenance:tpMaintenance:jiaoJing">
                                    <input type="hidden" name="jobDescription" value="${tpMaintenance.jobDescription}">
                                    ${fns:unescapeHtml(tpMaintenance.jobDescription)}
                                </shiro:lacksPermission>

                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>故障类型：</label>
                            <div class="col-sm-4">
                                <shiro:hasPermission name="tp:maintenance:tpMaintenance:jiaoJing">
                                    <form:select path="malfunctionType" class="form-control required">
                                        <form:option value="" label=""/>
                                        <form:options items="${fns:getDictList('malfunction_type')}" itemLabel="label"
                                                      itemValue="value" htmlEscape="false"/>
                                    </form:select>
                                </shiro:hasPermission>
                                <shiro:lacksPermission name="tp:maintenance:tpMaintenance:jiaoJing">
                                    <input type="hidden" name="source" value="${tpMaintenance.malfunctionType}">
                                    ${fns:getDictLabel(tpMaintenance.malfunctionType,'malfunction_type','')}
                                </shiro:lacksPermission>
                            </div>

                            <label class="col-sm-2 control-label"><font color="red">*</font>选择位置：</label>
                            <div class="col-sm-4">
                                <!-- Split button -->
                                <div class="btn-group">
                                    <shiro:hasAnyPermissions name="tp:maintenance:tpMaintenance:selectPostion">
                                        <button id="postionBtn" type="button" class="btn btn-primary "
                                                data-loading-text="正在计算..."
                                                title="${tpMaintenance.roadcross.name }${tpMaintenance.nearestJunction}"
                                                onclick="openSelectPostionDialog();"
                                                style="overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">
                                            <i class="fa fa-map-marker "></i>
                                            <span>${tpMaintenance.roadcross.name}${fns:abbr(tpMaintenance.nearestJunction,16) }</span>
                                        </button>
                                        <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown"
                                                aria-haspopup="true" aria-expanded="false">
                                            <span class="caret"></span>
                                            <span class="sr-only">Toggle Dropdown</span>
                                        </button>
                                        <ul class="dropdown-menu">
                                            <li id="_addressDetail_hide" name="_addressDetail" onclick="$('#my-address-detail').slideUp();">
                                                <a href="#">合起</a></li>
                                            <li id="_addressDetail_show" name="_addressDetail"
                                                onclick="$('#my-address-detail').slideDown();"><a href="#">展开</a></li>
                                        </ul>
                                    </shiro:hasAnyPermissions>
                                    <shiro:lacksPermission name="tp:maintenance:tpMaintenance:selectPostion">
                                        ${tpMaintenance.roadcross.name }${tpMaintenance.nearestJunction}
                                    </shiro:lacksPermission>

                                </div>

                            </div>
                        </div>

                        <div id="my-address-detail" style="display: none;">
                            <div class="form-group">
                                <label class="col-sm-2 control-label"><font color="red">*</font>所属区域：</label>
                                <div class="col-sm-4">
                                    <shiro:hasPermission name="tp:maintenance:tpMaintenance:jiaoJing">
                                        <sys:treeselect id="area" name="area.id" value="${tpMaintenance.area.id}"
                                                        labelName="area.name" labelValue="${tpMaintenance.area.name}"
                                                        title="区域" url="/sys/area/treeData" cssClass="form-control required"
                                                        allowClear="true" notAllowSelectParent="true"/>
                                    </shiro:hasPermission>
                                    <shiro:lacksPermission name="tp:maintenance:tpMaintenance:jiaoJing">
                                        <input type="hidden" name="area.id" value="${tpMaintenance.area.id}">
                                        ${tpMaintenance.area.name}
                                    </shiro:lacksPermission>
                                </div>
                                <label class="col-sm-2 control-label"><font color="red">*</font>经纬度：</label>
                                <div class="col-sm-4">
                                    <shiro:hasPermission name="tp:maintenance:tpMaintenance:jiaoJing">
                                        <form:input path="location" readonly="true" htmlEscape="false"
                                                    class="form-control required"/>
                                    </shiro:hasPermission>
                                    <shiro:lacksPermission name="tp:maintenance:tpMaintenance:jiaoJing">
                                        <input type="hidden" id="location" name="location" value="${tpMaintenance.location}">
                                        ${tpMaintenance.location}
                                    </shiro:lacksPermission>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label"><font color="red">*</font>所属路口：</label>
                                <div class="col-sm-4">
                                    <shiro:hasPermission name="tp:maintenance:tpMaintenance:jiaoJing">
                                        <sys:gridselect url="${ctx}/tp/roadcross/tpRoadCrossing/data" id="roadcross"
                                                        name="roadcross.id" value="${tpMaintenance.roadcross.id}"
                                                        labelName="roadcross.name"
                                                        labelValue="${tpMaintenance.roadcross.name}"
                                                        title="选择所属路口" cssClass="form-control required"
                                                        fieldLabels="路口名称|所属区域|所属街道" fieldKeys="name|sarea.name|township"
                                                        searchLabels="路口名称|所属区域"
                                                        searchKeys="name|sarea.name"></sys:gridselect>
                                    </shiro:hasPermission>
                                    <shiro:lacksPermission name="tp:maintenance:tpMaintenance:jiaoJing">
                                        <input type="hidden" name="roadcross.id" value="${tpMaintenance.roadcross.id}">
                                        <input type="hidden" id="roadcrossName" name="roadcross.id" value="${tpMaintenance.roadcross.name}">
                                        ${tpMaintenance.roadcross.name}
                                    </shiro:lacksPermission>
                                </div>
                                <label class="col-sm-2 control-label"><font color="red">*</font>所属路口相对位置：</label>
                                <div class="col-sm-4">
                                    <shiro:hasPermission name="tp:maintenance:tpMaintenance:jiaoJing">
                                        <form:input path="nearestJunction" htmlEscape="false"
                                                    class="form-control required"/>
                                    </shiro:hasPermission>
                                    <shiro:lacksPermission name="tp:maintenance:tpMaintenance:jiaoJing">
                                        <input type="hidden" name="nearestJunction" value="${tpMaintenance.nearestJunction}">
                                        ${tpMaintenance.nearestJunction}
                                    </shiro:lacksPermission>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">附近道路：</label>
                                <div class="col-sm-4">
                                    <shiro:hasPermission name="tp:maintenance:tpMaintenance:jiaoJing">
                                        <sys:gridselect url="${ctx}/tp/road/tpRoad/data" id="road" name="road.id"
                                                        value="${tpMaintenance.road.id}" labelName="road.name"
                                                        labelValue="${tpMaintenance.road.name}"
                                                        title="选择附近道路" cssClass="form-control " fieldLabels="道路名称|所属区域"
                                                        fieldKeys="name|sarea.name" searchLabels="道路名称|所属区域"
                                                        searchKeys="name|sarea.name"></sys:gridselect>
                                    </shiro:hasPermission>
                                    <shiro:lacksPermission name="tp:maintenance:tpMaintenance:jiaoJing">
                                        <input type="hidden" name="road.id" value="${tpMaintenance.road.id}">
                                        ${tpMaintenance.road.name}
                                    </shiro:lacksPermission>
                                </div>
                                <label class="col-sm-2 control-label">道路等级<font color="#b8860b">(尽量填写)</font>：</label>
                                <div class="col-sm-4">
                                    <shiro:hasPermission name="tp:maintenance:tpMaintenance:jiaoJing">
                                        <form:select path="road.roadType" class="form-control">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('road_type')}" itemLabel="label"
                                                          itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </shiro:hasPermission>
                                    <shiro:lacksPermission name="tp:maintenance:tpMaintenance:jiaoJing">
                                        <input type="hidden" name="road.roadType" value="${tpMaintenance.road.roadType}">
                                        ${fns:getDictLabel(tpMaintenance.road.roadType,'road_type','')}
                                    </shiro:lacksPermission>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">详细地址：</label>
                                <div class="col-sm-4">
                                    <form:input path="address" htmlEscape="false" class="form-control "/>
                                </div>

                                <label class="col-sm-2 control-label">详细地址相对位置：</label>
                                <div class="col-sm-4">
                                    <form:input path="nearestPoi" htmlEscape="false" class="form-control "/>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">派单人：</label>
                            <div class="col-sm-4">
                                <shiro:hasPermission name="tp:maintenance:tpMaintenance:weiBao">
                                    <sys:userselect id="sendBy" name="sendBy.id" value="${tpMaintenance.sendBy.id}"
                                                    labelName="sendBy.name" labelValue="${tpMaintenance.sendBy.name}"
                                                    cssClass="form-control "/>
                                </shiro:hasPermission>
                                <shiro:lacksPermission name="tp:maintenance:tpMaintenance:weiBao">
                                    <input type="hidden" name="sendBy.id" value="${tpMaintenance.sendBy.id}">
                                    ${tpMaintenance.sendBy.name}
                                </shiro:lacksPermission>
                            </div>
                            <label class="col-sm-2 control-label">派单时间：</label>
                            <div class="col-sm-4">
                                <shiro:hasPermission name="tp:maintenance:tpMaintenance:weiBao">
                                    <div class='input-group form_datetime' id='sendDate'>
                                        <input type='text' name="sendDate" class="form-control " autocomplete="off"
                                               value="<fmt:formatDate value="${tpMaintenance.sendDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-calendar"></span>
                                        </span>
                                    </div>
                                </shiro:hasPermission>
                                <shiro:lacksPermission name="tp:maintenance:tpMaintenance:weiBao">
                                    <input type="hidden" name="sendDate" value="${fns:formatDateTime(tpMaintenance.sendDate)}">
                                    ${fns:formatDateTime(tpMaintenance.sendDate)}
                                </shiro:lacksPermission>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>施工单位：</label>
                            <div class="col-sm-4">
                                <shiro:hasPermission name="tp:maintenance:tpMaintenance:jiaoJing">
                                    <sys:treeselect id="office" name="office.id" value="${tpMaintenance.office.id}"
                                                    labelName="office.name" labelValue="${tpMaintenance.office.name}"
                                                    title="施工单位" url="/sys/office/myTreeData?type=3"
                                                    cssClass="form-control required" allowClear="true"
                                                    notAllowSelectParent="true"/>
                                </shiro:hasPermission>
                                <shiro:lacksPermission name="tp:maintenance:tpMaintenance:jiaoJing">
                                    <input type="hidden" id="officeId" name="office.id" value="${tpMaintenance.office.id}">
                                    ${tpMaintenance.office.name}
                                </shiro:lacksPermission>
                            </div>
                            <label class="col-sm-2 control-label"><font color="red">*</font>施工负责人：</label>
                            <div class="col-sm-4">
                                <shiro:hasPermission name="tp:maintenance:tpMaintenance:weiBao">
                                    <sys:userselect id="leaderBy" name="leaderBy.id" value="${tpMaintenance.leaderBy.id}"
                                                    labelName="leaderBy.name" labelValue="${tpMaintenance.leaderBy.name}"
                                                    cssClass="form-control required" officeIptId="officeId"/>
                                </shiro:hasPermission>
                                <shiro:lacksPermission name="tp:maintenance:tpMaintenance:weiBao">
                                    <input type="hidden" name="leaderBy.id" value="${tpMaintenance.leaderBy.id}">
                                    ${tpMaintenance.leaderBy.name}
                                </shiro:lacksPermission>

                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>施工开始时间：</label>
                            <div class="col-sm-4">
                                <shiro:hasPermission name="tp:maintenance:tpMaintenance:weiBao">
                                    <div class='input-group form_datetime' id='jobBeginDate'>
                                        <input type='text' name="jobBeginDate" class="form-control required" autocomplete="off"
                                               value="<fmt:formatDate value="${tpMaintenance.jobBeginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                        <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-calendar"></span>
                                    </span>
                                    </div>
                                </shiro:hasPermission>
                                <shiro:lacksPermission name="tp:maintenance:tpMaintenance:weiBao">
                                    <input type="hidden" name="jobBeginDate" value="${fns:formatDateTime(tpMaintenance.jobBeginDate)}">
                                    ${fns:formatDateTime(tpMaintenance.jobBeginDate)}
                                </shiro:lacksPermission>

                            </div>
                            <label class="col-sm-2 control-label"><font color="red">*</font>施工结束时间：</label>
                            <div class="col-sm-4">
                                <shiro:hasPermission name="tp:maintenance:tpMaintenance:weiBao">
                                    <div class='input-group form_datetime' id='jobEndDate'>
                                        <input type='text' name="jobEndDate" class="form-control required" autocomplete="off"
                                               value="<fmt:formatDate value="${tpMaintenance.jobEndDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                        <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-calendar"></span>
                                    </span>
                                    </div>
                                </shiro:hasPermission>
                                <shiro:lacksPermission name="tp:maintenance:tpMaintenance:weiBao">
                                    <input type="hidden" name="jobEndDate" value="${fns:formatDateTime(tpMaintenance.jobEndDate)}">
                                    ${fns:formatDateTime(tpMaintenance.jobEndDate)}
                                </shiro:lacksPermission>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">施工过程：</label>
                            <div class="col-sm-10">
                                <shiro:hasPermission name="tp:maintenance:tpMaintenance:weiBao">
                                    <input type="hidden" name="process" value=" ${tpMaintenance.process}"/>
                                    <div id="process">
                                            ${fns:unescapeHtml(tpMaintenance.process)}
                                    </div>
                                </shiro:hasPermission>
                                <shiro:lacksPermission name="tp:maintenance:tpMaintenance:weiBao">
                                    <input type="hidden" name="process" value="${tpMaintenance.process}">
                                    ${fns:unescapeHtml(tpMaintenance.process)}
                                </shiro:lacksPermission>

                            </div>
                        </div>
                        <%!
                            // 匹配正则表达式方法
                            public static boolean matcherRegularExpression(String regEx, String str) {
                                Pattern pattern = Pattern.compile(regEx);
                                Matcher matcher = pattern.matcher(str);
                                boolean found = false;
                                while (matcher.find()) {
                                    //System.out.println("发现 \"" + matcher.group() + "\" 开始于 "
                                    //+ matcher.start() + " 结束于 " + matcher.end());
                                    found = true;
                                }
                                return found;
                            }
                        %>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">施工前照片：</label>
                            <div class="col-sm-2">
                                <shiro:hasPermission name="tp:maintenance:tpMaintenance:weiBao">
                                    <sys:fileUpload path="prePic" value="${tpMaintenance.prePic}" type="file"
                                                    uploadPath="/tp/maintenance/tpMaintenance"/>
                                </shiro:hasPermission>
                                <shiro:lacksPermission name="tp:maintenance:tpMaintenance:weiBao">
                                    <input type="hidden" name="prePic" value="${tpMaintenance.prePic}">
                                    <%
                                        TpMaintenance tpMaintenance = (TpMaintenance) request.getAttribute("tpMaintenance");
                                        String mpStr = tpMaintenance.getPrePic();
                                        if (StringUtils.isBlank(mpStr)) {
                                            out.print("无");
                                        } else {
                                            String[] mpList = mpStr.split("\\|");
                                            for (String imgSrc : mpList) {
                                                if (matcherRegularExpression("(gif|jpg|jpeg|png|GIF|JPG|PNG)$", imgSrc)) {
                                    %>
                                    <img onclick="jp.showPic('<%=imgSrc%>');$('.fixed-table-body').css('overflow-x','hidden')"
                                         width="80%" src="<%=imgSrc%>">
                                    <%
                                    } else {
                                        String fileName = imgSrc.substring(imgSrc.lastIndexOf("/") + 1);
                                        fileName = URLDecoder.decode(fileName, Charsets.UTF_8_NAME);
                                    %>
                                    <br>
                                    <a href="<%=imgSrc%>" target="_blank" title="点击下载<%=fileName%>"><%=fileName%>
                                    </a>
                                    <%
                                                }
                                            }
                                        }
                                    %>
                                </shiro:lacksPermission>
                            </div>
                            <label class="col-sm-2 control-label">施工中照片：</label>
                            <div class="col-sm-2">
                                <shiro:hasPermission name="tp:maintenance:tpMaintenance:weiBao">
                                    <sys:fileUpload path="middlePic" value="${tpMaintenance.middlePic}" type="file"
                                                    uploadPath="/tp/maintenance/tpMaintenance"/>
                                </shiro:hasPermission>
                                <shiro:lacksPermission name="tp:maintenance:tpMaintenance:weiBao">
                                    <input type="hidden" name="middlePic" value="${tpMaintenance.middlePic}">
                                    <%
                                        TpMaintenance tpMaintenance = (TpMaintenance) request.getAttribute("tpMaintenance");
                                        String mpStr = tpMaintenance.getMiddlePic();
                                        if (StringUtils.isBlank(mpStr)) {
                                            out.print("无");
                                        } else {
                                            String[] mpList = mpStr.split("\\|");
                                            for (String imgSrc : mpList) {
                                                if (matcherRegularExpression("(gif|jpg|jpeg|png|GIF|JPG|PNG)$", imgSrc)) {
                                    %>
                                    <img onclick="jp.showPic('<%=imgSrc%>');"
                                         width="80%" src="<%=imgSrc%>">
                                    <%
                                    } else {
                                        String fileName = imgSrc.substring(imgSrc.lastIndexOf("/") + 1);
                                        fileName = URLDecoder.decode(fileName, Charsets.UTF_8_NAME);
                                    %>
                                    <br>
                                    <a href="<%=imgSrc%>" target="_blank" title="点击下载<%=fileName%>"><%=fileName%>
                                    </a>
                                    <%
                                                }
                                            }
                                        }
                                    %>
                                </shiro:lacksPermission>

                            </div>
                            <label class="col-sm-2 control-label">施工后照片：</label>
                            <div class="col-sm-2">
                                <shiro:hasPermission name="tp:maintenance:tpMaintenance:weiBao">
                                    <sys:fileUpload path="afterPic" value="${tpMaintenance.afterPic}" type="file"
                                                    uploadPath="/tp/maintenance/tpMaintenance"/>
                                </shiro:hasPermission>
                                <shiro:lacksPermission name="tp:maintenance:tpMaintenance:weiBao">
                                    <input type="hidden" name="afterPic" value="${tpMaintenance.afterPic}">
                                    <%
                                        TpMaintenance tpMaintenance = (TpMaintenance) request.getAttribute("tpMaintenance");
                                        String mpStr = tpMaintenance.getAfterPic();
                                        if (StringUtils.isBlank(mpStr)) {
                                            out.print("无");
                                        } else {
                                            String[] mpList = mpStr.split("\\|");
                                            for (String imgSrc : mpList) {
                                                if (matcherRegularExpression("(gif|jpg|jpeg|png|GIF|JPG|PNG)$", imgSrc)) {
                                    %>
                                    <img onclick="jp.showPic('<%=imgSrc%>');$('.fixed-table-body').css('overflow-x','hidden')"
                                         width="80%" src="<%=imgSrc%>">
                                    <%
                                    } else {
                                        String fileName = imgSrc.substring(imgSrc.lastIndexOf("/") + 1);
                                        fileName = URLDecoder.decode(fileName, Charsets.UTF_8_NAME);
                                    %>
                                    <br>
                                    <a href="<%=imgSrc%>" target="_blank" title="点击下载<%=fileName%>"><%=fileName%>
                                    </a>
                                    <%
                                                }
                                            }
                                        }
                                    %>
                                </shiro:lacksPermission>

                            </div>
                        </div>
                        <div class="form-group">
                            <shiro:hasPermission name="tp:maintenance:tpMaintenance:approveEnabled">
                                <label class="col-sm-2 control-label">审批意见：</label>
                                <div class="col-sm-4">
                                    <shiro:hasPermission name="tp:maintenance:tpMaintenance:weiBao">
                                        <form:textarea path="approve" htmlEscape="false" rows="4" class="form-control "/>
                                    </shiro:hasPermission>
                                    <shiro:lacksPermission name="tp:maintenance:tpMaintenance:weiBao">
                                        <input type="hidden" name="approve" value="${tpMaintenance.approve}">
                                        ${tpMaintenance.approve}
                                    </shiro:lacksPermission>
                                </div>
                            </shiro:hasPermission>
                            <c:if test="${fns:getUser().admin or fns:getUser().admin or fns:getUser().roleNames.contains('系统管理员')}">
                                <label class="col-sm-2 control-label">任务状态：</label>
                                <div class="col-sm-4">
                                    <form:select path="status" class="form-control required">
                                        <form:option value="" label=""/>
                                        <form:options items="${fns:getDictList('job_status')}" itemLabel="label"
                                                      itemValue="value" htmlEscape="false"/>
                                    </form:select>
                                </div>
                            </c:if>
                        </div>
                        <div class="tabs-container">
                            <ul class="nav nav-tabs">
                                <li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">施工物料明细：</a>
                                </li>
                                <shiro:hasPermission name="tp:maintenance:tpMaintenance:money">
                                    <div style="float:right;padding-top:2px;padding-right:15px;line-height: 40px;">
                                        <label style="display:inline">物料总成本：</label>
                                        <span id="my-money-span">${tpMaintenance.money}元</span>
                                        <input id="money" name="money" value="${tpMaintenance.money}" type="hidden"
                                               class="form-control  isFloatGtZero">
                                    </div>
                                </shiro:hasPermission>
                            </ul>
                            <div class="tab-content">
                                <div id="tab-1" class="tab-pane fade in  active">

                                    <shiro:hasPermission name="tp:maintenance:tpMaintenance:weiBao">
                                        <div class="form-inline" style="height: 42px;line-height: 38px;">
                                            <a class="btn btn-white btn-large"
                                               onclick="addRow('#tpMaintenanceItemList', tpMaintenanceItemRowIdx, tpMaintenanceItemTpl);tpMaintenanceItemRowIdx = tpMaintenanceItemRowIdx + 1;"
                                               title="新增"><i class="fa fa-plus"></i> 添加物料</a>
                                            <input type="text" class="form-control "
                                                   style="width: 450px;display: inline-block!important;"
                                                   placeholder="关键词搜索后回车(快速添加方式)" id="my-mp-autocomplete">
                                            <a class="btn btn-white btn-mini"
                                               onclick="jp.openViewDialog('物料基础数据管理', '${ctx}/tp/material/tpMaterial', '1000px', '550px');"
                                               title="新增"><i class="fa fa-plus"></i> 先去添加基础数据</a>
                                                <%--<textarea id="___mptext___" style="display: none;"></textarea>--%>
                                            <button id="my-copy-btn" class="btn btn-primary btn-mini" type="button"
                                                    style="float: right;margin-top: 5px;" data-clipboard-text="#___mptext___">
                                                <i class="fa fa-copy"></i>
                                                复制物料明细为文本
                                            </button>
                                                <%--<button id="my-copy-btn-work" type="button" style="display: none"--%>
                                                <%--></button>--%>
                                        </div>
                                    </shiro:hasPermission>
                                    <table class="table table-striped table-bordered table-condensed">
                                        <thead>
                                        <tr>
                                            <th class="hide"></th>
                                            <th><font color="red">*</font>零件名称</th>
                                            <th>所属品类</th>
                                            <th>单位</th>
                                            <shiro:hasPermission name="tp:maintenance:tpMaintenance:money">
                                                <th>单价</th>
                                            </shiro:hasPermission>
                                            <th><font color="red">*</font>数量</th>
                                            <shiro:hasPermission name="tp:maintenance:tpMaintenance:money">
                                                <th>金额</th>
                                            </shiro:hasPermission>
                                            <th>维保方式</th>
                                            <shiro:hasPermission name="tp:maintenance:tpMaintenance:weiBao">
                                                <th width="10">&nbsp;</th>
                                            </shiro:hasPermission>
                                        </tr>
                                        </thead>
                                        <tbody id="tpMaintenanceItemList">
                                        </tbody>
                                    </table>
                                    <script type="text/html" id="tpMaintenanceItemTpl">//<!--
                                        <tr id="tpMaintenanceItemList{{idx}}">
                                            <td class="hide">
                                                <input id="tpMaintenanceItemList{{idx}}_id" name="tpMaintenanceItemList[{{idx}}].id"
                                                       type="hidden" value="{{row.id}}"/>
                                                <input id="tpMaintenanceItemList{{idx}}_delFlag"
                                                       name="tpMaintenanceItemList[{{idx}}].delFlag" type="hidden" value="0"/>
                                            </td>

                                            <td>
                                                <shiro:hasPermission name="tp:maintenance:tpMaintenance:weiBao">
                                                    <sys:gridselect url="${ctx}/tp/material/tpMaterialPart/data"
                                                                    id="tpMaintenanceItemList{{idx}}_materialPart"
                                                                    name="tpMaintenanceItemList[{{idx}}].materialPart.id"
                                                                    value="{{row.materialPart.id}}"
                                                                    labelName="tpMaintenanceItemList{{idx}}.materialPart.name"
                                                                    labelValue="{{row.materialPart.name}}"
                                                                    title="选择零件名称" cssClass="form-control required my-select-material-part"
                                                                    fieldLabels="零件名称|零件单位|零件单价|所属品类"
                                                                    fieldKeys="name|unit|price|material.name" searchLabels="零件名称|所属品类"
                                                                    searchKeys="name|material.name">
                                                    </sys:gridselect>
                                                </shiro:hasPermission>
                                                <shiro:lacksPermission name="tp:maintenance:tpMaintenance:weiBao">
                                                    <input type="hidden" name="tpMaintenanceItemList[{{idx}}].materialPart.id"
                                                           value="{{row.materialPart.id}}">
                                                    {{row.materialPart.name}}
                                                </shiro:lacksPermission>
                                            </td>

                                            <td>
                                                <shiro:hasPermission name="tp:maintenance:tpMaintenance:weiBao">
                                                    <input id="tpMaintenanceItemList{{idx}}_category" readonly="readonly"
                                                           name="tpMaintenanceItemList[{{idx}}].category" type="text"
                                                           value="{{row.category}}"
                                                           class="form-control my-category-name"/>
                                                </shiro:hasPermission>
                                                <shiro:lacksPermission name="tp:maintenance:tpMaintenance:weiBao">
                                                    <input type="hidden" name="tpMaintenanceItemList[{{idx}}].category"
                                                           value="{{row.category}}">
                                                    {{row.category}}
                                                </shiro:lacksPermission>
                                            </td>

                                            <td>

                                                <shiro:hasPermission name="tp:maintenance:tpMaintenance:weiBao">
                                                    <select id="tpMaintenanceItemList{{idx}}_unit" readonly="readonly"
                                                            name="tpMaintenanceItemList[{{idx}}].unit" data-value="{{row.unit}}"
                                                            class="form-control m-b  required my-unit" style="min-width:45px;">
                                                        <option value=""></option>
                                                        <c:forEach items="${fns:getDictList('material_unit')}" var="dict">
                                                            <option value="${dict.value}">${dict.label}</option>
                                                        </c:forEach>
                                                    </select>
                                                </shiro:hasPermission>
                                                <shiro:lacksPermission name="tp:maintenance:tpMaintenance:weiBao">
                                                    <input type="hidden" name="tpMaintenanceItemList[{{idx}}].unit" value="{{row.unit}}">
                                                    {{row.unitName}}
                                                </shiro:lacksPermission>
                                            </td>

                                            <shiro:hasPermission name="tp:maintenance:tpMaintenance:money">
                                                <td>
                                                    <input id="tpMaintenanceItemList{{idx}}_price" type="text" autocomplete="off"
                                                           name="tpMaintenanceItemList[{{idx}}].price" value="{{row.price}}"
                                                           class="form-control isFloatGteZero my-price "/>
                                                </td>
                                            </shiro:hasPermission>
                                            <shiro:lacksPermission name="tp:maintenance:tpMaintenance:money">
                                                <input id="tpMaintenanceItemList{{idx}}_price" type="hidden" autocomplete="off"
                                                       name="tpMaintenanceItemList[{{idx}}].price"  value="{{row.price}}"
                                                       class="form-control my-price"/>
                                            </shiro:lacksPermission>

                                            <td>
                                                <shiro:hasPermission name="tp:maintenance:tpMaintenance:weiBao">
                                                    <input id="tpMaintenanceItemList{{idx}}_count" type="number" autocomplete="off"
                                                           name="tpMaintenanceItemList[{{idx}}].count"  value="{{row.count}}"
                                                           class="form-control required isIntGtZero my-count"/>
                                                </shiro:hasPermission>
                                                <shiro:lacksPermission name="tp:maintenance:tpMaintenance:weiBao">
                                                    <input type="hidden" name="tpMaintenanceItemList[{{idx}}].count"
                                                           value="{{row.count}}">
                                                    {{row.count}}
                                                </shiro:lacksPermission>

                                            </td>

                                            <shiro:hasPermission name="tp:maintenance:tpMaintenance:money">
                                                <td>
                                                    <input id="tpMaintenanceItemList{{idx}}_money" readonly="readonly"
                                                           name="tpMaintenanceItemList[{{idx}}].money" type="text" value="{{row.money}}"
                                                           class="form-control my-money"/>
                                                </td>
                                            </shiro:hasPermission>
                                            <shiro:lacksPermission name="tp:maintenance:tpMaintenance:money">
                                                <input id="tpMaintenanceItemList{{idx}}_money" readonly="readonly"
                                                       name="tpMaintenanceItemList[{{idx}}].money" type="hidden" value="{{row.money}}"
                                                       class="form-control my-money"/>
                                            </shiro:lacksPermission>

                                            <td>

                                                  <shiro:hasPermission name="tp:maintenance:tpMaintenance:weiBao">
                                                    <select id="tpMaintenanceItemList{{idx}}_remarks" readonly="readonly"
                                                            name="tpMaintenanceItemList[{{idx}}].remarks" data-value="{{row.remarks}}"
                                                            class="form-control m-b  required my-remarks" style="min-width:45px;">
                                                        <option value=""></option>
                                                        <c:forEach items="${fns:getDictList('material_verb')}" var="dict">
                                                            <option value="${dict.value}">${dict.label}</option>
                                                        </c:forEach>
                                                    </select>
                                                </shiro:hasPermission>
                                                <shiro:lacksPermission name="tp:maintenance:tpMaintenance:weiBao">
                                                    <input type="hidden" name="tpMaintenanceItemList[{{idx}}].remarks" value="{{row.remarks}}">
                                                </shiro:lacksPermission>

                                            </td>

                                            <shiro:hasPermission name="tp:maintenance:tpMaintenance:weiBao">
                                                <td class="text-center" width="10">
                                                    {{#delBtn}}<span class="close btn my-close" data-toggle="tooltip" data-placement="right"
                                                                     onclick="delRow(this, '#tpMaintenanceItemList{{idx}}')"
                                                                     title="删除">&times;</span>{{/delBtn}}
                                                </td>
                                            </shiro:hasPermission>


                                        </tr>//-->
                                    </script>
                                    <script type="text/javascript">
                                        var tpMaintenanceItemRowIdx = 0,
                                            tpMaintenanceItemTpl = $("#tpMaintenanceItemTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "");
                                        $(document).ready(function () {
                                            var data = ${fns:toJson(tpMaintenance.tpMaintenanceItemList)};
                                            for (var i = 0; i < data.length; i++) {
                                                addRow('#tpMaintenanceItemList', tpMaintenanceItemRowIdx, tpMaintenanceItemTpl, data[i]);
                                                tpMaintenanceItemRowIdx = tpMaintenanceItemRowIdx + 1;
                                            }
                                        });
                                    </script>

                                </div>

                            </div>
                        </div>
                        <c:if test="${mode == 'add' || mode=='edit'}">
                            <div class="col-lg-3"></div>
                            <div class="col-lg-6">
                                <div class="text-center">
                                    <c:choose>
                                        <%-- 超级管理员和系统管理员都具有直接保存权限 --%>
                                        <c:when test="${fns:getUser().admin or fns:getUser().roleNames.contains('系统管理员')}">
                                            <button class="btn btn-primary btn-lg btn-parsley" style="width: 200px;"
                                                    data-loading-text="正在提交...">
                                                <i class="fa fa-save"></i>
                                                保存
                                            </button>
                                            <p class="text-danger" style="display: inline-block;margin-left: 20px;">注意：管理员修改数据要小心</p>
                                        </c:when>
                                        <c:otherwise>
                                            <input id="my-status" type="hidden" name="status" value="">

                                            <%--<h2>${tpMaintenance.status}</h2>--%>
                                            <c:if test="${tpMaintenance.status == null || tpMaintenance.status == ''}">
                                                <shiro:hasPermission name="tp:maintenance:tpMaintenance:paiDan">
                                                    <button class="btn btn-primary btn-lg btn-parsley" data-loading-text="正在提交..."
                                                            onclick="document.getElementById('my-status').value=1;">派单
                                                    </button>
                                                </shiro:hasPermission>
                                            </c:if>
                                            <c:if test="${ tpMaintenance.status == '1' || tpMaintenance.status == '2'}">
                                                <shiro:hasPermission name="tp:maintenance:tpMaintenance:save">
                                                    <button class="btn btn-primary btn-lg btn-parsley" data-loading-text="正在提交..."
                                                            onclick="document.getElementById('my-status').value=2;">保存草稿
                                                    </button>
                                                </shiro:hasPermission>
                                                <shiro:hasPermission name="tp:maintenance:tpMaintenance:done">
                                                    <button class="btn btn-primary btn-lg btn-parsley" data-loading-text="正在提交..."
                                                            onclick="document.getElementById('my-status').value=3;"
                                                            title="完成派单后，除管理员外不可再次修改!!">
                                                        完成派单
                                                    </button>
                                                </shiro:hasPermission>
                                            </c:if>
                                            <shiro:hasPermission name="tp:maintenance:tpMaintenance:approveEnabled">

                                                <c:if test="${ tpMaintenance.status == '3'}">
                                                    <shiro:hasPermission name="tp:maintenance:tpMaintenance:approveSubmit">
                                                        <button class="btn btn-primary btn-lg btn-parsley" data-loading-text="正在提交..."
                                                                onclick="document.getElementById('my-status').value=4;">提交审核
                                                        </button>
                                                    </shiro:hasPermission>
                                                </c:if>
                                                <c:if test="${ tpMaintenance.status == '4' ||  tpMaintenance.status == '5'}">
                                                    <shiro:hasPermission name="tp:maintenance:tpMaintenance:approveYes">
                                                        <button class="btn btn-primary btn-lg btn-parsley" data-loading-text="正在提交..."
                                                                onclick="document.getElementById('my-status').value=5;">审批通过
                                                        </button>
                                                    </shiro:hasPermission>
                                                    <shiro:hasPermission name="tp:maintenance:tpMaintenance:approveNo">
                                                        <button class="btn btn-primary btn-lg btn-parsley" data-loading-text="正在提交..."
                                                                onclick="document.getElementById('my-status').value=6;">审批不予通过
                                                        </button>
                                                    </shiro:hasPermission>
                                                </c:if>
                                            </shiro:hasPermission>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </c:if>
                    </form:form>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="${ctxStatic}/plugin/clipboard/clipboard.min.js"></script>
<script src="${ctxStatic}/plugin/jquery-autocomplete/jquery.easy-autocomplete.min.js"></script>
<script type="text/javascript">
    $(document).ready(function () {
        jp.ajaxForm("#inputForm", function (data) {
            if (data.success) {
                jp.success(data.msg);
                jp.go("${ctx}/tp/maintenance/tpMaintenance");
            } else {
                jp.error(data.msg);
                $("#inputForm").find("button:submit").button("reset");
            }
        });


        // 日期显示和，时间前后比较验证
        var sendDateDP = $('#sendDate').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
        var jobBeginDateDP = $('#jobBeginDate').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
        var jobEndDateDP = $('#jobEndDate').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});

        //动态设置最小值和最大值
        sendDateDP.on('dp.change', function (e) {
            jobBeginDateDP.data('DateTimePicker').minDate(e.date);
        });
        jobBeginDateDP.on('dp.change', function (e) {
            jobEndDateDP.data('DateTimePicker').minDate(e.date);
            sendDateDP.data('DateTimePicker').maxDate(e.date);
        });
        jobEndDateDP.on('dp.change', function (e) {
            jobBeginDateDP.data('DateTimePicker').maxDate(e.date);
        });

        //富文本初始化
        $('#process').summernote({
            height: 300,
            lang: 'zh-CN',
            callbacks: {
                onChange: function (contents, $editable) {
                    $("input[name='process']").val($('#process').summernote('code'));//取富文本的值
                }
            }
        });

        //富文本初始化
        $('#jobDescription').summernote({
            height: 300,
            lang: 'zh-CN',
            callbacks: {
                onChange: function (contents, $editable) {
                    $("input[name='jobDescription']").val($('#jobDescription').summernote('code'));//取富文本的值
                }
            }
        });

        // 零件单价，不允许输入非数字
        $(document).on("change", ".my-price", function () {
            var v = $(this).val();
            v = parseFloat(v);
            if (v >= 0) {
                v = v.toFixed(2);
                $(this).val(v);
            } else {
                $(this).val(0);
            }
            // calMoney();
        });

        // 零件数量，不允许输入非整数
        $(document).on("keyup", ".my-count", function () {
            var v = $(this).val();
            v = parseInt(v);
            if (v >= 0) {
                $(this).val(v);
            } else {
                $(this).val(0);
            }
            // calMoney();
        });

        // 价格计算
        $(document).on("keyup", ".my-count,.my-price", calMoney);

        // 自动完成物料零件，回车自动创建一行
        var _mp_id;
        $("#my-mp-autocomplete").easyAutocomplete({
            url: function (query) {
                return '${ctx}/tp/material/tpMaterialPart/autocomplete?query=' + query;
            },
            getValue: function (result) {
                _mp_id = result.code;
                return result.name;
            },
            list: {
                onSelectItemEvent: function () {
                    return false;
                },
                onChooseEvent: function () {
                    // 验证是否已经存在
                    var size = $('#tpMaintenanceItemList').find(':hidden').filter(function () {
                        return $(this).val() == _mp_id;
                    }).size();
                    if (size > 0) {
                        jp.warning('您已经添加过这个物料!');
                        $('#my-mp-autocomplete').select();
                        return false;
                    }

                    // 获取物料信息，自动填写表单
                    jp.get("${ctx}/tp/material/tpMaterialPart/getById?id=" + _mp_id, function (dataRow) {
                        if (dataRow) {
                            addRow('#tpMaintenanceItemList', tpMaintenanceItemRowIdx, tpMaintenanceItemTpl, {}, true);
                            var domRowId = '#tpMaintenanceItemList' + tpMaintenanceItemRowIdx;
                            var smp = $(domRowId).find('.my-select-material-part');
                            smp.val(dataRow['name']);
                            smp.parent().prev(':hidden').val(dataRow['id']);
                            $(domRowId).find('.my-category-name').val(dataRow['material']['name']);
                            $(domRowId).find('.my-unit').val(dataRow['unit']);
                            $(domRowId).find('.my-price').val(dataRow['price']);
                            tpMaintenanceItemRowIdx = tpMaintenanceItemRowIdx + 1;

                            jp.success('增加成功，您可以快速变换输入，增加下一个物料');
                            var st = setTimeout(function () {
                                $('#my-mp-autocomplete').val('').focus();
                                clearTimeout(st);
                            }, 300)
                        } else {
                            jp.error('服务器发生错误，快速添加方式不可用,请使用左侧新增物料按钮');
                        }
                    });
                }
            }
        });

        // $('#jobType').change(function () {
        //     var v = $(this).val();
        //     if (v === '1') { // 信号灯
        //         $('#roadName').parents('.col-sm-4').prev().text('附近道路：');
        //     } else {
        //         $('#roadName').parents('.col-sm-4').prev().text('所属道路：');
        //
        //     }
        // });


        // 生成文本，复制到剪贴板
        var clipboard = new ClipboardJS('#my-copy-btn');
        clipboard.on('success', function (e) {
            console.info('Action:', e.action);
            console.info('Text:', e.text);
            console.info('Trigger:', e.trigger);
            if (e.text) {
                jp.success('已经复制到剪贴板：<br/>' + e.text);
            }
            // e.clearSelection();
            // clipboard.destroy();
        });
        $('#my-copy-btn').click(function () {
            var rows = $('#tpMaintenanceItemList').children('tr').filter(function (i, n) {
                return $(n).find('.my-close').attr('title') === '删除';
            });
            var text = $.map(rows, function (n, i) {
                var row = $(n);
                var name = row.find('.my-select-material-part').val();
                var count = row.find('.my-count').val();
                var unit = row.find('.my-unit').find("option:selected").text();
                return name + '用了' + count + unit;
            }).join('，');
            $(this).attr('data-clipboard-text', '使用物料如下：\n' + text + '。'); // 设置要复制的文本
        });


    });

    function addRow(list, idx, tpl, row, notAutoOpen) {
        <shiro:lacksPermission name="tp:maintenance:tpMaintenance:weiBao">
        row.unitName = jp.getDictLabel(${fns:toJson(fns:getDictList('material_unit'))}, row.unit, '');
        </shiro:lacksPermission>
        var $row = $(Mustache.render(tpl, {
            idx: idx, delBtn: true, row: row
        }));
        $(list).append($row);
        $(list + idx).find("select").each(function () {
            $(this).val($(this).attr("data-value"));
        });
        $(list + idx).find("input[type='checkbox'], input[type='radio']").each(function () {
            var ss = $(this).attr("data-value").split(',');
            for (var i = 0; i < ss.length; i++) {
                if ($(this).val() == ss[i]) {
                    $(this).attr("checked", "checked");
                }
            }
        });
        $(list + idx).find(".form_datetime").each(function () {
            $(this).datetimepicker({
                format: "YYYY-MM-DD"
            });
        });
        // 自动打开选择物料零件窗口
        if (!notAutoOpen) $row.find('.my-select-material-part').trigger('click');
        // $row.find('.my-select-material-part').click();
    }

    function delRow(obj, prefix) {
        var id = $(prefix + "_id");
        var delFlag = $(prefix + "_delFlag");
        if (id.val() == "") {
            $(obj).parent().parent().remove();
        } else if (delFlag.val() == "0") {
            delFlag.val("1");
            $(obj).html("&divide;").attr("title", "撤销删除");
            $(obj).parent().parent().addClass("error");
        } else if (delFlag.val() == "1") {
            delFlag.val("0");
            $(obj).html("&times;").attr("title", "删除");
            $(obj).parent().parent().removeClass("error");
        }
        calMoney();
    }


    // 合计金额方法
    function calMoney() {
        var allMoney = 0.0;
        var context = $('.close').filter(function () {
            return $(this).attr('title') == '删除';
        }).parents('tr');
        $('.my-count', context).each(function (idx, ele) {
            var count = $(ele).val();
            var row = $(ele).parents('tr');
            var price = row.find('.my-price').val();
            if (count && price) {
                var result = parseFloat(count) * parseFloat(price);
                result = result.toFixed(2);
                row.find('.my-money').val(result);
                allMoney += parseFloat(result);
            } else {
                row.find('.my-money').val(0);
            }
        });
        allMoney = parseFloat(allMoney).toFixed(2);
        $('#money').val(allMoney);
        $('#my-money-span').text(allMoney + "¥");
    }

    // 施工物料选择回调函数
    function gridselectChange() { // 必须采用闭包的方式
        return function (id, items) {
            var domRowId = $('#' + id.split('_')[0]).get(0);
            var dataRow = items[0]; //单选
            if (domRowId && dataRow) {
                $(domRowId).find('.my-category-name').val(dataRow['material']['name']);
                $(domRowId).find('.my-unit').val(dataRow['unit']);
                $(domRowId).find('.my-price').val(dataRow['price']);
                // 修改零件，可能会引起金额变化，需要重新计算
                calMoney();
            }
        }
    }

    // 选择施工物料时，点击取消，清理空行
    function gridselectCancel() {
        return function (index) {
            var preAddRow = $('#tpMaintenanceItemList').children().last();
            if (!$(preAddRow).find('.my-category-name').val()) {
                $(preAddRow).find('.close').trigger('click');
            }
        }
    }

    // 地址选择，保存成功后，显示到施工管理表单控件中
    function postionSelectCallback(param) {
        // console.log(param);
        if (param && param.tpMaintenance) {
            var tm = param.tpMaintenance;

            var location = tm.location;
            $('#location').val(location);

            var area = tm.area;
            $('#areaName').val(area.name);
            $('#areaId').val(area.id);

            var roadcross = tm.roadcross;
            $('#roadcrossName').val(roadcross.name);
            $('#roadcrossId').val(roadcross.id);

            var nearestJunction = tm.nearestJunction;
            $('#nearestJunction').val(nearestJunction);

            var road = tm.road;
            $('#roadName').val(road.name);
            $('#roadId').val(road.id);

            $('#road\\.roadType').val(road.roadType);

            var address = tm.address;
            $('#address').val(address);

            var nearestPoi = tm.nearestPoi;
            $('#nearestPoi').val(nearestPoi);

            var position = roadcross.name + nearestJunction;
            $('#postionBtn').attr('title', position).text(position);

            //    打开详细地址信息
            $('#_addressDetail_show').trigger('click');
        }
    }

    // 打开选择详细地址对话框
    function openSelectPostionDialog() {
        var location = $('#location').val();
        var roadcrossName = $('#roadcrossName').val();
        jp.openChildDialog("编辑位置", "${ctx}/tp/maintenance/tpMaintenance/selectPostion?roadcrossName=" + roadcrossName + "&location=" + location, "1050px", "580px", postionSelectCallback);
    }


</script>
</body>
</html>