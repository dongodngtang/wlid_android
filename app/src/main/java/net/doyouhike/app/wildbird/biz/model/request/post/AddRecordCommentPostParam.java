package net.doyouhike.app.wildbird.biz.model.request.post;

import com.google.gson.annotations.Expose;

import net.doyouhike.app.wildbird.biz.model.base.BasePostRequest;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-15.
 */
public class AddRecordCommentPostParam extends BasePostRequest {
    @Expose
    private String record_id; // 观鸟记录id
    @Expose
    private String content; // 类型: string, 评论的内容

    public String getRecord_id() {
        return record_id;
    }

    public void setRecord_id(String record_id) {
        this.record_id = record_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
