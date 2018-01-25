package net.doyouhike.app.wildbird.biz.model.bean;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-15.
 */
public class AddRecordCommentResponse {

    /**
     * content : 好
     * record_id : 905530
     */

    private String content;
    private String record_id;

    public void setContent(String content) {
        this.content = content;
    }

    public void setRecord_id(String record_id) {
        this.record_id = record_id;
    }

    public String getContent() {
        return content;
    }

    public String getRecord_id() {
        return record_id;
    }
}
