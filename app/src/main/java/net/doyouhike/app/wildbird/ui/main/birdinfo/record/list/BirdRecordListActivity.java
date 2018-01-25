package net.doyouhike.app.wildbird.ui.main.birdinfo.record.list;

import android.content.Context;
import android.os.Bundle;

import net.doyouhike.app.wildbird.biz.model.base.BaseListActivity;
import net.doyouhike.app.wildbird.biz.model.base.BaseResponse;
import net.doyouhike.app.wildbird.biz.model.base.CommonResponse;
import net.doyouhike.app.wildbird.biz.model.bean.BirdRecordTotalItem;
import net.doyouhike.app.wildbird.biz.model.response.GetBirdRecordResponse;
import net.doyouhike.app.wildbird.ui.main.birdinfo.detail.BirdDetailActivity;
import net.doyouhike.app.wildbird.ui.main.discovery.ranking.page.PageBase;

import java.util.List;

/**
 * 观鸟记录列表
 */
public class BirdRecordListActivity extends BaseListActivity<BirdRecordTotalItem> {


    /**
     * 鸟种id
     */
    String speciesId;
    /**
     * 鸟的名字
     */
    String speciesName;

    @Override
    protected void initViewsAndEvents() {
        super.initViewsAndEvents();
        //设置标题
        mTitleView.setTitle(speciesName + "记录");
    }

    /**
     * @param extras 上级页面将鸟id和鸟名字传过来
     */
    @Override
    protected void getBundleExtras(Bundle extras) {
        speciesId = extras.getString(BirdDetailActivity.I_SPECIES_ID);
        speciesName = extras.getString(BirdDetailActivity.I_SPECIES_NAME);
    }

    /**
     * @param context
     * @return 鸟列表 页
     */
    @Override
    protected PageBase<BirdRecordTotalItem> getPage(Context context) {
        BirdRecordPage birdRecordPage = new BirdRecordPage(context);
        birdRecordPage.getRequestParam().setSpecies_id(speciesId);
        return birdRecordPage;
    }

    /**
     * @param response 网络响应
     * @return 将网络响应转换为 列表
     */
    @Override
    protected List<BirdRecordTotalItem> responseToItems(BaseResponse response) {

        return ((CommonResponse<GetBirdRecordResponse>)response).getT().getSpecies_records();

    }
}
