var $thisGrid = jQuery("#list2");
$thisGrid.rowId = 0;
$thisGrid.reqDate = {};
$thisGrid.content = {};
/**
 * 查询
 * @param {} depObject
 */
var postFormV = $("#postForm").valilater({eventTrigger:true});
function search(){
	if(postFormV.checkPassNull() == true){
		//校验
		$thisGrid.clearGridData();
		$thisGrid.setGridParam({
			postData:{
				"worker.workerId":$("#workerId").val(),
				"worker.workerTypeCode":$("#workerTypeCode").val(),
				"worker.wtype":$("#wtype").val(),
				"worker.refOreders":$("#refOreders").val(),
				"worker.workerStatus":$("#workerStatus").val(),
				"worker.refEworkerId":$("#refEworkerId").val(),
				"worker.createTime":$("#createTime").val(),
				"worker.msg":$("#msg").val()
			}
		}).trigger("reloadGrid");
	}
 	return true;
}
/**
 * 查询
 */
function show(rowIndex){
	$thisGrid.content.html("正在获取。。。");
	$thisGrid.content.dialog("open");
	var info = $thisGrid.reqDate.rows[rowIndex-1];
	$.ajax({
	   	type: "POST",
	   	url: "doShowWorkerContent.action?_charset_=utf-8",
	   	data:{
	   		"worker.workerId":info.workerId,
	   		"worker.refEworkerId":info.refEworkerId,
	   		"worker.wtype":info.wtype
	   	},
	   	dataType:"json",
	   	async: false,
	   	success: function(data, textStatus){
	   		if(data.success){
	   			var tempArray = new Array();
	   			for(var object in data.contentInfo){
	   				tempArray.push("<span class='boldText'>"+object+"</span>:<span class='redText'>"+data.contentInfo[object]+"</span>");
	   			}
	   			tempArray.push("原生xml："+data.info.workerContent.replace(/\</g,"&#60;").replace(/\>/g,"&#62;"));
	   			$thisGrid.content.html(tempArray.join("</br>"));
	   		}else if(data.errorMsg){
	   			$thisGrid.content.html(data.errorMsg);
	   		}else{
	   			$thisGrid.content.html('失败，请稍后再试');
	   		}
	   	},
	   	error:function (XMLHttpRequest, textStatus, errorThrown) {
	   		$thisGrid.content.html('服务器正忙，请稍后再试');
		}
	});
}
/**
 * 查询
 */
function reduce(rowIndex,thiz){
	var info = $thisGrid.reqDate.rows[rowIndex-1];
	$(thiz).showRuningMsg();
	$.ajax({
	   	type: "POST",
	   	url: "doReduceWorkerFallnum.action?_charset_=utf-8",
	   	data:{
	   		"worker.workerId":info.workerId
	   	},
	   	dataType:"json",
	   	async: false,
	   	success: function(data, textStatus){
	   		if(data.success){
	   			//重新加载
	   			$thisGrid.trigger("reloadGrid");
	   		}else if(data.errorMsg){
	   			alert(data.errorMsg);
	   		}else{
	   			alert('失败，请稍后再试');
	   		}
	   		$(thiz).removeRuningMsg();
	   	},
	   	error:function (XMLHttpRequest, textStatus, errorThrown) {
	   		alert('服务器正忙，请稍后再试');
	   		$(thiz).removeRuningMsg();
		}
	});
}
$(function() {
	$("#createTime").jMy97DatePicker({readOnly:true});
	/**
	 * 添加列号
	 */
	function rowIdformatter(cellval,opts,rwdat,_act){
		return $thisGrid.rowId++;
	}
	/**
	 * 展示信息
	 */
	function showInfoformatter(rowIndex,info){
		return "<a href='javascript:show("+rowIndex+")'>"+info["workerId"]+"</a>";
	}
	/**
	 * 执行次数减一
	 */
	function reduceformatter(rowIndex,info){
		return "<input type='button' onclick='javascript:reduce("+rowIndex+",this)' value='执行次数减一' />";
	}
	/**
	 * 添加处理函数
	 */
	function thisLoadComplete(reqDate){
		$thisGrid.reqDate = reqDate;
		$thisGrid.rowId = 0;
		var rows = reqDate.rows;
		for(var i=0;i<rows.length;i++){
			var workerId = rows[i]["workerId"];
			$thisGrid.jqGrid('setCell',i+1,'function',reduceformatter(i+1,rows[i]));
			$thisGrid.jqGrid('setCell',i+1,'workerId',showInfoformatter(i+1,rows[i]));
		}
//		alert("LoadComplete");
		return true;
	}
	$thisGrid.jqGrid({
		url:'doWorker.action?_charset_=utf-8', 
		datatype: "json", 
		mtype:"POST",
		colNames:['行号','任务编号','workerTypeCode','所属任务','wtype',
		          '任务类型','关联任务号','关联原单号','执行次数',
		          '超时时间','workerStatus','状态','预占应用名','yn',
		          '建立时间','消息','功能'], 
		colModel:[
			{name:'thisGridRowId',align:"center",sortable:true,hidden:true,width:55,formatter:rowIdformatter},
			{name:'workerId',align:"center"}, 
			{name:'workerTypeCode',align:"center",sortable:false,hidden:true}, 
			{name:'workerTypeName',align:"center",sortable:true,hidden:false}, 
			{name:'wtype',align:"center",sortable:false,cellEdit:true,hidden:true},
			{name:'wtypeText',align:"center",sortable:false,cellEdit:true},
			{name:'refEworkerId',align:"center",sortable:false}, 
			{name:'refOreders',align:"center",sortable:false}, 
			{name:'failMaxNum',align:"center",sortable:false},
			{name:'passTime',align:"center",sortable:false,formatoptions:{newformat: 'Y-m-d'},formatter:'date'}, 
			{name:'workerStatus',align:"center",sortable:false,hidden:true},
			{name:'workerStatusText',align:"center",sortable:true},
			{name:'appLogo',align:"center",sortable:false},
			{name:'yn',align:"center",sortable:false,hidden:true}, 
			{name:'createTime',align:"center",sortable:true,formatoptions:{newformat: 'Y-m-d'},formatter:'date'},
			{name:'msg',align:"center",sortable:false,cellEdit:true},
			{name:'function',align:"center",sortable:false,cellEdit:true}
		],
		width:baseSetting.boxWidth,
		height:350,
		rowNum:40,
		rowList:[20,40,60,80], 
		pager: '#pager2', 
		viewrecords: true, 
		editurl:"clientArray",
		loadComplete:thisLoadComplete,
		jsonReader:{repeatitems: false, id: "0"}
	});
	$thisGrid.content = $("<div/>").appendTo(document.body).dialog({
		title:"详细信息",
		autoOpen: false,
		width: 400,
		height: 500,
		modal: true,
		buttons: {
				'关闭': function() {
					$(this).dialog("close");
				}
			},
			close: function() {
			}
	});
	//系统初始化
	if($("#sql").val().trim() != ""){
		$("#sqlDiv").show();
	}
	$("#sqlSearch").bind("click",function(e){
		var thiz = $(this);
		thiz.showRuningMsg();
		$("#sqlDate").html("");
		$.ajax({
		   	type: "POST",
		   	url: "getCustSql.action?_charset_=utf-8",
		   	data:{
		   		"sql":$("#sql").val()
		   	},
		   	dataType:"json",
		   	success: function(data, textStatus){
		   		//查询出结果
		   		if(data.success == true){
		   			var sqlDates = data.info;
		   			var tmp = new Array();
		   			var first = true;
		   			for(var i = 0; i < sqlDates.length; i++){
		   				if(first){
		   					tmp.push("<tr>");
		   					for(var pro in sqlDates[i]){
		   						tmp.push("<th>");
		   						tmp.push(pro);
		   						tmp.push("</th>");
		   					}
		   					tmp.push("</tr>");
		   					first = false;
		   				}
		   				tmp.push("<tr>");
	   					for(var pro in sqlDates[i]){
	   						tmp.push("<td>");
	   						tmp.push(sqlDates[i][pro]);
	   						tmp.push("</td>");
	   					}
	   					tmp.push("</tr>");
		   			}
		   			$("#sqlDate").html(tmp.join(""));
		   		}else if(data.errorMsg){
		   			$("#sqlDate").html("<tr><td>"+data.errorMsg+"</td></tr>");
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
});