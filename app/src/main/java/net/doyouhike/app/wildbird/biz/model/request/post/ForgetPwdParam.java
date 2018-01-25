package net.doyouhike.app.wildbird.biz.model.request.post;

import com.google.gson.annotations.Expose;

import net.doyouhike.app.wildbird.biz.model.base.BasePostRequest;

/**
 * Created by zaitu on 15-12-4.
 */
public class ForgetPwdParam extends BasePostRequest {

    /**
     * phoneNumber : 类型: string, 手机号码（必选）
     * vcode : 类型: string, 短信验证码（必选）
     * theNewPwd : 类型: string, 用户新密码 为了兼容OC,加了the, MD5码（必选）
     */

    @Expose
    private String phoneNumber;
    @Expose
    private String vcode;
    @Expose
    private String theNewPwd;

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setVcode(String vcode) {
        this.vcode = vcode;
    }

    public void setTheNewPwd(String theNewPwd) {
        this.theNewPwd = theNewPwd;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getVcode() {
        return vcode;
    }

    public String getTheNewPwd() {
        return theNewPwd;
    }
}
