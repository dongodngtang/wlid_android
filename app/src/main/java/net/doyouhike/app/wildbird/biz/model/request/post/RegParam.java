package net.doyouhike.app.wildbird.biz.model.request.post;

import com.google.gson.annotations.Expose;

import net.doyouhike.app.wildbird.biz.model.base.BasePostRequest;

/**
 * Created by zaitu on 15-12-4.
 */
public class RegParam extends BasePostRequest {

    /**
     * phoneNumber : 手机号码，支持格式：+86,13888888888 或直接 13888888888 无区号的话系统会默认+86 (必填)
     * password : 密码 （md5码, 必填）
     * vcode : 手机内的短信验证码 （必填）
     * userName : 用户名 （必填）
     */
    @Expose
    private String phoneNumber;
    @Expose
    private String password;
    @Expose
    private String vcode;
    @Expose
    private String userName;

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setVcode(String vcode) {
        this.vcode = vcode;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public String getVcode() {
        return vcode;
    }

    public String getUserName() {
        return userName;
    }
}
