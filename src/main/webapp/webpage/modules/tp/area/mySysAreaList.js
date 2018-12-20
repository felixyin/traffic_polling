<%@ page contentType="text/html;charset=UTF-8" %>
<script>
	    var $mySysAreaTreeTable=null;  
		$(document).ready(function() {
			$mySysAreaTreeTable=$('#mySysAreaTreeTable').treeTable({  
		    	   theme:'vsStyle',	           
					expandLevel : 2,
					column:0,
					checkbox: false,
		            url:'${ctx}/tp/area/mySysArea/getChildren?parentId=',  
		            callback:function(item) { 
		            	 var treeTableTpl= $("#mySysAreaTreeTableTpl").html();
		            	 item.dict = {};
						item.dict.type = jp.getDictLabel(${fns:toJson(fns:getDictList(''))}, item.type, "-");
	           	  var result = laytpl(treeTableTpl).render({
								  row: item
							});
		                return result;                   
		            },  
		            beforeClick: function($mySysAreaTreeTable, id) { 
		                //异步获取数据 这里模拟替换处理  
		                $mySysAreaTreeTable.refreshPoint(id);  
		            },  
		            beforeExpand : function($mySysAreaTreeTable, id) {   
		            },  
		            afterExpand : function($mySysAreaTreeTable, id) {  
		            },  
		            beforeClose : function($mySysAreaTreeTable, id) {    
		            	
		            }  
		        });
		        
		        $mySysAreaTreeTable.initParents('${parentIds}', "0");//在保存编辑时定位展开当前节点
		       
		});
		
		function del(con,id){  
			jp.confirm('确认要删除区域吗？', function(){
				jp.loading();
	       	  	$.get("${ctx}/tp/area/mySysArea/delete?id="+id, function(data){
	       	  		if(data.success){
	       	  			$mySysAreaTreeTable.del(id);
	       	  			jp.success(data.msg);
	       	  		}else{
	       	  			jp.error(data.msg);
	       	  		}
	       	  	})
	       	   
       		});
	
		} 
		
		function add(){//新增
			jp.go('${ctx}/tp/area/mySysArea/form/add');
		}
		function edit(id){//编辑
			jp.go('${ctx}/tp/area/mySysArea/form/edit?id='+id);
		}
		function view(id){//查看
			jp.go('${ctx}/tp/area/mySysArea/form/view?id='+id);
		}
		function addChild(id){//添加下级机构
			jp.go('${ctx}/tp/area/mySysArea/form/add?parent.id='+id);
		}
		function refresh(){//刷新
			var index = jp.loading("正在加载，请稍等...");
			$mySysAreaTreeTable.refresh();
			jp.close(index);
		}
</script>
<script type="text/html" id="mySysAreaTreeTableTpl">
			<td>
			<c:choose>
			      <c:when test="${fns:hasPermission('tp:area:mySysArea:edit')}">
				    <a  href="${ctx}/tp/area/mySysArea/form/edit?id={{d.row.id}}">
							{{d.row.id === undefined ? "": d.row.id}}
					</a>	
			      </c:when>
			      <c:when test="${fns:hasPermission('tp:area:mySysArea:view')}">
				    <a  href="${ctx}/tp/area/mySysArea/form/view?id={{d.row.id}}">
							{{d.row.id === undefined ? "": d.row.id}}
					</a>	
			      </c:when>
			      <c:otherwise>
							{{d.row.id === undefined ? "": d.row.id}}
			      </c:otherwise>
			</c:choose>
			</td>
			<td>
							{{d.row.parent.name === undefined ? "": d.row.parent.name}}
			</td>
			<td>
							{{d.row.name === undefined ? "": d.row.name}}
			</td>
			<td>
							{{d.row.code === undefined ? "": d.row.code}}
			</td>
			<td>
							{{d.row.dict.type === undefined ? "": d.row.dict.type}}
			</td>
			<td>
							{{d.row.updateDate === undefined ? "": d.row.updateDate}}
			</td>
			<td>
							{{d.row.remarks === undefined ? "": d.row.remarks}}
			</td>
			<td>
				<div class="btn-group">
			 		<button type="button" class="btn  btn-primary btn-xs dropdown-toggle" data-toggle="dropdown">
						<i class="fa fa-cog"></i>
						<span class="fa fa-chevron-down"></span>
					</button>
				  <ul class="dropdown-menu" role="menu">
					<shiro:hasPermission name="tp:area:mySysArea:view">
						<li><a href="${ctx}/tp/area/mySysArea/form/view?id={{d.row.id}}"><i class="fa fa-search-plus"></i> 查看</a></li>
					</shiro:hasPermission>
					<shiro:hasPermission name="tp:area:mySysArea:edit">
		   				<li><a href="${ctx}/tp/area/mySysArea/form/edit?id={{d.row.id}}"><i class="fa fa-edit"></i> 修改</a></li>
		   			</shiro:hasPermission>
		   			<shiro:hasPermission name="tp:area:mySysArea:del">
		   				<li><a  onclick="return del(this, '{{d.row.id}}')"><i class="fa fa-trash"></i> 删除</a></li>
					</shiro:hasPermission>
		   			<shiro:hasPermission name="tp:area:mySysArea:add">
						<li><a href="${ctx}/tp/area/mySysArea/form/add?parent.id={{d.row.id}}"><i class="fa fa-plus"></i> 添加下级区域</a></li>
					</shiro:hasPermission>
				  </ul>
				</div>
			</td>
	</script>