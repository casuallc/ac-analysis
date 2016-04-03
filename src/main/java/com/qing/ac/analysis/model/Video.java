package com.qing.ac.analysis.model;

public class Video {

	private String url;
	private String title;
	private String intro;
	private String publishTime;
	private String catalog;
	private long views;
	private long comments;
	private long bananas;
	private long collections;
	private long danmu;
	private long yb;
	private String uid;

	public long getDanmu() {
		return danmu;
	}

	public void setDanmu(long danmu) {
		this.danmu = danmu;
	}

	public long getYb() {
		return yb;
	}

	public void setYb(long yb) {
		this.yb = yb;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	public long getViews() {
		return views;
	}

	public void setViews(long views) {
		this.views = views;
	}

	public long getComments() {
		return comments;
	}

	public void setComments(long comments) {
		this.comments = comments;
	}

	public long getBananas() {
		return bananas;
	}

	public void setBananas(long bananas) {
		this.bananas = bananas;
	}

	public long getCollections() {
		return collections;
	}

	public void setCollections(long collections) {
		this.collections = collections;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

}
