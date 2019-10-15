<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>车辆管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {

	        $('#insuranceDate').datetimepicker({
				 format: "YYYY-MM-DD"
		    });
	        $('#maintainDate').datetimepicker({
				 format: "YYYY-MM-DD"
		    });
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
					<td class="width-15 active"><label class="pull-right">所属单位：</label></td>
					<td class="width-35">
						<sys:treeselect id="office" name="office.id" value="${tpCar.office.id}"
										labelName="office.name" labelValue="${tpCar.office.name}"
										title="施工单位" url="/sys/office/myTreeData?type=3"
										cssClass="form-control required" allowClear="true"
										notAllowSelectParent="true"/>
					</td>
					<td class="width-15 active"><label class="pull-right">主要驾驶人：</label></td>
					<td class="width-35">
						<sys:userselect id="user" name="user.id" value="${tpCar.user.id}" labelName="user.name" labelValue="${tpCar.user.name}"
							    cssClass="form-control "/>
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
						<form:input path="carryingCapacity" htmlEscape="false"    class="form-control  isFloatGteZero"/>
					</td>
					<td class="width-15 active"><label class="pull-right">最后GPS位置：</label></td>
					<td class="width-35">
						<form:input path="location" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">最后位置名称：</label></td>
					<td class="width-35">
						<form:input path="locationName" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">装机时总里程：</label></td>
					<td class="width-35">
						<form:input path="startKm" htmlEscape="false"    class="form-control  isFloatGteZero"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">GPS总里程：</label></td>
					<td class="width-35">
						<form:input path="sumKm" htmlEscape="false"    class="form-control  isFloatGteZero"/>
					</td>
					<td class="width-15 active"><label class="pull-right">当前预计总里程：</label></td>
					<td class="width-35">
						<form:input path="currentKm" htmlEscape="false"    class="form-control  isFloatGteZero"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">GPS运行总时间：</label></td>
					<td class="width-35">
						<form:input path="sumTime" htmlEscape="false"    class="form-control  isIntGteZero"/>
					</td>
					<td class="width-15 active"><label class="pull-right">油耗（升/每百公里）：</label></td>
					<td class="width-35">
						<form:input path="consumption" htmlEscape="false"    class="form-control  isFloatGteZero"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">投保公司：</label></td>
					<td class="width-35">
						<form:input path="insuranceCompany" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">投保日期：</label></td>
					<td class="width-35">
						<div class='input-group form_datetime' id='insuranceDate'>
							<input type='text'  name="insuranceDate" class="form-control "  value="<fmt:formatDate value="${tpCar.insuranceDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">保养时公里数：</label></td>
					<td class="width-35">
						<form:input path="maintainKm" htmlEscape="false"    class="form-control  isFloatGteZero"/>
					</td>
					<td class="width-15 active"><label class="pull-right">保养日期：</label></td>
					<td class="width-35">
						<div class='input-group form_datetime' id='maintainDate'>
							<input type='text'  name="maintainDate" class="form-control "  value="<fmt:formatDate value="${tpCar.maintainDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
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