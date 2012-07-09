/**
 * 身份证号录入验证
 * @param idcard
 * @returns
 */
function checkIdcard(idcard) {
    var Errors = new Array(
            'yes',
            "身份证号码位数不对!",
            "身份证号码出生日期超出范围或含有非法字符!",
            "身份证号码校验错误!",
            "身份证地区非法!"
            );
    var area = {11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外"}

    var idcard,Y,JYM;
    var S,M;
    var idcard_array = new Array();
    idcard_array = idcard.split("");
    //地区检验
    if (area[parseInt(idcard.substr(0, 2))] == null) return Errors[4];
    //身份号码位数及格式检验
    switch (idcard.length) {
        case 15:
            if ((parseInt(idcard.substr(6, 2)) + 1900) % 4 == 0 || ((parseInt(idcard.substr(6, 2)) + 1900) % 100 == 0 && (parseInt(idcard.substr(6, 2)) + 1900) % 4 == 0 )) {
                ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$/;//测试出生日期的合法性
            } else {
                ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$/;//测试出生日期的合法性
            }
            if (ereg.test(idcard)) return Errors[0];
            else return Errors[2];
            break;
        case 18:
            //18位身份号码检测
            //出生日期的合法性检查
            //闰年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))
            //平年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))
            if (parseInt(idcard.substr(6, 4)) % 4 == 0 || (parseInt(idcard.substr(6, 4)) % 100 == 0 && parseInt(idcard.substr(6, 4)) % 4 == 0 )) {
                ereg = /^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/;//闰年出生日期的合法性正则表达式
            } else {
                ereg = /^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/;//平年出生日期的合法性正则表达式
            }
            if (ereg.test(idcard)) {//测试出生日期的合法性
                //计算校验位
                S = (parseInt(idcard_array[0]) + parseInt(idcard_array[10])) * 7
                        + (parseInt(idcard_array[1]) + parseInt(idcard_array[11])) * 9
                        + (parseInt(idcard_array[2]) + parseInt(idcard_array[12])) * 10
                        + (parseInt(idcard_array[3]) + parseInt(idcard_array[13])) * 5
                        + (parseInt(idcard_array[4]) + parseInt(idcard_array[14])) * 8
                        + (parseInt(idcard_array[5]) + parseInt(idcard_array[15])) * 4
                        + (parseInt(idcard_array[6]) + parseInt(idcard_array[16])) * 2
                        + parseInt(idcard_array[7]) * 1
                        + parseInt(idcard_array[8]) * 6
                        + parseInt(idcard_array[9]) * 3;
                Y = S % 11;
                M = "F";
                JYM = "10X98765432";
                M = JYM.substr(Y, 1);//判断校验位
                if (M == idcard_array[17] || "X" == idcard_array[17] || 'x' == idcard_array[17]) return Errors[0]; //检测ID的校验位
                else return Errors[3];
            }
            else return Errors[2];
            break;
        default:
            return Errors[1];
            break;
    }
}
/**  
 * 此函数处理2011-08-04T16:24:55时间的时候，输出不准，原因待查
 * 时间对象的格式化;  
 * format 支持三种格式
 * yyyy-MM-dd
 * yyyy-MM-dd hh:mm:ss
 * yyyy-MM-ddThh:mm:ss
 */ 
function datePase(str) {
	if(typeof str == 'string'){  
	    var results = str.match(/^ *(\d{4})-(\d{1,2})-(\d{1,2}) *$/);   
	    if(results && results.length>3){
	    	return new Date(parseInt(results[1]),parseInt(results[2]) -1,parseInt(results[3]));    
	    }
	    results = str.match(/^ *(\d{4})-(\d{1,2})-(\d{1,2}) +(\d{1,2}):(\d{1,2}):(\d{1,2}) *$/);   
	    if(results && results.length>6){
	    	return new Date(parseInt(results[1]),parseInt(results[2]) -1,parseInt(results[3]),parseInt(results[4]),parseInt(results[5]),parseInt(results[6]));    
	    }
	    results = str.match(/^ *(\d{4})-(\d{1,2})-(\d{1,2})T+(\d{1,2}):(\d{1,2}):(\d{1,2}) *$/);   
	    if(results && results.length>6){
	    	return new Date(parseInt(results[1]),parseInt(results[2])-1,parseInt(results[3]),parseInt(results[4]),parseInt(results[5]),parseInt(results[6]));    
	    }
	  }
	  return new Date();   
};
/**
 * 高亮提示信息
 * @param object
 * @param text
 * @param errorTipId
 */
function updateErrorTip(object,text,errorTipId) {
	$("span[class='ui-state-highlight']").remove();
	if("" != text){
		var errorSpan = "<span class='ui-state-highlight'>"+text+"</span>";
		$('#'+errorTipId).html(errorSpan);
	}
}
/**
 * 校验对象(jQuery对象)的长度是否合法
 * @param object
 * @param oname
 * @param min
 * @param max
 * @param errorTipId
 * @returns {Boolean}
 */
function checkLength( object, oname, min, max,errorTipId) {
	if ( object.val().length > max || object.val().length < min ) {
		object.addClass( "ui-state-error" );
		updateErrorTip(object,oname + "的长度必须在 " + min + " 和 " + max + "之间.",errorTipId);
		return false;
	} else {
		object.removeClass( "ui-state-error" );
		return true;
	}
}
/**
 * 校验对象(jQuery对象)是否为空
 * @param object
 * @param oname
 * @param errorTipId
 * @param custom
 * @returns {Boolean}
 */
function checkIsNull(object, oname,errorTipId,custom) {
	if ( object.val() == '' ) {
		object.addClass( "ui-state-error" );
		if(custom == true){
			updateErrorTip(object,oname,errorTipId);
		}else{
			updateErrorTip(object,oname + "不能为空",errorTipId);
		}
		return false;
	} else {
		object.removeClass( "ui-state-error" );
		return true;
	}
}
/**
 * 校验对象(jQuery对象)判定条件是否正确
 * @param object
 * @param isError
 * @param errorTipText
 * @param errorTipId
 * @returns {Boolean}
 */
function checkIsFalse(object,isError,errorTipText,errorTipId) {
	if (isError == true) {
		if(object){
			object.addClass( "ui-state-error" );
		}
		updateErrorTip(object,errorTipText,errorTipId);
		return false;
	} else {
		if(object){
			object.removeClass( "ui-state-error" );
		}
		updateErrorTip(object,"",errorTipId);
		return true;
	}
}
/**
 * 金额验证(用户单独使用)
 * @param numStr：要检验的字符串数字
 * @param exponent：小数位数
 * @param maxNum：最大值
 * @returns
 */
function checkPrice(price,oname,exponent,maxNum) {
	var array=new Array('y',
			oname+'非法数字',
			oname+'保留'+exponent+'位小数',
			oname+'必须小于等于:'+maxNum+' ',
			oname+'不能为负数或者零');
	
	var valid = true;
	var errorTipText = "";
	numStr = price + "";
	if(isNaN(numStr) || ""==numStr){
		errorTipText = array[1];
		valid = false;
	}else if(parseFloat(numStr)<=0){
    	errorTipText = array[4];
    	valid = false;
	}else if(numStr.indexOf('.')!=-1){
		var splitArray=numStr.split('.');
		if(splitArray[1].length>exponent){
			errorTipText = array[2];
			valid = false;
		}
	}else if(parseFloat(numStr)>parseFloat(maxNum)){
    	errorTipText = array[3];
    	valid = false;
    }
	if(valid == true){
    	errorTipText = array[0];
    }
	return errorTipText;
}
/**
 * 校验金额（页面直接高亮提示）
 * @param object：dom的jquery对象
 * @param oname：对象名
 * @param exponent：小数位数
 * @param maxNum：最大值
 * @param errorTipId：错误提示的div的id
 * @returns {Boolean}
 */
function checkIsPrice(object,oname,exponent,maxNum,errorTipId) {
		
	var price = object.val();
	var errorTipText = checkPrice(price,oname,exponent,maxNum);
    
	if (errorTipText != 'y') {
		object.addClass( "ui-state-error" );
		updateErrorTip(object,errorTipText,errorTipId);
		return false;
	} else {
		object.removeClass( "ui-state-error" );
		return true;
	}
}
/**
 * 整数验证(用户单独使用)
 * @param numStr：要检验的字符串数字
 * @param exponent：小数位数
 * @param maxNum：最大值
 * @returns
 */
function checkInt(num,oname,minNum,maxNum) {
	var array=new Array('y',
			oname+'非法数字',
			oname+'数字必须为整数',
			oname+'必须小于等于:'+maxNum+' ',
			oname+'必须大于等于:'+minNum+' ');
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
}
/**
 * 根据输入的正则验证是否有效
 * @param object
 * @param regexp
 * @param text
 * @param errorTipId
 * @returns {Boolean}
 */
function checkRegexp(object, regexp, text,errorTipId) {
	if(typeof object === "string"){
		if ( !( regexp.test( object ) ) ) {
			updateErrorTip( object,text,errorTipId);
			return false;
		} else {
			return true;
		}
	}
	if ( !( regexp.test( object.val() ) ) ) {
		object.addClass( "ui-state-error" );
		updateErrorTip( object,text,errorTipId);
		return false;
	} else {
		return true;
	}
}
/**
 * 统一验证form的类
 * @param containerId
 * @returns {Boolean}
 */
function validateForm(containerId){
	var formValidatePass = true;
	var container = null;
	if(containerId){
		container = $("#"+containerId);
	}else{
		container = $(document);
	}
	container.find("span[class='ui-state-highlight']").remove();
	var domList = container.find("input[type='text'],textarea,select");
	for(var i = 0; i<domList.length; i++){
		var domObject = $(domList[i]);
		var thisDomPass = true;
		var value = domObject.val();
		var can_null = (domObject.attr("can_null") == 'y') ? true : false;
		var object_type = domObject.attr("object_type");
		var ref_text = domObject.attr("ref_text");
		var ref_text = ref_text ? ref_text : domObject.parent().prev().text();
		//错误提示消息
		var errorSpanText = "";
		
		domObject.removeClass( "ui-state-error" );
		if( '' == value && can_null === false){
			//验证非空
			errorSpanText = "非空";
			thisDomPass = false;
		}else{
			if(object_type){
				if("int" == object_type){
					
				}else if("price" == object_type){
					
				}else if("length" == object_type){
					var minLength = parseInt(domObject.attr("min_length")?domObject.attr("min_length") : "0");
					var maxLength = parseInt(domObject.attr("max_length")?domObject.attr("max_length") : 50);
					
				}else if("regexp" == object_type){
					var regexp = new RegExp(domObject.attr("regexp_formula"));
					regexp.test(value);
				}
			}
		}
		if(thisDomPass === false){
			domObject.addClass("ui-state-error").after("<span class='ui-state-highlight'>"+errorSpanText+"</span>");
		}
		formValidatePass = formValidatePass && thisDomPass;
	}
	return formValidatePass;
}
