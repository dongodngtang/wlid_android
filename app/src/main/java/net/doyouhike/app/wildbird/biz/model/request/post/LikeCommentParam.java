package net.doyouhike.app.wildbird.biz.model.request.post;

import com.google.gson.annotations.Expose;

import net.doyouhike.app.wildbird.biz.model.base.BasePostRequest;

/**
 * Created by zaitu on 15-12-4.
 */
public class LikeCommentParam extends BasePostRequest {

    @Expose
    private Long commentID;

    public Long getCommentID() {
        return commentID;
    }

    public void setCommentID(Long commentID) {
        this.commentID = commentID;
    }
}
