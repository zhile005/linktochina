package com.oa.common.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * Date: 2010-9-10
 * Time: 11:03:31
 * 去除页面缓存
 */
public class CacheInterceptor extends AbstractInterceptor {
    private final static Log log = LogFactory.getLog(CacheInterceptor.class);

    public String intercept(ActionInvocation actionInvocation) throws Exception {
        try {
            HttpServletResponse response = ServletActionContext.getResponse();
            response.setHeader("Cache-Control","no-cache");
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragrma", "no-cache");
            response.setDateHeader("Expires", 0);
        } catch (Exception e) {
              log.debug("clear cache exception!",e);
        }

        return actionInvocation.invoke();
    }
}
