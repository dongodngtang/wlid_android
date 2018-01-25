package net.doyouhike.app.wildbird.biz.model.base;

import android.content.Context;
import android.view.View;

import net.doyouhike.app.library.ui.netstatus.NetUtils;
import net.doyouhike.app.library.ui.widgets.LoadMoreListView;
import net.doyouhike.app.library.ui.widgets.XSwipeRefreshLayout;
import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.ui.base.BaseAppActivity;
import net.doyouhike.app.wildbird.ui.base.IBaseView;
import net.doyouhike.app.wildbird.ui.main.discovery.ranking.page.PageBase;
import net.doyouhike.app.wildbird.util.listhelper.SimpleListDataHelper;
import net.doyouhike.app.wildbird.ui.view.TitleView;

import java.util.List;

import butterknife.InjectView;

public abstract class BaseListActivity<T> extends BaseAppActivity {

    @InjectView(R.id.lv_load_more)
    protected LoadMoreListView lvLoadMore;
    @InjectView(R.id.vi_refresh)
    protected XSwipeRefreshLayout viRefresh;
    @InjectView(R.id.titleview_activity_list)
    protected TitleView mTitleView;

    protected SimpleListDataHelper<T> mListHelper;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.layout_refresh_list;
    }

    @Override
    protected View getLoadingTargetView() {
        return viRefresh;
    }

    @Override
    protected void initViewsAndEvents() {
        super.initViewsAndEvents();
        mListHelper = getListHelper(lvLoadMore, viRefresh, this, getPage(this));
        mListHelper.getData(true);
    }

    @Override
    protected void onDestroy() {
        mListHelper.onDestroy();
        super.onDestroy();
    }

    /**
     * @param context
     * @return 获取列表页
     */
    protected abstract PageBase<T> getPage(Context context);

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {
        if (type!=NetUtils.NetType.NONE){
            if (null!=mListHelper&&!mListHelper.getItems().isEmpty()){
                mListHelper.getData(true);
            }
        }
    }

    /**
     * @param loadMoreListView 上拉加载更多listview
     * @param refreshLayout 下拉刷新View
     * @param iBaseView 更新视图调用
     * @param page 列表页
     * @return 列表控制器
     */
    protected SimpleListDataHelper<T> getListHelper(LoadMoreListView loadMoreListView,
                                                    XSwipeRefreshLayout refreshLayout,
                                                    IBaseView iBaseView,
                                                    PageBase<T> page) {


        return new SimpleListDataHelper<T>(loadMoreListView, refreshLayout, iBaseView, page) {
            @Override
            protected List<T> response(BaseResponse response) {
                return responseToItems(response);
            }

            @Override
            public void onRefresh() {
                BaseListActivity.this.onRefresh();
            }
        };
    }

    /**
     * 下拉刷新时调用
     */
    public void onRefresh() {

    }

    /**
     * @param response 网络响应
     * @return 列表 提取网络成功回调的数据
     */
    protected abstract List<T> responseToItems(BaseResponse response);

}
