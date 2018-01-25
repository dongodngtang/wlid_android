package net.doyouhike.app.wildbird.util.listhelper;

import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.android.volley.VolleyError;

import net.doyouhike.app.library.ui.uistate.UiState;
import net.doyouhike.app.library.ui.widgets.LoadMoreListView;
import net.doyouhike.app.library.ui.widgets.XSwipeRefreshLayout;
import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.biz.dao.net.NetException;
import net.doyouhike.app.wildbird.biz.model.base.BaseListGetParam;
import net.doyouhike.app.wildbird.util.StateUtil;

import java.util.List;

/**
 * 功能：基础列表数据加载器
 * 管理下拉刷新,上拉加载更多的数据请求,页面显示,比如刷新状态的管理,加载完数据的页面展示等
 *
 * @author：曾江 日期：16-4-6.
 */
public abstract class BaseListDataHelper<T> {
    /**
     * 上拉加载更多listview
     */
    protected LoadMoreListView loadMoreListView;
    /**
     * 上拉刷新view
     */
    protected XSwipeRefreshLayout refreshLayout;
    /**
     * 销毁标志,不更新界面
     */
    private boolean onDestroy =false;

    /**
     * 填充数据
     * @param responseItems 列表数据
     * @param isRefresh 是否刷新,true为刷新,刷新状态为清理列表再填充数据
     */
    public void onGetDataResponse(List<T> responseItems,boolean isRefresh){

        if (isRefresh) {
            getItems().clear();
            refreshLayout.setRefreshing(false);
            updateView(UiState.NORMAL);
        }

        loadMoreListView.setCanLoadMore(responseItems.size() >= getParams().getCount());

        getItems().addAll(responseItems);

        if (getItems().isEmpty()) {
            updateView(UiState.EMPTY.setMsg(getEmptyTip()));
        } else {
            getAdapter().notifyDataSetChanged();
        }
    }

    /**
     * @param hasMore 有无更多数据
     * @param isRefresh 是否刷新
     */
    public void onResponse(boolean hasMore,boolean isRefresh){

        if (onDestroy){
            return;
        }

        if (isRefresh) {
            refreshLayout.setRefreshing(false);
            updateView(UiState.NORMAL);
        }else {
            loadMoreListView.onLoadMoreComplete();
        }

        //设置是否能够加载更多
        loadMoreListView.setCanLoadMore(hasMore);

        if (getItems().isEmpty()) {
            //设置empty视图
            updateView(UiState.EMPTY.setMsg(getEmptyTip()));
            //
            loadMoreListView.showLoadMoreView(false);
        } else {
            //设置能够显示更多
            loadMoreListView.showLoadMoreView(true);
            getAdapter().notifyDataSetChanged();
        }
    }

    /**
     * 下拉刷新时调用此方法
     */
    public void onRefresh(){

    }

    /**
     * @param error 加载列表错误回调
     */
    public void onError(VolleyError error){
        if (onDestroy){
            return;
        }

        UiState state;

        if (error instanceof NetException){

            state= StateUtil.toUiState((NetException)error);

        }else {
            if (getItems().isEmpty()){
                state=UiState.ERROR.setMsg("网络不佳，点击刷新。");
            }else {
                state=UiState.ERROR.setMsg("加载失败。");
            }
        }


        if (refreshLayout.isRefreshing()){
            refreshLayout.setRefreshing(false);
        }

        loadMoreListView.onLoadMoreComplete();

        if (getItems().isEmpty()){
            updateView(state);
        }else {

            if (!TextUtils.isEmpty(getErrTip())){
                Toast.makeText(refreshLayout.getContext(), getErrTip(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    /**
     * @param msg 错误信息
     */
    private void onError(String msg){

        if (refreshLayout.isRefreshing()){
            refreshLayout.setRefreshing(false);
        }

        loadMoreListView.onLoadMoreComplete();

        if (getItems().isEmpty()){
            updateView(UiState.ERROR.setMsg(msg));
        }else {
            Toast.makeText(refreshLayout.getContext(), getErrTip(), Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * @param loadMoreListView   下拉加载更多
     * @param refreshLayout 上拉刷新
     */
    public void initalize(
            LoadMoreListView loadMoreListView,
            XSwipeRefreshLayout refreshLayout) {

        this.loadMoreListView = loadMoreListView;
        this.refreshLayout = refreshLayout;

        //下拉刷新
        refreshLayout.setColorSchemeColors(
                refreshLayout.getResources().getColor(R.color.app_theme));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(true);
            }
        });


        //上拉加载更多
        loadMoreListView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getData(false);
            }
        });
        //设置适配器
        setAdapter();
    }

    /**
     * 设置适配器
     */
    public void setAdapter(){
        if (null!=getAdapter()){
            loadMoreListView.setAdapter(getAdapter());
        }
    }


    /**
     * 获取数据
     * @param isRefresh 是否刷新
     */
    public void getData(final boolean isRefresh) {

        final BaseListGetParam param=getParams(isRefresh);

        if (isRefresh){
            BaseListDataHelper.this.onRefresh();
            if (getItems().isEmpty()){
                updateView(UiState.LOADING);
            }else {
                setRefreshView();
            }
            //从0项开始获取数据
            param.setSkip(0);
        }else {
            setLoadMoreView();
            param.setSkip(getItems().size());
        }
        //设置页码标志,用于响应时判断是刷新还是请求更多数据
        param.setTag(param.getSkip());
        getData(param, isRefresh);
    }

    /**
     * 请求数据
     * @param param 请求参数
     * @param isRefresh 是否刷新
     */
    protected abstract void getData(BaseListGetParam param, boolean isRefresh);


    /**
     * 设置刷新的视图
     */
    private void setRefreshView() {
        loadMoreListView.onLoadMoreComplete();
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (null!=refreshLayout)
                refreshLayout.setRefreshing(true);
            }
        });
    }

    /**
     * 设置加载更多的视图
     */
    private void setLoadMoreView() {
        refreshLayout.setRefreshing(false);
    }

    /**
     * @return 列表数据
     */
    public abstract List<T> getItems();

    /**
     * @return 不同的适配器
     */
    public abstract BaseAdapter getAdapter();

    /**
     * @param isRefresh 是否刷新
     * @return 获取请求数据参数
     */
    public abstract BaseListGetParam getParams(boolean isRefresh);

    /**
     * @return 获取请求数据参数
     */
    public abstract BaseListGetParam getParams();

    /**
     * @param uiState 更新ui ui状态
     */
    public abstract void updateView(UiState uiState);

    /**
     * @return 错误提示信息
     */
    protected abstract String getErrTip();

    /**
     * @return 列表为空时的提示信息
     */
    protected abstract String getEmptyTip();

    /**
     * 页码销毁调用
     */
    public void onDestroy() {
        onDestroy =true;
    }

}
