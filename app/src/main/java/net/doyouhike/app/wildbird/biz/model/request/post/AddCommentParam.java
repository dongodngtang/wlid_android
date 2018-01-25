package net.doyouhike.app.wildbird.biz.model.request.post;

import com.google.gson.annotations.Expose;

import net.doyouhike.app.wildbird.biz.model.base.BasePostRequest;

/**
 * Created by zaitu on 15-12-4.
 */
public class AddCommentParam extends BasePostRequest {

    /**
     * speciesID : 1
     * content : 评论内容
     */

    @Expose
    private int speciesID;
    @Expose
    private String content;

    public void setSpeciesID(int speciesID) {
        this.speciesID = speciesID;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSpeciesID() {
        return speciesID;
    }

    public String getContent() {
        return content;
    }
}
