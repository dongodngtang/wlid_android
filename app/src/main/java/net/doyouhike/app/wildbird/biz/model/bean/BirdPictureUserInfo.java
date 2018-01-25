package net.doyouhike.app.wildbird.biz.model.bean;

import java.io.Serializable;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-16.
 */
public class BirdPictureUserInfo implements Serializable{
    private String avatar;
    private String userName;
    private String location;
    private long time;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
