package net.doyouhike.app.wildbird.biz.model.response;

import net.doyouhike.app.wildbird.biz.model.bean.RankAreaItem;

import java.util.List;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-8.
 */
public class RankAreaResponse {

    private List<RankAreaItem> area_list;

    public List<RankAreaItem> getArea_list() {
        return area_list;
    }

    public void setArea_list(List<RankAreaItem> area_list) {
        this.area_list = area_list;
    }
}
