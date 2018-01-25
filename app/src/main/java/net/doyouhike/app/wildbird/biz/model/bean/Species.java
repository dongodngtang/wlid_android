package net.doyouhike.app.wildbird.biz.model.bean;

import java.io.Serializable;

public class Species implements Serializable{

	private static final long serialVersionUID = 1L;

	private int speciesID = 0;
	private String speciesName = "";
	private int starNum = 0;
	private String imageUrl = "";
	private int record_count=0;
	
	public int getSpeciesID() {
		return speciesID;
	}
	public void setSpeciesID(int speciesID) {
		this.speciesID = speciesID;
	}
	public String getSpeciesName() {
		return speciesName;
	}
	public void setSpeciesName(String speciesName) {
		this.speciesName = speciesName;
	}
	public int getStarNum() {
		return starNum;
	}
	public void setStarNum(int starNum) {
		this.starNum = starNum;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public int getRecord_count() {
		return record_count;
	}

	public void setRecord_count(int record_count) {
		this.record_count = record_count;
	}
}
