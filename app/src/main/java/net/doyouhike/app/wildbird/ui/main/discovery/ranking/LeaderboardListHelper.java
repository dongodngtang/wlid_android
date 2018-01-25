package net.doyouhike.app.wildbird.ui.main.discovery.ranking;

import android.view.View;
import android.widget.BaseAdapter;

import net.doyouhike.app.library.ui.uistate.UiState;
import net.doyouhike.app.library.ui.widgets.LoadMoreListView;
import net.doyouhike.app.library.ui.widgets.XSwipeRefreshLayout;
import net.doyouhike.app.wildbird.biz.model.base.BaseListGetParam;
import net.doyouhike.app.wildbird.biz.model.bean.LeaderboardItem;
import net.doyouhike.app.wildbird.ui.main.discovery.ranking.page.PageBase;
import net.doyouhike.app.wildbird.util.listhelper.BaseListDataHelper;

import java.util.List;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-7.
 */
public class LeaderboardListHelper extends BaseListDataHelper<LeaderboardItem> {
    DataHelperListener listener;
    private PageBase<LeaderboardItem> mPage;

    public LeaderboardListHelper(
            LoadMoreListView loadMoreListView,
            XSwipeRefreshLayout refreshLayout, PageBase<LeaderboardItem> page) {

        mPage = page;
        initalize(loadMoreListView, refreshLayout);

    }

    public void onResponse(boolean isRefresh) {
        onResponse(mPage.hasMore(), isRefresh);
    }

    public void setListener(DataHelperListener listener) {
        this.listener = listener;
    }

    public void setPage(PageBase<LeaderboardItem> page) {

        this.mPage = page;

        if (null == listener) {
            return;
        }


        switch (mPage.getState()) {
            case EMPTY:
                //空提示
                updateView(UiState.EMPTY.setMsg(mPage.getEmptyTip()));
                break;
            case NULL:
                //刷新数据
                updateView((UiState.LOADING));
                setAdapter();
                getData(true);
                break;
            case NORMAL:
                //正常显示
                updateView((UiState.NORMAL));
                updateAdapter();
                if (mPage.hasMore()) {
                    loadMoreListView.setCanLoadMore(true);
                } else {
                    loadMoreListView.setCanLoadMore(false);
                }
                break;
            case ERROR:
                //显示错误页
                updateView((UiState.ERROR.setMsg(mPage.getErrTip())));

                break;
        }
    }

    private void updateAdapter() {
        loadMoreListView.setAdapter(mPage.getAdapter());
        mPage.getAdapter().notifyDataSetChanged();
    }

    @Override
    protected void getData(BaseListGetParam param, boolean isRefresh) {
        if (null != listener)
            listener.getListDate(param, isRefresh);
    }

    @Override
    public BaseListGetParam getParams(boolean isRefresh) {
        return mPage.getRequestParam(isRefresh);
    }

    @Override
    public BaseListGetParam getParams() {
        return mPage.getRequestParam();
    }

    @Override
    public void updateView(UiState uiState) {
        if (null != listener){
            listener.setViewState(uiState,
                    new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getData(true);
                }
            });
        }
    }

    @Override
    protected String getErrTip() {
        return mPage.getErrTip();
    }

    @Override
    protected String getEmptyTip() {
        return mPage.getEmptyTip();
    }


    @Override
    public List<LeaderboardItem> getItems() {
        return mPage.getItems();
    }

    @Override
    public BaseAdapter getAdapter() {
        if (mPage == null) {
            return null;
        }
        return mPage.getAdapter();
    }


    public interface DataHelperListener {

        /**
         * @param param     获取列表数据
         * @param isRefresh
         */
        void getListDate(BaseListGetParam param, boolean isRefresh);

        /**
         * @param state 更新UI状态
         */
        void setViewState(UiState state,View.OnClickListener listener);

    }
}
