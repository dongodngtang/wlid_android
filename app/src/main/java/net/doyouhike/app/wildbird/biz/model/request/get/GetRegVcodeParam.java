package net.doyouhike.app.wildbird.biz.model.request.get;

import net.doyouhike.app.wildbird.biz.model.base.BaseGetRequest;

/**
 * Created by zaitu on 15-12-4.
 */
public class GetRegVcodeParam extends BaseGetRequest{

    private String phoneNumber;

    @Override
    protected void setMapValue() {
        map.put("phoneNumber",phoneNumber);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
