<%@ page contentType="text/html;charset=UTF-8" %>
<script>
    $(document).ready(function () {
        $('#tpCarTrackTable').bootstrapTable({

            //请求方法
            method: 'post',
            //类型json
            dataType: "json",
            contentType: "application/x-www-form-urlencoded",
            //显示检索按钮
            showSearch: true,
            //显示刷新按钮
            showRefresh: true,
            //显示切换手机试图按钮
            showToggle: true,
            //显示 内容列下拉框
            showColumns: true,
            //显示到处按钮
            showExport: true,
            //显示切换分页按钮
            showPaginationSwitch: true,
            //最低显示2行
            minimumCountColumns: 2,
            //是否显示行间隔色
            striped: true,
            //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            cache: false,
            //是否显示分页（*）
            pagination: true,
            //排序方式
            sortOrder: "asc",
            //初始化加载第一页，默认第一页
            pageNumber: 1,
            //每页的记录行数（*）
            pageSize: 10,
            //可供选择的每页的行数（*）
            pageList: [10, 25, 50, 100],
            //这个接口需要处理bootstrap table传递的固定参数,并返回特定格式的json数据
            url: "${ctx}/tp/cartrack/tpCarTrack/data",
            //默认值为 'limit',传给服务端的参数为：limit, offset, search, sort, order Else
            //queryParamsType:'',
            ////查询参数,每次调用是会带上这个参数，可自定义
            queryParams: function (params) {
                var searchParam = $("#searchForm").serializeJSON();
                searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
                searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
                searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
                return searchParam;
            },
            //分页方式：client客户端分页，server服务端分页（*）
            sidePagination: "server",
            contextMenuTrigger: "right",//pc端 按右键弹出菜单
            contextMenuTriggerMobile: "press",//手机端 弹出菜单，click：单击， press：长按。
            contextMenu: '#context-menu',
            onContextMenuItem: function (row, $el) {
                if ($el.data("item") == "edit") {
                    edit(row.id);
                } else if ($el.data("item") == "view") {
                    view(row.id);
                } else if ($el.data("item") == "delete") {
                    jp.confirm('确认要删除该出车记录记录吗？', function () {
                        jp.loading();
                        jp.get("${ctx}/tp/cartrack/tpCarTrack/delete?id=" + row.id, function (data) {
                            if (data.success) {
                                $('#tpCarTrackTable').bootstrapTable('refresh');
                                jp.success(data.msg);
                            } else {
                                jp.error(data.msg);
                            }
                        })

                    });

                }
            },

            onClickRow: function (row, $el) {
            },
            onShowSearch: function () {
                $("#search-collapse").slideToggle();
            },
            columns: [{
                checkbox: true

            }
                , {
                    field: 'id',
                    title: '查看/编辑',
                    sortable: false,
                    sortName: 'id'
                    , formatter: function (value, row, index) {

                        if (value == null || value == "") {
                            value = "-";
                        }
                        <c:choose>
                        <c:when test="${fns:hasPermission('tp:cartrack:tpCarTrack:edit')}">
                        return "<a href='javascript:edit(\"" + row.id + "\")'>修改</a>";
                        </c:when>
                        <c:when test="${fns:hasPermission('tp:cartrack:tpCarTrack:view')}">
                        return "<a href='javascript:view(\"" + row.id + "\")'>查看</a>";
                        </c:when>
                        <c:otherwise>
                        return value;
                        </c:otherwise>
                        </c:choose>

                    },
                },
                {
                    field: 'id',
                    title: '行驶轨迹',
                    sortable: false,
                    sortName: 'id'
                    , formatter: function (value, row, index) {

                        if (value == null || value == "") {
                            value = "-";
                        }
                        <c:choose>
                        <c:when test="${fns:hasPermission('tp:cartrack:tpCarTrack:edit')}">
                        return "<a href='javascript:viewTrack(\"" + row.id + "\")'>行驶轨迹</a>";
                        </c:when>
                        <c:when test="${fns:hasPermission('tp:cartrack:tpCarTrack:view')}">
                        return "<a href='javascript:viewTrack(\"" + row.id + "\")'>行驶轨迹</a>";
                        </c:when>
                        <c:otherwise>
                        return value;
                        </c:otherwise>
                        </c:choose>

                    }

                }
                // , {
                //     field: 'locationBegin',
                //     title: '开始位置',
                //     sortable: true,
                //     sortName: 'locationBegin'
                //
                // }
                ,
                {
                    field: 'nameBegin',
                    title: '开始位置地名',
                    sortable: true,
                    sortName: 'nameBegin'
                    , formatter: function (value, row, index) {
                        if (value == null || value == "") {
                            value = "-";
                        }
                        <c:choose>
                        <c:when test="${fns:hasPermission('tp:cartrack:tpCarTrack:edit')}">
                        return "<a href='javascript:viewLocation(" + JSON.stringify({
                            location: row.locationBegin,
                            locationName: row.nameBegin
                        }) + ")'>" + value + "</a>";
                        </c:when>
                        <c:when test="${fns:hasPermission('tp:cartrack:tpCarTrack:view')}">
                        return "<a href='javascript:viewLocation(" + JSON.stringify({
                            location: row.locationBegin,
                            locationName: row.nameBegin
                        }) + ")'>" + value + "</a>";
                        </c:when>
                        <c:otherwise>
                        return value;
                        </c:otherwise>
                        </c:choose>

                    },
                }
                // , {
                //     field: 'locationEnd',
                //     title: '结束位置',
                //     sortable: true,
                //     sortName: 'locationEnd'
                //
                // }
                , {
                    field: 'nameEnd',
                    title: '结束位置地名',
                    sortable: true,
                    sortName: 'nameEnd'
                    , formatter: function (value, row, index) {

                        if (value == null || value == "") {
                            value = "-";
                        }
                        <c:choose>
                        <c:when test="${fns:hasPermission('tp:cartrack:tpCarTrack:edit')}">
                        return "<a href='javascript:viewLocation(" + JSON.stringify({
                            location: row.locationEnd,
                            locationName: row.nameEnd
                        }) + ")'>" + value + "</a>";
                        </c:when>
                        <c:when test="${fns:hasPermission('tp:cartrack:tpCarTrack:view')}">
                        return "<a href='javascript:viewLocation(" + JSON.stringify({
                            location: row.locationEnd,
                            locationName: row.nameEnd
                        }) + ")'>" + value + "</a>";
                        </c:when>
                        <c:otherwise>
                        return value;
                        </c:otherwise>
                        </c:choose>

                    },
                },
                {
                    field: 'car.name',
                    title: '关联车辆',
                    sortable: true,
                    sortName: 'car.name'
                    , formatter: function (value, row, index) {
                        if (value == null || value == "") {
                            value = "-";
                        }
                        <c:choose>
                        <c:when test="${fns:hasPermission('tp:cartrack:tpCarTrack:edit')}">
                        return "<a href='javascript:viewCar(" + JSON.stringify({
                            carId: row.car?row.car.id:'',
                        }) + ")'>" + value + "</a>";
                        </c:when>
                        <c:when test="${fns:hasPermission('tp:cartrack:tpCarTrack:view')}">
                        return "<a href='javascript:viewCar(" + JSON.stringify({
                            carId: row.car?row.car.id:'',
                        }) + ")'>" + value + "</a>";
                        </c:when>
                        <c:otherwise>
                        return value;
                        </c:otherwise>
                        </c:choose>

                    },
                }
                , {
                    field: 'whatDay',
                    title: '星期几',
                    sortable: true,
                    sortName: 'whatDay',
                    formatter: function (value, row, index) {
                        return jp.getDictLabel(${fns:toJson(fns:getDictList('what_day'))}, value, "-");
                    }
                }
                , {
                    field: 'timeBegin',
                    title: '开始时间',
                    sortable: true,
                    sortName: 'timeBegin'

                }
                , {
                    field: 'timeEnd',
                    title: '结束时间',
                    sortable: true,
                    sortName: 'timeEnd'

                }
                , {
                    field: 'km',
                    title: '行驶里程',
                    sortable: true,
                    sortName: 'km'

                }
                , {
                    field: 'driverType',
                    title: '用车类型',
                    sortable: true,
                    sortName: 'driverType',
                    formatter: function (value, row, index) {
                        return jp.getDictLabel(${fns:toJson(fns:getDictList('driver_type'))}, value, "-");
                    }

                }
                , {
                    field: 'maintenance.num',
                    title: '关联任务',
                    sortable: true,
                    sortName: 'maintenance.num'

                }
                , {
                    field: 'jobDesc',
                    title: '任务描述',
                    sortable: true,
                    sortName: 'jobDesc'

                }
                , {
                    field: 'user.name',
                    title: '关联驾驶人',
                    sortable: true,
                    sortName: 'user.name'

                }
                , {
                    field: 'remarks',
                    title: '备注信息',
                    sortable: true,
                    sortName: 'remarks'

                }
            ]

        });


        if (navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)) {//如果是移动端


            $('#tpCarTrackTable').bootstrapTable("toggleView");
        }

        $('#tpCarTrackTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
            'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', !$('#tpCarTrackTable').bootstrapTable('getSelections').length);
            $('#view,#edit').prop('disabled', $('#tpCarTrackTable').bootstrapTable('getSelections').length != 1);
        });

        $("#btnImport").click(function () {
            jp.open({
                type: 2,
                area: [500, 200],
                auto: true,
                title: "导入数据",
                content: "${ctx}/tag/importExcel",
                btn: ['下载模板', '确定', '关闭'],
                btn1: function (index, layero) {
                    jp.downloadFile('${ctx}/tp/cartrack/tpCarTrack/import/template');
                },
                btn2: function (index, layero) {
                    var iframeWin = layero.find('iframe')[0]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
                    iframeWin.contentWindow.importExcel('${ctx}/tp/cartrack/tpCarTrack/import', function (data) {
                        if (data.success) {
                            jp.success(data.msg);
                            refresh();
                        } else {
                            jp.error(data.msg);
                        }
                        jp.close(index);
                    });//调用保存事件
                    return false;
                },

                btn3: function (index) {
                    jp.close(index);
                }
            });
        });


        $("#export").click(function () {//导出Excel文件
            var searchParam = $("#searchForm").serializeJSON();
            searchParam.pageNo = 1;
            searchParam.pageSize = -1;
            var sortName = $('#tpCarTrackTable').bootstrapTable("getOptions", "none").sortName;
            var sortOrder = $('#tpCarTrackTable').bootstrapTable("getOptions", "none").sortOrder;
            var values = "";
            for (var key in searchParam) {
                values = values + key + "=" + searchParam[key] + "&";
            }
            if (sortName != undefined && sortOrder != undefined) {
                values = values + "orderBy=" + sortName + " " + sortOrder;
            }

            jp.downloadFile('${ctx}/tp/cartrack/tpCarTrack/export?' + values);
        })


        $("#search").click("click", function () {// 绑定查询按扭
            $('#tpCarTrackTable').bootstrapTable('refresh');
        });

        $("#reset").click("click", function () {// 绑定查询按扭
            $("#searchForm  input").val("");
            $("#searchForm  select").val("");
            $("#searchForm  .select-item").html("");
            $('#tpCarTrackTable').bootstrapTable('refresh');
        });

        $('#beginDate').datetimepicker({
            format: "YYYY-MM-DD"
        });
        $('#endDate').datetimepicker({
            format: "YYYY-MM-DD"
        });
    });

    function getIdSelections() {
        return $.map($("#tpCarTrackTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }

    function deleteAll() {

        jp.confirm('确认要删除该出车记录记录吗？', function () {
            jp.loading();
            jp.get("${ctx}/tp/cartrack/tpCarTrack/deleteAll?ids=" + getIdSelections(), function (data) {
                if (data.success) {
                    $('#tpCarTrackTable').bootstrapTable('refresh');
                    jp.success(data.msg);
                } else {
                    jp.error(data.msg);
                }
            })

        })
    }

    //刷新列表
    function refresh() {
        $('#tpCarTrackTable').bootstrapTable('refresh');
    }

    function add() {
        jp.openSaveDialog('新增出车记录', "${ctx}/tp/cartrack/tpCarTrack/form", '800px', '500px');
    }


    function edit(id) {//没有权限时，不显示确定按钮
        if (id == undefined) {
            id = getIdSelections();
        }
        jp.openSaveDialog('编辑出车记录', "${ctx}/tp/cartrack/tpCarTrack/form?id=" + id, '800px', '500px');
    }

    function view(id) {//没有权限时，不显示确定按钮
        if (id == undefined) {
            id = getIdSelections();
        }
        jp.openViewDialog('查看出车记录', "${ctx}/tp/cartrack/tpCarTrack/form?id=" + id, '800px', '500px');
    }

    // 查看行驶轨迹
    function viewTrack(carTrackId) {
        jp.openViewDialog("查看行驶轨迹", "${ctx}/tp/cartrack/tpCarTrack/selectGpsHistory?id=" + carTrackId, "1050px", "580px", postionSelectCallback);
    }

    // 地址选择，保存成功后，显示到施工管理表单控件中
    function postionSelectCallback(param) {
        console.log(param);
    }

    // 打开选择详细地址对话框
    function viewLocation(row) {
        var location = row.location;
        var roadcrossName = row.locationName;
        jp.openViewDialog("查看最后位置", "${ctx}/tp/cartrack/tpCarTrack/showPostion?roadcrossName=" + roadcrossName + "&location=" + location, "1050px", "580px", postionSelectCallback);
    }

    function viewCar(row){
        var carId= row.carId;
        jp.openViewDialog("查看车辆", "${ctx}/tp/car/tpCar/form?id=" + carId, "1050px", "580px", postionSelectCallback);
    }

</script>