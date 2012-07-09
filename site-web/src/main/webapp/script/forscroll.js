// JavaScript Document
//yeleft
//ywright
$(function() {
	var indexLen = $(".scroll li").length;
	var showingNow = 3;
	var myWidth = $(".scroll li:first").outerWidth(true);// 就是li加上PADDING的宽度
	var myTotal = indexLen - showingNow;
	var myCount = 0;

	$("#scroll-left").click(function() {
		if (myCount < myTotal) {
			myCount++;
			$(".show-con").animate({
				"margin-left" : -myCount * myWidth
			}, 300);
		}
	});
	$("#scroll-right").click(function() {
		if (myCount > 0) {
			myCount--;
			$(".show-con").animate({
				"margin-left" : -myCount * myWidth
			}, 300);
		}
	});
	var scrollLen = $(".tab-2 .myscroll li").length;
	var myShowScroll = 6;
	var myScrollWidth = 148;
	var scrollTotal = scrollLen - myShowScroll;
	var scrollCount = 0;
	var scrollLen = $(".tab-2 .myscroll-2 li").length;
	var myShowScroll = 4;
	var myScrollWidth = 222;
	var scrollTotal = scrollLen - myShowScroll;
	var scrollCount = 0;
	$(".left-btn img").click(function() {
		if (scrollCount < scrollTotal) {
			scrollCount++;
			$(".scroll-box .myscroll").animate({
				"margin-left" : -myScrollWidth * scrollCount
			}, 300);
		}
	});
	$(".right-btn img").click(function() {
		if (scrollCount > 0) {
			scrollCount--;
			$(".scroll-box .myscroll").animate({
				"margin-left" : -myScrollWidth * scrollCount
			}, 300);
		}
	});
});