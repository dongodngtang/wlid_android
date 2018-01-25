package net.doyouhike.app.wildbird.biz.model.event;

import net.doyouhike.app.wildbird.biz.model.response.GetRecordDetailResp;

/**
 * Created by zaitu on 15-12-9.
 */
public class EventGetRecordDetail {

    boolean isSuc;
    String strErrMsg;
    GetRecordDetailResp result;

    public boolean isSuc() {
        return isSuc;
    }

    public void setIsSuc(boolean isSuc) {
        this.isSuc = isSuc;
    }

    public String getStrErrMsg() {
        return strErrMsg;
    }

    public void setStrErrMsg(String strErrMsg) {
        this.strErrMsg = strErrMsg;
    }

    public GetRecordDetailResp getResult() {
        return result;
    }

    public void setResult(GetRecordDetailResp result) {
        this.result = result;
    }
}
