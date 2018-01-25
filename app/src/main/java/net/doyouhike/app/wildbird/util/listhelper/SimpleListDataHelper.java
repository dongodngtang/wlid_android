package net.doyouhike.app.wildbird.util.listhelper;

import android.text.TextUtils;
import android.view.View;
import android.widget.BaseAdapter;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import net.doyouhike.app.library.ui.uistate.UiState;
import net.doyouhike.app.library.ui.widgets.LoadMoreListView;
import net.doyouhike.app.library.ui.widgets.XSwipeRefreshLayout;
import net.doyouhike.app.wildbird.biz.model.base.BaseListGetParam;
import net.doyouhike.app.wildbird.biz.model.base.BaseResponse;
import net.doyouhike.app.wildbird.biz.service.net.ApiReq;
import net.doyouhike.app.wildbird.ui.base.IBaseView;
import net.doyouhike.app.wildbird.ui.main.discovery.ranking.page.PageBase;

import java.util.List;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-6.
 */
public abstract class SimpleListDataHelper<T> extends BaseListDataHelper<T> {

    /**
     * mvp中的v
     */
    IBaseView iBaseView;
    PageBase<T> mPage;

    public SimpleListDataHelper(LoadMoreListView loadMoreListView,
                                XSwipeRefreshLayout refreshLayout,
                                IBaseView iBaseView,
                                PageBase<T> page) {
        mPage = page;
        this.iBaseView=iBaseView;
        initalize(loadMoreListView, refreshLayout);
    }

    public void setiBaseView(IBaseView iBaseView) {
        this.iBaseView = iBaseView;
    }

    public PageBase<T> getmPage() {
        return mPage;
    }


    @Override
    public void onDestroy() {
        setiBaseView(null);
        super.onDestroy();
    }

    @Override
    public void updateView(UiState uiState) {
        if (null!=iBaseView){
            iBaseView.updateView(uiState, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getData(true);
                }
            });
        }
    }

    @Override
    public List<T> getItems() {
        return mPage.getItems();
    }


    @Override
    public BaseAdapter getAdapter() {
        return mPage.getAdapter();
    }

    @Override
    public BaseListGetParam getParams() {
        return mPage.getRequestParam();
    }
    @Override
    public BaseListGetParam getParams(boolean isRefresh) {
        return mPage.getRequestParam(isRefresh);
    }

    @Override
    protected String getErrTip() {
        return mPage.getErrTip();
    }

    @Override
    protected String getEmptyTip() {
        return mPage.getEmptyTip();
    }

    /**
     * @param param     请求参数
     * @param isRefresh 是否刷新
     */
    @Override
    protected void getData(final BaseListGetParam param, final boolean isRefresh) {

        if (!TextUtils.isEmpty(mPage.getRequestTag())){
            ApiReq.doCancel(mPage.getRequestTag());
        }

        ApiReq.doGet(param, "", new Response.Listener<BaseResponse>() {
            @Override
            public void onResponse(BaseResponse response) {
                List<T> responseItems = response(response);
                mPage.updateItem(responseItems,isRefresh);
                SimpleListDataHelper.this.onResponse(mPage.hasMore(), isRefresh);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onError(error);
            }
        });
    }

    protected abstract List<T> response(BaseResponse response);


}
