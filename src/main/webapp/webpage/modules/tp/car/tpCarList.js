<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#tpCarTable').bootstrapTable({
		 
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
               pageNumber:1,   
               //每页的记录行数（*）   
               pageSize: 10,  
               //可供选择的每页的行数（*）    
               pageList: [10, 25, 50, 100],
               //这个接口需要处理bootstrap table传递的固定参数,并返回特定格式的json数据  
               url: "${ctx}/tp/car/tpCar/data",
               //默认值为 'limit',传给服务端的参数为：limit, offset, search, sort, order Else
               //queryParamsType:'',   
               ////查询参数,每次调用是会带上这个参数，可自定义                         
               queryParams : function(params) {
               	var searchParam = $("#searchForm").serializeJSON();
               	searchParam.pageNo = params.limit === undefined? "1" :params.offset/params.limit+1;
               	searchParam.pageSize = params.limit === undefined? -1 : params.limit;
               	searchParam.orderBy = params.sort === undefined? "" : params.sort+ " "+  params.order;
                   return searchParam;
               },
               //分页方式：client客户端分页，server服务端分页（*）
               sidePagination: "server",
               contextMenuTrigger:"right",//pc端 按右键弹出菜单
               contextMenuTriggerMobile:"press",//手机端 弹出菜单，click：单击， press：长按。
               contextMenu: '#context-menu',
               onContextMenuItem: function(row, $el){
                   if($el.data("item") == "edit"){
                   		edit(row.id);
                   }else if($el.data("item") == "view"){
                       view(row.id);
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该车辆记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/tp/car/tpCar/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#tpCarTable').bootstrapTable('refresh');
                   	  			jp.success(data.msg);
                   	  		}else{
                   	  			jp.error(data.msg);
                   	  		}
                   	  	})
                   	   
                   	});
                      
                   } 
               },
              
               onClickRow: function(row, $el){
               },
               	onShowSearch: function () {
			$("#search-collapse").slideToggle();
		},
               columns: [{
		        checkbox: true
		       
		    }
           , {
               field: 'deviceId',
               title: '车辆编号',
               sortable: true,
               sortName: 'deviceId'
               , formatter: function (value, row, index) {
                   value = jp.unescapeHTML(value);
               <c:choose>
                   <c:when test="${fns:hasPermission('tp:car:tpCar:edit')}">
                   return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
               </c:when>
                   <c:when test="${fns:hasPermission('tp:car:tpCar:view')}">
                   return "<a href='javascript:view(\"" + row.id + "\")'>" + value + "</a>";
               </c:when>
                   <c:otherwise>
                   return value;
               </c:otherwise>
                   </c:choose>
               }

           }
			,{
		        field: 'office.name',
		        title: '所属单位',
		        sortable: true,
		        sortName: 'office.name'

		    }
			,{
		        field: 'user.name',
		        title: '主要驾驶人',
		        sortable: true,
		        sortName: 'user.name'
		       
		    }
			,{
		        field: 'name',
		        title: '车辆名称',
		        sortable: true,
		        sortName: 'name'
		       
		    }
			,{
		        field: 'brand',
		        title: '车辆品牌',
		        sortable: true,
		        sortName: 'brand'
		       
		    }
			,{
		        field: 'purpose',
		        title: '车辆用途',
		        sortable: true,
		        sortName: 'purpose'
		       
		    }
			,{
		        field: 'personCount',
		        title: '载人数量',
		        sortable: true,
		        sortName: 'personCount'
		       
		    }
			,{
		        field: 'carryingCapacity',
		        title: '载货重量（吨）',
		        sortable: true,
		        sortName: 'carryingCapacity'
		       
		    }
           , {
               field: 'location',
               title: '最新GPS位置',
               sortable: true,
               sortName: 'location'
               , formatter: function (value, row, index) {
                   value = jp.unescapeHTML(value);
               <c:choose>
                   <c:when test="${fns:hasPermission('tp:car:tpCar:edit')}">
                   return "<a href='javascript:viewLocation(" + JSON.stringify(row) + ")'>" + value + "</a>";
               </c:when>
                   <c:when test="${fns:hasPermission('tp:car:tpCar:view')}">
                   return "<a href='javascript:viewLocation(" + JSON.stringify(row) + ")'>" + value + "</a>";
               </c:when>
                   <c:otherwise>
                   return value;
               </c:otherwise>
                   </c:choose>
               }
           }
			,{
		        field: 'locationName',
		        title: '最后停车位置',
		        sortable: true,
		        sortName: 'locationName'
		       
		    }
			,{
		        field: 'startKm',
		        title: '装机时总里程',
		        sortable: true,
		        sortName: 'startKm'
		       
		    }
			,{
		        field: 'sumKm',
		        title: 'GPS总里程',
		        sortable: true,
		        sortName: 'sumKm'
		       
		    }
			,{
		        field: 'currentKm',
		        title: '当前预计总里程',
		        sortable: true,
		        sortName: 'currentKm'
		       
		    }
			,{
		        field: 'sumTime',
		        title: 'GPS运行总时间',
		        sortable: true,
		        sortName: 'sumTime',
                formatter: function (value, row, index) {
		        	if(value){
						var xiaoShi = Math.floor(value / 60);
						var fenZhong = value % 60;
		        		return xiaoShi + '小时' +fenZhong + '分钟';
					}else{
		        		return '';
					}
                }
		       
		    }
			,{
		        field: 'consumption',
		        title: '油耗（升/每百公里）',
		        sortable: true,
		        sortName: 'consumption'
		       
		    }
			,{
		        field: 'insuranceCompany',
		        title: '投保公司',
		        sortable: true,
		        sortName: 'insuranceCompany'
		       
		    }
			,{
		        field: 'insuranceDate',
		        title: '投保日期',
		        sortable: true,
		        sortName: 'insuranceDate'
		       
		    }
			,{
		        field: 'maintainKm',
		        title: '保养时公里数',
		        sortable: true,
		        sortName: 'maintainKm'
		       
		    }
			,{
		        field: 'maintainDate',
		        title: '保养日期',
		        sortable: true,
		        sortName: 'maintainDate'
		       
		    }
			,{
		        field: 'remarks',
		        title: '备注信息',
		        sortable: true,
		        sortName: 'remarks'
		       
		    }
			,{
		        field: 'updateDate',
		        title: '更新时间',
		        sortable: true,
		        sortName: 'updateDate'
		       
		    }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#tpCarTable').bootstrapTable("toggleView");
		}
	  
	  $('#tpCarTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#tpCarTable').bootstrapTable('getSelections').length);
            $('#view,#edit').prop('disabled', $('#tpCarTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 2,
                area: [500, 200],
                auto: true,
			    title:"导入数据",
			    content: "${ctx}/tag/importExcel" ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  jp.downloadFile('${ctx}/tp/car/tpCar/import/template');
				  },
			    btn2: function(index, layero){
				        var iframeWin = layero.find('iframe')[0]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
						iframeWin.contentWindow.importExcel('${ctx}/tp/car/tpCar/import', function (data) {
							if(data.success){
								jp.success(data.msg);
								refresh();
							}else{
								jp.error(data.msg);
							}
					   		jp.close(index);
						});//调用保存事件
						return false;
				  },
				 
				  btn3: function(index){ 
					  jp.close(index);
	    	       }
			}); 
		});
		
		
	 $("#export").click(function(){//导出Excel文件
	        var searchParam = $("#searchForm").serializeJSON();
	        searchParam.pageNo = 1;
	        searchParam.pageSize = -1;
            var sortName = $('#tpCarTable').bootstrapTable("getOptions", "none").sortName;
            var sortOrder = $('#tpCarTable').bootstrapTable("getOptions", "none").sortOrder;
            var values = "";
            for(var key in searchParam){
                values = values + key + "=" + searchParam[key] + "&";
            }
            if(sortName != undefined && sortOrder != undefined){
                values = values + "orderBy=" + sortName + " "+sortOrder;
            }

			jp.downloadFile('${ctx}/tp/car/tpCar/export?'+values);
	  })

		    
	  $("#search").click("click", function() {// 绑定查询按扭
		  $('#tpCarTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		  $("#searchForm  .select-item").html("");
		  $('#tpCarTable').bootstrapTable('refresh');
		});
		
		$('#beginInsuranceDate').datetimepicker({
			 format: "YYYY-MM-DD"
		});
		$('#endInsuranceDate').datetimepicker({
			 format: "YYYY-MM-DD"
		});
		$('#beginMaintainDate').datetimepicker({
			 format: "YYYY-MM-DD"
		});
		$('#endMaintainDate').datetimepicker({
			 format: "YYYY-MM-DD"
		});
		
	});
		
  function getIdSelections() {
        return $.map($("#tpCarTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该车辆记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/tp/car/tpCar/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#tpCarTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }

    //刷新列表
  function refresh(){
  	$('#tpCarTable').bootstrapTable('refresh');
  }
  
   function add(){
	  jp.openSaveDialog('新增车辆', "${ctx}/tp/car/tpCar/form",'800px', '500px');
  }


  
   function edit(id){//没有权限时，不显示确定按钮
       if(id == undefined){
	      id = getIdSelections();
	}
	jp.openSaveDialog('编辑车辆', "${ctx}/tp/car/tpCar/form?id=" + id, '800px', '500px');
  }
  
 function view(id){//没有权限时，不显示确定按钮
      if(id == undefined){
             id = getIdSelections();
      }
        jp.openViewDialog('查看车辆', "${ctx}/tp/car/tpCar/form?id=" + id, '800px', '500px');
 }


// 地址选择，保存成功后，显示到施工管理表单控件中
function postionSelectCallback(param) {
	console.log(param);
}

// 打开选择详细地址对话框
function viewLocation(row) {
	var location = row.location;
	var roadcrossName = row.locationName;
	jp.openChildDialog("查看最后位置", "${ctx}/tp/cartrack/tpCarTrack/showPostion?roadcrossName=" + roadcrossName + "&location=" + location, "1050px", "580px", postionSelectCallback);
}

function  realtimeLocations() {
<%--jp.openChildDialog("实时位置大屏", "${ctx}/tp/car/tpCar/realtimeLocations", "1050px", "580px", postionSelectCallback);--%>
	jp.windowOpen("${ctx}/tp/car/tpCar/realtimeLocations", "实时位置大屏", "1050px", "580px")
}

</script>