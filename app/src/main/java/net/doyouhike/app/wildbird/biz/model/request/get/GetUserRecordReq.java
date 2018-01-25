package net.doyouhike.app.wildbird.biz.model.request.get;

import android.text.TextUtils;

import net.doyouhike.app.wildbird.biz.model.base.BaseListGetParam;

/**
 * 请求用户观鸟纪录
 * Created by zengjiang on 16/6/8.
 */
public class GetUserRecordReq extends BaseListGetParam {
    @Override
    protected void setMapValue() {
        if (!TextUtils.isEmpty(user_id)){
            map.put("user_id",user_id);
        }
        if (!TextUtils.isEmpty(date)) {
            map.put("date", date);
        }
    }

    private String user_id; // 类型: int, 用户id
    private String date;// 类型: string,格式“2016-3-15” 取指定日之后五天的数据


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
