package net.doyouhike.app.wildbird.biz.model.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by zaitu on 15-12-4.
 */
public class PublRecImgEntity {

    private String name;

    private String content;


    @Expose
    private int imageID; // 类型: int, 图片的ID
    @Expose
    private String imageUrl;// 类型: string, 图片的 url 地址

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
