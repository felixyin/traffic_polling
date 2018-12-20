<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#tpMaintenanceTable').bootstrapTable({
		 
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
               pageNumber:1,   
               //每页的记录行数（*）   
               pageSize: 10,  
               //可供选择的每页的行数（*）    
               pageList: [10, 25, 50, 100],
               //这个接口需要处理bootstrap table传递的固定参数,并返回特定格式的json数据  
               url: "${ctx}/tp/maintenance/tpMaintenance/data",
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
                        jp.confirm('确认要删除该施工记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/tp/maintenance/tpMaintenance/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#tpMaintenanceTable').bootstrapTable('refresh');
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
		        field: 'num',
		        title: '任务编号',
		        sortable: true,
		        sortName: 'num'
		        ,formatter:function(value, row , index){
		        	value = jp.unescapeHTML(value);
				   <c:choose>
					   <c:when test="${fns:hasPermission('tp:maintenance:tpMaintenance:edit')}">
					      return "<a href='javascript:edit(\""+row.id+"\")'>"+value+"</a>";
				      </c:when>
					  <c:when test="${fns:hasPermission('tp:maintenance:tpMaintenance:view')}">
					      return "<a href='javascript:view(\""+row.id+"\")'>"+value+"</a>";
				      </c:when>
					  <c:otherwise>
					      return value;
				      </c:otherwise>
				   </c:choose>
		         }
		       
		    }
			,{
		        field: 'jobType',
		        title: '任务类型',
		        sortable: true,
		        sortName: 'jobType',
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('job_type'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'source',
		        title: '任务来源',
		        sortable: true,
		        sortName: 'source',
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('job_source'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'roadCrossing.name',
		        title: '任务地点',
		        sortable: true,
		        sortName: 'roadCrossing.name'
		       
		    }
			,{
		        field: 'postion',
		        title: '具体位置',
		        sortable: true,
		        sortName: 'postion'
		       
		    }
			,{
		        field: 'sendBy.name',
		        title: '派单人',
		        sortable: true,
		        sortName: 'sendBy.name'
		       
		    }
			,{
		        field: 'sendDate',
		        title: '派单时间',
		        sortable: true,
		        sortName: 'sendDate'
		       
		    }
			,{
		        field: 'office.name',
		        title: '施工单位',
		        sortable: true,
		        sortName: 'office.name'
		       
		    }
			,{
		        field: 'jobBeginDate',
		        title: '施工开始时间',
		        sortable: true,
		        sortName: 'jobBeginDate'
		       
		    }
			,{
		        field: 'jobEndDate',
		        title: '施工结束时间',
		        sortable: true,
		        sortName: 'jobEndDate'
		       
		    }
			,{
		        field: 'money',
		        title: '总费用',
		        sortable: true,
		        sortName: 'money'
		       
		    }
		    ,{
		        field: 'process',
		        title: '施工过程',
		        sortable: true,
		        sortName: 'process',
		        formatter:function(value, row , index){
		        	return jp.unescapeHTML(value);
		        }
		       
		    }
		    ,{
		        field: 'prePic',
		        title: '施工前照片',
		        sortable: true,
		        sortName: 'prePic',
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
		        field: 'middlePic',
		        title: '施工中照片',
		        sortable: true,
		        sortName: 'middlePic',
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
		        field: 'afterPic',
		        title: '施工后照片',
		        sortable: true,
		        sortName: 'afterPic',
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
		        field: 'approve',
		        title: '审批意见',
		        sortable: true,
		        sortName: 'approve'
		       
		    }
			,{
		        field: 'status',
		        title: '任务状态',
		        sortable: true,
		        sortName: 'status',
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('job_status'))}, value, "-");
		        }
		       
		    }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#tpMaintenanceTable').bootstrapTable("toggleView");
		}
	  
	  $('#tpMaintenanceTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#tpMaintenanceTable').bootstrapTable('getSelections').length);
            $('#view,#edit').prop('disabled', $('#tpMaintenanceTable').bootstrapTable('getSelections').length!=1);
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
					  jp.downloadFile('${ctx}/tp/maintenance/tpMaintenance/import/template');
				  },
			    btn2: function(index, layero){
				        var iframeWin = layero.find('iframe')[0]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
						iframeWin.contentWindow.importExcel('${ctx}/tp/maintenance/tpMaintenance/import', function (data) {
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
            var sortName = $('#tpMaintenanceTable').bootstrapTable("getOptions", "none").sortName;
            var sortOrder = $('#tpMaintenanceTable').bootstrapTable("getOptions", "none").sortOrder;
            var values = "";
            for(var key in searchParam){
                values = values + key + "=" + searchParam[key] + "&";
            }
            if(sortName != undefined && sortOrder != undefined){
                values = values + "orderBy=" + sortName + " "+sortOrder;
            }

			jp.downloadFile('${ctx}/tp/maintenance/tpMaintenance/export?'+values);
	  })
		    
	  $("#search").click("click", function() {// 绑定查询按扭
		  $('#tpMaintenanceTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		   $("#searchForm  .select-item").html("");
		  $('#tpMaintenanceTable').bootstrapTable('refresh');
		});
		
				$('#beginSendDate').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});
				$('#endSendDate').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});
				$('#beginJobBeginDate').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});
				$('#endJobBeginDate').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});
				$('#beginJobEndDate').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});
				$('#endJobEndDate').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});
		
	});
		
  function getIdSelections() {
        return $.map($("#tpMaintenanceTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该施工记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/tp/maintenance/tpMaintenance/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#tpMaintenanceTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }

     //刷新列表
  function refresh(){
  	$('#tpMaintenanceTable').bootstrapTable('refresh');
  }
  function add(){
		jp.go("${ctx}/tp/maintenance/tpMaintenance/form/add");
	}

  function edit(id){
	  if(id == undefined){
		  id = getIdSelections();
	  }
	  jp.go("${ctx}/tp/maintenance/tpMaintenance/form/edit?id=" + id);
  }
  
 function view(id){//没有权限时，不显示确定按钮
      if(id == undefined){
             id = getIdSelections();
      }
         jp.go("${ctx}/tp/maintenance/tpMaintenance/form/view?id=" + id);
 }

  
  
  
		   
  function detailFormatter(index, row) {
	  var htmltpl =  $("#tpMaintenanceChildrenTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
	  var html = Mustache.render(htmltpl, {
			idx:row.id
		});
	  $.get("${ctx}/tp/maintenance/tpMaintenance/detail?id="+row.id, function(tpMaintenance){
    	var tpMaintenanceChild1RowIdx = 0, tpMaintenanceChild1Tpl = $("#tpMaintenanceChild1Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data1 =  tpMaintenance.tpMaintenanceItemList;
		for (var i=0; i<data1.length; i++){
			data1[i].dict = {};
			addRow('#tpMaintenanceChild-'+row.id+'-1-List', tpMaintenanceChild1RowIdx, tpMaintenanceChild1Tpl, data1[i]);
			tpMaintenanceChild1RowIdx = tpMaintenanceChild1RowIdx + 1;
		}
				
      	  			
      })
     
        return html;
    }
  
	function addRow(list, idx, tpl, row){
		$(list).append(Mustache.render(tpl, {
			idx: idx, delBtn: true, row: row
		}));
	}
			
</script>
<script type="text/template" id="tpMaintenanceChildrenTpl">//<!--
	<div class="tabs-container">
		<ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-{{idx}}-1" aria-expanded="true">施工物料</a></li>
		</ul>
		<div class="tab-content">
				 <div id="tab-{{idx}}-1" class="tab-pane fade in active">
						<table class="ani table">
						<thead>
							<tr>
								<th>零件名称</th>
								<th>数量</th>
								<th>金额</th>
								<th>备注信息</th>
							</tr>
						</thead>
						<tbody id="tpMaintenanceChild-{{idx}}-1-List">
						</tbody>
					</table>
				</div>
		</div>//-->
	</script>
	<script type="text/template" id="tpMaintenanceChild1Tpl">//<!--
				<tr>
					<td>
						{{row.materialPart.name}}
					</td>
					<td>
						{{row.count}}
					</td>
					<td>
						{{row.money}}
					</td>
					<td>
						{{row.remarks}}
					</td>
				</tr>//-->
	</script>
