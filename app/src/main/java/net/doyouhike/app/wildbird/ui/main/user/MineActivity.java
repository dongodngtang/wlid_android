package net.doyouhike.app.wildbird.ui.main.user;

import android.content.Context;

import net.doyouhike.app.library.ui.uistate.UiStateController;
import net.doyouhike.app.wildbird.biz.model.base.BaseListActivity;
import net.doyouhike.app.wildbird.biz.model.base.BaseResponse;
import net.doyouhike.app.wildbird.biz.model.bean.MyRecord;
import net.doyouhike.app.wildbird.ui.main.discovery.ranking.page.PageBase;

import java.util.List;

public class MineActivity extends BaseListActivity<MyRecord> {


    @Override
    protected void initViewsAndEvents() {

        mListHelper=getListHelper(lvLoadMore,viRefresh,this,getPage(this));
        uiStateController=new UiStateController(new UserUiHandler(lvLoadMore,mListHelper.getAdapter()));
        mListHelper.getData(true);
    }

    @Override
    protected PageBase<MyRecord> getPage(Context context) {
        return null;
    }

    @Override
    protected List<MyRecord> responseToItems(BaseResponse response) {
        return null;
    }
}
