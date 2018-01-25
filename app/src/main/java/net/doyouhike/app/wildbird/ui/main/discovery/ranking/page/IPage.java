package net.doyouhike.app.wildbird.ui.main.discovery.ranking.page;

import android.widget.BaseAdapter;

import com.android.volley.VolleyError;

import net.doyouhike.app.wildbird.biz.model.base.BaseListGetParam;

import java.util.List;

/**
 * 功能：状态行为
 * 作者：曾江
 * 日期：16-1-12.
 */
public interface IPage {

    /**
     * @return 返回列表
     */
    List getItems();
    /**
     * @param item      列表内容
     * @param isRefresh 是否更新
     */
    void updateItem(List item, boolean isRefresh);


    /**
     * @return 列表是否为空
     */
    boolean isEmpty();

    /**
     * @return 是否拥有更多
     */
    boolean hasMore();

    /**
     * @param hasMore 是否拥有更多
     */
    void sethasMore(boolean hasMore);

    /**
     * @return 获取适配器
     */
    BaseAdapter getAdapter();

    /**
     * @return 获取网络请求参数
     */
    BaseListGetParam getRequestParam();

    BaseListGetParam getRequestParam(boolean isRefresh);

    /**
     * @return 下一页
     */
    void setRequestPage();

    /**
     * @return 获取相关状态
     */
    State getState();

    /**
     * @param msgs 获取列表失败信息
     */
    void getDataErr(VolleyError msgs);

    void notifyDataChange();

    public String getEmptyTip();

    public void setEmptyTip(String emptyTip);

    public String getErrTip();

    public void setErrTip(String errTip);

    String getRequestTag();
}
