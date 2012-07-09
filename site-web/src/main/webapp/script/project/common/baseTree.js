/**
 * 自定义树jQuery插件
 * 使用方式
	$("#storeName").examleTree({
		thisCallback:function(returnObject){
			$("#storeId").val(returnObject["supplierId"]);
			$("#storeName").val(returnObject["supplierName"]);
		}
	});
 */
;(function($){
	var configdefault = {
			thiz:false,
			showDialog:false,
			title:"",
			autoOpen:false,
			width: 300,
			height:400,
			url:"",
			returnObject:{},
			thisCallback:false,
			beforPost:function(nodeDate) {
				return {};
			},
			onSelect:function(thisdefaultConfig,selectedDate){
			}
	};
	var resetparaObject = function(returnObject){
		var resetpara = {};
		$.extend(resetpara,returnObject);
		for(var index in resetpara){
			if(resetpara[index] != resetpara){
				resetpara[index] = "";
			}
		}
	    return resetpara;
	};
	var choice = function(thisdefault) {
		thisdefault.thisCallback.apply(thisdefault.returnObject, [thisdefault.returnObject]);
		thisdefault.showDialog.dialog("close");
	};
	var init = function(thisdefault){
		var thiz = thisdefault.thiz;
		var showDialog = thisdefault.showDialog;
		var thisCallback = thisdefault.thisCallback;
		if(!$.isFunction(thisCallback)){
			alert('第一个参数不是全局回调函数（object）,请重新设定开发参数');
			return false;
		}
		if(showDialog==false){
			thisdefault.showDialog = showDialog = $('<div/>').appendTo(document.body);
			showDialog.dialog({
				title:thisdefault.title,
				autoOpen: thisdefault.autoOpen,
				width: thisdefault.width,
				height:thisdefault.height,
				modal: true,
				buttons: {
					"选择": function(){
						choice(thisdefault);
					},
					'关闭': function() {
						$(this).dialog("close");
					}
				},
				close: function() {
				}
			});
			showDialog.jstree({
				"plugins" : ["core", "themes", "json_data","ui"],
				"json_data" : {
					"ajax" : {
						"type" : "post",
						"url" : thisdefault.url,
						"data" : thisdefault.beforPost
					}
				}
			}).bind("select_node.jstree", function(e, data) {
				//单击事件
				var isSelect = thisdefault.onSelect(thisdefault.returnObject,data.rslt.obj,e);
				if(isSelect === false){
					//若返回false，则取消选择
					$.jstree._focused().deselect_node();
					//$.jstree._focused().set_state($.extend(true, {}, false));
				}
	        }).bind("dblclick.jstree", function(e, data) {
	        	//双击事件
	        	choice(thisdefault);
	        });
		}
		thiz.attr("readyonly","true");
		if(thiz.is("button,input[type='button'")){
			thiz.button();
		}
		thiz.bind('click',function(e){
			showDialog.dialog("open");
			return false;
		});
	};
	$.fn.baseTree = function(config){
		var thisdefault = $.extend(true,{},configdefault,config);
		thisdefault.thiz = $(this);
		return this.each(function(){
			init(thisdefault);
		});
	};
})(jQuery);
