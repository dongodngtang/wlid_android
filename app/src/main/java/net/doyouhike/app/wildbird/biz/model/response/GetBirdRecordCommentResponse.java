package net.doyouhike.app.wildbird.biz.model.response;

import net.doyouhike.app.wildbird.biz.model.bean.BirdRecordDetailCommentItem;

import java.util.List;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-12.
 */
public class GetBirdRecordCommentResponse {
    private List<BirdRecordDetailCommentItem> comment_list;

    public List<BirdRecordDetailCommentItem> getComment_list() {
        return comment_list;
    }

    public void setComment_list(List<BirdRecordDetailCommentItem> comment_list) {
        this.comment_list = comment_list;
    }
}
