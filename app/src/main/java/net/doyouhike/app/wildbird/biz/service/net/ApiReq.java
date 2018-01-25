package net.doyouhike.app.wildbird.biz.service.net;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;

import net.doyouhike.app.wildbird.biz.dao.base.IResponseProcess;
import net.doyouhike.app.wildbird.biz.dao.net.CommonProcess;
import net.doyouhike.app.wildbird.biz.dao.net.EmptyProcess;
import net.doyouhike.app.wildbird.biz.model.Content;
import net.doyouhike.app.wildbird.biz.model.base.BaseGetRequest;
import net.doyouhike.app.wildbird.biz.model.base.BasePostRequest;
import net.doyouhike.app.wildbird.biz.model.bean.AddRecordCommentResponse;
import net.doyouhike.app.wildbird.biz.model.request.get.AreaListRequestParam;
import net.doyouhike.app.wildbird.biz.model.request.get.CheckDataUpdateGetParam;
import net.doyouhike.app.wildbird.biz.model.request.get.CheckVersionReq;
import net.doyouhike.app.wildbird.biz.model.request.get.GetAreaRankRequestParam;
import net.doyouhike.app.wildbird.biz.model.request.get.GetBirdDetailParam;
import net.doyouhike.app.wildbird.biz.model.request.get.GetBirdNewsRequestParam;
import net.doyouhike.app.wildbird.biz.model.request.get.GetBirdRecordRequestParam;
import net.doyouhike.app.wildbird.biz.model.request.get.GetBannerListRequestParam;
import net.doyouhike.app.wildbird.biz.model.request.get.GetCommentsParam;
import net.doyouhike.app.wildbird.biz.model.request.get.GetMyRecordReq;
import net.doyouhike.app.wildbird.biz.model.request.get.GetMyRecordsParam;
import net.doyouhike.app.wildbird.biz.model.request.get.GetPersonalInfoRequestParam;
import net.doyouhike.app.wildbird.biz.model.request.get.GetRecordCommentRequestParam;
import net.doyouhike.app.wildbird.biz.model.request.get.GetRecordDetailParam;
import net.doyouhike.app.wildbird.biz.model.request.get.GetRecordStats;
import net.doyouhike.app.wildbird.biz.model.request.get.GetRegVcodeParam;
import net.doyouhike.app.wildbird.biz.model.request.get.GetUpdateBirdReq;
import net.doyouhike.app.wildbird.biz.model.request.get.GetUserRecStatsParam;
import net.doyouhike.app.wildbird.biz.model.request.get.GetUserRecordReq;
import net.doyouhike.app.wildbird.biz.model.request.get.GetpersonRecordRequestParam;
import net.doyouhike.app.wildbird.biz.model.request.get.LeaderboardRequestParam;
import net.doyouhike.app.wildbird.biz.model.request.get.PersonRankRequestParam;
import net.doyouhike.app.wildbird.biz.model.request.get.ResetPwEmParam;
import net.doyouhike.app.wildbird.biz.model.request.post.AddCommentParam;
import net.doyouhike.app.wildbird.biz.model.request.post.AddRecordCommentPostParam;
import net.doyouhike.app.wildbird.biz.model.request.post.AddRecordLikePostParam;
import net.doyouhike.app.wildbird.biz.model.request.post.DeleteRecordParam;
import net.doyouhike.app.wildbird.biz.model.request.post.ForgetPwdParam;
import net.doyouhike.app.wildbird.biz.model.request.post.GetPwdVcodeParam;
import net.doyouhike.app.wildbird.biz.model.request.post.LikeCommentParam;
import net.doyouhike.app.wildbird.biz.model.request.post.LoginParam;
import net.doyouhike.app.wildbird.biz.model.request.post.ModifyAvatarParam;
import net.doyouhike.app.wildbird.biz.model.request.post.ModifyRecordParam;
import net.doyouhike.app.wildbird.biz.model.request.post.PublishRecordParam;
import net.doyouhike.app.wildbird.biz.model.request.post.RegParam;
import net.doyouhike.app.wildbird.biz.model.response.AddCommentResp;
import net.doyouhike.app.wildbird.biz.model.response.Avatar;
import net.doyouhike.app.wildbird.biz.model.response.BirdDetailsResp;
import net.doyouhike.app.wildbird.biz.model.response.CheckDataUpdSucRepo;
import net.doyouhike.app.wildbird.biz.model.response.CheckVersionResponse;
import net.doyouhike.app.wildbird.biz.model.response.GetAreaRankResponse;
import net.doyouhike.app.wildbird.biz.model.response.GetBirdNewsResponse;
import net.doyouhike.app.wildbird.biz.model.response.GetBirdRecordCommentResponse;
import net.doyouhike.app.wildbird.biz.model.response.GetBirdRecordResponse;
import net.doyouhike.app.wildbird.biz.model.response.GetBrannerListResponse;
import net.doyouhike.app.wildbird.biz.model.response.GetCommentsResp;
import net.doyouhike.app.wildbird.biz.model.response.GetMyRecordResp;
import net.doyouhike.app.wildbird.biz.model.response.GetRecordDetailResp;
import net.doyouhike.app.wildbird.biz.model.response.GetRecordStatsResp;
import net.doyouhike.app.wildbird.biz.model.response.GetUpdateBirdResponse;
import net.doyouhike.app.wildbird.biz.model.response.LeaderboardResponse;
import net.doyouhike.app.wildbird.biz.model.response.LoginResp;
import net.doyouhike.app.wildbird.biz.model.response.ModifyInfoReq;
import net.doyouhike.app.wildbird.biz.model.response.PersonRankResponse;
import net.doyouhike.app.wildbird.biz.model.response.RankAreaResponse;
import net.doyouhike.app.wildbird.biz.model.response.UploadRecordResp;
import net.doyouhike.app.wildbird.biz.model.response.UserRecStats;
import net.doyouhike.app.wildbird.biz.net.GetRequest;
import net.doyouhike.app.wildbird.biz.net.PostRequest;

/**
 * 网络请求接口集合
 * Created by zaitu on 15-11-25.
 */
public class ApiReq extends BaseApiReq {
    /**
     * 取消请求
     *
     * @param tag
     */
    public static void doCancel(
            @NonNull final String tag
    ) {
        RequestUtil.getInstance().cancelAllRequests(tag);
    }

    /**
     * get请求
     *
     * @param request
     * @param listener      成功调用
     * @param errorListener 失败调用
     */
    public static void doGet(
            @NonNull final BaseGetRequest request,
            @NonNull final Response.Listener listener,
            @NonNull final Response.ErrorListener errorListener
    ) {
        doGet(request, null, listener, errorListener);
    }

    /**
     * get请求
     *
     * @param request
     * @param tag
     * @param listener      成功调用
     * @param errorListener 失败调用
     */
    public static void doGet(
            @NonNull final BaseGetRequest request,
            String tag,
            @NonNull final Response.Listener listener,
            @NonNull final Response.ErrorListener errorListener
    ) {

        String subUrl = "";
        String fullUrl = null;

        IResponseProcess process = new EmptyProcess();
        DefaultRetryPolicy policy = null;//超时重连设置
//        process = new CommonProcess<TokenInfo>(TokenInfo.class);
//        process = new ListProcess<Timeline>(new TypeToken<List<Timeline>>() {}.getType());

        if (request instanceof CheckDataUpdateGetParam) {
            //检查数据包更新
            subUrl = Content.REQ_CHECK_DATAUPDATE;
            process = new CommonProcess<CheckDataUpdSucRepo>(CheckDataUpdSucRepo.class);
            //此接口设置6秒超时
            policy = new DefaultRetryPolicy(6000, 0, 0);
        } else if (request instanceof GetBirdDetailParam) {
            //查询鸟种详情
            subUrl = Content.REQ_GET_DETAILS;
            process = new CommonProcess<BirdDetailsResp>(BirdDetailsResp.class);
        } else if (request instanceof GetCommentsParam) {
            //获取更多评论
            subUrl = Content.REQ_getComments;
            process = new CommonProcess<GetCommentsResp>(GetCommentsResp.class);
        } else if (request instanceof GetRegVcodeParam) {
            //获取注册验证码接口
            subUrl = Content.REQ_getRegVcode;
        } else if (request instanceof GetMyRecordsParam) {
            //获取我的记录
            subUrl = Content.REQ_getMyRecords;
            process = new CommonProcess<GetMyRecordResp>(GetMyRecordResp.class);
        } else if (request instanceof GetRecordStats) {
            //野鸟记录统计查询
            subUrl = Content.REQ_getRecordStats;
            process = new CommonProcess<GetRecordStatsResp>(GetRecordStatsResp.class);
            //此接口设置70秒超时
            policy = new DefaultRetryPolicy(70000, 1, 1);
        } else if (request instanceof GetUserRecStatsParam) {
            //个人记录统计接口
            subUrl = Content.REQ_getUserRecStats;
            process = new CommonProcess<UserRecStats>(UserRecStats.class);
        } else if (request instanceof ResetPwEmParam) {
            fullUrl = Content.WEB_MOBILE_URL + Content.REQ_RESET_PW_EMAIL;
            subUrl = Content.REQ_RESET_PW_EMAIL;
        } else if (request instanceof GetRecordDetailParam) {
            subUrl = Content.REQ_getRecordDetail;
            process = new CommonProcess<GetRecordDetailResp>(GetRecordDetailResp.class);
        } else if (request instanceof LeaderboardRequestParam) {
            //排行榜
            subUrl = Content.REQ_RANK_PERSONLIST;
            process = new CommonProcess<LeaderboardResponse>(LeaderboardResponse.class);
        } else if (request instanceof PersonRankRequestParam) {
            //获取自己的观鸟记录排名（年榜和总榜）
            subUrl = Content.REQ_PERSON_RANK;
            process = new CommonProcess<PersonRankResponse>(PersonRankResponse.class);
        } else if (request instanceof AreaListRequestParam) {
            //获取自己的观鸟记录排名（年榜和总榜）
            subUrl = Content.REQ_RNK_AREA_LIST;
            process = new CommonProcess<RankAreaResponse>(RankAreaResponse.class);
        } else if (request instanceof GetBirdNewsRequestParam) {
            //获取鸟种相关新闻信息
            subUrl = Content.REQ_NEWS_GET_LIST;
            process = new CommonProcess<GetBirdNewsResponse>(GetBirdNewsResponse.class);
        } else if (request instanceof GetBirdRecordRequestParam) {
            //获取鸟种记录列表
            subUrl = Content.REQ_RECORD_GET_SPECIES_RECORDS;
            process = new CommonProcess<GetBirdRecordResponse>(GetBirdRecordResponse.class);
        } else if (request instanceof GetRecordCommentRequestParam) {
            //获取鸟种记录评论列表
            subUrl = Content.REQ_GET_COMMENT_LIST;
            process = new CommonProcess<GetBirdRecordCommentResponse>(GetBirdRecordCommentResponse.class);
        } else if (request instanceof GetPersonalInfoRequestParam) {
            //根据用户id获取个人信息
            subUrl = Content.REQ_USER_PERSON_INFO;
            process = new CommonProcess<LoginResp>(LoginResp.class);
        } else if (request instanceof GetpersonRecordRequestParam) {
            //根据用户id获取个人观鸟记录
            subUrl = Content.REQ_RECORD_PERSON_RECORD;
            process = new CommonProcess<GetMyRecordResp>(GetMyRecordResp.class);
        } else if (request instanceof GetAreaRankRequestParam) {
            //根据省份id获取排名（年榜和总榜）
            subUrl = Content.REQ_RNK_AREA_RANK;
            process = new CommonProcess<GetAreaRankResponse>(GetAreaRankResponse.class);
        } else if (request instanceof GetBannerListRequestParam) {
            //获取广告列表
            subUrl = Content.REQ_BANNER_GETLIST;
            process = new CommonProcess<GetBrannerListResponse>(GetBrannerListResponse.class);
        } else if (request instanceof GetUpdateBirdReq) {
            //检测是否有更新的鸟种
            subUrl = Content.REQ_GET_UPDATE_BIRD;
            process = new CommonProcess<GetUpdateBirdResponse>(GetUpdateBirdResponse.class);
        } else if (request instanceof GetUserRecordReq) {
            //获取他人观鸟记录（新版结构）
            subUrl = Content.REQ_RECORD_GET_USER_SPECIES_RECORDS;
            process = new CommonProcess<GetBirdRecordResponse>(GetBirdRecordResponse.class);
        } else if (request instanceof GetMyRecordReq) {
            //获取自己的观鸟记录（新版结构）
            subUrl = Content.REQ_RECORD_GET_MY_SPECIES_RECORDS;
            process = new CommonProcess<GetBirdRecordResponse>(GetBirdRecordResponse.class);
        } else if (request instanceof CheckVersionReq) {
            //版本检测
            subUrl = Content.REQ_VERSION_CHECK;
            process = new CommonProcess<CheckVersionResponse>(CheckVersionResponse.class);
        }

        String url = Content.WB_URL + subUrl;

        if (!TextUtils.isEmpty(fullUrl)) {
            url = fullUrl;
        }

        GetRequest getRequest = baseGet(url, request, process, listener, errorListener, policy);


        if (null == tag) {
            tag = subUrl;
        }

        RequestUtil.getInstance().addRequest(getRequest, tag);
    }


    /**
     * post请求
     *
     * @param request
     * @param tag
     * @param listener      成功调用
     * @param errorListener 失败调用
     */
    public static void doPost(
            @NonNull final BasePostRequest request,
            @NonNull final String tag,
            @NonNull final Response.Listener listener,
            @NonNull final Response.ErrorListener errorListener
    ) {


        String url = Content.WB_URL;
        IResponseProcess process = new EmptyProcess();
        DefaultRetryPolicy policy = null;//超时重连设置

        if (request instanceof RegParam) {
            //注册接口
            url += Content.REQ_reg;
            policy = new DefaultRetryPolicy(60000, 0, 0);
            process = new CommonProcess<LoginResp>(LoginResp.class);
        } else if (request instanceof LoginParam) {
            //登录接口
            url += Content.REQ_login;
            process = new CommonProcess<LoginResp>(LoginResp.class);
            //设置60秒超时
            policy = new DefaultRetryPolicy(60000, 1, 1);
        } else if (request instanceof GetPwdVcodeParam) {
            //获取忘记密码验证码接口
            url += Content.REQ_getPwdVcode;
        } else if (request instanceof ForgetPwdParam) {
            //忘记密码
            url += Content.REQ_forgetPwd;
        } else if (request instanceof LikeCommentParam) {
            //对评论点赞接口
            url += Content.REQ_likeComment;
        } else if (request instanceof AddCommentParam) {
            //对鸟种增加评论
            url += Content.REQ_addComment;
            process = new CommonProcess<AddCommentResp>(AddCommentResp.class);
        } else if (request instanceof DeleteRecordParam) {
            //删除观鸟记录
            url += Content.REQ_deleteRecord;

        } else if (request instanceof ModifyAvatarParam) {
            //更改头像接口
            url += Content.REQ_modify_avatar;
            process = new CommonProcess<Avatar>(Avatar.class);
        } else if (request instanceof AddRecordCommentPostParam) {
            //评论观鸟记录
            url += Content.REQ_RECORD_ADD_COMMENT;
            policy = new DefaultRetryPolicy(60000, 0, 0);
            process = new CommonProcess<AddRecordCommentResponse>(AddRecordCommentResponse.class);
        } else if (request instanceof AddRecordLikePostParam) {
            //点赞观鸟记录接口
            policy = new DefaultRetryPolicy(60000, 0, 0);
            url += Content.REQ_RECORD_ADD_ZAN;
        }else if (request instanceof ModifyInfoReq) {
            //点赞观鸟记录接口
            policy = new DefaultRetryPolicy(60000, 0, 0);
            url += Content.REQ_RECORD_MODIFY_INFO;
        }


        PostRequest postRequest = basePost(url, request, process, listener, errorListener, policy);
        RequestUtil.getInstance().addRequest(postRequest, tag);
    }

    /**
     * @param tag
     * @param request
     * @param listener
     * @param errorListener
     */
    public static void uploadRecord(
            @NonNull final BasePostRequest request,
            @NonNull final String tag,
            @NonNull final Response.Listener listener,
            @NonNull final Response.ErrorListener errorListener
    ) {
        String url = Content.WB_URL;
        IResponseProcess process = new EmptyProcess();

        if (request instanceof PublishRecordParam) {
            //增加观鸟记录
            url += Content.REQ_publishBirdRecord;
            process = new CommonProcess<UploadRecordResp>(UploadRecordResp.class);
        } else if (request instanceof ModifyRecordParam) {
            //更改观鸟记录
            url += Content.REQ_modifyBirdRecord;
            process = new CommonProcess<UploadRecordResp>(UploadRecordResp.class);
        }
        PostRequest postRequest = uploadRecordRequest(url, request, process, listener, errorListener);
        RequestUtil.getInstance().addRequest(postRequest, tag);


    }
}
