package net.doyouhike.app.wildbird.ui.main.discovery.ranking.area;

import com.android.volley.VolleyError;

import net.doyouhike.app.wildbird.biz.model.bean.AreaRank;
import net.doyouhike.app.wildbird.biz.model.bean.RankAreaItem;

import java.util.List;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-8.
 */
public interface IRankAreaView {
    void onGetPersonalTotalRank(AreaRank rank);
    void onGetPersonalYearRank(AreaRank rank);
    void onGetTotalErr(VolleyError msg);
    void onGetYearErr(VolleyError msg);
    void onGetYearList(List<RankAreaItem> yearItems, boolean isRefresh);
    void onGetTotalList(List<RankAreaItem> totalItems, boolean isRefresh);
}
