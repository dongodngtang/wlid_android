package net.doyouhike.app.wildbird.biz.dao.sharepref;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;

import net.doyouhike.app.wildbird.app.MyApplication;
import net.doyouhike.app.wildbird.biz.model.Content;
import net.doyouhike.app.wildbird.biz.model.bean.UserInfo;
import net.doyouhike.app.wildbird.biz.model.response.LoginResp;
import net.doyouhike.app.wildbird.biz.model.response.ModifyInfoReq;
import net.doyouhike.app.wildbird.util.LocalSharePreferences;

/**
 * Created by zaitu on 15-12-7.
 */
public class UserInfoSpUtil {

    /**
     * 保存用户登陆信息
     *
     * @param context
     * @param resp
     */
    private static UserInfoSpUtil instance;

    public synchronized static UserInfoSpUtil getInstance() {
        if (null == instance) {
            initInatance();
        }

        return instance;
    }

    private synchronized static void initInatance() {

        if (null == instance) {
            instance = new UserInfoSpUtil();
        }

    }


    public boolean isLogin() {

        if (null != getUserInfo()) {
            return true;
        }

        return false;
    }

    public void saveUserInfo(LoginResp resp) {
        String strLoginResp = "";
        if (null != resp) {
            strLoginResp = new Gson().toJson(resp);
        }

        saveData(resp);

        LocalSharePreferences.commit(MyApplication.getInstance().getApplicationContext(),
                LocalSharePreferences.KEY_USER_INFO, strLoginResp);
    }


    public void updateUserInfo(ModifyInfoReq modifyInfo) {
        if (null == getUserInfo()) {
            return;
        }


        if (null == getLoginResp()) {
            return;
        }

        if (null == modifyInfo) {
            return;
        }


        UserInfo info = getUserInfo();
        info.setCity_id(modifyInfo.getCity_id());
        info.setInsitution(modifyInfo.getInsitution());
        info.setCamera(modifyInfo.getCamera());
        info.setTelescope(modifyInfo.getTelescope());

        LoginResp resp = getLoginResp();
        resp.setUserInfo(info);

        saveUserInfo(resp);
    }

    public UserInfo getUserInfo() {

        if (null != getLoginResp()) {
            return getLoginResp().getUserInfo();
        }

        return null;
    }

    public String getUserId() {

        if (null != getUserInfo()) {
            return getUserInfo().getUserId();
        }


        return "";
    }

    public String getToken() {

        if (null != getLoginResp()) {
            return getLoginResp().getToken();
        }

        return "";
    }


    private LoginResp getLoginResp() {

        String strLoginResp = LocalSharePreferences.getValue(MyApplication.getInstance().getApplicationContext(),
                LocalSharePreferences.KEY_USER_INFO);

        if (!TextUtils.isEmpty(strLoginResp)) {
            LoginResp loginResp = new Gson().fromJson(strLoginResp, LoginResp.class);
            return loginResp;
        }
        return null;
    }


    /**
     * 为了兼容之前保存的信息
     *
     * @param resp
     */
    private void saveData(LoginResp resp) {

        boolean isNull = null == resp;

        Context context = MyApplication.getInstance().getApplicationContext();
        LocalSharePreferences.commit(context, Content.SP_TOKEN, isNull ? "" : resp.getToken());
        LocalSharePreferences.commit(context, Content.SP_USER_ID, isNull ? "" : resp.getUserInfo().getUserId());
        LocalSharePreferences.commit(context, Content.SP_USER_NAME, isNull ? "" : resp.getUserInfo().getUserName());

        LocalSharePreferences.commit(context, "avatar", isNull ? "" : resp.getUserInfo().getAvatar());
        LocalSharePreferences.commit(context, "sex", isNull ? 0 : resp.getUserInfo().getSex());
        LocalSharePreferences.commit(context, "recordNum", isNull ? "" : resp.getUserInfo().getRecordNum());
        LocalSharePreferences.commit(context, "speciesNum", isNull ? "" : resp.getUserInfo().getSpeciesNum());
        LocalSharePreferences.commit(context, "thisYearRecordNum", isNull ? "" : resp.getUserInfo().getThisYearRecordNum());
        LocalSharePreferences.commit(context, "thisYearSpeciesNum", isNull ? "" : resp.getUserInfo().getThisYearSpeciesNum());
        LocalSharePreferences.commit(context, "database", "database");

    }

    public String getAvatarUrl() {

        if (null != getUserInfo()) {
            return getUserInfo().getAvatar();
        }

        return "";
    }

    public String getShareUrl() {

        UserInfo userInfo = getUserInfo();

        if (null != userInfo) {
            return userInfo.getShare_url();
        }

        return "";
    }

    public String getUserNm() {

        UserInfo userInfo = getUserInfo();

        if (null != userInfo) {
            return userInfo.getUserName();
        }

        return "";
    }

    /**
     * @return 所属机构
     */
    public String getInsitution() {

        UserInfo userInfo = getUserInfo();

        if (null != userInfo && !TextUtils.isEmpty(userInfo.getInsitution())) {
            return userInfo.getInsitution();
        }

        return "";
    }

    /**
     * @return 相机
     */
    public String getCamera() {

        UserInfo userInfo = getUserInfo();

        if (null != userInfo && !TextUtils.isEmpty(userInfo.getCamera())) {
            return userInfo.getCamera();
        }

        return "";
    }

    /**
     * @return 望远镜
     */
    public String getTelescope() {

        UserInfo userInfo = getUserInfo();

        if (null != userInfo && !TextUtils.isEmpty(userInfo.getTelescope())) {
            return userInfo.getTelescope();
        }

        return "";
    }

    /**
     * @return 城市id
     */
    public int getCityId() {

        UserInfo userInfo = getUserInfo();

        if (null != userInfo) {
            return userInfo.getCity_id();
        }

        return 0;
    }

    public void updateAvatar(String localAvatar) {


        LoginResp resp = getLoginResp();

        if (null != resp && null != resp.getUserInfo()) {
            resp.getUserInfo().setAvatar(localAvatar);
            saveUserInfo(resp);
        }
    }
}
