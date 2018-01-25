package net.doyouhike.app.wildbird.biz.model.request.get;

import net.doyouhike.app.wildbird.biz.model.base.BaseListGetParam;

/**
 * 功能：排行榜
 *
 * @author：曾江 日期：16-4-8.
 */
public class AreaListRequestParam extends BaseListGetParam {
    @Override
    protected void setMapValue() {
        super.setMapValue();
        map.put("type", type);

    }

    private String type; // 类型: int, 0年榜 1 总榜

    public String getType() {
        return type;
    }

    public void setType(int type) {
        this.type = String.valueOf(type);
    }

}
