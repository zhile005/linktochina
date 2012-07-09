package com.oa.common;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.oa.common.pojo.Paginable;
import com.oa.common.pojo.PaginableVm;
import com.oa.common.pojo.Result;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.util.ValueStack;

public class JsonBaseAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {

	protected Logger log = Logger.getLogger(getClass());
	protected final String ERROR = "error";
	protected final String SUCCESS = "success";
	protected final String JSON_OBJECT = "jsonObject";
	protected final String JSON_GRID = "jsonGrid";
	protected final String JSON_MAP = "jsonMap";

	protected Result result = new Result(false);
	protected HttpServletResponse response;
	protected HttpServletRequest request;

	protected Object jsonObject;
	protected Paginable jsonGrid;
	protected Map<String, Object> jsonMap = new HashMap<String, Object>();
	protected PaginableVm pageInfo = null;
	// 当前页码
	protected int page;
	// 当前每页记录数
	protected int rows = 20;
	// 排序字段
	protected String sidx;
	// 排序种类(asc/desc)
	protected String sord = "ASC";
	// 开始时间
	protected Date startDate;
	// 结束时间
	protected Date endDate;
	// 隐藏的dom（页面）
	protected String hiddenDomExp;

	/**
	 * 将objectMap中的值写入值栈
	 */
	protected void toMapVm(Map<String, Object> objectMap) {
		ValueStack context = ActionContext.getContext().getValueStack();
		if (null != objectMap) {
			Set<Entry<String, Object>> entrySet = objectMap.entrySet();
			for (Entry<String, Object> object : entrySet) {
				context.set(object.getKey(), object.getValue());
			}
		}
	}
	/**
	 * 将object写入值栈
	 */
	protected void toVm(String key, Object object) {
		ValueStack context = ActionContext.getContext().getValueStack();
		context.set(key, object);
	}

	/**
	 * 将result中的值写入值栈 会写入result变量，同时会把reuslt里面map的内容写入。
	 * 对于消息。如果result返回成功，则写入message，否则写入error
	 * 
	 * @param result
	 *            结果
	 */
	protected void toVm(Result result) {
		ValueStack context = ActionContext.getContext().getValueStack();
		context.set("result", result);
		Set<String> set = result.keySet();
		for (String key : set) {
			context.set(key, result.get(key));
		}
		String resultCode = result.getResultCode();
		if (null == resultCode) {
			String text;
			String[] params = result.getResultCodeParams();
			if (params != null && params.length > 0) {
				text = getText(resultCode, params);
			} else {
				text = getText(resultCode);
			}
			if (result.getSuccess()) {
				addActionMessage(text);
			} else {
				addActionError(text);
			}
		}
	}

	public int getPage() {
		return page;
	}

	public void setPage(String pageStr) {
		if (StringUtils.isNumeric(pageStr.trim())) {
			this.page = Double.valueOf(pageStr).intValue();
		}
		if (this.page == 0) {
			this.page = 1;
		}
	}

	public int getRows() {
		return rows;
	}

	public void setRows(String rowsStr) {
		if (StringUtils.isNumeric(rowsStr)) {
			this.rows = Double.valueOf(rowsStr).intValue();
		} else {
			this.rows = 20;
		}
	}

	public Object getJsonObject() {
		return jsonObject;
	}

	public Paginable getJsonGrid() {
		return jsonGrid;
	}

	public Map<String, Object> getJsonMap() {
		return jsonMap;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getHiddenDomExp() {
		return hiddenDomExp;
	}

	public void setHiddenDomExp(String hiddenDomExp) {
		this.hiddenDomExp = hiddenDomExp;
	}

	public Result getResult() {
		return result;
	}

	public String getSidx() {
		return sidx;
	}

	public void setSidx(String sidx) {
		this.sidx = sidx;
	}

	public String getSord() {
		return "desc".equalsIgnoreCase(sord) ? "DESC" : "ASC";
	}

	public void setSord(String sord) {
		this.sord = sord;
	}

	public PaginableVm getPageInfo() {
		return pageInfo;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
}
