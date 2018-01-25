package net.doyouhike.app.wildbird.ui.main.birdinfo.news;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.doyouhike.app.library.ui.adapterutil.CommonAdapter;
import net.doyouhike.app.library.ui.adapterutil.ViewHolder;
import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.biz.model.bean.BirdNewsItem;
import net.doyouhike.app.wildbird.util.TimeUtil;
import net.doyouhike.app.wildbird.util.UiUtils;

import java.util.List;

/**
 * 功能：国际新闻适配
 *
 * @author：曾江 日期：16-4-11.
 */
public class BirdNewsAdapter extends CommonAdapter<BirdNewsItem> {
    public BirdNewsAdapter(Context context, List<BirdNewsItem> datas) {
        super(context, datas, R.layout.item_bird_news);
    }

    @Override
    public void convert(ViewHolder holder, BirdNewsItem birdNewsItem) {
        holder.setText(R.id.tv_bird_news_content, birdNewsItem.getContent());
        holder.setText(R.id.tv_bird_news_title, birdNewsItem.getTitle());
        holder.setText(R.id.tv_bird_news_author, birdNewsItem.getAuthor());
        holder.setText(R.id.tv_bird_news_time, TimeUtil.getFormatTimeFromTimestamp(birdNewsItem.getCreated_at()*1000, "yyyy-MM-dd"));

        final TextView textView = holder.getView(R.id.tv_bird_news_content);
        final ImageView imageView=holder.getView(R.id.iv_bird_news_content_show_indicate);
        final ImageView ivAvatar=holder.getView(R.id.iv_bird_news_avatar);
//        ImageLoaderUtil.getInstance().showAvatar(ivAvatar,birdNewsItem.get);


        holder.setOnClickListener(R.id.vi_bird_new_content_parent, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean isVisible = textView.isShown();

                imageView.setImageDrawable(isVisible ?
                        mContext.getResources().getDrawable(R.drawable.ic_bird_detail_content_indicate_down) :
                        mContext.getResources().getDrawable(R.drawable.ic_bird_detail_content_indicate_up));

                UiUtils.showView(textView, !isVisible);

            }
        });
    }
}
