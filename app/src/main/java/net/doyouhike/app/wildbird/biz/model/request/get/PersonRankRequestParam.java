package net.doyouhike.app.wildbird.biz.model.request.get;

import net.doyouhike.app.wildbird.biz.model.base.BaseGetRequest;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-8.
 */
public class PersonRankRequestParam extends BaseGetRequest {
    @Override
    protected void setMapValue() {
        map.put("user_id",user_id);
        map.put("type",type);
    }

    private String user_id;// 类型: int,用户id
    private String type;// 类型: int, 0年榜 1 总榜

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getType() {
        return type;
    }

    public void setType(int type) {
        this.type = String.valueOf(type);
    }
}
