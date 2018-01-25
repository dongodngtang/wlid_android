package net.doyouhike.app.wildbird.ui.main.fragment;

import com.android.volley.VolleyError;

import net.doyouhike.app.wildbird.biz.model.response.GetRecordStatsResp;

/**
 * Created by ${luochangdong} on 15-12-7.
 */
public interface IRecordFragView {

    void updateRecord(GetRecordStatsResp resp,int skip);

    void fail(VolleyError error);
}
