package net.doyouhike.app.wildbird.ui.main.discovery.ranking.leaderboar;

import net.doyouhike.app.wildbird.biz.model.base.BaseListGetParam;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-8.
 */
public interface ILeaderboardPresenter {
    void getPersonalYearRank();
    void getPersonalTotalRank();
    void getTotal(BaseListGetParam param);
    void getYear(BaseListGetParam param);
    void onDestory();
}
