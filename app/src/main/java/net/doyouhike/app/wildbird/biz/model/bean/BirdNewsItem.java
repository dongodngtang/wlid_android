package net.doyouhike.app.wildbird.biz.model.bean;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-11.
 */
public class BirdNewsItem {
    /**
     * title: xxx,  // 类型: string, 标题
     * content:  xxx,  // 类型: string, 内容
     * author: xxx,  // 类型: string, 作者
     * created_at: // 类型: int, 发布时间,unix时间戳
     */
    private String title;  // 类型: string, 标题
    private String content;  // 类型: string, 内容
    private String author;  // 类型: string, 作者
    private long created_at; // 类型: int, 发布时间,unix时间戳

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }
}
