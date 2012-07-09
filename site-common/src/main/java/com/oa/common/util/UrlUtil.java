package com.oa.common.util;


public class UrlUtil implements Cloneable {
    /**
     * 上下文路径
     */
    private static String contextPath = "";
    /**
     * 添加
     * @param url
     * @return
     */
    public static String getUrl(String url){
    	return UrlUtil.contextPath + url;
    }
	public void setContextPath(String contextPath) {
		UrlUtil.contextPath = contextPath;
	}
}
