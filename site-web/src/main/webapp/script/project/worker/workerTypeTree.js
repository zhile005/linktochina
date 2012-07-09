$(function(){
	var editInfoV = $("#editInfo").valilater({eventTrigger:true,debug:true});
	var newInfoV = $("#newInfo").valilater({eventTrigger:true});
	var thisdefault = {
			url:"doWorkerTypeTree.action",
			currentObject:{},
			beforPost:function(nodeDate) {
				return {
					"operation" : "get_treeNode",
					"workerType.parentTypeCode" : nodeDate.attr ? nodeDate.attr("workerTypeCode") : '0'
				};
			},
			postSuccess:function(data, textStatus){
				var have = false;
				for(var i = 0;i < data.length; i++){
					if(data[i].attr["parentTypeCode"] == '0'){
						//已经初始化完毕隐藏初始化按钮
						$("#initbtn").hide();
					}
					if(thisdefault.currentObject["workerTypeCode"] == data[i].attr["workerTypeCode"]){
						thisdefault.currentObject = data[i].attr;
						have = true;
						break;
					}
				}
				if(have == true){
					thisdefault.show();
				}
//				alert(thisdefault.currentObject["activeNum"]);
			},
			show:function(){
				$("#workerTypeCode").text(thisdefault.currentObject["workerTypeCode"]);
				$("#workerTypeName").text(thisdefault.currentObject["workerTypeName"]);
				$("#wtype").text(thisdefault.currentObject["wtype"]);
				$("#wtypeText").text(thisdefault.currentObject["wtypeText"]);
				$("#version").text(thisdefault.currentObject["version"]);
				$("#failMaxNum").text(thisdefault.currentObject["failMaxNum"]);
				$("#passTimeNum").text(thisdefault.currentObject["passTimeNum"]);
				$("#parentTypeCode").text(thisdefault.currentObject["parentTypeCode"]);
				$("#yn").text(thisdefault.currentObject["yn"]);
				$("#ynText").text(thisdefault.currentObject["ynText"]);
				$("#cronexpression").text(thisdefault.currentObject["cronexpression"]);
				$("#watchYnText").text(thisdefault.currentObject["watchYnText"]);
				$("#activeNum").text(thisdefault.currentObject["activeNum"]);
				$("#waitOrders").text(thisdefault.currentObject["waitOrders"]);
				if(thisdefault.currentObject["wtype"] == 1){
					//同步事件
					$("#new_node").show();
				}else{
					//worker
					$("#new_node").hide();
				}
				showDiv("showInfo");
			},
			edit:function(){
				$("#workerTypeCode"+"_edit").val(thisdefault.currentObject["workerTypeCode"]);
				$("#wtype"+"_edit").val(thisdefault.currentObject["wtype"]);
				$("#workerTypeCode_text"+"_edit").text(thisdefault.currentObject["workerTypeCode"]);
				$("#workerTypeName"+"_edit").val(thisdefault.currentObject["workerTypeName"]);
				$("#wtypeText"+"_edit").text(thisdefault.currentObject["wtypeText"]);
				$("#version"+"_edit").text(thisdefault.currentObject["version"]);
				$("#failMaxNum"+"_edit").val(thisdefault.currentObject["failMaxNum"]);
				$("#passTimeNum"+"_edit").val(thisdefault.currentObject["passTimeNum"]);
				$("#parentTypeCode"+"_edit").text(thisdefault.currentObject["parentTypeCode"]);
				$("#yn"+"_edit").val(thisdefault.currentObject["yn"]);
				$("#cronexpression"+"_edit").val(thisdefault.currentObject["cronexpression"]);
				$("#watchYn"+"_edit").val(thisdefault.currentObject["watchYn"]);
				$("#activeNum"+"_edit").text(thisdefault.currentObject["activeNum"]);
				$("#waitOrders"+"_edit").text(thisdefault.currentObject["waitOrders"]);
				showDiv("editInfo");
				editInfoV.reInitV();
			}
	};
	var showDiv = function(showDomId){
		$("#info > div.jd_hide").hide();
		$("#"+showDomId).show();
	};
	var workerTree = $("#workTree").jstree({
		"plugins" : ["core", "themes", "json_data","ui"],
		"json_data" : {
			"ajax" : {
				"type" : "post",
				"url" : thisdefault.url,
				"data" : thisdefault.beforPost,
				"success" : thisdefault.postSuccess
			}
		}
	}).bind("select_node.jstree", function(e, data) {
		//单击事件
		var selectedDate = data.rslt.obj;
		if($("#editInfo").is(":visible") 
				&& thisdefault.currentObject["workerTypeCode"] !== selectedDate.attr("workerTypeCode")){
			if(confirm("确认不需要保存表单吗") == false){
				return;
			}
		}
		thisdefault.currentObject["workerTypeCode"] = selectedDate.attr("workerTypeCode");
		thisdefault.currentObject["workerTypeName"] = selectedDate.attr("workerTypeName");
		thisdefault.currentObject["wtype"] = selectedDate.attr("wtype");
		thisdefault.currentObject["wtypeText"] = selectedDate.attr("wtypeText");
		thisdefault.currentObject["version"] = selectedDate.attr("version");
		thisdefault.currentObject["failMaxNum"] = selectedDate.attr("failMaxNum");
		thisdefault.currentObject["passTimeNum"] = selectedDate.attr("passTimeNum");
		thisdefault.currentObject["parentTypeCode"] = selectedDate.attr("parentTypeCode");
		thisdefault.currentObject["yn"] = selectedDate.attr("yn");
		thisdefault.currentObject["ynText"] = selectedDate.attr("ynText");
		thisdefault.currentObject["cronexpression"] = selectedDate.attr("cronexpression");
		thisdefault.currentObject["watchYn"] = selectedDate.attr("watchYn");
		thisdefault.currentObject["watchYnText"] = selectedDate.attr("watchYnText");
		thisdefault.currentObject["activeNum"] = selectedDate.attr("activeNum");
		thisdefault.currentObject["waitOrders"] = selectedDate.attr("waitOrders");
		thisdefault.show();
    }).bind("dblclick.jstree", function(event) {
    	//双击事件
		thisdefault.edit();
    }).bind("refresh.jstree", function(event) {
    	workerTree.jstree("reopen");
    	workerTree.jstree("open_node",this);
    });
	//系统初始化
	$("#initbtn").bind("click",function(e){
		var thiz = $(this);
		thiz.showRuningMsg();
		$.ajax({
		   	type: "POST",
		   	url: "doInit.action?_charset_=utf-8",
		   	dataType:"json",
		   	success: function(data, textStatus){
		   		//查询出结果
		   		if(data.success == true){
		   			workerTree.jstree("refresh","-1");
		   			alert('初始化成功,请立即执行重启worker服务器!');
		   		}else if(data.errorMsg){
		   			alert(data.errorMsg);
		   		}else{
		   			alert('服务器正忙，请稍后再试');
		   		}
		   		thiz.removeRuningMsg();
		   	},
		   	error:function (XMLHttpRequest, textStatus, errorThrown) {
		   		alert('服务器正忙，请稍后再试');
		   		thiz.removeRuningMsg();
			}
		});
	});
	//刷新按钮
	$("#refresh_node").bind("click",function(e){
		var refTypeCode = thisdefault.currentObject["parentTypeCode"];
		workerTree.jstree("refresh","[workerTypeCode='"+refTypeCode+"']");
		workerTree.jstree("open_node", '[workerTypeCode="'+refTypeCode+'"]');
	});
	$("#workerTypeCode_new").bind("click",function(e){
		var selected = $(this).find("option:selected");
		if(selected.val() != ""){
			$("#workerTypeName_new").val(selected.text());
		}else{
			$("#workerTypeName_new").val("");
		}
	});
	//点击新建节点时,取得最新的没有部署的worker列表
	$("#new_node").bind("click",function(e){
		$.ajax({
		   	type: "POST",
		   	url: "getNoDeployWorkerList.action?_charset_=utf-8",
		   	dataType:"json",
		   	success: function(data, textStatus){
		   		//查询出结果
		   		if(data.success == true){
		   			var workerArray = data.info;
		   			var selectHtml = "<option value=''>请选择</option>";
		   			for(var i = 0; i < workerArray.length; i++){
		   				selectHtml = selectHtml + "<option value='"+workerArray[i].workerTypeCode+"' >"+workerArray[i].workerTypeName+"</option>";
		   			}
		   			$("#workerTypeCode_new").html(selectHtml);
		   			$("#workerTypeName_new").val("");
		   			$("#parentTypeCodeName_new").text(thisdefault.currentObject["workerTypeName"]+"["+thisdefault.currentObject["workerTypeCode"]+"]");
		   			$("#parentTypeCode_new").val(thisdefault.currentObject["workerTypeCode"]);
		   			showDiv("newInfo");
					newInfoV.reInitV();
		   		}else if(data.errorMsg){
		   			alert(data.errorMsg);
		   		}else{
		   			alert('服务器正忙，请稍后再试');
		   		}
		   	},
		   	error:function (XMLHttpRequest, textStatus, errorThrown) {
		   		alert('服务器正忙，请稍后再试');
			}
		});
	});
	//删除节点
	$("#del_node").bind("click",function(e){
		var thiz = $(this);
		var postForm = {};
		postForm["workerType.workerTypeCode"] = thisdefault.currentObject["workerTypeCode"];
		postForm["workerType.parentTypeCode"] = thisdefault.currentObject["parentTypeCode"];
		if(confirm("确定删除吗？") == false){
			return;
		}
		thiz.showRuningMsg();
		$.ajax({
		   	type: "POST",
		   	url: "doDeleteWorkerType.action?_charset_=utf-8",
		   	data: postForm,
		   	dataType:"json",
		   	success: function(data, textStatus){
		   		//查询出结果
		   		if(data.success == true){
		   			thisdefault.currentObject = {};
		   			showDiv("");
//		   			workerTree.find("[workerTypeCode='"+refTypeCode+"']").trigger("click");
		   			var refTypeCode = data.info["parentTypeCode"];
	   				workerTree.jstree("refresh","[workerTypeCode='"+refTypeCode+"']");
		   		}else if(data.errorMsg){
		   			alert(data.errorMsg);
		   		}else{
		   			alert('服务器正忙，请稍后再试');
		   		}
		   		thiz.removeRuningMsg();
		   	},
		   	error:function (XMLHttpRequest, textStatus, errorThrown) {
		   		alert('服务器正忙，请稍后再试');
		   		thiz.removeRuningMsg();
			}
		});
	});
	//保存
	$("#save_new").bind("click",function(e){
		alert(newInfoV.attr("id")+"="+newInfoV.check());
		if(newInfoV.check() == true){
			var thiz = $(this);
			var postForm = {};
			postForm["workerType.workerTypeCode"] = $("#workerTypeCode"+"_new").val();
			postForm["workerType.workerTypeName"] = $("#workerTypeName"+"_new").val();
			postForm["workerType.wtype"] = $("#wtype"+"_new").val();
			postForm["workerType.failMaxNum"] = $("#failMaxNum"+"_new").val();
			postForm["workerType.passTimeNum"] = $("#passTimeNum"+"_new").val();
			postForm["workerType.parentTypeCode"] = $("#parentTypeCode"+"_new").val();
			postForm["workerType.yn"] = $("#yn"+"_new").val();
			postForm["workerType.cronexpression"] = $("#cronexpression"+"_new").val();
			postForm["workerType.parentTypeCode"] = $("#parentTypeCode"+"_new").val();
			postForm["workerType.watchYn"] = $("#watchYn"+"_new").val();
			if(confirm("确定添加吗？") == false){
				return;
			}
			thiz.showRuningMsg();
			$.ajax({
				type: "POST",
				url: "doSaveWorkerType.action?_charset_=utf-8",
				data: postForm,
				dataType:"json",
				success: function(data, textStatus){
					//查询出结果
					var refTypeCode = $("#parentTypeCode"+"_new").val();
					if(data.success == true){
						//有个bug，假如当前节点没有子节点，则点击打开变成已打开状态，但是添加子节点后，不能通过reopen 新打开子节点
						workerTree.jstree("refresh","[workerTypeCode='"+refTypeCode+"']");
						var is_closed = workerTree.jstree("is_closed","[workerTypeCode='"+refTypeCode+"']");
						if(is_closed == true){
							workerTree.jstree("open_node","[workerTypeCode='"+refTypeCode+"']");
						}
						thisdefault.show();
					}else if(data.errorMsg){
						alert(data.errorMsg);
					}else{
						alert('服务器正忙，请稍后再试');
					}
					thiz.removeRuningMsg();
				},
				error:function (XMLHttpRequest, textStatus, errorThrown) {
					alert('服务器正忙，请稍后再试');
					thiz.removeRuningMsg();
				}
			});
		}
	});
	$("#reset_new").bind("click",function(e){
		thisdefault.show();
	});
	//保存更改
	$("#save_edit").bind("click",function(e){
		if(editInfoV.check() == true){
			var thiz = $(this);
			var postForm = {};
			postForm["workerType.workerTypeCode"] = $("#workerTypeCode"+"_edit").val();
			postForm["workerType.workerTypeName"] = $("#workerTypeName"+"_edit").val();
			postForm["workerType.wtype"] = $("#wtype"+"_edit").val();
			postForm["workerType.failMaxNum"] = $("#failMaxNum"+"_edit").val();
			postForm["workerType.passTimeNum"] = $("#passTimeNum"+"_edit").val();
			postForm["workerType.parentTypeCode"] = $("#parentTypeCode"+"_edit").val();
			postForm["workerType.yn"] = $("#yn"+"_edit").val();
			postForm["workerType.cronexpression"] = $("#cronexpression"+"_edit").val();
			postForm["workerType.parentTypeCode"] = $("#parentTypeCode"+"_edit").val();
			postForm["workerType.watchYn"] = $("#watchYn"+"_edit").val();
			if(confirm("确定修改吗？") == false){
				return;
			}
			thiz.showRuningMsg();
			$.ajax({
				type: "POST",
				url: "doUpdateWorkerType.action?_charset_=utf-8",
				data: postForm,
				dataType:"json",
				success: function(data, textStatus){
					//查询出结果
					if(data.success == true){
						thisdefault.currentObject = data.info;
						workerTree.jstree("refresh","[workerTypeCode='"+thisdefault.currentObject["parentTypeCode"]+"']");
						thisdefault.show();
					}else if(data.errorMsg){
						alert(data.errorMsg);
					}else{
						alert('服务器正忙，请稍后再试');
					}
					thiz.removeRuningMsg();
				},
				error:function (XMLHttpRequest, textStatus, errorThrown) {
					alert('服务器正忙，请稍后再试');
					thiz.removeRuningMsg();
				}
			});
		}
	});
	$("#reset_edit").bind("click",function(e){
		thisdefault.show();
	});
});
