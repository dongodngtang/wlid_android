package net.doyouhike.app.wildbird.biz.model.request.get;

import net.doyouhike.app.wildbird.biz.model.base.BaseGetRequest;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-15.
 */
public class GetAreaRankRequestParam extends BaseGetRequest {

//    * province_id: 'x', // 类型: int, 省份id
//    * type: 'z', // 类型: int, 0年榜 1 总榜

    @Override
    protected void setMapValue() {
        map.put("province_id",province_id);
        map.put("type",type);
    }

    private String province_id; // 类型:  省份id
    private String type; // 类型:  0年榜 1 总榜

    public String getProvince_id() {
        return province_id;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
    }

    public String getType() {
        return type;
    }

    public void setType(int type) {
        this.type = String.valueOf(type);
    }
}
