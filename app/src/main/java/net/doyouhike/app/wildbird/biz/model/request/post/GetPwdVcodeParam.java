package net.doyouhike.app.wildbird.biz.model.request.post;

import com.google.gson.annotations.Expose;

import net.doyouhike.app.wildbird.biz.model.base.BasePostRequest;

/**
 * Created by zaitu on 15-12-4.
 */
public class GetPwdVcodeParam extends BasePostRequest {

    @Expose
    private String phoneNumber;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
