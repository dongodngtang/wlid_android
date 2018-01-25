package net.doyouhike.app.wildbird.ui.main.birdinfo.record.detail;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import net.doyouhike.app.library.ui.uistate.UiState;
import net.doyouhike.app.wildbird.biz.dao.sharepref.UserInfoSpUtil;
import net.doyouhike.app.wildbird.biz.model.event.EventGetRecordDetail;
import net.doyouhike.app.wildbird.biz.model.Content;
import net.doyouhike.app.wildbird.biz.model.base.BaseResponse;
import net.doyouhike.app.wildbird.biz.model.base.CommonResponse;
import net.doyouhike.app.wildbird.biz.model.bean.AddRecordCommentResponse;
import net.doyouhike.app.wildbird.biz.model.bean.BirdRecordDetailCommentItem;
import net.doyouhike.app.wildbird.biz.model.request.post.AddRecordCommentPostParam;
import net.doyouhike.app.wildbird.biz.model.request.post.AddRecordLikePostParam;
import net.doyouhike.app.wildbird.biz.service.net.ApiReq;
import net.doyouhike.app.wildbird.biz.service.net.GetRecordDetailService;

import de.greenrobot.event.EventBus;

/**
 * 功能：观鸟记录详情
 *
 * @author：曾江 日期：16-4-13.
 */
public class RecordDetailPresenterImp implements IRecordDetailPresenter {

    /**
     * 观鸟记录activity
     */
    BirdRecordDetailActivity activity;

    public RecordDetailPresenterImp(BirdRecordDetailActivity activity) {
        this.activity = activity;
        EventBus.getDefault().register(this);
    }

    /**
     * 获取观鸟记录详情
     * @param recordId 观鸟记录id
     */
    @Override
    public void getRecordDetail(String recordId) {
        GetRecordDetailService.getInstance().getRecordDetail(recordId);
    }

    /**
     * 发送评论
     * @param recordId   观鸟记录id
     * @param strComment 评论内容
     */
    @Override
    public void sendComment(String recordId,String strComment) {
        //封装发送参数
        AddRecordCommentPostParam param=new AddRecordCommentPostParam();
        param.setRecord_id(recordId);
        param.setContent(strComment);
        param.setTag(strComment);

        if(activity==null){
            return;
        }
        //弹出对话框
        activity.updateView(UiState.LOADING_DIALOG);
        //发送数据
        ApiReq.doPost(param, Content.REQ_RECORD_ADD_COMMENT,addCommentSuc,addCommentErr);


    }

    /**
     * 点赞操作
     * @param recordId 观鸟记录id
     */
    @Override
    public void doLike(String recordId) {

        AddRecordLikePostParam param=new AddRecordLikePostParam();
        param.setRecord_id(recordId);


        if(activity==null){
            return;
        }
        //更新点赞状态,显示为已经点赞
        activity.updateLikeState(true);
        //弹出加载框
        activity.updateView(UiState.LOADING_DIALOG);
        //取消上次的发送
        ApiReq.doCancel( Content.REQ_RECORD_ADD_ZAN);
        //发送点赞
        ApiReq.doPost(param, Content.REQ_RECORD_ADD_ZAN, addZanSuc, addZanErr);

    }

    @Override
    public void onDestroy() {
        //取消获取野鸟详情
        GetRecordDetailService.getInstance().cancel();
        EventBus.getDefault().unregister(this);
        activity=null;
    }

    /**
     * 获取观鸟记录返回
     * @param result 观鸟记录详情
     */
    public void onEventMainThread(EventGetRecordDetail result) {


        if(activity==null){
            return;
        }


        if (result.isSuc()) {

            activity.updateView(UiState.NORMAL);
            //绑定数据
            activity.setRecordDetail(result.getResult());

        } else {
//            activity.toast(result.getStrErrMsg());
        }
    }

    /**
     * 评论失败
     */
    Response.ErrorListener addCommentErr=new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

            if(activity!=null){
                activity.toast("评论失败");
                activity.updateView(UiState.NORMAL);
            }
        }
    };
    /**
     * 评论成功回调
     */
    Response.Listener<CommonResponse<AddRecordCommentResponse>> addCommentSuc=new Response.Listener<CommonResponse<AddRecordCommentResponse>>() {
        @Override
        public void onResponse(CommonResponse<AddRecordCommentResponse> response) {
            if(activity!=null){
                activity.toast("评论成功");
                activity.updateView(UiState.NORMAL);

                String strContent=(String)response.getTag();

                BirdRecordDetailCommentItem item=new BirdRecordDetailCommentItem();
                item.setContent(strContent);
                item.setCreated(System.currentTimeMillis());
                item.setAvatar(UserInfoSpUtil.getInstance().getAvatarUrl());
                item.setUser_name(UserInfoSpUtil.getInstance().getUserNm());
                item.setUser_id(UserInfoSpUtil.getInstance().getUserId());

                activity.onAddCommentSuc(item);
            }
        }
    };

    /**
     * 点赞失败
     */
    Response.ErrorListener addZanErr=new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            if(activity!=null){
                activity.toast("点赞失败");
                activity.updateLikeState(false);
                activity.updateView(UiState.NORMAL);
            }
        }
    };
    /**
     * 点赞成功
     */
    Response.Listener<BaseResponse> addZanSuc=new Response.Listener<BaseResponse> () {
        @Override
        public void onResponse(BaseResponse response) {
            if(activity!=null){
//                activity.toast("点赞成功");
                activity.updateView(UiState.NORMAL);
                activity.onAddZanSuc();
            }
        }
    };
}
