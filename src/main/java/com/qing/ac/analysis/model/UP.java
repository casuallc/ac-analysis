package com.qing.ac.analysis.model;

public class UP {

	private String uid;
	private String nick;
	private String registTime;
	private String url;
	private String address;

	private long posts;
	private long followers;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getRegistTime() {
		return registTime;
	}

	public void setRegistTime(String registTime) {
		this.registTime = registTime;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public long getPosts() {
		return posts;
	}

	public void setPosts(long posts) {
		this.posts = posts;
	}

	public long getFollowers() {
		return followers;
	}

	public void setFollowers(long followers) {
		this.followers = followers;
	}

}
