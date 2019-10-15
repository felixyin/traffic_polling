<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>施工物料管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {

		});
		function save() {
            var isValidate = jp.validateForm('#inputForm');//校验表单
            if(!isValidate){
                return false;
			}else{
                jp.loading();
                jp.post("${ctx}/tp/maintenance/tpMaintenanceItem/save",$('#inputForm').serialize(),function(data){
                    if(data.success){
                        jp.getParent().refresh();
                        var dialogIndex = parent.layer.getFrameIndex(window.name); // 获取窗口索引
                        parent.layer.close(dialogIndex);
                        jp.success(data.msg)

                    }else{
                        jp.error(data.msg);
                    }
                })
			}

        }
	</script>
</head>
<body class="bg-white">
		<form:form id="inputForm" modelAttribute="tpMaintenanceItem" class="form-horizontal">
		<form:hidden path="id"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>零件名称：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/tp/material/tpMaterialPart/data" id="materialPart" name="materialPart.id" value="${tpMaintenanceItem.materialPart.id}" labelName="materialPart.name" labelValue="${tpMaintenanceItem.materialPart.name}"
							 title="选择零件名称" cssClass="form-control required" fieldLabels="零件名称|零件单位|零件单价|所属品类" fieldKeys="name|unit|price|material.name" searchLabels="零件名称|所属品类" searchKeys="name|material.id" ></sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right">所属品类：</label></td>
					<td class="width-35">
						<form:input path="category" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">单位：</label></td>
					<td class="width-35">
						<form:input path="unit" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">单价：</label></td>
					<td class="width-35">
						<form:input path="price" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>数量：</label></td>
					<td class="width-35">
						<form:input path="count" htmlEscape="false"    class="form-control required isIntGtZero"/>
					</td>
					<td class="width-15 active"><label class="pull-right">金额：</label></td>
					<td class="width-35">
						<form:input path="money" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:input path="remarks" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"></td>
		   			<td class="width-35" ></td>
		  		</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>