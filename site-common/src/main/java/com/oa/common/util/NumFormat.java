package com.oa.common.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: yd
 * Date: 2010-10-21
 * Time: 11:28:46
 * To change this template use File | Settings | File Templates.
 */
public class NumFormat {

    public static final String dataPattern = "yyyy-MM-dd";

//    保留两位小数

    public static String round(BigDecimal bd) {
        if (bd == null) {
            return "0";
        }
        String str = bd.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        return str;
    }
    
    public static BigDecimal string2bigDecimal(String str){
    	BigDecimal bd=new BigDecimal(str); 
    	bd=bd.setScale(2, BigDecimal.ROUND_HALF_UP);
    	return bd;
    }

    public static String getToday() {
        return formatDate(new Date(), dataPattern);
    }

    public static String getTomorrow() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        return formatDate(calendar.getTime(), dataPattern);
    }

     public static String addDay(Date date,int count) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, count);
        return formatDate(calendar.getTime(), dataPattern);
    }

    public static String getTomorrow(String date)throws Exception{
        Date d = formatDate(date,NumFormat.dataPattern);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.add(Calendar.DATE, 1);
        return formatDate(calendar.getTime(), dataPattern);
    }

    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static Date formatDate(String date, String pattern)throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.parse(date);
    }

    /**
     * 将日期中的 T 替换为空格
     */
    public static String relaceDate(String date) {
        if (date == null && "".equals(date)) {
            return "";
        }
        return date.replace("T", " ");

    }


}
