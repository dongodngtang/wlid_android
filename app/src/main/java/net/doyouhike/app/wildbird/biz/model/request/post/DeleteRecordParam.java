package net.doyouhike.app.wildbird.biz.model.request.post;

import com.google.gson.annotations.Expose;

import net.doyouhike.app.wildbird.biz.model.base.BasePostRequest;

/**
 * Created by zaitu on 15-12-4.
 */
public class DeleteRecordParam extends BasePostRequest {
    @Expose
    private int recordID;

    public int getRecordID() {
        return recordID;
    }

    public void setRecordID(int recordID) {
        this.recordID = recordID;
    }
}
