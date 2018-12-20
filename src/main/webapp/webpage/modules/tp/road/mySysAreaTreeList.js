<%@ page contentType="text/html;charset=UTF-8" %>
	<script>
		$(document).ready(function() {
			var to = false;
			$('#search_q').keyup(function () {
				if(to) { clearTimeout(to); }
				to = setTimeout(function () {
					var v = $('#search_q').val();
					$('#mySysAreajsTree').jstree(true).search(v);
				}, 250);
			});
			$('#mySysAreajsTree').jstree({
				'core' : {
					"multiple" : false,
					"animation" : 0,
					"themes" : { "variant" : "large", "icons":true , "stripes":true},
					'data' : {
						"url" : "${ctx}/tp/road/mySysArea/treeData",
						"dataType" : "json" 
					}
				},
				"conditionalselect" : function (node, event) {
					return false;
				},
				'plugins': ["contextmenu", 'types', 'wholerow', "search"],
				"contextmenu": {
					"items": function (node) {
						var tmp = $.jstree.defaults.contextmenu.items();
						delete tmp.create.action;
						delete tmp.rename.action;
						tmp.rename = null;
						tmp.create = {
							"label": "添加下级道路",
							"action": function (data) {
								var inst = jQuery.jstree.reference(data.reference),
									obj = inst.get_node(data.reference);
								jp.openSaveDialog('添加下级道路', '${ctx}/tp/road/mySysArea/form?parent.id=' + obj.id + "&parent.name=" + obj.text, '800px', '500px');
							}
						};
						tmp.remove = {
							"label": "删除道路",
							"action": function (data) {
								var inst = jQuery.jstree.reference(data.reference),
									obj = inst.get_node(data.reference);
								jp.confirm('确认要删除道路吗？', function(){
									jp.loading();
									$.get("${ctx}/tp/road/mySysArea/delete?id="+obj.id, function(data){
										if(data.success){
											$('#mySysAreajsTree').jstree("refresh");
											jp.success(data.msg);
										}else{
											jp.error(data.msg);
										}
									})

								});
							}
						}
						tmp.ccp = {
							"label": "编辑道路",
							"action": function (data) {
								var inst = jQuery.jstree.reference(data.reference),
									obj = inst.get_node(data.reference);
								var parentId = inst.get_parent(data.reference);
								var parent = inst.get_node(parentId);
								jp.openSaveDialog('编辑道路', '${ctx}/tp/road/mySysArea/form?id=' + obj.id, '800px', '500px');
							}
						}
						return tmp;
					}

				},
				"types":{
					'default' : { 'icon' : 'fa fa-folder' },
					'1' : {'icon' : 'fa fa-home'},
					'2' : {'icon' : 'fa fa-umbrella' },
					'3' : { 'icon' : 'fa fa-group'},
					'4' : { 'icon' : 'fa fa-file-text-o' }
				}

			}).bind("activate_node.jstree", function (obj, e) {
				var node = $('#mySysAreajsTree').jstree(true).get_selected(true)[0];
				var opt = {
					silent: true,
					query:{
						'area.id':node.id
					}
				};
				$("#areaId").val(node.id);
				$("#areaName").val(node.text);
				$('#tpRoadTable').bootstrapTable('refresh',opt);
			}).on('loaded.jstree', function() {
				$("#mySysAreajsTree").jstree('open_all');
			});
		});

		function refreshTree() {
	            $('#mySysAreajsTree').jstree("refresh");
	        }
	</script>