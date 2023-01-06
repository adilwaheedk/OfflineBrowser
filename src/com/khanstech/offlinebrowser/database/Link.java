package com.khanstech.offlinebrowser.database;

public class Link {
	private String title;
	private String url;

	public Link() {
	}

	public Link(String title, String url) {
		this.title = title;
		this.url = url;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
}
