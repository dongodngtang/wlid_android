package net.doyouhike.app.wildbird.biz.model.bean;

import java.io.Serializable;

public class Image implements Serializable{

	private static final long serialVersionUID = 1L;

	protected String url = "";
	protected String author = "";
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Image(String url, String author) {
		this.url = url;
		this.author = author;
	}

	public Image() {
	}
}
