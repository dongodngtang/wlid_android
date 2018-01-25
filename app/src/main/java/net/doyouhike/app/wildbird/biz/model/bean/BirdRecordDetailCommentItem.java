package net.doyouhike.app.wildbird.biz.model.bean;

/**
 * 功能：评论列表的item
 *
 * @author：曾江 日期：16-4-12.
 */
public class BirdRecordDetailCommentItem {

    private String user_id;  // 类型: int, 用户id
    private String user_name; // 类型: string, 用户名
    private String avatar;  // 类型: int, 用户头像
    private String content; // 类型: string, 评论内容
    private long created;  // 类型: int, 评论时间,unix时间戳

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }
}
