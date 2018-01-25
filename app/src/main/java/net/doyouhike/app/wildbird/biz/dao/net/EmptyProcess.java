package net.doyouhike.app.wildbird.biz.dao.net;


import net.doyouhike.app.wildbird.biz.dao.base.BaseResponseProcess;
import net.doyouhike.app.wildbird.biz.model.base.BaseResponse;

import org.json.JSONObject;

/**
 * Created by zaitu on 15-11-26.
 */
public class EmptyProcess extends BaseResponseProcess<BaseResponse> {
    public EmptyProcess() {
        super(new BaseResponse());
    }

    @Override
    protected void parserDataJson(String strJson) {

    }

    @Override
    protected void doNext(JSONObject jsonObject)  {

    }
}
