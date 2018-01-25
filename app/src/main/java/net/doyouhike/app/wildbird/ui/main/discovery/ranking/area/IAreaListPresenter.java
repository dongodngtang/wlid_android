package net.doyouhike.app.wildbird.ui.main.discovery.ranking.area;

import net.doyouhike.app.wildbird.biz.model.base.BaseListGetParam;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-8.
 */
public interface IAreaListPresenter {

    void getTotal(BaseListGetParam param);
    void getYear(BaseListGetParam param);
    void onDestory();

    void getPersonalYearRank();
    void getPersonalTotalRank();
    void getAreaTotalRank(String id);
    void getAreaYearRank(String id);
}
