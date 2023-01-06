package com.khanstech.offlinebrowser.database;

public class Webpage {
	private String title;
	private String url;

	public Webpage() {
	}

	public Webpage(String title, String url) {
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
