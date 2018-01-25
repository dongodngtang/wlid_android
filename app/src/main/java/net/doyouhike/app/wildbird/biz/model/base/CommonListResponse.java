package net.doyouhike.app.wildbird.biz.model.base;

import java.util.List;

/**
 * Created by zaitu on 15-11-18.
 */
public class CommonListResponse<T> extends BaseResponse {
    private boolean HasMore;
    private List<T> items;
    private int recCount;

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public boolean isHasMore() {
        return HasMore;
    }

    public void setHasMore(boolean hasMore) {
        HasMore = hasMore;
    }

    public int getRecCount() {
        return recCount;
    }

    public void setRecCount(int recCount) {
        this.recCount = recCount;
    }
}
