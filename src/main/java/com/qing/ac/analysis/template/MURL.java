package com.qing.ac.analysis.template;

public class MURL {

	private String url;

	private int status; // -1 未请求，10 解析成功

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
