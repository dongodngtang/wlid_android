package net.doyouhike.app.wildbird.ui.main.user.other;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import net.doyouhike.app.wildbird.biz.model.base.CommonResponse;
import net.doyouhike.app.wildbird.biz.model.bean.RecStatsEntity;
import net.doyouhike.app.wildbird.biz.model.bean.UserInfo;
import net.doyouhike.app.wildbird.biz.model.request.get.GetPersonalInfoRequestParam;
import net.doyouhike.app.wildbird.biz.model.response.LoginResp;
import net.doyouhike.app.wildbird.biz.service.net.ApiReq;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-14.
 */
public class OtherPresenterImp implements IOtherPresenter {

    OtherFragment iView;

    public OtherPresenterImp(OtherFragment iView) {
        this.iView = iView;
    }

    /**
     * 获取用户信息
     * @param userId 用户id
     */
    @Override
    public void getUserInfo(String userId) {
        GetPersonalInfoRequestParam param = new GetPersonalInfoRequestParam();
        param.setUser_id(userId);
        ApiReq.doGet(param,getUserInfoResp,getUserInfoErr);
    }


    @Override
    public void onDestroy() {
        iView=null;
    }


    private Response.ErrorListener getUserInfoErr = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {


        }
    };

    //      获取个人观鸟记录统计成功回调
    private Response.Listener<CommonResponse<LoginResp>> getUserInfoResp =
            new Response.Listener<CommonResponse<LoginResp>>() {

                @Override
                public void onResponse(CommonResponse<LoginResp> response) {
                    if (null != iView){
                        UserInfo userInfo=response.getT().getUserInfo();
                        iView.updateUserInfo(userInfo);

                        RecStatsEntity recStatsEntity=new RecStatsEntity();

                        recStatsEntity.setRecordNum(Integer.valueOf(userInfo.getRecordNum()));
                        recStatsEntity.setSpeciesNum(Integer.valueOf(userInfo.getSpeciesNum()));
                        recStatsEntity.setThisYearRecordNum(Integer.valueOf(userInfo.getThisYearRecordNum()));
                        recStatsEntity.setThisYearSpeciesNum(Integer.valueOf(userInfo.getThisYearSpeciesNum()));

                        iView.updateRecordCount(recStatsEntity);
                    }
                }
            };

}
