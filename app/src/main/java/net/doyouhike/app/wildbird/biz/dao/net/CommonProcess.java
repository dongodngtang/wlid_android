package net.doyouhike.app.wildbird.biz.dao.net;

import android.support.annotation.NonNull;

import net.doyouhike.app.wildbird.biz.dao.base.BaseResponseProcess;
import net.doyouhike.app.wildbird.biz.model.base.CommonResponse;

import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * Created by zaitu on 15-11-27.
 */
public class CommonProcess<S> extends BaseResponseProcess<CommonResponse<S>> {
    private Type type;

    public CommonProcess(@NonNull Type type) {
        super(new CommonResponse<S>());
        this.type = type;
    }

    @Override
    protected void parserDataJson(String strJson) {

        S s = gson.fromJson(strJson, type);
        t.setT(s);
    }

    @Override
    protected void doNext(JSONObject jsonObject) {

    }
}
