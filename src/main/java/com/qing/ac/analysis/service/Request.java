package com.qing.ac.analysis.service;

public class Request {

	private String url;
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getUrl() {
		url.trim();
		return url;
	}
	
	public Request(String url) {
		setUrl(url);
	}

}
