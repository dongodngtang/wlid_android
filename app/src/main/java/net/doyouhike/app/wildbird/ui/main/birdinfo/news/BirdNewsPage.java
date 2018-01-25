package net.doyouhike.app.wildbird.ui.main.birdinfo.news;

import android.content.Context;

import net.doyouhike.app.wildbird.biz.model.bean.BirdNewsItem;
import net.doyouhike.app.wildbird.biz.model.request.get.GetBirdNewsRequestParam;
import net.doyouhike.app.wildbird.ui.main.discovery.ranking.page.PageBase;

/**
 * 功能： 鸟新闻
 *
 * @author：曾江 日期：16-4-11.
 */
public class BirdNewsPage extends PageBase<BirdNewsItem> {


    public BirdNewsPage(Context context) {
        super();
        mRequest = new GetBirdNewsRequestParam();
        adapter = new BirdNewsAdapter(context, items);
    }

    @Override
    protected void setEmptyMsg() {

    }

    @Override
    public GetBirdNewsRequestParam getRequestParam() {
        return (GetBirdNewsRequestParam) mRequest;
    }
}
