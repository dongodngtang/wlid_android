package net.doyouhike.app.wildbird.ui.main.user.mine;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import net.doyouhike.app.library.ui.uistate.UiState;
import net.doyouhike.app.wildbird.app.MyApplication;
import net.doyouhike.app.wildbird.biz.dao.sharepref.UserInfoSpUtil;
import net.doyouhike.app.wildbird.biz.model.Content;
import net.doyouhike.app.wildbird.biz.model.base.BaseResponse;
import net.doyouhike.app.wildbird.biz.model.base.CommonResponse;
import net.doyouhike.app.wildbird.biz.model.bean.BirdRecordCityItem;
import net.doyouhike.app.wildbird.biz.model.bean.BirdRecordDetailItem;
import net.doyouhike.app.wildbird.biz.model.bean.BirdRecordTotalItem;
import net.doyouhike.app.wildbird.biz.model.bean.RecordEntity;
import net.doyouhike.app.wildbird.biz.model.request.get.GetUserRecStatsParam;
import net.doyouhike.app.wildbird.biz.model.request.post.DeleteRecordParam;
import net.doyouhike.app.wildbird.biz.model.request.post.ModifyAvatarParam;
import net.doyouhike.app.wildbird.biz.model.response.Avatar;
import net.doyouhike.app.wildbird.biz.model.response.UserRecStats;
import net.doyouhike.app.wildbird.biz.service.net.ApiReq;
import net.doyouhike.app.wildbird.biz.service.net.RequestUtil;
import net.doyouhike.app.wildbird.util.BitmapUtil;
import net.doyouhike.app.wildbird.util.LocalSharePreferences;

import java.util.Collections;
import java.util.List;

/**
 * Created by zaitu on 15-12-4.
 */
public class MeFragPresenterImp implements IMeFragPresenter {

    private MeFragment iView;

    public MeFragPresenterImp(MeFragment iView) {
        this.iView = iView;
    }

    @Override
    public void updRecordCount() {
        RequestUtil.getInstance().cancelAllRequests(Content.REQ_getUserRecStats);
        ApiReq.doGet(new GetUserRecStatsParam(), Content.REQ_getUserRecStats, updRecordCountSuc, updRecordCountErr);
    }


    @Override
    public void deleteRecord(BirdRecordDetailItem item) {
        DeleteRecordParam paraqm = new DeleteRecordParam();
        paraqm.setRecordID(item.getRecord_id());
        paraqm.setTag(item);
        ApiReq.doPost(paraqm, Content.REQ_deleteRecord, delRecSuc, delRecErr);
    }

    @Override
    public void modifyAvatar(String filePath) {
        System.gc();
        ModifyAvatarParam param = new ModifyAvatarParam();
        ModifyAvatarParam.AvatarEntity entity = new ModifyAvatarParam.AvatarEntity();

        entity.setContent(BitmapUtil.bitmapToString(filePath));
        entity.setName(filePath.replace(Content.FILE_PATH_PARENT_AVATAR, ""));

        param.setAvatar(entity);


        ApiReq.doPost(param, Content.REQ_modify_avatar, modeifyAvatarSuc, modeifyAvatarErr);
    }

    @Override
    public void updateDelItem(List<BirdRecordTotalItem> items, BirdRecordDetailItem item) {

        BirdRecordTotalItem delTotalItem = null;

        for (BirdRecordTotalItem totalItem : items) {


            if (null != totalItem.getArea_list()) {

                BirdRecordCityItem delCityItem = null;

                for (BirdRecordCityItem cityItem : totalItem.getArea_list()) {


                    if (null != cityItem.getRecord_list()) {
                        //删除记录细节item
                        if (cityItem.getRecord_list().contains(item)) {
                            cityItem.getRecord_list().remove(item);
                        }
                        //如果城市列表里的记录列表为空,则删除cityItem
                        if (null == cityItem.getRecord_list() || cityItem.getRecord_list().isEmpty()) {
                            delCityItem = cityItem;
                            break;
                        }
                    }
                }

                //删除cityItem
                if (null != delCityItem) {
                    if (totalItem.getArea_list().contains(delCityItem)) {
                        totalItem.getArea_list().remove(delCityItem);
                    }
                }
                //如果日期的记录列表为空,则删除totalItem
                if (null == totalItem.getArea_list() || totalItem.getArea_list().isEmpty()) {
                    delTotalItem = totalItem;
                    break;
                }

            }

        }

        if (null!=delTotalItem){


            if (items.contains(delTotalItem)){
                items.remove(delTotalItem);
            }

        }
    }


    //更换头像失败回调
    private Response.ErrorListener modeifyAvatarErr = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            if (null != iView) iView.toast("更换头像失败，请重试。");
            String localAvatar = UserInfoSpUtil.getInstance().getAvatarUrl();

            if (null != iView) {
                iView.updateAvater(localAvatar);
                iView.updateAvaterFail();
            }
        }
    };

    //      更换头像成功回调
    private Response.Listener<CommonResponse<Avatar>> modeifyAvatarSuc =
            new Response.Listener<CommonResponse<Avatar>>() {

                @Override
                public void onResponse(CommonResponse<Avatar> response) {

                    String strAvatar = response.getT().getAvatar_url();
                    LocalSharePreferences.commit(
                            MyApplication.getInstance().getApplicationContext(),
                            Content.SP_AVATAR,
                            strAvatar);


                    UserInfoSpUtil.getInstance().updateAvatar(strAvatar);

                    if (null != iView) iView.updateAvater(strAvatar);
                }
            };


    //删除观鸟记录失败回调
    private Response.ErrorListener delRecErr = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

            if (null != iView) {
                iView.toast("删除失败");
                iView.updateView(UiState.DISMISS_DIALOG);
            }
        }
    };

    //      删除观鸟记录成功回调
    private Response.Listener<BaseResponse> delRecSuc =
            new Response.Listener<BaseResponse>() {

                @Override
                public void onResponse(BaseResponse response) {
                    if (null != iView) iView.removedMyRec((BirdRecordDetailItem) response.getTag());
                }
            };


    //      获取个人观鸟记录统计失败回调
    private Response.ErrorListener updRecordCountErr = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

//            if (null!=iView)iView.toast("更新统计失败");
            if (null != iView) iView.updateRecordCountErr();

        }
    };

    //      获取个人观鸟记录统计成功回调
    private Response.Listener<CommonResponse<UserRecStats>> updRecordCountSuc =
            new Response.Listener<CommonResponse<UserRecStats>>() {

                @Override
                public void onResponse(CommonResponse<UserRecStats> response) {
                    if (null != iView) iView.updateRecordCount(response.getT().getRecStats());
                }
            };


    private MyRecordList transformToMyRecordList(List<RecordEntity> entities) {

        Collections.sort(entities, Collections.reverseOrder());

        MyRecordList myRecords = new MyRecordList();

        for (RecordEntity entity : entities) {
            myRecords.addItem(entity);
        }

        return myRecords;

    }

    @Override
    public void onDestroy() {
        iView = null;
    }
}
