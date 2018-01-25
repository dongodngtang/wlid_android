package net.doyouhike.app.wildbird.biz.model.request.get;

import android.text.TextUtils;

import net.doyouhike.app.wildbird.biz.model.base.BaseListGetParam;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-14.
 */
public class GetpersonRecordRequestParam extends BaseListGetParam {

    @Override
    protected void setMapValue() {
        super.setMapValue();

        if (!TextUtils.isEmpty(user_id))
            map.put("user_id", user_id);
    }

    private String user_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
