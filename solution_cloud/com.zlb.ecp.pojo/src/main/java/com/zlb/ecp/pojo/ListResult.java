package com.zlb.ecp.pojo;

import java.io.Serializable;
import java.util.List;

public class ListResult<T> implements Serializable {
	private static final long serialVersionUID = -5875357261054529668L;
	private String result = "1";
	private int pageindex;
	private int pages;
	private int records;
	private String msg;
	private List<T> rows;

	public String getMsg() {
		return this.msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getResult() {
		return this.result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public int getPageindex() {
		return this.pageindex;
	}

	public void setPageindex(int pageIndex) {
		this.pageindex = pageIndex;
	}

	public int getPages() {
		return this.pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public int getRecords() {
		return this.records;
	}

	public void setRecords(int records) {
		this.records = records;
	}

	public List<T> getRows() {
		return this.rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}
}
