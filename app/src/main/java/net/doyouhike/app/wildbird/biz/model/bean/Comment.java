package net.doyouhike.app.wildbird.biz.model.bean;

import java.io.Serializable;

public class Comment implements Serializable{
	
	private static final long serialVersionUID = 1L;

	protected Long commentID = 0l;
	protected int isLike = 0;
	protected String userID = "";
	protected String userName = "";
	protected String avatar = "";
	protected int likeNum = 0;
	protected String content = "";
	protected String time = "";
	
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public Long getCommentID() {
		return commentID;
	}
	public void setCommentID(Long commentID) {
		this.commentID = commentID;
	}
	public int getIsLike() {
		return isLike;
	}
	public void setIsLike(int isLike) {
		this.isLike = isLike;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public int getLikeNum() {
		return likeNum;
	}
	public void setLikeNum(int likeNum) {
		this.likeNum = likeNum;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public Comment(long commentID, int isLike, String userID, String userName, String avatar, int likeNum, String content, String time) {
		this.commentID = commentID;
		this.isLike = isLike;
		this.userID = userID;
		this.userName = userName;
		this.avatar = avatar;
		this.likeNum = likeNum;
		this.content = content;
		this.time = time;
	}

	public Comment() {
	}
}
