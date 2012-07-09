/**
 * 自定义包装WdatePicker为jquery插件，并修正bug
 * 做成：刘慎宝
 */
;(function($){
	$.fn.jMy97DatePicker = function(config){
		return this.each(function(){
			if(config && config['readOnly'] == true){
				//修正原插件bug
				$(this).attr('readOnly',true);
			}
			$(this).addClass("Wdate").bind('click',function(){
				WdatePicker(config);
			});
		});
	};
})(jQuery);
/**
 * 异步提交时候button的提示信息
 * 做成：刘慎宝
 * 用法：
 * 显示等待消息：$(this).showRuningMsg();
 * 取消等待消息：$(this).removeRuningMsg();
 */
;(function($){
	$.fn.showRuningMsg = function(){
		return this.each(function(){
			var thiz = $(this);
			thiz.hide().after("<span class='redText center' forid='"+thiz.attr("id")+"' style='width:"+thiz.width()+";height:"+thiz.height()+"'>提交中，请稍后</span>");
		});
	};
	$.fn.removeRuningMsg = function(){
		return this.each(function(){
			$(this).show().next("span[forid='"+$(this).attr("id")+"']").remove();
		});
	};
})(jQuery);
/**
 * 自定义jqueryForm校验插件
 * 做成：刘慎宝
 * 注意事项：每个要校验的dom对象必须设置name属性
 * 使用样例
 * $("#formId").valilater();
 * 支持自定义设置
 * $("#formId").valilater({
 * 		alertMsg:true,//支持弹出alert提示方式
 * 		checkPassPro:"checkPass",//以下为自定义属性名
 * 		refTextPro:"refText",
 * 		refTypePro:"refType",
 * 		highlightPro:"warning",
 * 		warningTextPro:"warningText",
 * 		debug:true,//输出debug校验信息(页面底部)
 *      eventTrigger:false,//是否失去焦点立即触发
 *      passNull:false//校验是否略过空值(和eventTrigger:false搭配使用)
 * });
 * formValiter.check();
 * 执行校验所有form表单[text,radio,select,checkbox](包含非空校验：用在提交form表单时校验)
 * formValiter.checkPassNull();
 * 执行校验所有form表单[text,radio,select,checkbox](不包含非空校验：用在提交查询时校验)
 * formValiter.reInitV();
 * 重新初始化(一般用在隐藏dom对象又隐藏变为显示后重新初始化)
 * formValiter.resert();
 * 取消提示消息
 * form表单内元素属性列表
 * 如例：
 * <input type="text" name="txtGongS" value="$!txtGongS" checkPass="y"/>
 * 添加checkPass="y"则逃过校验
 * <input type="text" name="txtGongS" value="$!txtGongS" refText="公司名称"/>
 * 添加refText="公司名称"则提示名称以refText为准，如：公司名称不能为空
 * 若无refText，默认以当前表格td的前一个单元格的内容为名称如：<th>公司</th><td><input</td>：提示:公司不能为空
 * <input type="text" name="txtGongS" value="$!txtGongS" refType="int+"/>
 * 添加refType则校验类型
 * refType种类：int+、int-、int、length(位数)、length[int/string](位数)、double+(整数位,小数位)、、double+(整数位,小数位)、double(整数位,小数位)、maxLength(位数)
 */
;(function($){
	var configdefault = {
		thiz:false,
		checkNames:{},
		checkPass:true,
		checkPassPro:"checkPass",
		refTextPro:"refText",
		refTypePro:"refType",
		highlightPro:"warning",
		warningTextPro:"warningText",
		alertMsg:false,
		debug:false,
		passNull:false,
		eventTrigger:false
	};
	var int1Rex = /^int\+$/;
	var int2Rex = /^int\-$/;
	var intRex = /^int$/;
	var double1Rex = /^double\+\(([0-9]+),([0-9]+)\)$/;
	var double2Rex = /^double\-\(([0-9]+),([0-9]+)\)$/;
	var doubleRex = /^double\(([0-9]+),([0-9]+)\)$/;
	var length1Rex = /^length\(([0-9]+)\)$/;
	var lengthRex = /^length\[([a-z]+)\]\(([0-9]+)\)$/;
	var maxLengthRex = /^maxlength\(([0-9]+)\)$/i;
	
	var showMsg = function(errorMag,jTarget,thisdefault){
		var errorSpanText = "<span class='"+thisdefault.warningTextPro+"'>"+errorMag+"</span>";
		if(thisdefault.alertMsg == true){
			jTarget.focus();
			alert(errorMag);
			return false;
		}
		if(jTarget.parent().find("."+thisdefault.warningTextPro).size() == 0){
			//若已经有提示则不提示
			jTarget.addClass(thisdefault.highlightPro).parent().append(errorSpanText);
		}
		return true;
	};
	var removeMsg = function(thisdefault){
		$("span[class='"+thisdefault.warningTextPro+"']").remove();
		thisdefault.thiz.find("."+thisdefault.highlightPro).removeClass(thisdefault.highlightPro);
	};
	var showNullMsg = function(name,jTarget,thisdefault){
		return showMsg(name+"不能为空",jTarget,thisdefault);
	};
	/**
	 * 整数验证(用户单独使用)
	 * @param numStr：要检验的字符串数字
	 * @param exponent：小数位数
	 * @param maxNum：最大值
	 * @returns
	 */
	var checkInt = function(num,oname,minNum,maxNum){
		var array=new Array('y',
				oname+'非法数字',
				oname+'数字必须为整数',
				oname+'必须小于等于'+maxNum+' ',
				oname+'必须大于等于'+minNum+' ');
		var valid = true;
		var errorTipText = "";
		numStr = num + "";
		if(isNaN(numStr) || ""==numStr){
			errorTipText = array[1];
			valid = false;
		}else if(!/[0-9]+/.test(numStr)){
	    	errorTipText = array[2];
	    	valid = false;
		}else if(parseInt(numStr)<parseInt(minNum)){
	    	errorTipText = array[4];
	    	valid = false;
		}else if(parseInt(numStr)>parseInt(maxNum)){
	    	errorTipText = array[3];
	    	valid = false;
	    }
		if(valid == true){
	    	errorTipText = array[0];
	    }
		return errorTipText;
	};
	/**
	 * 金额验证(用户单独使用)
	 * @param numStr：要检验的字符串数字
	 * @param exponent：小数位数
	 * @param maxNum：最大值
	 * @param onlyPositive：是否仅仅正数
	 * @returns
	 */
	var checkPrice = function(price,oname,exponent,maxNum,onlyPositive){
		var array=new Array('y',
				oname+'非法数字',
				oname+'保留'+exponent+'位小数',
				oname+'必须小于'+maxNum+'',
				oname+'不能为负数或者零');
		var valid = true;
		var errorTipText = "";
		numStr = price + "";
		if(isNaN(numStr) || ""==numStr){
			errorTipText = array[1];
			valid = false;
		}else if(parseFloat(numStr)<=0){
			if(onlyPositive == true){
				errorTipText = array[4];
				valid = false;
			}
		}else if(numStr.indexOf('.') != -1){
			var splitArray=numStr.split('.');
			if(splitArray[1].length>exponent){
				errorTipText = array[2];
				valid = false;
			}
		}
		if(parseFloat(numStr) > parseFloat(maxNum)){
	    	errorTipText = array[3];
	    	valid = false;
	    }
		//alert("parseFloat("+numStr+")>parseFloat("+maxNum+")="+(parseFloat(numStr)>parseFloat(maxNum)));
		if(valid == true){
	    	errorTipText = array[0];
	    }
		return errorTipText;
	};
	/**
	 * 长度验证
	 * @param value：要检验的字符串
	 * @param oname：显示文字
	 * @param type：类型只能int或者string
	 * @param length：长度
	 * @returns
	 */
	var checkLength = function(value,oname,type,length){
		var array=new Array('y',
				oname+'非法数字',
				oname+'长度必须等于'+length+'位',
				oname+'长度必须等于'+length+'位[半角](1全角=2半角)');
		var valid = true;
		var errorTipText = "";
		numStr = value + "";
		length = parseInt(length);
		if(type == 'int'){
			var rex = new RegExp("^[0-9]+$");
			if(rex.test(value) == false){
				errorTipText = array[1];
				valid = false;
			}else if(value.length != length){
				errorTipText = array[2];
				valid = false;
			}
		}else if(type == 'string'){
			 var cArr = value.match(/[^\x00-\xff]/ig);   
			 var strLength = value.length + (cArr == null ? 0 : cArr.length);   
			 if(strLength != length){
				 errorTipText = array[3];
				 valid = false;
			 }
		}else{
			throw new Error("refType='length[int/string](3)'：type must [int] or [string]");
		}
		if(valid == true){
	    	errorTipText = array[0];
	    }
		return errorTipText;
	};
	/**
	 * 最大长度验证
	 * @param value：要检验的字符串
	 * @param oname：显示文字
	 * @param type：类型只能int或者string
	 * @param length：长度
	 * @returns
	 */
	var checkMaxLength = function(value,oname,length){
		var array=new Array('y',
				oname+'长度必须小于'+length+'位[半角](1全角=2半角)');
		var valid = true;
		var errorTipText = "";
		value = value + "";
		length = parseInt(length);
		var cArr = value.match(/[^\x00-\xff]/ig);   
		var strLength = value.length + (cArr == null ? 0 : cArr.length);   
		if(strLength > length){
			errorTipText = array[1];
			valid = false;
		}
		if(valid == true){
	    	errorTipText = array[0];
	    }
		return errorTipText;
	};
	var checkType = function(name,jTarget,thisdefault){
		var type = jTarget.attr(thisdefault.refTypePro);
		if(type){
			type = type.toLowerCase();
			var value = jTarget.val().replace(/,/g,'');
			var msg = 'y';
			var tmp = [];
			if((tmp = int1Rex.exec(type)) != null){//正整数【int+】
				msg = checkInt(value,name,1,2147483647);
			}else if((tmp = int2Rex.exec(type)) != null){//负整数【int-】
				msg = checkInt(value,name,-2147483647,-1);
			}else if((tmp = intRex.exec(type)) != null){//整数【int】
				msg = checkInt(value,name,-2147483647,2147483647);
			}else if((tmp = double1Rex.exec(type)) != null){ //double类型【double+(1,3)】
				msg = checkPrice(value,name,tmp[2],parseInt(tmp[1]) * 10 + 1,true);
			}else if((tmp = double2Rex.exec(type)) != null){ //double类型【double-(1,3)】
				msg = checkPrice(value,name,tmp[2],0,false);
			}else if((tmp = doubleRex.exec(type)) != null){ //double类型【double(1,3)】
				msg = checkPrice(value,name,tmp[2],parseInt(tmp[1]) * 10 + 1,false);
			}else if((tmp = length1Rex.exec(type)) != null){//定长类型【length(3)】
				msg = checkLength(value,name,'int',tmp[1]);
			}else if((tmp = lengthRex.exec(type)) != null){//定长类型【length[int/string](3)】
				msg = checkLength(value,name,tmp[1],tmp[2]);
			}else if((tmp = maxLengthRex.exec(type)) != null){//定长类型【length[int/string](3)】
				msg = checkMaxLength(value,name,tmp[1]);
			}else{
				throw new Error("dom对象name["+jTarget.attr("name")+"]的"+thisdefault.refTypePro+"属性配置错误，请从以下样式选择int+、int-、int、maxLength(位数)、length(位数)、length[int/string](位数)、double+(整数位,小数位)、double-(整数位,小数位)、double(整数位,小数位)");
			}
			if(thisdefault.debug == true){
				$(document.body).append("["+name+"]["+thisdefault.refTypePro+"="+type+"]验证结果:"+msg).append("<br/>");
			}
			if(msg != 'y'){
				thisdefault.checkPass = false;
				return showMsg(msg,jTarget,thisdefault);
			}
		}
	};
	var checkNull = function(name,jTarget,thisdefault){
		var pass = true;
		if(jTarget.is("input[type='radio']")){
			jTarget = jTarget.filter(":checked");
			if(jTarget.size() == 0 || jTarget.val() == ""){
				pass = false;
			}
		}else if(jTarget.is("input[type='checkbox']")){
			jTarget = jTarget.filter(":checked");
			if(jTarget.size() == 0 || jTarget.val() == ""){
				pass = false;
			}
		}else if(jTarget.is("select")){
			if(jTarget.val() == ""){
				pass = false;
			}
		}else if(jTarget.is("input[type='text']")){
			if(jTarget.val() == ""){
				pass = false;
			}
		}
		if(thisdefault.debug == true){
			$(document.body).append("["+name+"][非空]验证结果:"+pass).append("<br/>");
		}
		if(pass == false && thisdefault.passNull == false){
			thisdefault.checkPass = false;
			return showNullMsg(name,jTarget,thisdefault);
		}
	};
	var check = function(thisdefault){
		thisdefault.checkPass = true;
		var checkNames = thisdefault.checkNames;
		var refText = null;
		var type = null;
		var jobject = null;
		var continueCheck = false;//是否继续校验【提示方式采用alert方式，则采取一个个校验的方式】
		for(var name in checkNames){
			if(checkNames[name] != true){
				continue;
			}
			jobject = null;
			var jobjects = $("[name='"+name+"']");
			refText = jobjects.attr(thisdefault.refTextPro);
			//若没有refText属性，则取当前td的前一个单元格的内容
			refText = refText == undefined ? jobjects.parent().prev().text() : refText;
			if(jobjects.is("input[type='text']")){
				if(jobjects.val() == ""){
					continueCheck = checkNull(refText,jobjects,thisdefault);
				}else{
					continueCheck = checkType(refText,jobjects,thisdefault);
				}
			}else {
				continueCheck = checkNull(refText,jobjects,thisdefault);
			}
			if(continueCheck === false){
				break;
			}
		}
		return thisdefault.checkPass;
	};
	var exe = function(thisdefault){
		removeMsg(thisdefault);
		return check(thisdefault);
	};
	var bindEvent = function(event){
		exe(event.data["thisdefault"]);
	};
	var init = function(thisdefault){
		var inputs = thisdefault.thiz.find("input,select");
		for(var i = 0; i < inputs.length; i++){
			var jobject = $(inputs[i]);
			var name = jobject.attr("name");
			var pass = jobject.attr(thisdefault.checkPassPro) == "y" ? true : false;
			var hide = jobject.is(":hidden");
			if(name && pass === false && hide === false){
				thisdefault.checkNames[name] = true;
				if(thisdefault.debug == true){
					$(document.body).append("校验对象name["+name+"]已添加").append("<br/>");
				}
			}else{
				if(thisdefault.debug == true){
					$(document.body).append("校验对象name["+name+"]没有添加，原因:checkPass='true'["+pass+"]/hide["+hide+"]/name["+name+"]").append("<br/>");
				}
			}
		}
		if(thisdefault.eventTrigger == true){
			inputs.unbind("focusout",bindEvent);
			inputs.bind("focusout",{"thisdefault":thisdefault},bindEvent);
		}
	};
	$.fn.valilater = function(config){
		var thisdefault = $.extend({},configdefault,config);
		thisdefault.thiz = $(this);
		//添加可执行方法
		this.resert = function(){ /**取消提示消息*/
			removeMsg(thisdefault);
			return thisdefault.thiz;
		};
		this.reInitV = function(){ /**重新初始化*/
			this.resert();
			init(thisdefault);
			return thisdefault.thiz;
		};
		this.checkPassNull = function(){ /**执行校验(跳过空值)*/
			thisdefault.passNull = true;
			return exe(thisdefault);
		},
		this.check = function(){ /**执行校验*/
			thisdefault.passNull = false;
			return exe(thisdefault);
		};
		return this.each(function(){
			//初始化
			init(thisdefault);
		});
	};
})(jQuery);
/**
 * 自定义jquery下载插件
 * 做成：刘慎宝
 * 如例：
 * //导出Excel
 *$("#btnOutExcel").pagedownload({
 *	onePageNum:$pageInfo.pageSize*10,
 *	totalNum:$pageInfo.totalItem,
 *	dateUrl:"paySheetsToExcel.action"
 *});
 */
;(function($){
	var configdefault = {
			thiz:false,
			showDialog:false,
			downloadiframe:false,
			title:"数据导出",
			autoOpen:false,
			width:300,
			height:400,
			contentId:false,
			onePageNum:2000,
			totalNum:0,
			getToatlNumUrl:"",
			dateUrl:"",
			paraDate:{},
			pagePara:"page",
			pageSizePara:"rows"
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
	var initDate = function(thisdefault){
		var ul = thisdefault.showDialog.children("ul");
		ul.html("");
		var tempArray = new Array();
		var endNum = 0;
		for(var i = 1; i <= thisdefault.totalNum; i = i + thisdefault.onePageNum){
			thisdefault.paraDate[thisdefault.pagePara] = (i+thisdefault.onePageNum-1)/thisdefault.onePageNum;
			thisdefault.paraDate[thisdefault.pageSizePara] = thisdefault.onePageNum;
			if((i + thisdefault.onePageNum) < thisdefault.totalNum){
				endNum = (i + thisdefault.onePageNum-1);
			}else{
				endNum = thisdefault.totalNum;
			}
			tempArray.push("<li refhref='"+thisdefault.dateUrl+"?"+$.param(thisdefault.paraDate)+"'>"+"下载>" + (i) + "-" + endNum + "</li>");
		}
		ul.append(tempArray.join(""));
	};
	var init = function(thisdefault){
		var thiz = thisdefault.thiz;
		var thisCallback = thisdefault.thisCallback;
		if( thisdefault.showDialog === false ){
			var showDialog = thisdefault.showDialog = $('<div class="jd_showDialog"><ul class="pagedownload"></ul></div>');
//			var iframe = thisdefault.downloadiframe = document.createElement("iframe");
//			$(iframe).appendTo(document.body);
			thisdefault.showDialog.appendTo(document.body);
			showDialog.dialog({
				title:thisdefault.title,
				autoOpen: thisdefault.autoOpen,
				width: thisdefault.width,
				height:thisdefault.height,
				modal: true,
				buttons: {
					'导出': function() {
						var sl = thisdefault.showDialog.find("li[class='selected']");
						if(sl.size() > 0){
							window.location.href=thisdefault.showDialog.find("li[class='selected']").attr("refhref");
						}else{
							alert("请先选择打印范围记录数");
						}
					},'关闭': function() {
						$(this).dialog("close");
					}
				},
				close: function() {
				}
			});
			var ul = showDialog.children("ul");
			ul.delegate("li", "mouseover", function(event){
				$(this).addClass("activit");
			});
			ul.delegate("li", "mouseout", function(event){
				$(this).removeClass("activit");
			});
			ul.delegate("li", "click", function(event){
//				thisdefault.downloadiframe.src = $(this).attr("refhref");
//				window.location.href=$(this).attr("refhref");
				thisdefault.showDialog.find("li").removeClass("selected");
				$(this).addClass("selected");
			});
//			$(thisdefault.downloadiframe.window).bind("load",function(){
//				alert("加载完毕");
//			});
		}
		var inputs = null;
		if(false === thisdefault.contentId){
			inputs = $(document).find("input,select");
		}else{
			inputs = $("#"+thisdefault.contentId).find("input,select");
		}
		var jobject = null;
		for(var index = 0; index < inputs.length; index++){
			jobject = $(inputs[index]);
			var name = jobject.attr("name");
			var value = jobject.val();
			if(name && value){
				if(jobject.is("input[type='radio']:checked")){
					thisdefault.paraDate[name] = value;
				}else if(jobject.is("input[type='checkbox']:checked")){
					if(thisdefault.paraDate[name]){
						thisdefault.paraDate[name] = thisdefault.paraDate[name] + "," + value;
					}else{
						thisdefault.paraDate[name] = value;
					}
				}else if(jobject.is("input[type='text'],input[type='hidden'],select")){
					thisdefault.paraDate[name] = value;
				}
				
			}
		}
		if(thisdefault.autoOpen === true){
			//同步显示的时候
			initDate(thisdefault);
		}else{
			thiz.bind('click',function(e){
				initDate(thisdefault);
				showDialog.dialog("open");
				return false;
			});
		}
	};
	$.fn.pagedownload = function(config){
		var thisdefault = $.extend({},configdefault,config);
		var thiz = thisdefault.thiz = $(this);
		return this.each(function(){
			init(thisdefault);
		});
	};
})(jQuery);