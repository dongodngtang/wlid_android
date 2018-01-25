package net.doyouhike.app.wildbird.ui.main.birdinfo.news;

import android.content.Context;
import android.os.Bundle;

import net.doyouhike.app.wildbird.biz.model.base.BaseListActivity;
import net.doyouhike.app.wildbird.biz.model.base.BaseResponse;
import net.doyouhike.app.wildbird.biz.model.base.CommonResponse;
import net.doyouhike.app.wildbird.biz.model.bean.BirdNewsItem;
import net.doyouhike.app.wildbird.biz.model.response.GetBirdNewsResponse;
import net.doyouhike.app.wildbird.ui.main.birdinfo.detail.BirdDetailActivity;
import net.doyouhike.app.wildbird.ui.main.discovery.ranking.page.PageBase;

import java.util.List;

/**
 * 国际新闻
 */
public  class BirdNewsActivity extends BaseListActivity<BirdNewsItem> {


    String speciesId;
    String speciesName;

    @Override
    protected void getBundleExtras(Bundle extras) {
        speciesId=extras.getString(BirdDetailActivity.I_SPECIES_ID);
        speciesName=extras.getString(BirdDetailActivity.I_SPECIES_NAME);
    }

    @Override
    protected void initViewsAndEvents() {
        super.initViewsAndEvents();
        mTitleView.setTitle(speciesName+"消息");
    }

    @Override
    protected PageBase<BirdNewsItem> getPage(Context context) {
        BirdNewsPage birdNewsPage=new BirdNewsPage(this);
        birdNewsPage.getRequestParam().setSpecies_id(speciesId);
        return birdNewsPage;
    }

    @Override
    protected List<BirdNewsItem> responseToItems(BaseResponse response) {

        return ((CommonResponse<GetBirdNewsResponse>)response).getT().getNews_list();
    }


}
