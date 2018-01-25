package net.doyouhike.app.wildbird.ui.main.birdinfo.detail;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import net.doyouhike.app.wildbird.biz.model.Content;
import net.doyouhike.app.wildbird.biz.model.base.CommonResponse;
import net.doyouhike.app.wildbird.biz.model.bean.SpeciesInfo;
import net.doyouhike.app.wildbird.biz.model.request.get.GetBirdDetailParam;
import net.doyouhike.app.wildbird.biz.model.response.BirdDetailsResp;
import net.doyouhike.app.wildbird.biz.service.database.WildbirdDbService;
import net.doyouhike.app.wildbird.biz.service.net.ApiReq;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-11.
 */
public class BirdDetailPresenterIml implements IBirdDetailPresenter {

    BirdDetailActivity iBirdDetailView;

    public BirdDetailPresenterIml(BirdDetailActivity iBirdDetailView) {
        this.iBirdDetailView = iBirdDetailView;
    }

    @Override
    public void getDetail(String speciesID) {

        GetBirdDetailParam detailParam = new GetBirdDetailParam();
        detailParam.setSpeciesID(speciesID);

        if (null == iBirdDetailView){
            return;
        }

        iBirdDetailView.setRefreshView(true);

        ApiReq.doCancel( Content.REQ_GET_DETAILS);
        ApiReq.doGet(detailParam, Content.REQ_GET_DETAILS, getBirdDetailSuc, getBirdDetailErr);
    }

    @Override
    public void getOfflineSpecies(String speciesID) {
        SpeciesInfo bird = WildbirdDbService.getInstance().getSpeciesInfo(speciesID);

        if (null != iBirdDetailView)
        iBirdDetailView.updateDetail(bird, false);
    }

    @Override
    public void onDestroy() {
        iBirdDetailView = null;
    }


    //      获取野鸟详情成功回调
    private Response.Listener<CommonResponse<BirdDetailsResp>> getBirdDetailSuc =
            new Response.Listener<CommonResponse<BirdDetailsResp>>() {

                @Override
                public void onResponse(CommonResponse<BirdDetailsResp> response) {

                    SpeciesInfo speciesInfo = response.getT().getSpeciesInfo();
                    WildbirdDbService.getInstance().insertBirdImage(speciesInfo.getSpeciesID(), speciesInfo.getImages());




                    if (null != iBirdDetailView){
                        iBirdDetailView.setRefreshView(false);
                        iBirdDetailView.updateDetail(speciesInfo, true);
                    }
                }
            };


    //获取野鸟详情失败回调
    private Response.ErrorListener getBirdDetailErr = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

            if (null != iBirdDetailView){
                iBirdDetailView.setRefreshView(false);
                iBirdDetailView.toast("获取最新鸟种详情失败");
            }
        }
    };

}
