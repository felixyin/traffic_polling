<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>资产管理</title>
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
                jp.post("${ctx}/tp/dam/tpDam/save",$('#inputForm').serialize(),function(data){
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



		// 地址选择，保存成功后，显示到施工管理表单控件中
		function postionSelectCallback(param) {
			// console.log(param);
			if (param && param.tpMaintenance) {
				var tm = param.tpMaintenance;

				var location = tm.location;
				$('#location').val(location);

				var area = tm.area;
				$('#areaName').val(area.name);
				$('#areaId').val(area.id);

				var roadcross = tm.roadcross;
				$('#roadcrossName').val(roadcross.name);
				$('#roadcrossId').val(roadcross.id);

				var nearestJunction = tm.nearestJunction;
				$('#nearestJunction').val(nearestJunction);

				var road = tm.road;
				$('#roadName').val(road.name);
				$('#roadId').val(road.id);

				$('#road\\.roadType').val(road.roadType);

				var address = tm.address;
				$('#address').val(address);

				var nearestPoi = tm.nearestPoi;
				$('#nearestPoi').val(nearestPoi);

				//    打开详细地址信息
				$('#_addressDetail_show').trigger('click');
			}
		}

		// 打开选择详细地址对话框
		function openSelectPostionDialog() {
			var location = $('#location').val();
			var roadcrossName = $('#roadcrossName').val();
			jp.openChildDialog("编辑位置", "${ctx}/tp/maintenance/tpMaintenance/selectPostion?roadcrossName=" + roadcrossName + "&location=" + location, "1050px", "580px", postionSelectCallback);
		}

	</script>
</head>
<body class="bg-white">
		<form:form id="inputForm" modelAttribute="tpDam" class="form-horizontal">
		<form:hidden path="id"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>资产类型：</label></td>
					<td class="width-35">
						<form:select path="damType" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('dam_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>选择位置：</label></td>
					<td class="width-35" >
						<button type="button" class="btn btn-primary btn-block  btn-parsley"
								data-loading-text="正在计算..."
								onclick="openSelectPostionDialog();">
						<i class="fa fa-map-marker "></i>
							${tpDam.roadcross.name }${tpDam.nearestJunction}
						</button>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>资产描述：</label></td>
					<td class="width-35" colspan="3">
						<form:textarea path="description" htmlEscape="false" rows="8"    class="form-control required"/>
					</td>
				</tr>

				<tr>
					<td class="width-15 active"><label class="pull-right">图片：</label></td>
					<td class="width-35">
						<sys:fileUpload path="pic"  value="${tpDam.pic}" type="file" uploadPath="/tp/dam/tpDam"/>
					</td>
					<td class="width-15 active"><label class="pull-right">尺寸：</label></td>
					<td class="width-35">
						<form:input path="size" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>经纬度：</label></td>
					<td class="width-35">
						<form:input path="location" htmlEscape="false"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>所属区域：</label></td>
					<td class="width-35">
						<sys:treeselect id="area" name="area.id" value="${tpDam.area.id}" labelName="area.name" labelValue="${tpDam.area.name}"
							title="区域" url="/sys/area/treeData" cssClass="form-control required" allowClear="true" notAllowSelectParent="true"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>所属路口：</label></td>
					<td class="width-35">
						<sys:gridselect   url="${ctx}/tp/roadcross/tpRoadCrossing/data" id="roadcross"
										name="roadcross.id" value="${tpDam.roadcross.id}"
										labelName="roadcross.name"
										labelValue="${tpDam.roadcross.name}"
										title="选择所属路口" cssClass="form-control required"
										fieldLabels="路口名称|所属区域|所属街道" fieldKeys="name|sarea.name|township"
										searchLabels="路口名称|所属区域"
										searchKeys="name|sarea.name"></sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>所属路口相对位置：</label></td>
					<td class="width-35">
						<form:input path="nearestJunction" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>所属道路：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/tp/road/tpRoad/data" id="road" name="road.id"
										value="${tpDam.road.id}" labelName="road.name"
										labelValue="${tpDam.road.name}"
										title="选择附近道路" cssClass="form-control " fieldLabels="道路名称|所属区域"
										fieldKeys="name|area.name" searchLabels="道路名称|所属区域"
										searchKeys="name|area.name"></sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>搜索用地址：</label></td>
					<td class="width-35">
						<form:input path="address" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>搜索地址相对位置：</label></td>
					<td class="width-35">
						<form:input path="nearestPoi" htmlEscape="false"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>