package net.doyouhike.app.wildbird.ui.main.discovery.ranking.leaderboar;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import net.doyouhike.app.library.ui.adapterutil.CommonAdapter;
import net.doyouhike.app.library.ui.adapterutil.ViewHolder;
import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.biz.model.bean.LeaderboardItem;
import net.doyouhike.app.wildbird.util.GotoActivityUtil;
import net.doyouhike.app.wildbird.util.ImageLoaderUtil;

import java.util.List;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-8.
 */
public class LeaderboardAdapter extends CommonAdapter<LeaderboardItem>  {
    public LeaderboardAdapter(Context context, List<LeaderboardItem> datas) {
        super(context, datas, R.layout.item_leaderboard);
    }

    @Override
    public void convert(ViewHolder holder, final LeaderboardItem leaderboardItem) {
        holder.setText(R.id.tv_item_leaderboard_index, leaderboardItem.getRank());
        holder.setText(R.id.tv_item_leaderboard_name, leaderboardItem.getUser_name());
        holder.setText(R.id.tv_item_leaderboard_kind, leaderboardItem.getSpecies_count() + "种");


        holder.setOnClickListener(R.id.ll_item_leaderboard_parent, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GotoActivityUtil.gotoOtherActivity(v.getContext(),leaderboardItem.getUser_id(),leaderboardItem.getUser_name());
            }
        });
        ImageView ivAvatar=holder.getView(R.id.iv_item_leaderboard_avatar);

        ImageLoaderUtil.getInstance().showAvatar(ivAvatar,leaderboardItem.getAvatar());
    }


}
