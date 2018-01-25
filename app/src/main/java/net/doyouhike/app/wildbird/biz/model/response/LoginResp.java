package net.doyouhike.app.wildbird.biz.model.response;

import net.doyouhike.app.wildbird.biz.model.bean.UserInfo;

/**
 * Created by zaitu on 15-12-7.
 */
public class LoginResp {

    /**
     * token : 5af07ce0d9041744ebc24b7d5f43b585
     * userInfo : {"userId":"1213026","userName":"曾江","sex":1,"recordNum":"4","speciesNum":"3","thisYearRecordNum":"4","thisYearSpeciesNum":"3","avatar":"http://static.test.doyouhike.net/files/faces/b/3/b30e0a9b8.jpg"}
     */

    private String token;

    private UserInfo userInfo;

    public void setToken(String token) {
        this.token = token;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getToken() {
        return token;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    @Override
    public String toString() {
        return "LoginResp{" +
                "token='" + token + '\'' +
                ", userInfo=" + userInfo +
                '}';
    }
}
