package net.doyouhike.app.wildbird.ui.main.discovery.ranking.page;

import android.text.TextUtils;
import android.widget.BaseAdapter;


import com.android.volley.VolleyError;

import net.doyouhike.app.wildbird.biz.model.base.BaseListGetParam;
import net.doyouhike.app.wildbird.util.CheckNetWork;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：列表页
 * 每个列表都有对应的adapter list 还有请求数据的BaseListGetParam
 *
 * 作者：曾江
 * 日期：16-1-12.
 */
public abstract class PageBase<T> implements IPage {

    protected BaseAdapter adapter;
    protected List<T> items;
    boolean hasMore = true;
    /**
     *
     */
    protected BaseListGetParam mRequest;
    /**
     * 列表状态,列表为空,请求数据出错了,正常获取到了数据,初始化状态
     */
    protected State state = State.NULL;

    String emptyTip;
    String errTip;

    public PageBase() {
        items=new ArrayList<>();
    }

    @Override
    public void updateItem(List item, boolean isRefresh) {


        if (isRefresh) {
            items.clear();
        }


        if (null == item) {
            hasMore = false;

            if (isRefresh) {
                //刷新返回空
                state = State.EMPTY;
                setEmptyMsg();
            }
            return;
        }

        hasMore = mRequest.getCount() <= item.size();

        items.addAll(item);

        if (items.isEmpty()) {
            state = State.EMPTY;
            setEmptyMsg();

        } else {
            state = State.NORMAL;
        }

    }

    protected abstract void setEmptyMsg();

    @Override
    public String getRequestTag() {
        return "";
    }

    @Override
    public BaseListGetParam getRequestParam(boolean isRefresh) {
        return getRequestParam();
    }

    @Override
    public BaseAdapter getAdapter() {
        return adapter;
    }

    @Override
    public List<T> getItems() {
        return items;
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public boolean hasMore() {
        return hasMore;
    }

    @Override
    public void sethasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    @Override
    public void setRequestPage() {

        int page = items.size();
        mRequest.setSkip(page);
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public void getDataErr(VolleyError error) {
        if (items.isEmpty()) {
            state = State.ERROR;
            setErrTip(CheckNetWork.getErrorMsg(error));
        }
    }

    @Override
    public void notifyDataChange() {
        adapter.notifyDataSetChanged();
    }


    @Override
    public String getEmptyTip() {
        return TextUtils.isEmpty(emptyTip)?"暂无内容":emptyTip;
    }

    @Override
    public void setEmptyTip(String emptyTip) {
        this.emptyTip = emptyTip;
    }

    @Override
    public String getErrTip() {
        return errTip;
    }

    @Override
    public void setErrTip(String errTip) {
        this.errTip = errTip;
    }
}
