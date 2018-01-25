package net.doyouhike.app.wildbird.ui.main.discovery.ranking.area;

import android.content.Context;

import net.doyouhike.app.library.ui.adapterutil.CommonAdapter;
import net.doyouhike.app.library.ui.adapterutil.ViewHolder;
import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.biz.model.bean.RankAreaItem;

import java.util.List;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-8.
 */
public class RankAreaAdapter extends CommonAdapter<RankAreaItem> {
    public RankAreaAdapter(Context context, List<RankAreaItem> datas) {
        super(context, datas, R.layout.item_rank_area);
    }

    @Override
    public void convert(ViewHolder holder, RankAreaItem item) {
        holder.setText(R.id.tv_item_rank_area_index,item.getRank());

        holder.setText(R.id.tv_item_rank_area_province, item.getProvince_name());

        holder.setText(R.id.tv_item_rank_area_kind, item.getSpecies_count() + "种");

    }
}
