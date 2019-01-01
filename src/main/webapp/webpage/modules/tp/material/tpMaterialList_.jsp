<%@ page contentType="text/html;charset=UTF-8" %>
<script>
    $(document).ready(function () {
        $('#tpMaterialTable').bootstrapTable({

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
            //显示详情按钮
            detailView: true,
            //显示详细内容函数
            detailFormatter: "detailFormatter",
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
            url: "${ctx}/tp/material/tpMaterial/data",
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
                    jp.confirm('确认要删除该物料记录吗？', function () {
                        jp.loading();
                        jp.get("${ctx}/tp/material/tpMaterial/delete?id=" + row.id, function (data) {
                            if (data.success) {
                                $('#tpMaterialTable').bootstrapTable('refresh');
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
                    field: 'name',
                    title: '名称',
                    sortable: true,
                    sortName: 'name'
                    , formatter: function (value, row, index) {
                        value = jp.unescapeHTML(value);
                        <c:choose>
                        <c:when test="${fns:hasPermission('tp:material:tpMaterial:edit')}">
                        return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
                        </c:when>
                        <c:when test="${fns:hasPermission('tp:material:tpMaterial:view')}">
                        return "<a href='javascript:view(\"" + row.id + "\")'>" + value + "</a>";
                        </c:when>
                        <c:otherwise>
                        return value;
                        </c:otherwise>
                        </c:choose>
                    }

                }
                , {
                    field: 'type',
                    title: '物料类型',
                    sortable: true,
                    sortName: 'type',
                    formatter: function (value, row, index) {
                        return jp.getDictLabel(${fns:toJson(fns:getDictList('material_type'))}, value, "-");
                    }

                }
                , {
                    field: 'company',
                    title: '生产单位',
                    sortable: true,
                    sortName: 'company',
                    formatter: function (value, row, index) {
                        return jp.getDictLabel(${fns:toJson(fns:getDictList('material_comany'))}, value, "-");
                    }

                }
                , {
                    field: 'pic',
                    title: '图片',
                    sortable: true,
                    sortName: 'pic',
                    formatter: function (value, row, index) {
                        var valueArray = value.split("|");
                        var labelArray = [];
                        for (var i = 0; i < valueArray.length; i++) {
                            if (!/\.(gif|jpg|jpeg|png|GIF|JPG|PNG)$/.test(valueArray[i])) {
                                labelArray[i] = "<a href=\"" + valueArray[i] + "\" url=\"" + valueArray[i] + "\" target=\"_blank\">" + decodeURIComponent(valueArray[i].substring(valueArray[i].lastIndexOf("/") + 1)) + "</a>"
                            } else {
                                labelArray[i] = '<img   onclick="jp.showPic(\'' + valueArray[i] + '\')"' + ' height="50px" src="' + valueArray[i] + '">';
                            }
                        }
                        return labelArray.join(" ");
                    }

                }
                , {
                    field: 'standards',
                    title: '规格描述',
                    sortable: true,
                    sortName: 'standards'

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


            $('#tpMaterialTable').bootstrapTable("toggleView");
        }

        $('#tpMaterialTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
            'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', !$('#tpMaterialTable').bootstrapTable('getSelections').length);
            $('#view,#edit').prop('disabled', $('#tpMaterialTable').bootstrapTable('getSelections').length != 1);
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
                    jp.downloadFile('${ctx}/tp/material/tpMaterial/import/template');
                },
                btn2: function (index, layero) {
                    var iframeWin = layero.find('iframe')[0]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
                    iframeWin.contentWindow.importExcel('${ctx}/tp/material/tpMaterial/import', function (data) {
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
            var sortName = $('#tpMaterialTable').bootstrapTable("getOptions", "none").sortName;
            var sortOrder = $('#tpMaterialTable').bootstrapTable("getOptions", "none").sortOrder;
            var values = "";
            for (var key in searchParam) {
                values = values + key + "=" + searchParam[key] + "&";
            }
            if (sortName != undefined && sortOrder != undefined) {
                values = values + "orderBy=" + sortName + " " + sortOrder;
            }

            jp.downloadFile('${ctx}/tp/material/tpMaterial/export?' + values);
        })

        $("#search").click("click", function () {// 绑定查询按扭
            $('#tpMaterialTable').bootstrapTable('refresh');
        });

        $("#reset").click("click", function () {// 绑定查询按扭
            $("#searchForm  input").val("");
            $("#searchForm  select").val("");
            $("#searchForm  .select-item").html("");
            $('#tpMaterialTable').bootstrapTable('refresh');
        });


    });

    function getIdSelections() {
        return $.map($("#tpMaterialTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }

    function deleteAll() {

        jp.confirm('确认要删除该物料记录吗？', function () {
            jp.loading();
            jp.get("${ctx}/tp/material/tpMaterial/deleteAll?ids=" + getIdSelections(), function (data) {
                if (data.success) {
                    $('#tpMaterialTable').bootstrapTable('refresh');
                    jp.success(data.msg);
                } else {
                    jp.error(data.msg);
                }
            })

        })
    }

    //刷新列表
    function refresh() {
        $('#tpMaterialTable').bootstrapTable('refresh');
    }

    function add() {
        jp.go("${ctx}/tp/material/tpMaterial/form/add");
    }

    function edit(id) {
        if (id == undefined) {
            id = getIdSelections();
        }
        jp.go("${ctx}/tp/material/tpMaterial/form/edit?id=" + id);
    }

    function view(id) {//没有权限时，不显示确定按钮
        if (id == undefined) {
            id = getIdSelections();
        }
        jp.go("${ctx}/tp/material/tpMaterial/form/view?id=" + id);
    }


    function detailFormatter(index, row) {
        var htmltpl = $("#tpMaterialChildrenTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "");
        var html = Mustache.render(htmltpl, {
            idx: row.id
        });
        $.get("${ctx}/tp/material/tpMaterial/detail?id=" + row.id, function (tpMaterial) {
            var tpMaterialChild1RowIdx = 0,
                tpMaterialChild1Tpl = $("#tpMaterialChild1Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "");
            var data1 = tpMaterial.tpMaterialPartList;
            for (var i = 0; i < data1.length; i++) {
                data1[i].dict = {};
                data1[i].dict.unit = jp.getDictLabel(${fns:toJson(fns:getDictList('material_unit'))}, data1[i].unit, "-");
                data1[i].pic = getPicTabHtml(data1[i].pic, row, index);
                addRow('#tpMaterialChild-' + row.id + '-1-List', tpMaterialChild1RowIdx, tpMaterialChild1Tpl, data1[i]);
                tpMaterialChild1RowIdx = tpMaterialChild1RowIdx + 1;
            }
        });

        return html;
    }

    function addRow(list, idx, tpl, row) {
        var html = Mustache.render(tpl, {
            idx: idx, delBtn: true, row: row
        });
        html = jp.unescapeHTML(html);
        $(list).append(html);
    }

    function getPicTabHtml(value, row, index) {
        var valueArray = value.split("|");
        var labelArray = [];
        for (var i = 0; i < valueArray.length; i++) {
            if (!/\.(gif|jpg|jpeg|png|GIF|JPG|PNG)$/.test(valueArray[i])) {
                if (valueArray[i]) {
                    labelArray[i] = "<a href=\"" + valueArray[i] + "\" url=\"" + valueArray[i] + "\" target=\"_blank\">" + decodeURIComponent(valueArray[i].substring(valueArray[i].lastIndexOf("/") + 1)) + "</a>"
                }
            } else {
                // $('.fixed-table-body').css('overflowX','hidden')
                labelArray[i] = '<img   onclick="jp.showPic(\'' + valueArray[i] + '\');"' + ' height="150px" src="' + valueArray[i] + '">';
            }
        }
        return  labelArray.join(" ");
    }

</script>
<script type="text/template" id="tpMaterialChildrenTpl">//<!--
	<div class="tabs-container">
		<ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-{{idx}}-1" aria-expanded="true">物料零件</a></li>
		</ul>
		<div class="tab-content">
				 <div id="tab-{{idx}}-1" class="tab-pane fade in active">
						<table class="ani table">
						<thead>
							<tr>
								<th>零件名称</th>
								<th>零件单位</th>
								<th>零件单价</th>
								<th>零件图片</th>
								<th>零件规格</th>
								<th>备注信息</th>
							</tr>
						</thead>
						<tbody id="tpMaterialChild-{{idx}}-1-List">
						</tbody>
					</table>
				</div>
		</div>//-->
</script>
<script type="text/template" id="tpMaterialChild1Tpl">//<!--
				<tr>
					<td>
						{{row.name}}
					</td>
					<td>
						{{row.dict.unit}}
					</td>
					<td>
						{{row.price}}
					</td>
					<td>
						{{row.pic}}
					</td>
					<td>
						{{row.standards}}
					</td>
					<td>
						{{row.remarks}}
					</td>
				</tr>//-->
</script>
