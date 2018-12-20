<%@ page contentType="text/html;charset=UTF-8" %>
<script>
	    var $tpAreaTreeTable=null;  
		$(document).ready(function() {
			$tpAreaTreeTable=$('#tpAreaTreeTable').treeTable({  
		    	   theme:'vsStyle',	           
					expandLevel : 2,
					column:0,
					checkbox: false,
		            url:'${ctx}/tp/area/tpArea/getChildren?parentId=',  
		            callback:function(item) { 
		            	 var treeTableTpl= $("#tpAreaTreeTableTpl").html();
		            	 item.dict = {};

		            	 var result = laytpl(treeTableTpl).render({
								  row: item
							});
		                return result;                   
		            },  
		            beforeClick: function($tpAreaTreeTable, id) { 
		                //异步获取数据 这里模拟替换处理  
		                $tpAreaTreeTable.refreshPoint(id);  
		            },  
		            beforeExpand : function($tpAreaTreeTable, id) {   
		            },  
		            afterExpand : function($tpAreaTreeTable, id) {  
		            },  
		            beforeClose : function($tpAreaTreeTable, id) {    
		            	
		            }  
		        });
		        
		        $tpAreaTreeTable.initParents('${parentIds}', "0");//在保存编辑时定位展开当前节点
		       
		});
		
		function del(con,id){  
			jp.confirm('确认要删除省市区吗？', function(){
				jp.loading();
	       	  	$.get("${ctx}/tp/area/tpArea/delete?id="+id, function(data){
	       	  		if(data.success){
	       	  			$tpAreaTreeTable.del(id);
	       	  			jp.success(data.msg);
	       	  		}else{
	       	  			jp.error(data.msg);
	       	  		}
	       	  	})
	       	   
       		});
	
		} 
		
		function refreshNode(data) {//刷新节点
            var current_id = data.body.tpArea.id;
			var target = $tpAreaTreeTable.get(current_id);
			var old_parent_id = target.attr("pid") == undefined?'1':target.attr("pid");
			var current_parent_id = data.body.tpArea.parentId;
			var current_parent_ids = data.body.tpArea.parentIds;
			if(old_parent_id == current_parent_id){
				if(current_parent_id == '0'){
					$tpAreaTreeTable.refreshPoint(-1);
				}else{
					$tpAreaTreeTable.refreshPoint(current_parent_id);
				}
			}else{
				$tpAreaTreeTable.del(current_id);//刷新删除旧节点
				$tpAreaTreeTable.initParents(current_parent_ids, "0");
			}
        }
		function refresh(){//刷新
			var index = jp.loading("正在加载，请稍等...");
			$tpAreaTreeTable.refresh();
			jp.close(index);
		}
</script>
<script type="text/html" id="tpAreaTreeTableTpl">
			<td>
			<c:choose>
			      <c:when test="${fns:hasPermission('tp:area:tpArea:edit')}">
				    <a  href="#" onclick="jp.openSaveDialog('编辑省市区', '${ctx}/tp/area/tpArea/form?id={{d.row.id}}','800px', '500px')">
							{{d.row.name === undefined ? "": d.row.name}}
					</a>
			      </c:when>
			      <c:when test="${fns:hasPermission('tp:area:tpArea:view')}">
				    <a  href="#" onclick="jp.openViewDialog('查看省市区', '${ctx}/tp/area/tpArea/form?id={{d.row.id}}','800px', '500px')">
							{{d.row.name === undefined ? "": d.row.name}}
					</a>
			      </c:when>
			      <c:otherwise>
							{{d.row.name === undefined ? "": d.row.name}}
			      </c:otherwise>
			</c:choose>
			</td>
			<td>
				<div class="btn-group">
			 		<button type="button" class="btn  btn-primary btn-xs dropdown-toggle" data-toggle="dropdown">
						<i class="fa fa-cog"></i>
						<span class="fa fa-chevron-down"></span>
					</button>
				  <ul class="dropdown-menu" role="menu">
					<shiro:hasPermission name="tp:area:tpArea:view">
						<li><a href="#" onclick="jp.openViewDialog('查看省市区', '${ctx}/tp/area/tpArea/form?id={{d.row.id}}','800px', '500px')"><i class="fa fa-search-plus"></i> 查看</a></li>
					</shiro:hasPermission>
					<shiro:hasPermission name="tp:area:tpArea:edit">
						<li><a href="#" onclick="jp.openSaveDialog('修改省市区', '${ctx}/tp/area/tpArea/form?id={{d.row.id}}','800px', '500px')"><i class="fa fa-edit"></i> 修改</a></li>
		   			</shiro:hasPermission>
		   			<shiro:hasPermission name="tp:area:tpArea:del">
		   				<li><a  onclick="return del(this, '{{d.row.id}}')"><i class="fa fa-trash"></i> 删除</a></li>
					</shiro:hasPermission>
		   			<shiro:hasPermission name="tp:area:tpArea:add">
						<li><a href="#" onclick="jp.openSaveDialog('添加下级省市区', '${ctx}/tp/area/tpArea/form?parent.id={{d.row.id}}','800px', '500px')"><i class="fa fa-plus"></i> 添加下级省市区</a></li>
					</shiro:hasPermission>
				  </ul>
				</div>
			</td>
	</script>