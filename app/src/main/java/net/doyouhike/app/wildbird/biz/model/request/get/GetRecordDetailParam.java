package net.doyouhike.app.wildbird.biz.model.request.get;

import android.text.TextUtils;

import net.doyouhike.app.wildbird.biz.model.base.BaseGetRequest;

/**
 * Created by zaitu on 15-12-4.
 */
public class GetRecordDetailParam extends BaseGetRequest {

    private String recordID;

    @Override
    protected void setMapValue() {
        if (!TextUtils.isEmpty(recordID)){
            map.put("recordID",recordID);
        }
    }

    public String getRecordID() {
        return recordID;
    }

    public void setRecordID(String recordID) {
        this.recordID = recordID;
    }
}
