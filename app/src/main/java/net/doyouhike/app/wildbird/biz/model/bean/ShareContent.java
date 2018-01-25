package net.doyouhike.app.wildbird.biz.model.bean;

import net.doyouhike.app.wildbird.R;

/**
 * 功能：分享功能对象
 *
 * @author：曾江 日期：16-3-7.
 */
public class ShareContent {

    private String title;//分享的标题
    private String imgUrl;//分享的图片的网址
    private String content;//分享的文本内容
    private String url;//用户点击分享后跳转的地址
    private boolean haveContent=false; //是否已经获取到了分享内容
    private int defaultImg= R.drawable.ic_launcher;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isHaveContent() {
        return haveContent;
    }

    public void  setHaveContent(boolean haveContent) {
        this.haveContent = haveContent;
    }

    public int getDefaultImg() {
        return defaultImg;
    }

    public void setDefaultImg(int defaultImg) {
        this.defaultImg = defaultImg;
    }

    @Override
    public String toString() {
        return "ShareContent{" +
                "title='" + title + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                ", haveContent=" + haveContent +
                ", defaultImg=" + defaultImg +
                '}';
    }
}
