package net.doyouhike.app.wildbird.biz.model.request.post;

import com.google.gson.annotations.Expose;

import net.doyouhike.app.wildbird.biz.model.base.BasePostRequest;

/**
 * 功能：点赞
 *
 * @author：曾江 日期：16-4-15.
 */
public class AddRecordLikePostParam extends BasePostRequest{
    @Expose
    private String record_id; // 观鸟记录id

    public String getRecord_id() {
        return record_id;
    }

    public void setRecord_id(String record_id) {
        this.record_id = record_id;
    }
}
