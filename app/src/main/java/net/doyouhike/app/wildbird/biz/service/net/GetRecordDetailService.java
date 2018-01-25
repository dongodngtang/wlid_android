package net.doyouhike.app.wildbird.biz.service.net;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import net.doyouhike.app.wildbird.biz.model.event.EventGetRecordDetail;
import net.doyouhike.app.wildbird.biz.model.Content;
import net.doyouhike.app.wildbird.biz.model.base.CommonResponse;
import net.doyouhike.app.wildbird.biz.model.request.get.GetRecordDetailParam;
import net.doyouhike.app.wildbird.biz.model.response.GetRecordDetailResp;

import de.greenrobot.event.EventBus;

/**
 * Created by zaitu on 15-12-9.
 */
public class GetRecordDetailService {


    public static GetRecordDetailService instance = new GetRecordDetailService();

    public static GetRecordDetailService getInstance() {
        return instance;
    }

    public void getRecordDetail(int recordId) {
        getRecordDetail(recordId+"");
    }
    public void getRecordDetail(String recordId) {

        GetRecordDetailParam param = new GetRecordDetailParam();
        param.setRecordID(recordId);
        param.setTag(recordId);

        ApiReq.doGet(param, Content.REQ_getRecordDetail, getRecordDetailSuc, getRecordDetailErr);
    }

    public void cancel() {
        RequestUtil.getInstance().cancelAllRequests(Content.REQ_getRecordDetail);
    }


    //      获取个人观鸟记录详情失败回调
    private Response.ErrorListener getRecordDetailErr = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

            String errMsg = error.getMessage();

            if (errMsg == null || errMsg.isEmpty() || errMsg.contains("网络不佳")) {
                errMsg = "获取记录失败。";
            }

            EventGetRecordDetail event = new EventGetRecordDetail();
            event.setIsSuc(false);
            event.setStrErrMsg(errMsg);

            EventBus.getDefault().post(event);
        }
    };

    //      获取个人观鸟记录详情成功回调
    private Response.Listener<CommonResponse<GetRecordDetailResp>> getRecordDetailSuc =
            new Response.Listener<CommonResponse<GetRecordDetailResp>>() {

                @Override
                public void onResponse(CommonResponse<GetRecordDetailResp> response) {

                    EventGetRecordDetail event = new EventGetRecordDetail();
                    event.setIsSuc(true);
                    event.setResult(response.getT());

                    EventBus.getDefault().post(event);
                }
            };
}
