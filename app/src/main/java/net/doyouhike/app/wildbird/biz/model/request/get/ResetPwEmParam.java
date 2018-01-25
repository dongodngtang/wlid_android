package net.doyouhike.app.wildbird.biz.model.request.get;

import net.doyouhike.app.wildbird.biz.model.base.BaseGetRequest;

/**
 * Created by zaitu on 15-12-8.
 */
public class ResetPwEmParam extends BaseGetRequest {

    private String email;

    @Override
    protected void setMapValue() {
        map.put("step_act","4");
        map.put("email",email);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
