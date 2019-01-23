<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>路口管理管理</title>
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
                jp.post("${ctx}/tp/roadcross/tpRoadCrossing/save",$('#inputForm').serialize(),function(data){
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
		<form:form id="inputForm" modelAttribute="tpRoadCrossing" class="form-horizontal">
		<form:hidden path="id"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>所属区域：</label></td>
					<td class="width-35">
						<sys:treeselect id="sarea" name="sarea.id" value="${tpRoadCrossing.sarea.id}" labelName="sarea.name" labelValue="${tpRoadCrossing.sarea.name}"
							title="所属区域" url="/tp/roadcross/sysArea2/treeData" extId="${tpRoadCrossing.id}" cssClass="form-control required" allowClear="true"/>
					</td>
					<td class="width-15 active"><label class="pull-right">所属街道：</label></td>
					<td class="width-35">
						<form:input path="township" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>道路1：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/tp/road/tpRoad/data" id="tpRoad1" name="tpRoad1.id" value="${tpRoadCrossing.tpRoad1.id}" labelName="tpRoad1.name" labelValue="${tpRoadCrossing.tpRoad1.name}"
							 title="选择道路1" cssClass="form-control required" fieldLabels="道路1" fieldKeys="name" searchLabels="道路1" searchKeys="name" ></sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>道路2：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/tp/road/tpRoad/data" id="tpRoad2" name="tpRoad2.id" value="${tpRoadCrossing.tpRoad2.id}" labelName="tpRoad2.name" labelValue="${tpRoadCrossing.tpRoad2.name}"
							 title="选择道路2" cssClass="form-control required" fieldLabels="道路2" fieldKeys="name" searchLabels="道路2" searchKeys="name" ></sys:gridselect>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">道路3：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/tp/road/tpRoad/data" id="tpRoad3" name="tpRoad3.id" value="${tpRoadCrossing.tpRoad3.id}" labelName="tpRoad3.name" labelValue="${tpRoadCrossing.tpRoad3.name}"
							 title="选择道路3" cssClass="form-control " fieldLabels="道路3" fieldKeys="name" searchLabels="道路3" searchKeys="name" ></sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right">道路4：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/tp/road/tpRoad/data" id="tpRoad4" name="tpRoad4.id" value="${tpRoadCrossing.tpRoad4.id}" labelName="tpRoad4.name" labelValue="${tpRoadCrossing.tpRoad4.name}"
							 title="选择道路4" cssClass="form-control " fieldLabels="道路4" fieldKeys="name" searchLabels="道路4" searchKeys="name" ></sys:gridselect>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>经度：</label></td>
					<td class="width-35">
						<form:input path="lng" htmlEscape="false"    class="form-control required isFloatGtZero"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>维度：</label></td>
					<td class="width-35">
						<form:input path="latCal" htmlEscape="false"    class="form-control required isFloatGtZero"/>
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