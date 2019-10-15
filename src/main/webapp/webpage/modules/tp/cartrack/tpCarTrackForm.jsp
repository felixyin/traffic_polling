<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>出车记录管理</title>
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
                jp.post("${ctx}/tp/cartrack/tpCarTrack/save",$('#inputForm').serialize(),function(data){
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
		<form:form id="inputForm" modelAttribute="tpCarTrack" class="form-horizontal">
		<form:hidden path="id"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>关联车辆：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/tp/car/tpCar/data" id="car" name="car.id" value="${tpCarTrack.car.id}" labelName="car.name" labelValue="${tpCarTrack.car.name}"
							 title="选择关联车辆" cssClass="form-control required" fieldLabels="车辆名称|所属单位" fieldKeys="name|office.name" searchLabels="车辆名称|所属单位" searchKeys="name|office.name" ></sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right">开始位置：</label></td>
					<td class="width-35">
						<form:input path="locationBegin" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">开始位置地名：</label></td>
					<td class="width-35">
						<form:input path="nameBegin" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">结束位置：</label></td>
					<td class="width-35">
						<form:input path="locationEnd" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">结束位置地名：</label></td>
					<td class="width-35">
						<form:input path="nameEnd" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>开始时间：</label></td>
					<td class="width-35">
						<form:input path="timeBegin" htmlEscape="false"  value="${fns:formatDateTime(tpCarTrack.timeBegin)}"  class="form-control required date"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>结束时间：</label></td>
					<td class="width-35">
						<form:input path="timeEnd" htmlEscape="false"    value="${fns:formatDateTime(tpCarTrack.timeEnd)}"   class="form-control required date"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>行驶里程：</label></td>
					<td class="width-35">
						<form:input path="km" htmlEscape="false"    class="form-control required isFloatGtZero"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>用车类型：</label></td>
					<td class="width-35">
						<form:select path="driverType" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('driver_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right">关联任务：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/tp/maintenance/tpMaintenance/data" id="maintenance" name="maintenance.id" value="${tpCarTrack.maintenance.id}" labelName="maintenance.num" labelValue="${tpCarTrack.maintenance.num}"
							 title="选择关联任务" cssClass="form-control " fieldLabels="任务编号|所属路口|所属道路|施工单位|施工负责人" fieldKeys="num|roadcross.name|road.name|office.name|leaderBy.name" searchLabels="任务编号|任务类型|施工负责人" searchKeys="num|jobSource|leaderBy.name" ></sys:gridselect>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">任务描述：</label></td>
					<td class="width-35">
						<form:input path="jobDesc" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>关联驾驶人：</label></td>
					<td class="width-35">
						<sys:userselect id="user" name="user.id" value="${tpCarTrack.user.id}" labelName="user.name" labelValue="${tpCarTrack.user.name}"
							    cssClass="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
					<td class="width-15 active"></td>
		   			<td class="width-35" ></td>
		  		</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>