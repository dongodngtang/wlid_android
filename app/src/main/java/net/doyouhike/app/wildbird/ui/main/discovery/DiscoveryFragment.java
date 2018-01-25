package net.doyouhike.app.wildbird.ui.main.discovery;

import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.ui.base.BaseFragment;
import net.doyouhike.app.wildbird.ui.main.discovery.nearby.NearbyActivity;
import net.doyouhike.app.wildbird.ui.main.discovery.ranking.area.AreaListActivity;
import net.doyouhike.app.wildbird.ui.main.discovery.ranking.leaderboar.LeaderboardActivity;

import butterknife.OnClick;

/**
 * 功能：首页发现
 * {@link net.doyouhike.app.wildbird.ui.main.MainActivity}
 *
 * @author：曾江 日期：16-4-7.
 */
public class DiscoveryFragment extends BaseFragment {


    @OnClick(R.id.tv_discovery_nearby)
    public void nearby() {
        //附近
        readyGo(NearbyActivity.class);
    }

    @OnClick(R.id.tv_discovery_leaderboard)
    public void leaderboard() {
        //排行榜
        readyGo(LeaderboardActivity.class);
    }

    @OnClick(R.id.tv_discovery_area)
    public void area() {
        //区域榜
        readyGo(AreaListActivity.class);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_discovery;
    }

}
