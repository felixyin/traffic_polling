<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>施工管理</title>
    <meta name="decorator" content="ani"/>
    <!-- SUMMERNOTE -->
    <%@include file="/webpage/include/summernote.jsp" %>
    <link href="${ctxStatic}/plugin/jquery-autocomplete/easy-autocomplete.min.css" rel="stylesheet">
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

            $('#sendDate').datetimepicker({
                format: "YYYY-MM-DD HH:mm:ss"
            });
            $('#jobBeginDate').datetimepicker({
                format: "YYYY-MM-DD HH:mm:ss"
            });
            $('#jobEndDate').datetimepicker({
                format: "YYYY-MM-DD HH:mm:ss"
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

            // 合计金额方法
            $(document).on("keyup", ".my-count,.my-price", function () {
                var allMoney = 0;
                $('.my-count').each(function (idx, ele) {
                    var count = $(ele).val();
                    var row = $(ele).parents('tr');
                    var price = row.find('.my-price').val();
                    if (count && price) {
                        var result = parseInt(count) * parseInt(price);
                        row.find('.my-money').val(result);
                        allMoney += result;
                    } else {
                        row.find('.my-money').val(0);
                    }
                });
                $('#my-money-id').val(allMoney);
            });

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
        });

        function addRow(list, idx, tpl, row, notAutoOpen) {
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
                    format: "YYYY-MM-DD HH:mm:ss"
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

                var address = tm.address;
                $('#address').val(address);

                var nearestPoi = tm.nearestPoi;
                $('#nearestPoi').val(nearestPoi);
            }
        }

        // 打开选择详细地址对话框
        function openSelectPostionDialog() {
            var location = $('#location').val();
            var roadcrossName = $('#roadcrossName').val();
            jp.openChildDialog("编辑位置", "${ctx}/tp/maintenance/tpMaintenance/selectPostion?roadcrossName=" + roadcrossName + "&location=" + location, "1050px", "580px", postionSelectCallback);
        }
    </script>
    <style type="text/css">
        .panel-body {
            width: 95% !important;
        }

        .easy-autocomplete {
            display: inline-block;
            z-index: 1000;
        }

        .note-editing-area {
            height: 120px;
        }
    </style>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="row">
        <div class="col-md-12">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h3 class="panel-title">
                        <a class="panelButton" href="${ctx}/tp/maintenance/tpMaintenance"><i class="ti-angle-left"></i>
                            返回</a>
                    </h3>
                </div>
                <div class="panel-body">
                    <form:form id="inputForm" modelAttribute="tpMaintenance"
                               action="${ctx}/tp/maintenance/tpMaintenance/save" method="post" class="form-horizontal">
                        <form:hidden path="id"/>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>任务编号：</label>
                            <div class="col-sm-10">
                                <form:input path="num" htmlEscape="false" class="form-control required"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>任务类型：</label>
                            <div class="col-sm-10">
                                <form:select path="jobType" class="form-control required">
                                    <form:option value="" label=""/>
                                    <form:options items="${fns:getDictList('job_type')}" itemLabel="label"
                                                  itemValue="value" htmlEscape="false"/>
                                </form:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>任务来源：</label>
                            <div class="col-sm-10">
                                <form:select path="source" class="form-control required">
                                    <form:option value="" label=""/>
                                    <form:options items="${fns:getDictList('job_source')}" itemLabel="label"
                                                  itemValue="value" htmlEscape="false"/>
                                </form:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>选择位置：</label>
                            <div class="col-sm-10">
                                <input type="button" class="btn btn-primary btn-block  btn-parsley"
                                       data-loading-text="正在计算..." value="选择地点"
                                       onclick="openSelectPostionDialog();"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>经纬度：</label>
                            <div class="col-sm-10">
                                <form:input path="location" htmlEscape="false" class="form-control required"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>所属区域：</label>
                            <div class="col-sm-10">
                                <sys:treeselect id="area" name="area.id" value="${tpMaintenance.area.id}"
                                                labelName="area.name" labelValue="${tpMaintenance.area.name}"
                                                title="区域" url="/sys/area/treeData" cssClass="form-control required"
                                                allowClear="true" notAllowSelectParent="true"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>所属路口：</label>
                            <div class="col-sm-10">
                                <sys:gridselect url="${ctx}/tp/roadcross/tpRoadCrossing/data" id="roadcross"
                                                name="roadcross.id" value="${tpMaintenance.roadcross.id}"
                                                labelName="roadcross.name" labelValue="${tpMaintenance.roadcross.name}"
                                                title="选择所属路口" cssClass="form-control required"
                                                fieldLabels="路口名称|所属区域|所属街道" fieldKeys="name|sarea.name|township"
                                                searchLabels="路口名称|所属区域" searchKeys="name|sarea.name"></sys:gridselect>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>所属路口相对位置：</label>
                            <div class="col-sm-10">
                                <form:input path="nearestJunction" htmlEscape="false" class="form-control required"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">所属道路：</label>
                            <div class="col-sm-10">
                                <sys:gridselect url="${ctx}/tp/road/tpRoad/data" id="road" name="road.id"
                                                value="${tpMaintenance.road.id}" labelName="road.name"
                                                labelValue="${tpMaintenance.road.name}"
                                                title="选择所属道路" cssClass="form-control " fieldLabels="道路名称|所属区域"
                                                fieldKeys="name|sarea.name" searchLabels="道路名称|所属区域"
                                                searchKeys="name|sarea.name"></sys:gridselect>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">搜索用地址：</label>
                            <div class="col-sm-10">
                                <form:input path="address" htmlEscape="false" class="form-control "/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">搜索地址相对位置：</label>
                            <div class="col-sm-10">
                                <form:input path="nearestPoi" htmlEscape="false" class="form-control "/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">派单人：</label>
                            <div class="col-sm-10">
                                <sys:userselect id="sendBy" name="sendBy.id" value="${tpMaintenance.sendBy.id}"
                                                labelName="sendBy.name" labelValue="${tpMaintenance.sendBy.name}"
                                                cssClass="form-control "/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">派单时间：</label>
                            <div class="col-sm-10">
                                <div class='input-group form_datetime' id='sendDate'>
                                    <input type='text' name="sendDate" class="form-control "
                                           value="<fmt:formatDate value="${tpMaintenance.sendDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                    <span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>施工单位：</label>
                            <div class="col-sm-10">
                                <sys:treeselect id="office" name="office.id" value="${tpMaintenance.office.id}"
                                                labelName="office.name" labelValue="${tpMaintenance.office.name}"
                                                title="部门" url="/sys/office/treeData?type=2"
                                                cssClass="form-control required" allowClear="true"
                                                notAllowSelectParent="true"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>施工开始时间：</label>
                            <div class="col-sm-10">
                                <div class='input-group form_datetime' id='jobBeginDate'>
                                    <input type='text' name="jobBeginDate" class="form-control required"
                                           value="<fmt:formatDate value="${tpMaintenance.jobBeginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                    <span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>施工结束时间：</label>
                            <div class="col-sm-10">
                                <div class='input-group form_datetime' id='jobEndDate'>
                                    <input type='text' name="jobEndDate" class="form-control required"
                                           value="<fmt:formatDate value="${tpMaintenance.jobEndDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                    <span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">总费用：</label>
                            <div class="col-sm-10">
                                <form:input path="money" htmlEscape="false" class="form-control  isFloatGtZero"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">施工过程：</label>
                            <div class="col-sm-10">
                                <input type="hidden" name="process" value=" ${tpMaintenance.process}"/>
                                <div id="process">
                                        ${fns:unescapeHtml(tpMaintenance.process)}
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">施工前照片：</label>
                            <div class="col-sm-10">
                                <sys:fileUpload path="prePic" value="${tpMaintenance.prePic}" type="file"
                                                uploadPath="/tp/maintenance/tpMaintenance"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">施工中照片：</label>
                            <div class="col-sm-10">
                                <sys:fileUpload path="middlePic" value="${tpMaintenance.middlePic}" type="file"
                                                uploadPath="/tp/maintenance/tpMaintenance"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">施工后照片：</label>
                            <div class="col-sm-10">
                                <sys:fileUpload path="afterPic" value="${tpMaintenance.afterPic}" type="file"
                                                uploadPath="/tp/maintenance/tpMaintenance"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">审批意见：</label>
                            <div class="col-sm-10">
                                <form:textarea path="approve" htmlEscape="false" rows="4" class="form-control "/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">任务状态：</label>
                            <div class="col-sm-10">
                                <form:radiobuttons path="status" items="${fns:getDictList('job_status')}"
                                                   itemLabel="label" itemValue="value" htmlEscape="false"
                                                   class="i-checks "/>
                            </div>
                        </div>
                        <div class="tabs-container">
                            <ul class="nav nav-tabs">
                                <li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">施工物料明细：</a>
                                </li>
                            </ul>
                            <div class="tab-content">
                                <div id="tab-1" class="tab-pane fade in  active">
                                    <div class="form-inline" style="height: 42px;line-height: 38px;">
                                        <a class="btn btn-white btn-large"
                                           onclick="addRow('#tpMaintenanceItemList', tpMaintenanceItemRowIdx, tpMaintenanceItemTpl);tpMaintenanceItemRowIdx = tpMaintenanceItemRowIdx + 1;"
                                           title="新增"><i class="fa fa-plus"></i> 新增物料</a>
                                        <input type="text" class="form-control "
                                               style="width: 450px;display: inline-block!important;"
                                               placeholder="关键词搜索后回车(快速添加方式)" id="my-mp-autocomplete">
                                    </div>
                                    <table class="table table-striped table-bordered table-condensed">
                                        <thead>
                                        <tr>
                                            <th class="hide"></th>
                                            <th><font color="red">*</font>零件名称</th>
                                            <th>所属品类</th>
                                            <th>单位</th>
                                            <th>单价</th>
                                            <th><font color="red">*</font>数量</th>
                                            <th>金额</th>
                                            <th>备注信息</th>
                                            <th width="10">&nbsp;</th>
                                        </tr>
                                        </thead>
                                        <tbody id="tpMaintenanceItemList">
                                        </tbody>
                                    </table>
                                    <script type="text/template" id="tpMaintenanceItemTpl">//<!--
				<tr id="tpMaintenanceItemList{{idx}}">
					<td class="hide">
						<input id="tpMaintenanceItemList{{idx}}_id" name="tpMaintenanceItemList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="tpMaintenanceItemList{{idx}}_delFlag" name="tpMaintenanceItemList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<sys:gridselect url="${ctx}/tp/material/tpMaterialPart/data" id="tpMaintenanceItemList{{idx}}_materialPart"
                            name="tpMaintenanceItemList[{{idx}}].materialPart.id" value="{{row.materialPart.id}}"
                            labelName="tpMaintenanceItemList{{idx}}.materialPart.name" labelValue="{{row.materialPart.name}}"
                            title="选择零件名称" cssClass="form-control required my-select-material-part" fieldLabels="零件名称|零件单位|零件单价|所属品类"
                            fieldKeys="name|unit|price|material.name" searchLabels="零件名称|所属品类" searchKeys="name|material.name" >
                        </sys:gridselect>
					</td>

					<td>
						<input id="tpMaintenanceItemList{{idx}}_category" name="tpMaintenanceItemList[{{idx}}].category" type="text" value="{{row.category}}"    class="form-control my-category-name"/>
					</td>

					<td>
						<select id="tpMaintenanceItemList{{idx}}_unit" name="tpMaintenanceItemList[{{idx}}].unit" data-value="{{row.unit}}" class="form-control m-b  required my-unit">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('material_unit')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
					</td>

					<td>
						<input id="tpMaintenanceItemList{{idx}}_price" name="tpMaintenanceItemList[{{idx}}].price" type="text" value="{{row.price}}"    class="form-control my-price"/>
					</td>
					
					
					<td>
						<input id="tpMaintenanceItemList{{idx}}_count" name="tpMaintenanceItemList[{{idx}}].count" type="text" value="{{row.count}}"    class="form-control required isIntGtZero"/>
					</td>
					
					
					<td>
						<input id="tpMaintenanceItemList{{idx}}_money" name="tpMaintenanceItemList[{{idx}}].money" type="text" value="{{row.money}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="tpMaintenanceItemList{{idx}}_remarks" name="tpMaintenanceItemList[{{idx}}].remarks" type="text" value="{{row.remarks}}"    class="form-control "/>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#tpMaintenanceItemList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
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
                                <div class="form-group text-center">
                                    <div>
                                        <button class="btn btn-primary btn-block btn-lg btn-parsley"
                                                data-loading-text="正在提交...">提 交
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                    </form:form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>