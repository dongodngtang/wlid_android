package net.doyouhike.app.wildbird.ui.main.birdinfo.detail;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 功能:鸟种图片适配器→鸟种详情\观鸟记录详情里用到
 * {@link net.doyouhike.app.wildbird.ui.main.birdinfo.detail.BirdDetailActivity}
 * {@link net.doyouhike.app.wildbird.ui.main.birdinfo.record.detail.BirdRecordDetailActivity}
 *
 * @author：曾江 日期：16-4-11.
 */
public class BirdPictureAdapter extends PagerAdapter {
    /**
     * 图片视图
     */
    private List<View> views;

    public BirdPictureAdapter(List<View> views) {
        this.views = views;
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView(views.get(position % views.size()));

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position % views.size()), 0);
        return views.get(position % views.size());
    }
}
