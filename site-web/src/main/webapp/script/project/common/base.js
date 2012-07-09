var baseSetting = {
	funcrootpath : ""
};
$(function() {
	$(".headercenter .headerright .mainmenu ul > li > a").bind("mouseover", function() {
		$(this).next("ul").show("slow");
	}).bind("mouseout", function() {
		$(this).next("ul").hide();
	});
	$(".headercenter .headerright .mainmenu ul > li > ul").bind("mouseover", function() {
		$(this).show();
	}).bind("mouseout", function() {
		$(this).hide();
	});
	// 给String对象添加trim方法
	String.prototype.trim = function() {
		return this.replace(/(^\s*)|(\s*$)/g, "");
	};
	String.prototype.ltrim = function() {
		return this.replace(/(^\s*)/g, "");
	};
	String.prototype.rtrim = function() {
		return this.replace(/(\s*$)/g, "");
	};
	// 自动添加在每个页面，自动去掉text和texeeare的文本的前后空格
	$(document).delegate("input[type='text'],textarea", "keyup",
			function(event) {
				var value = $(this).val();
				if (/(^\s+)|(\s+$)/g.test(value)) {
					$(this).val(value.trim());
				}
			});
	/**
	 * 时间对象的格式化;
	 */
	Date.prototype.format = function(format) {
		/*
		 * eg:format="yyyy-MM-dd hh:mm:ss";
		 */
		var o = {
			"M+" : this.getMonth() + 1, // month
			"d+" : this.getDate(), // day
			"h+" : this.getHours(), // hour
			"m+" : this.getMinutes(), // minute
			"s+" : this.getSeconds(), // second
			"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
			"S" : this.getMilliseconds()
		// millisecond
		};
		if (/(y+)/.test(format)) {
			format = format.replace(RegExp.$1, (this.getFullYear() + "")
					.substr(4 - RegExp.$1.length));
		}
		;
		for ( var k in o) {
			if (new RegExp("(" + k + ")").test(format)) {
				format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
						: ("00" + o[k]).substr(("" + o[k]).length));
			}
		}
		return format;
	};
});
/**
 * 返回一个输入对象的属性值全部为''的对象
 * 
 * @param returnObject
 * @returns {___anonymous9746_9747}
 */
function resetparaObject(returnObject) {
	var resetpara = {};
	$.extend(resetpara, returnObject);
	for ( var index in resetpara) {
		if (resetpara[index] != resetpara) {
			resetpara[index] = "";
		}
	}
	return resetpara;
}
/**
 * 格式化数字
 * 
 * @param num
 * @param exponent
 * @returns
 */
function formatNumber(num, exponent) {
	if (exponent < 1)
		return num;
	var numStr = num + "";
	if (num && null != num) {
		if (numStr.indexOf(".") != -1) {
			if (numStr.split(".")[1].length >= exponent) {
				return numStr;
			} else {
				return formatNumber(numStr + "0", exponent);
			}
		} else {
			return formatNumber(numStr + ".0", exponent);
		}
	} else {
		return "";
	}
}
