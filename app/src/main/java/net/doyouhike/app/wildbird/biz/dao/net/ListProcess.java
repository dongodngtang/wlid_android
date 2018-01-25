package net.doyouhike.app.wildbird.biz.dao.net;


import net.doyouhike.app.wildbird.biz.dao.base.BaseResponseProcess;
import net.doyouhike.app.wildbird.biz.model.base.CommonListResponse;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by zaitu on 15-11-26.
 */
public class ListProcess<S> extends BaseResponseProcess<CommonListResponse<S>> {
    private Type type;
    public ListProcess(Type type) {
        super(new CommonListResponse<S>());
        this.type=type;
    }

    @Override
    protected void parserDataJson(String strJson)  {


        List<S> ps = gson.fromJson(strJson, type);

        if (ps.size() == 20) {
            t.setIsSuccess(true);
        }
        t.setItems(ps);
    }

    @Override
    protected void doNext(JSONObject jsonObject)  {

    }


}
