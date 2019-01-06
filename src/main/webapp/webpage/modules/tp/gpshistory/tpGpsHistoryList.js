<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#tpGpsHistoryTable').bootstrapTable({
		 
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
               url: "${ctx}/tp/gpshistory/tpGpsHistory/data",
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
                        jp.confirm('确认要删除该历史轨迹记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/tp/gpshistory/tpGpsHistory/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#tpGpsHistoryTable').bootstrapTable('refresh');
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
			,{
		        field: 'carTrack.name',
		        title: '出车记录表外键',
		        sortable: true,
		        sortName: 'carTrack.name'
		        ,formatter:function(value, row , index){

			   if(value == null || value ==""){
				   value = "-";
			   }
			   <c:choose>
				   <c:when test="${fns:hasPermission('tp:gpshistory:tpGpsHistory:edit')}">
				      return "<a href='javascript:edit(\""+row.id+"\")'>"+value+"</a>";
			      </c:when>
				  <c:when test="${fns:hasPermission('tp:gpshistory:tpGpsHistory:view')}">
				      return "<a href='javascript:view(\""+row.id+"\")'>"+value+"</a>";
			      </c:when>
				  <c:otherwise>
				      return value;
			      </c:otherwise>
			   </c:choose>

		        }
		       
		    }
			,{
		        field: 'car.name',
		        title: '车辆表外键',
		        sortable: true,
		        sortName: 'car.name'
		       
		    }
			,{
		        field: 'deviceId',
		        title: '设备编号',
		        sortable: true,
		        sortName: 'deviceId'
		       
		    }
			,{
		        field: 'upTime',
		        title: '上传时间',
		        sortable: true,
		        sortName: 'upTime'
		       
		    }
			,{
		        field: 'locationStatus',
		        title: '定位状态',
		        sortable: true,
		        sortName: 'locationStatus'
		       
		    }
			,{
		        field: 'latGps',
		        title: 'GPS纬度',
		        sortable: true,
		        sortName: 'latGps'
		       
		    }
			,{
		        field: 'latHemisphere',
		        title: '纬度半球',
		        sortable: true,
		        sortName: 'latHemisphere'
		       
		    }
			,{
		        field: 'lonGps',
		        title: 'GPS经度',
		        sortable: true,
		        sortName: 'lonGps'
		       
		    }
			,{
		        field: 'lonHemisphere',
		        title: '经度半球',
		        sortable: true,
		        sortName: 'lonHemisphere'
		       
		    }
			,{
		        field: 'groundRate',
		        title: '地面速率',
		        sortable: true,
		        sortName: 'groundRate'
		       
		    }
			,{
		        field: 'groundDirection',
		        title: '地面航向',
		        sortable: true,
		        sortName: 'groundDirection'
		       
		    }
			,{
		        field: 'declination',
		        title: '磁偏角',
		        sortable: true,
		        sortName: 'declination'
		       
		    }
			,{
		        field: 'declinationDirection',
		        title: '磁偏角方向',
		        sortable: true,
		        sortName: 'declinationDirection'
		       
		    }
			,{
		        field: 'model',
		        title: '模式指示',
		        sortable: true,
		        sortName: 'model'
		       
		    }
			,{
		        field: 'latCal',
		        title: '解密后纬度',
		        sortable: true,
		        sortName: 'latCal'
		       
		    }
			,{
		        field: 'lonCal',
		        title: '解密后经度',
		        sortable: true,
		        sortName: 'lonCal'
		       
		    }
			,{
		        field: 'latGD',
		        title: '转换高德纬度',
		        sortable: true,
		        sortName: 'latGD'
		       
		    }
			,{
		        field: 'lonGD',
		        title: '转换高德经度',
		        sortable: true,
		        sortName: 'lonGD'
		       
		    }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#tpGpsHistoryTable').bootstrapTable("toggleView");
		}
	  
	  $('#tpGpsHistoryTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#tpGpsHistoryTable').bootstrapTable('getSelections').length);
            $('#view,#edit').prop('disabled', $('#tpGpsHistoryTable').bootstrapTable('getSelections').length!=1);
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
					  jp.downloadFile('${ctx}/tp/gpshistory/tpGpsHistory/import/template');
				  },
			    btn2: function(index, layero){
				        var iframeWin = layero.find('iframe')[0]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
						iframeWin.contentWindow.importExcel('${ctx}/tp/gpshistory/tpGpsHistory/import', function (data) {
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
            var sortName = $('#tpGpsHistoryTable').bootstrapTable("getOptions", "none").sortName;
            var sortOrder = $('#tpGpsHistoryTable').bootstrapTable("getOptions", "none").sortOrder;
            var values = "";
            for(var key in searchParam){
                values = values + key + "=" + searchParam[key] + "&";
            }
            if(sortName != undefined && sortOrder != undefined){
                values = values + "orderBy=" + sortName + " "+sortOrder;
            }

			jp.downloadFile('${ctx}/tp/gpshistory/tpGpsHistory/export?'+values);
	  })

		    
	  $("#search").click("click", function() {// 绑定查询按扭
		  $('#tpGpsHistoryTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		  $("#searchForm  .select-item").html("");
		  $('#tpGpsHistoryTable').bootstrapTable('refresh');
		});
		
		
	});
		
  function getIdSelections() {
        return $.map($("#tpGpsHistoryTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该历史轨迹记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/tp/gpshistory/tpGpsHistory/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#tpGpsHistoryTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }

    //刷新列表
  function refresh(){
  	$('#tpGpsHistoryTable').bootstrapTable('refresh');
  }
  
   function add(){
	  jp.openSaveDialog('新增历史轨迹', "${ctx}/tp/gpshistory/tpGpsHistory/form",'800px', '500px');
  }


  
   function edit(id){//没有权限时，不显示确定按钮
       if(id == undefined){
	      id = getIdSelections();
	}
	jp.openSaveDialog('编辑历史轨迹', "${ctx}/tp/gpshistory/tpGpsHistory/form?id=" + id, '800px', '500px');
  }
  
 function view(id){//没有权限时，不显示确定按钮
      if(id == undefined){
             id = getIdSelections();
      }
        jp.openViewDialog('查看历史轨迹', "${ctx}/tp/gpshistory/tpGpsHistory/form?id=" + id, '800px', '500px');
 }



</script>