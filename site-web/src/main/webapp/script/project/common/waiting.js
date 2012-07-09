$(function() {
	var $_waitMsg = false;
	var getShowId = function(baseDivId){
		var returnShowTreeId = "";
		if(!baseDivId){
			baseDivId = "_baseTreeId";
		}
		for(var i=0;;i++){
			if($("#"+baseDivId+i).size()==0){
				returnShowTreeId = ""+baseDivId+i;
				break;
			}
		}
		return returnShowTreeId;
	};
	//添加外部函数
	window.showWaitMsg = function(){
		if($_waitMsg === false){
			var id = getShowId("_showWaitMsgId");
			$(document.body).append('<div id="'+id+'" class="stockcenter">正在更新，请稍等</div>');
			$_waitMsg = $("#"+id).dialog({
				title:"提示信息",
				autoOpen: false,
				width: 300,
				modal: true
			});
		}
		$_waitMsg.dialog("open");
	};
	//添加外部函数
	window.closeWaitMsg = function(){
		$_waitMsg.dialog("close");
	};
});
