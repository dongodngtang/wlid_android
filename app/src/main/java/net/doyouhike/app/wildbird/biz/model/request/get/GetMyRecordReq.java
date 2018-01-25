package net.doyouhike.app.wildbird.biz.model.request.get;

import android.text.TextUtils;

import net.doyouhike.app.wildbird.biz.model.base.BaseGetRequest;
import net.doyouhike.app.wildbird.biz.model.base.BaseListGetParam;

/**
 * 请求我的观鸟纪录
 * Created by zengjiang on 16/6/8.
 */
public class GetMyRecordReq extends BaseListGetParam {
    @Override
    protected void setMapValue() {
        if (!TextUtils.isEmpty(date)) {
            map.put("date", date);
        }
    }

    private String date;// 类型: string,格式“2016-3-15” 取指定日之后五天的数据


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
