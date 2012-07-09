package com.oa.common.pojo;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;

public class JsTreeNode<T> {
	
	public class NodeDate{
		private String title;
		private String extTitle;
		private String icon;
		private String language;
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getIcon() {
			return icon;
		}
		public void setIcon(String icon) {
			this.icon = icon;
		}
		public String getLanguage() {
			return language;
		}
		public void setLanguage(String language) {
			this.language = language;
		}
		public String getExtTitle() {
			return extTitle;
		}
		public void setExtTitle(String extTitle) {
			this.extTitle = extTitle;
		}
	}
	
	private T attr;
	private String state;
	private NodeDate data;
	private List<JsTreeNode<T>> children = new ArrayList<JsTreeNode<T>>();
	
	public String toChildrenJsonStr() throws JSONException {
		return JSONUtil.serialize(children);
	}
	
	public NodeDate createNodeDate(){
		return new NodeDate();
	}
	
	public void setAttr(T attr) {
		this.attr = attr;
	}
	public T getAttr() {
		return this.attr;
	}
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public NodeDate getData() {
		return data;
	}

	public void setData(NodeDate data) {
		this.data = data;
	}
	
	public List<JsTreeNode<T>> getChildren() {
		return this.children;
	}
	
	public void addChild(JsTreeNode child) {
		this.children.add(child);
	}
}
