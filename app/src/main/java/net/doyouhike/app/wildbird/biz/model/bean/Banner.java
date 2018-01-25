package net.doyouhike.app.wildbird.biz.model.bean;

/**
 * 功能：获取广告列表
 *
 * @author：曾江 日期：16-4-18.
 */
public class Banner {
    private String image_url;  // 类型: string,广告图片地址
    private String http_url;  // 类型: string ,外部url地址，有可能为空

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getHttp_url() {
        return http_url;
    }

    public void setHttp_url(String http_url) {
        this.http_url = http_url;
    }
}
