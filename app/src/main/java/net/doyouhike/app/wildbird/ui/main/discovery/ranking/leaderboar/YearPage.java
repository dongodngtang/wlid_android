package net.doyouhike.app.wildbird.ui.main.discovery.ranking.leaderboar;

import android.content.Context;

import net.doyouhike.app.wildbird.biz.model.bean.LeaderboardItem;
import net.doyouhike.app.wildbird.biz.model.request.get.LeaderboardRequestParam;
import net.doyouhike.app.wildbird.ui.main.discovery.ranking.page.PageBase;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-7.
 */
public class YearPage extends PageBase<LeaderboardItem> {

    public YearPage(Context context) {
        super();
        mRequest = new LeaderboardRequestParam();
        ((LeaderboardRequestParam)mRequest).setType(0);
        adapter=new LeaderboardAdapter(context,items);
    }

    @Override
    protected void setEmptyMsg() {

    }

    @Override
    public LeaderboardRequestParam getRequestParam() {
        return (LeaderboardRequestParam)mRequest;
    }
}
