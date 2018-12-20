<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>施工管理</title>
	<meta name="decorator" content="ani"/>
	<!-- SUMMERNOTE -->
	<%@include file="/webpage/include/summernote.jsp" %>
	<script type="text/javascript">

		$(document).ready(function() {
			jp.ajaxForm("#inputForm",function(data){
				if(data.success){
				    jp.success(data.msg);
					jp.go("${ctx}/tp/maintenance/tpMaintenance");
				}else{
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
                    onChange: function(contents, $editable) {
                        $("input[name='process']").val($('#process').summernote('code'));//取富文本的值
                    }
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
					 format: "YYYY-MM-DD HH:mm:ss"
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
		<div class="panel-heading">
			<h3 class="panel-title"> 
				<a class="panelButton" href="${ctx}/tp/maintenance/tpMaintenance"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="tpMaintenance" action="${ctx}/tp/maintenance/tpMaintenance/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>任务编号：</label>
					<div class="col-sm-10">
						<form:input path="num" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>任务类型：</label>
					<div class="col-sm-10">
						<form:select path="jobType" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('job_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>任务来源：</label>
					<div class="col-sm-10">
						<form:select path="source" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('job_source')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>任务地点：</label>
					<div class="col-sm-10">
						<sys:gridselect url="${ctx}/tp/roadcross/tpRoadCrossing/data" id="roadCrossing" name="roadCrossing.id" value="${tpMaintenance.roadCrossing.id}" labelName="roadCrossing.name" labelValue="${tpMaintenance.roadCrossing.name}"
							 title="选择任务地点" cssClass="form-control required" fieldLabels="路口名称|所属区域" fieldKeys="name|sarea.name" searchLabels="路口名称|所属区域" searchKeys="name|sarea.name" ></sys:gridselect>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>具体位置：</label>
					<div class="col-sm-10">
						<form:input path="postion" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">派单人：</label>
					<div class="col-sm-10">
						<sys:userselect id="sendBy" name="sendBy.id" value="${tpMaintenance.sendBy.id}" labelName="sendBy.name" labelValue="${tpMaintenance.sendBy.name}"
							    cssClass="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">派单时间：</label>
					<div class="col-sm-10">
						<div class='input-group form_datetime' id='sendDate'>
							<input type='text'  name="sendDate" class="form-control "  value="<fmt:formatDate value="${tpMaintenance.sendDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>施工单位：</label>
					<div class="col-sm-10">
						<sys:treeselect id="office" name="office.id" value="${tpMaintenance.office.id}" labelName="office.name" labelValue="${tpMaintenance.office.name}"
							title="部门" url="/sys/office/treeData?type=2" cssClass="form-control required" allowClear="true" notAllowSelectParent="true"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>施工开始时间：</label>
					<div class="col-sm-10">
						<div class='input-group form_datetime' id='jobBeginDate'>
							<input type='text'  name="jobBeginDate" class="form-control required"  value="<fmt:formatDate value="${tpMaintenance.jobBeginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
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
							<input type='text'  name="jobEndDate" class="form-control required"  value="<fmt:formatDate value="${tpMaintenance.jobEndDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">总费用：</label>
					<div class="col-sm-10">
						<form:input path="money" htmlEscape="false"    class="form-control  isFloatGtZero"/>
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
						<sys:fileUpload path="prePic"  value="${tpMaintenance.prePic}" type="file" uploadPath="/tp/maintenance/tpMaintenance"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">施工中照片：</label>
					<div class="col-sm-10">
						<sys:fileUpload path="middlePic"  value="${tpMaintenance.middlePic}" type="file" uploadPath="/tp/maintenance/tpMaintenance"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">施工后照片：</label>
					<div class="col-sm-10">
						<sys:fileUpload path="afterPic"  value="${tpMaintenance.afterPic}" type="file" uploadPath="/tp/maintenance/tpMaintenance"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">审批意见：</label>
					<div class="col-sm-10">
						<form:textarea path="approve" htmlEscape="false" rows="4"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">任务状态：</label>
					<div class="col-sm-10">
						<form:radiobuttons path="status" items="${fns:getDictList('job_status')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks "/>
					</div>
				</div>
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">施工物料：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			<a class="btn btn-white btn-sm" onclick="addRow('#tpMaintenanceItemList', tpMaintenanceItemRowIdx, tpMaintenanceItemTpl);tpMaintenanceItemRowIdx = tpMaintenanceItemRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th><font color="red">*</font>零件名称</th>
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
						<sys:gridselect url="${ctx}/tp/guardrail/tpMaterialPart/data" id="tpMaintenanceItemList{{idx}}_materialPart" name="tpMaintenanceItemList[{{idx}}].materialPart.id" value="{{row.materialPart.id}}" labelName="tpMaintenanceItemList{{idx}}.materialPart.name" labelValue="{{row.materialPart.name}}"
							 title="选择零件名称" cssClass="form-control  required" fieldLabels="零件名称|零件单位|零件单价|所属品类" fieldKeys="name|unit|price|material.name" searchLabels="零件名称|所属品类" searchKeys="name|material.id" ></sys:gridselect>
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
				var tpMaintenanceItemRowIdx = 0, tpMaintenanceItemTpl = $("#tpMaintenanceItemTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(tpMaintenance.tpMaintenanceItemList)};
					for (var i=0; i<data.length; i++){
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
		                     <button class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在提交...">提 交</button>
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