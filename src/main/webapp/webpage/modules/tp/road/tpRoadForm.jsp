<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>道路管理</title>
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
                jp.post("${ctx}/tp/road/tpRoad/save",$('#inputForm').serialize(),function(data){
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
		<form:form id="inputForm" modelAttribute="tpRoad" class="form-horizontal">
		<form:hidden path="id"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>道路名称：</label></td>
					<td class="width-35">
						<form:input path="name" htmlEscape="false" maxlength="100"  minlength="3"   class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>所属区域：</label></td>
					<td class="width-35">
						<sys:treeselect id="area" name="area.id" value="${tpRoad.area.id}" labelName="area.name" labelValue="${tpRoad.area.name}"
							title="所属区域" url="/tp/road/sysArea/treeData" extId="${tpRoad.id}" cssClass="form-control required" allowClear="true"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>道路类型：</label></td>
					<td class="width-35">
						<form:select path="roadType" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('road_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right">道路长度(m)：</label></td>
					<td class="width-35">
						<form:input path="length" htmlEscape="false"    class="form-control  isIntGtZero"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">道路宽度(m)：</label></td>
					<td class="width-35">
						<form:input path="width" htmlEscape="false"    class="form-control  isIntGtZero"/>
					</td>
					<td class="width-15 active"><label class="pull-right">占地面积(m2)：</label></td>
					<td class="width-35">
						<form:input path="acreage" htmlEscape="false"    class="form-control  isIntGtZero"/>
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