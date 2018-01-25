package net.doyouhike.app.wildbird.biz.model.request.post;

import com.google.gson.annotations.Expose;

import net.doyouhike.app.wildbird.biz.model.base.BasePostRequest;

/**
 * Created by zaitu on 15-12-4.
 */
public class LoginParam extends BasePostRequest {

    /**
     * phoneNumber : 类型: string, 手机号码/用户名/邮箱（必选
     * password : 类型: string,  密码（必选
     */

    @Expose
    private String phoneNumber;
    @Expose
    private String password;

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }
}
