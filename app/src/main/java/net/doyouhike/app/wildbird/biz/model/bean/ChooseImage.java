package net.doyouhike.app.wildbird.biz.model.bean;

import java.io.Serializable;
import java.util.List;

public class ChooseImage implements Serializable{
 
	private static final long serialVersionUID = 1L;
	private List<RecordImage> list;
	private List<PhotoInfo> photolist;
	
	public List<PhotoInfo> getPhotolist() {
		return photolist;
	}
	public void setPhotolist(List<PhotoInfo> photolist) {
		this.photolist = photolist;
	}
	public List<RecordImage> getList() {
		return list;
	}
	public void setList(List<RecordImage> list) {
		this.list = list;
	}
	
}
