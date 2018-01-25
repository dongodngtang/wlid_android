package net.doyouhike.app.wildbird.biz.model.request.get;

import net.doyouhike.app.wildbird.biz.model.base.BaseGetRequest;

/**
 * 版本检查
 * Created by zengjiang on 16/6/14.
 */
public class CheckVersionReq extends BaseGetRequest {
    @Override
    protected void setMapValue() {
        map.put("app_type","1");//app_type: 0, // 类型: int,0表示IOS  1表示安卓
    }
}
