package net.doyouhike.app.wildbird.ui.main.fragment;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import net.doyouhike.app.wildbird.biz.model.Content;
import net.doyouhike.app.wildbird.biz.model.base.CommonResponse;
import net.doyouhike.app.wildbird.biz.model.request.get.GetRecordStats;
import net.doyouhike.app.wildbird.biz.model.response.GetRecordStatsResp;
import net.doyouhike.app.wildbird.biz.service.net.ApiReq;
import net.doyouhike.app.wildbird.util.LogUtil;

/**
 * 查询附近观鸟记录
 * Created by ${luochangdong} on 15-12-7.
 */
public class RecordFragPret implements IRecordFragPret {

    private IRecordFragView recordFragView;


    public RecordFragPret(IRecordFragView recordFragView) {
        this.recordFragView = recordFragView;
    }

    public Response.Listener<CommonResponse<GetRecordStatsResp>> recordResp = new Response.Listener<CommonResponse<GetRecordStatsResp>>() {
        @Override
        public void onResponse(CommonResponse<GetRecordStatsResp> response) {
            LogUtil.d(response.getT().toString());

            int skip=(int)response.getTag();
            recordFragView.updateRecord(response.getT(), skip);
        }
    };

    public Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            recordFragView.fail(error);
        }
    };

    @Override
    public void getRecord(GetRecordStats bean) {
        ApiReq.doCancel(Content.REQ_getRecordStats);
        bean.setTag(bean.getSkip());
        ApiReq.doGet(bean, Content.REQ_getRecordStats, recordResp, errorListener);
    }

    @Override
    public void searchRecord(GetRecordStats bean) {

    }
}
