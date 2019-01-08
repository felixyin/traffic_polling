<%@ page contentType="text/html;charset=UTF-8" %>
<script>

	// 打开选择详细地址对话框
	function viewLocation(row) {
		var location = row.location;
		var roadcrossName = row.locationName;
		jp.openChildDialog("查看最后位置", "${ctx}/tp/cartrack/tpCarTrack/showPostion?roadcrossName=" + roadcrossName + "&location=" + location, "1050px", "580px");
	}

$(document).ready(function() {
	$('#tpDamTable').bootstrapTable({
		 
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
               url: "${ctx}/tp/dam/tpDam/data",
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
                        jp.confirm('确认要删除该资产记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/tp/dam/tpDam/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#tpDamTable').bootstrapTable('refresh');
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
					   field: 'id',
					   title: '查看/编辑',
					   sortable: false,
					   sortName: 'id',
					   formatter:function(value, row , index){
					   <c:choose>
						   <c:when test="${fns:hasPermission('tp:dam:tpDam:edit')}">
						   return "<a href='javascript:edit(\""+row.id+"\")'>编辑</a>";
					   </c:when>
						   <c:when test="${fns:hasPermission('tp:dam:tpDam:view')}">
						   return "<a href='javascript:view(\""+row.id+"\")'>查看</a>";
					   </c:when>
						   <c:otherwise>
						   return value;
					   </c:otherwise>
						   </c:choose>
					   }

				   }
			,{
		        field: 'damType',
		        title: '资产类型',
		        sortable: true,
		        sortName: 'damType',
		        formatter:function(value, row , index){
		        	   value = jp.getDictLabel(${fns:toJson(fns:getDictList('dam_type'))}, value, "-");
					      return value;
		        }

		    }
			,{
		        field: 'description',
		        title: '资产描述',
		        sortable: true,
		        sortName: 'description'

		    }
		    ,{
		        field: 'pic',
		        title: '图片',
		        sortable: true,
		        sortName: 'pic',
		        formatter:function(value, row , index){
		        	var valueArray = value.split("|");
		        	var labelArray = [];
		        	for(var i =0 ; i<valueArray.length; i++){
		        		if(!/\.(gif|jpg|jpeg|png|GIF|JPG|PNG)$/.test(valueArray[i]))
		        		{
		        			labelArray[i] = "<a href=\""+valueArray[i]+"\" url=\""+valueArray[i]+"\" target=\"_blank\">"+decodeURIComponent(valueArray[i].substring(valueArray[i].lastIndexOf("/")+1))+"</a>"
		        		}else{
		        			labelArray[i] = '<img   onclick="jp.showPic(\''+valueArray[i]+'\')"'+' height="50px" src="'+valueArray[i]+'">';
		        		}
		        	}
		        	return labelArray.join(" ");
		        }
		       
		    }
			,{
		        field: 'size',
		        title: '尺寸',
		        sortable: true,
		        sortName: 'size'
		       
		    }
			,{
		        field: 'location',
		        title: '经纬度',
		        sortable: true,
		        sortName: 'location'
					   , formatter: function (value, row, index) {
						   value = jp.unescapeHTML(value);
					   <c:choose>
						   <c:when test="${fns:hasPermission('tp:dam:tpDam:edit')}">
						   return "<a href='javascript:viewLocation(" + JSON.stringify(row) + ")'>" + value + "</a>";
					   </c:when>
						   <c:when test="${fns:hasPermission('tp:dam:tpDam:view')}">
						   return "<a href='javascript:viewLocation(" + JSON.stringify(row) + ")'>" + value + "</a>";
					   </c:when>
						   <c:otherwise>
						   return value;
					   </c:otherwise>
						   </c:choose>
					   }
		    }
			,{
		        field: 'area.name',
		        title: '所属区域',
		        sortable: true,
		        sortName: 'area.name'
		       
		    }
			,{
		        field: 'roadcross.name',
		        title: '所属路口',
		        sortable: true,
		        sortName: 'roadcross.name'
		       
		    }
			,{
		        field: 'nearestJunction',
		        title: '所属路口相对位置',
		        sortable: true,
		        sortName: 'nearestJunction'
		       
		    }
			,{
		        field: 'road.name',
		        title: '所属道路',
		        sortable: true,
		        sortName: 'road.name'
		       
		    }
			,{
		        field: 'address',
		        title: '搜索用地址',
		        sortable: true,
		        sortName: 'address'
		       
		    }
			,{
		        field: 'nearestPoi',
		        title: '搜索地址相对位置',
		        sortable: true,
		        sortName: 'nearestPoi'
		       
		    }
			,{
		        field: 'remarks',
		        title: '备注信息',
		        sortable: true,
		        sortName: 'remarks'
		       
		    }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#tpDamTable').bootstrapTable("toggleView");
		}
	  
	  $('#tpDamTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#tpDamTable').bootstrapTable('getSelections').length);
            $('#view,#edit').prop('disabled', $('#tpDamTable').bootstrapTable('getSelections').length!=1);
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
					  jp.downloadFile('${ctx}/tp/dam/tpDam/import/template');
				  },
			    btn2: function(index, layero){
				        var iframeWin = layero.find('iframe')[0]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
						iframeWin.contentWindow.importExcel('${ctx}/tp/dam/tpDam/import', function (data) {
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
            var sortName = $('#tpDamTable').bootstrapTable("getOptions", "none").sortName;
            var sortOrder = $('#tpDamTable').bootstrapTable("getOptions", "none").sortOrder;
            var values = "";
            for(var key in searchParam){
                values = values + key + "=" + searchParam[key] + "&";
            }
            if(sortName != undefined && sortOrder != undefined){
                values = values + "orderBy=" + sortName + " "+sortOrder;
            }

			jp.downloadFile('${ctx}/tp/dam/tpDam/export?'+values);
	  })

		    
	  $("#search").click("click", function() {// 绑定查询按扭
		  $('#tpDamTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		  $("#searchForm  .select-item").html("");
		  $('#tpDamTable').bootstrapTable('refresh');
		});
		
		
	});
		
  function getIdSelections() {
        return $.map($("#tpDamTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该资产记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/tp/dam/tpDam/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#tpDamTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }

    //刷新列表
  function refresh(){
  	$('#tpDamTable').bootstrapTable('refresh');
  }
  
   function add(){
	  jp.openSaveDialog('新增资产', "${ctx}/tp/dam/tpDam/form",'800px', '500px');
  }


  
   function edit(id){//没有权限时，不显示确定按钮
       if(id == undefined){
	      id = getIdSelections();
	}
	jp.openSaveDialog('编辑资产', "${ctx}/tp/dam/tpDam/form?id=" + id, '800px', '500px');
  }
  
 function view(id){//没有权限时，不显示确定按钮
      if(id == undefined){
             id = getIdSelections();
      }
        jp.openViewDialog('查看资产', "${ctx}/tp/dam/tpDam/form?id=" + id, '800px', '500px');
 }



</script>