package com.oa.common;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.dispatcher.VelocityResult;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;

public class VelocityLayoutResult extends VelocityResult {

	private static final long serialVersionUID = 155255686289017734L;
	@Resource(name="velocityTools")
	private Map<String, Object> velocityTools;

	private void mergeContext(ActionContext context) {
		merge(context, velocityTools);
	}

	private void merge(ActionContext context, Map<String, Object> map) {
		if (map != null) {
			for (Map.Entry<String, Object> stringObjectEntry : map.entrySet()) {
				context.put(stringObjectEntry.getKey(), stringObjectEntry.getValue());
			}
		}
	}

	@Override
	public void doExecute(String finalLocation, ActionInvocation invocation) throws Exception {
		mergeContext(ActionContext.getContext());
		super.doExecute(finalLocation, invocation);
	}
}