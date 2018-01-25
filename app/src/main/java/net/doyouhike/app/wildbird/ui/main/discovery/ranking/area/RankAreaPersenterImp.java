package net.doyouhike.app.wildbird.ui.main.discovery.ranking.area;

import android.text.TextUtils;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import net.doyouhike.app.wildbird.biz.dao.sharepref.UserInfoSpUtil;
import net.doyouhike.app.wildbird.biz.model.Content;
import net.doyouhike.app.wildbird.biz.model.base.BaseGetRequest;
import net.doyouhike.app.wildbird.biz.model.base.BaseListGetParam;
import net.doyouhike.app.wildbird.biz.model.base.CommonResponse;
import net.doyouhike.app.wildbird.biz.model.request.get.GetAreaRankRequestParam;
import net.doyouhike.app.wildbird.biz.model.request.get.PersonRankRequestParam;
import net.doyouhike.app.wildbird.biz.model.response.PersonRankResponse;
import net.doyouhike.app.wildbird.biz.model.response.RankAreaResponse;
import net.doyouhike.app.wildbird.biz.service.net.ApiReq;
import net.doyouhike.app.wildbird.biz.service.net.RequestUtil;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-8.
 */
public class RankAreaPersenterImp implements IAreaListPresenter {
    AreaListActivity iRankAreaView;

    public RankAreaPersenterImp(AreaListActivity iRankAreaView) {
        this.iRankAreaView=iRankAreaView;
    }

    @Override
    public void getAreaYearRank(String id) {

        BaseGetRequest param=getPersonalParam(id, 0);


        if (null==param){
            return;
        }

        ApiReq.doGet(param, yearPersonListener, errPerson);
    }

    @Override
    public void getAreaTotalRank(String id) {

        BaseGetRequest param=getPersonalParam(id,1);


        if (null==param){
            return;
        }

        ApiReq.doGet(param, totalPersonListener, errPerson);
    }


    @Override
    public void getTotal(BaseListGetParam param) {

        RequestUtil.getInstance().cancelAllRequests(Content.REQ_RANK_PERSONLIST + "1");
        ApiReq.doGet(param, Content.REQ_RANK_PERSONLIST + "1", totalListener, errTotal);
    }

    @Override
    public void getYear(BaseListGetParam param) {
        RequestUtil.getInstance().cancelAllRequests(Content.REQ_RANK_PERSONLIST + "0");
        ApiReq.doGet(param, Content.REQ_RANK_PERSONLIST + "0", yearListener, errYear);
    }

    @Override
    public void onDestory() {
        iRankAreaView = null;
    }

    /**
     * 个人年榜
     */
    @Override
    public void getPersonalYearRank() {

        BaseGetRequest param=getPersonalParam(0);

        if (null==param){
            return;
        }

        ApiReq.doGet(param, yearPersonListener, errPerson);
    }

    /**
     * 个人总榜
     */
    @Override
    public void getPersonalTotalRank() {

        BaseGetRequest param=getPersonalParam(1);

        if (null==param){
            return;
        }

        ApiReq.doGet(param, totalPersonListener, errPerson);
    }



    Response.Listener yearPersonListener = new Response.Listener<CommonResponse<PersonRankResponse>>() {
        @Override
        public void onResponse(CommonResponse<PersonRankResponse> response) {

            if (null != iRankAreaView) {
                iRankAreaView.onGetPersonalYearRank(response.getT().getPerson_rank());
            }
        }
    };
    Response.Listener totalPersonListener = new Response.Listener<CommonResponse<PersonRankResponse>>() {
        @Override
        public void onResponse(CommonResponse<PersonRankResponse> response) {

            if (null != iRankAreaView) {
                iRankAreaView.onGetPersonalTotalRank(response.getT().getPerson_rank());
            }
        }

    };

    Response.ErrorListener errPerson = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

        }
    };


    Response.Listener yearListener = new Response.Listener<CommonResponse<RankAreaResponse>>() {
        @Override
        public void onResponse(CommonResponse<RankAreaResponse> response) {

            if (null != iRankAreaView) {
                int skip = (int) response.getTag();
                iRankAreaView.onGetYearList(response.getT().getArea_list(), skip == 0);
            }
        }

    };

    Response.ErrorListener errYear = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            if (null != iRankAreaView) {
                iRankAreaView.onGetYearErr(error);
            }
        }
    };


    Response.Listener totalListener = new Response.Listener<CommonResponse<RankAreaResponse>>() {
        @Override
        public void onResponse(CommonResponse<RankAreaResponse> response) {

            if (null != iRankAreaView) {

                int skip = (int) response.getTag();

                iRankAreaView.onGetTotalList(response.getT().getArea_list(), skip == 0);
            }
        }

    };


    Response.ErrorListener errTotal = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            if (null != iRankAreaView) {
                iRankAreaView.onGetTotalErr(error);
            }
        }
    };

    private GetAreaRankRequestParam getPersonalParam(String id, int type){

        if(TextUtils.isEmpty(id)){
            return null;
        }

        GetAreaRankRequestParam param=new GetAreaRankRequestParam();
        param.setProvince_id(id);
        param.setType(type);

        return param;

    }

    private PersonRankRequestParam getPersonalParam(int type){

        String userId = UserInfoSpUtil.getInstance().getUserId();

        if(TextUtils.isEmpty(userId)){
            return null;
        }

        PersonRankRequestParam param=new PersonRankRequestParam();
        param.setUser_id(userId);
        param.setType(type);

        return param;

    }
}
