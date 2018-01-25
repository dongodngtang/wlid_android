package net.doyouhike.app.wildbird.ui.main.discovery.ranking.leaderboar;

import com.android.volley.VolleyError;

import net.doyouhike.app.wildbird.biz.model.bean.LeaderboardItem;
import net.doyouhike.app.wildbird.biz.model.bean.PersonRank;

import java.util.List;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-8.
 */
public interface ILeaderboardView {
    void onGetPersonalTotalRank(PersonRank rank);
    void onGetPersonalYearRank(PersonRank rank);
    void onGetTotalErr(VolleyError msg);
    void onGetYearErr(VolleyError msg);
    void onGetYearList(List<LeaderboardItem> yearItems, boolean isRefresh);
    void onGetTotalList(List<LeaderboardItem> totalItems, boolean isRefresh);
}
