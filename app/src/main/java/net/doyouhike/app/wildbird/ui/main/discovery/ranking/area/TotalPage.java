package net.doyouhike.app.wildbird.ui.main.discovery.ranking.area;

import android.content.Context;

import net.doyouhike.app.wildbird.biz.model.bean.RankAreaItem;
import net.doyouhike.app.wildbird.biz.model.request.get.AreaListRequestParam;
import net.doyouhike.app.wildbird.ui.main.discovery.ranking.page.PageBase;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-7.
 */
public class TotalPage extends PageBase<RankAreaItem> {

    public TotalPage(Context context) {
        super();
        mRequest = new AreaListRequestParam();
        ((AreaListRequestParam)mRequest).setType(1);
        adapter=new RankAreaAdapter(context,items);
    }

    @Override
    protected void setEmptyMsg() {

    }

    @Override
    public AreaListRequestParam getRequestParam() {
        return (AreaListRequestParam)mRequest;
    }
}
