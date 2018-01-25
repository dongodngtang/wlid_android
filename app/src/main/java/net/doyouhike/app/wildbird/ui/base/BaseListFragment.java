package net.doyouhike.app.wildbird.ui.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import net.doyouhike.app.library.ui.widgets.LoadMoreListView;
import net.doyouhike.app.library.ui.widgets.XSwipeRefreshLayout;
import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.biz.dao.sharepref.UserInfoSpUtil;
import net.doyouhike.app.wildbird.biz.model.base.BaseResponse;
import net.doyouhike.app.wildbird.ui.main.discovery.ranking.page.PageBase;
import net.doyouhike.app.wildbird.ui.main.user.mine.MePage;
import net.doyouhike.app.wildbird.ui.view.TitleView;
import net.doyouhike.app.wildbird.util.listhelper.SimpleListDataHelper;

import java.util.List;

import butterknife.InjectView;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-13.
 */
public abstract class BaseListFragment<T> extends BaseFragment implements IBaseView {

    protected LoadMoreListView lvLoadMore;
    @InjectView(R.id.vi_refresh)
    protected XSwipeRefreshLayout viRefresh;
    @InjectView(R.id.titleview_activity_list)
    protected TitleView mTitleView;

    protected SimpleListDataHelper<T> mListHelper;

    public void onRefresh() {

    }

    protected View getHeadView() {
        return null;
    }

    /**
     * @param response 网络响应
     * @return 网络响应转换为列表数据
     */
    protected abstract List<T> responseToItems(BaseResponse response);


    public void notifyDataChange() {
        mListHelper.getAdapter().notifyDataSetChanged();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.layout_refresh_list;
    }

    @Override
    protected View getLoadingTargetView() {
        return viRefresh;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        lvLoadMore = (LoadMoreListView) view.findViewById(R.id.lv_load_more);

        getActivity().registerReceiver(netStatusReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        if (null != getHeadView()) {
            lvLoadMore.addHeaderView(getHeadView());
        }
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        getActivity().unregisterReceiver(netStatusReceiver);
        mListHelper.onDestroy();
        super.onDestroyView();
    }

    @Override
    protected void initViewsAndEvents() {
        super.initViewsAndEvents();
        mListHelper = getListHelper(lvLoadMore, viRefresh, this, getPage(getContext()));
        mListHelper.getData(true);
    }

    protected abstract PageBase<T> getPage(Context context);

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
                BaseListFragment.this.onRefresh();
            }
        };
    }

    /**
     * 监听网络变化
     */
    private BroadcastReceiver netStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if (intent != null) {
                String action = intent.getAction();
                if (action != null) {
                    if (action.equalsIgnoreCase(ConnectivityManager.CONNECTIVITY_ACTION)) {

                        if (null != mListHelper && mListHelper.getItems().isEmpty()) {

                            if (mListHelper.getmPage() instanceof MePage && !UserInfoSpUtil.getInstance().isLogin()){
                                //个人主页未登陆时检测到网络变化不刷新数据
                                return;
                            }
                            mListHelper.getData(true);
                        }
                    }
                }
            }
        }
    };
}
