<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>物料管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {
			jp.ajaxForm("#inputForm",function(data){
				if(data.success){
				    jp.success(data.msg);
					jp.go("${ctx}/tp/material/tpMaterial");
				}else{
				    jp.error(data.msg);
				    $("#inputForm").find("button:submit").button("reset");
				}
			});
			
		});
		
		function addRow(list, idx, tpl, row){
			$(list).append(Mustache.render(tpl, {
				idx: idx, delBtn: true, row: row
			}));
			$(list+idx).find("select").each(function(){
				$(this).val($(this).attr("data-value"));
			});
			$(list+idx).find("input[type='checkbox'], input[type='radio']").each(function(){
				var ss = $(this).attr("data-value").split(',');
				for (var i=0; i<ss.length; i++){
					if($(this).val() == ss[i]){
						$(this).attr("checked","checked");
					}
				}
			});
			$(list+idx).find(".form_datetime").each(function(){
				 $(this).datetimepicker({
					 format: "YYYY-MM-DD"
			    });
			});
		}
		function delRow(obj, prefix){
			var id = $(prefix+"_id");
			var delFlag = $(prefix+"_delFlag");
			if (id.val() == ""){
				$(obj).parent().parent().remove();
			}else if(delFlag.val() == "0"){
				delFlag.val("1");
				$(obj).html("&divide;").attr("title", "撤销删除");
				$(obj).parent().parent().addClass("error");
			}else if(delFlag.val() == "1"){
				delFlag.val("0");
				$(obj).html("&times;").attr("title", "删除");
				$(obj).parent().parent().removeClass("error");
			}
		}
	</script>
</head>
<body>
<div class="wrapper wrapper-content">				
<div class="row">
	<div class="col-md-12">
	<div class="panel panel-primary">
		<%--<div class="panel-heading">--%>
			<%--<h3 class="panel-title"> --%>
				<%--<a class="panelButton" href="${ctx}/tp/material/tpMaterial"><i class="ti-angle-left"></i> 返回</a>--%>
			<%--</h3>--%>
		<%--</div>--%>
		<a class="panelButton btn btn-primary" href="${ctx}/tp/material/tpMaterial"><i class="fa fa-reply"></i>
			返回</a>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="tpMaterial" action="${ctx}/tp/material/tpMaterial/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>名称：</label>
					<div class="col-sm-10">
						<form:input path="name" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>物料类型：</label>
					<div class="col-sm-10">
						<form:select path="type" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('material_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">生产单位：</label>
					<div class="col-sm-10">
						<form:select path="company" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('material_comany')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">图片：</label>
					<div class="col-sm-10">
						<sys:fileUpload path="pic"  value="${tpMaterial.pic}" type="file" uploadPath="/tp/material/tpMaterial"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">规格描述：</label>
					<div class="col-sm-10">
						<form:textarea path="standards" htmlEscape="false" rows="4"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">备注信息：</label>
					<div class="col-sm-10">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</div>
				</div>
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">物料零件：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			<a class="btn btn-white btn-sm" onclick="addRow('#tpMaterialPartList', tpMaterialPartRowIdx, tpMaterialPartTpl);tpMaterialPartRowIdx = tpMaterialPartRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th><font color="red">*</font>零件名称</th>
						<th><font color="red">*</font>零件单位</th>
						<th><font color="red">*</font>零件单价</th>
						<th>零件图片</th>
						<th>零件规格</th>
						<th>备注信息</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="tpMaterialPartList">
				</tbody>
			</table>
			<script type="text/template" id="tpMaterialPartTpl">//<!--
				<tr id="tpMaterialPartList{{idx}}">
					<td class="hide">
						<input id="tpMaterialPartList{{idx}}_id" name="tpMaterialPartList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="tpMaterialPartList{{idx}}_delFlag" name="tpMaterialPartList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<input id="tpMaterialPartList{{idx}}_name" name="tpMaterialPartList[{{idx}}].name" type="text" value="{{row.name}}"    class="form-control required"/>
					</td>
					
					
					<td>
						<select id="tpMaterialPartList{{idx}}_unit" name="tpMaterialPartList[{{idx}}].unit" data-value="{{row.unit}}" class="form-control m-b  required" >
							<option value=""></option>
							<c:forEach items="${fns:getDictList('material_unit')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
					</td>
					
					
					<td>
						<input id="tpMaterialPartList{{idx}}_price" name="tpMaterialPartList[{{idx}}].price" type="text" value="{{row.price}}"    class="form-control required"/>
					</td>
					
					
					<td>
						<sys:fileUpload path="tpMaterialPartList[{{idx}}].pic" value="{{row.pic}}" type="file" uploadPath="/tp/material/tpMaterialPart"/>
					</td>
					
					
					<td>
						<input id="tpMaterialPartList{{idx}}_standards" name="tpMaterialPartList[{{idx}}].standards" type="text" value="{{row.standards}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="tpMaterialPartList{{idx}}_remarks" name="tpMaterialPartList[{{idx}}].remarks" type="text" value="{{row.remarks}}"    class="form-control "/>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#tpMaterialPartList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var tpMaterialPartRowIdx = 0, tpMaterialPartTpl = $("#tpMaterialPartTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(tpMaterial.tpMaterialPartList)};
					for (var i=0; i<data.length; i++){
						addRow('#tpMaterialPartList', tpMaterialPartRowIdx, tpMaterialPartTpl, data[i]);
						tpMaterialPartRowIdx = tpMaterialPartRowIdx + 1;
					}
				});
			</script>
			</div>
		</div>
		</div>
		<c:if test="${mode == 'add' || mode=='edit'}">
				<div class="col-lg-3"></div>
		        <div class="col-lg-6">
					<center>
		                     <button class="btn btn-primary btn-block btn-lg btn-parsley" style="width: 150px" data-loading-text="正在提交...">
								 <i class="fa fa-save"></i>  保存</button>
					</center>
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