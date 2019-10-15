<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>历史轨迹管理</title>
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
                jp.post("${ctx}/tp/gpshistory/tpGpsHistory/save",$('#inputForm').serialize(),function(data){
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
		<form:form id="inputForm" modelAttribute="tpGpsHistory" class="form-horizontal">
		<form:hidden path="id"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>出车记录表外键：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/tp/cartrack/tpCarTrack/data" id="carTrack" name="carTrack.id" value="${tpGpsHistory.carTrack.id}" labelName="carTrack.name" labelValue="${tpGpsHistory.carTrack.name}"
							 title="选择出车记录表外键" cssClass="form-control required" fieldLabels="" fieldKeys="" searchLabels="" searchKeys="" ></sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right">车辆表外键：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/tp/car/tpCar/data" id="car" name="car.id" value="${tpGpsHistory.car.id}" labelName="car.name" labelValue="${tpGpsHistory.car.name}"
							 title="选择车辆表外键" cssClass="form-control " fieldLabels="" fieldKeys="" searchLabels="" searchKeys="" ></sys:gridselect>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>设备编号：</label></td>
					<td class="width-35">
						<form:input path="deviceId" htmlEscape="false"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>上传时间：</label></td>
					<td class="width-35">
						<form:input path="upTime" htmlEscape="false"    class="form-control required date"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>定位状态：</label></td>
					<td class="width-35">
						<form:input path="locationStatus" htmlEscape="false"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>GPS纬度：</label></td>
					<td class="width-35">
						<form:input path="latGps" htmlEscape="false"    class="form-control required isFloatGtZero"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>纬度半球：</label></td>
					<td class="width-35">
						<form:input path="latHemisphere" htmlEscape="false"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>GPS经度：</label></td>
					<td class="width-35">
						<form:input path="lonGps" htmlEscape="false"    class="form-control required isFloatGtZero"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>经度半球：</label></td>
					<td class="width-35">
						<form:input path="lonHemisphere" htmlEscape="false"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>地面速率：</label></td>
					<td class="width-35">
						<form:input path="groundRate" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>地面航向：</label></td>
					<td class="width-35">
						<form:input path="groundDirection" htmlEscape="false"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>磁偏角：</label></td>
					<td class="width-35">
						<form:input path="declination" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>磁偏角方向：</label></td>
					<td class="width-35">
						<form:input path="declinationDirection" htmlEscape="false"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>模式指示：</label></td>
					<td class="width-35">
						<form:input path="model" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>解密后纬度：</label></td>
					<td class="width-35">
						<form:input path="latCal" htmlEscape="false"    class="form-control required isFloatGtZero"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>解密后经度：</label></td>
					<td class="width-35">
						<form:input path="lonCal" htmlEscape="false"    class="form-control required isFloatGtZero"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">转换高德纬度：</label></td>
					<td class="width-35">
						<form:input path="latGD" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">转换高德经度：</label></td>
					<td class="width-35">
						<form:input path="lonGD" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>