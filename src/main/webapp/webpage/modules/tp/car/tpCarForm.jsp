<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>车辆管理</title>
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
                jp.post("${ctx}/tp/car/tpCar/save",$('#inputForm').serialize(),function(data){
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
		<form:form id="inputForm" modelAttribute="tpCar" class="form-horizontal">
		<form:hidden path="id"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">车辆编号：</label></td>
					<td class="width-35">
						<form:input path="deviceId" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">所属单位：</label></td>
					<td class="width-35">
						<sys:treeselect id="office" name="office.id" value="${tpCar.office.id}" labelName="office.name" labelValue="${tpCar.office.name}"
							title="部门" url="/sys/office/treeData?type=2" cssClass="form-control " allowClear="true" notAllowSelectParent="true"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">车辆名称：</label></td>
					<td class="width-35">
						<form:input path="name" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">车辆品牌：</label></td>
					<td class="width-35">
						<form:input path="brand" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">车辆用途：</label></td>
					<td class="width-35">
						<form:input path="purpose" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">载人数量：</label></td>
					<td class="width-35">
						<form:input path="personCount" htmlEscape="false"    class="form-control  isIntGtZero"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">载货重量（吨）：</label></td>
					<td class="width-35">
						<form:input path="carryingCapacity" htmlEscape="false"    class="form-control  isFloatGtZero"/>
					</td>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">最后GPS位置：</label></td>
					<td class="width-35">
						<form:input path="location" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">最后位置名称：</label></td>
					<td class="width-35">
						<form:input path="locationName" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">装机时总里程：</label></td>
					<td class="width-35">
						<form:input path="startKm" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">GPS总里程：</label></td>
					<td class="width-35">
						<form:input path="sumKm" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">当前预计总里程：</label></td>
					<td class="width-35">
						<form:input path="currentKm" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">GPS运行总时间：</label></td>
					<td class="width-35">
						<form:input path="sumTime" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>