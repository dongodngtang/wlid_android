package net.doyouhike.app.wildbird.ui.main.discovery.ranking.leaderboar;

import android.text.TextUtils;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import net.doyouhike.app.wildbird.biz.dao.sharepref.UserInfoSpUtil;
import net.doyouhike.app.wildbird.biz.model.Content;
import net.doyouhike.app.wildbird.biz.model.base.BaseGetRequest;
import net.doyouhike.app.wildbird.biz.model.base.BaseListGetParam;
import net.doyouhike.app.wildbird.biz.model.base.CommonResponse;
import net.doyouhike.app.wildbird.biz.model.request.get.PersonRankRequestParam;
import net.doyouhike.app.wildbird.biz.model.response.LeaderboardResponse;
import net.doyouhike.app.wildbird.biz.model.response.PersonRankResponse;
import net.doyouhike.app.wildbird.biz.service.net.ApiReq;
import net.doyouhike.app.wildbird.biz.service.net.RequestUtil;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-8.
 */
public class LeaderboardPresenterImp implements ILeaderboardPresenter {

    ILeaderboardView iLeaderboardView;

    public LeaderboardPresenterImp(ILeaderboardView iLeaderboardView) {
        this.iLeaderboardView = iLeaderboardView;
    }

    @Override
    public void getPersonalYearRank() {

        BaseGetRequest param=getPersonalParam(0);


        if (null==param){
            return;
        }

        ApiReq.doGet(param, yearPersonListener, errPerson);
    }

    @Override
    public void getPersonalTotalRank() {

        BaseGetRequest param=getPersonalParam(1);


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
        iLeaderboardView = null;
    }


    Response.Listener yearPersonListener = new Response.Listener<CommonResponse<PersonRankResponse>>() {
        @Override
        public void onResponse(CommonResponse<PersonRankResponse> response) {

            if (null != iLeaderboardView) {
                iLeaderboardView.onGetPersonalYearRank(response.getT().getPerson_rank());
            }
        }
    };
    Response.Listener totalPersonListener = new Response.Listener<CommonResponse<PersonRankResponse>>() {
        @Override
        public void onResponse(CommonResponse<PersonRankResponse> response) {

            if (null != iLeaderboardView) {
                iLeaderboardView.onGetPersonalTotalRank(response.getT().getPerson_rank());
            }
        }

    };

    Response.ErrorListener errPerson = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

        }
    };


    Response.Listener yearListener = new Response.Listener<CommonResponse<LeaderboardResponse>>() {
        @Override
        public void onResponse(CommonResponse<LeaderboardResponse> response) {

            if (null != iLeaderboardView) {
                int skip = (int) response.getTag();
                iLeaderboardView.onGetYearList(response.getT().getPerson_list(), skip == 0);
            }
        }

    };

    Response.ErrorListener errYear = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            if (null != iLeaderboardView) {
                iLeaderboardView.onGetYearErr(error);
            }
        }
    };


    Response.Listener totalListener = new Response.Listener<CommonResponse<LeaderboardResponse>>() {
        @Override
        public void onResponse(CommonResponse<LeaderboardResponse> response) {

            if (null != iLeaderboardView) {

                int skip = (int) response.getTag();

                iLeaderboardView.onGetTotalList(response.getT().getPerson_list(), skip == 0);
            }
        }

    };


    Response.ErrorListener errTotal = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            if (null != iLeaderboardView) {
                iLeaderboardView.onGetTotalErr(error);
            }
        }
    };

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
