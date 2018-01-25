package net.doyouhike.app.wildbird.ui.main.user.other;

import android.content.Context;

import net.doyouhike.app.wildbird.biz.model.base.BaseListGetParam;
import net.doyouhike.app.wildbird.biz.model.bean.BirdRecordTotalItem;
import net.doyouhike.app.wildbird.biz.model.request.get.GetUserRecordReq;
import net.doyouhike.app.wildbird.ui.main.discovery.ranking.page.PageBase;
import net.doyouhike.app.wildbird.ui.main.user.UserRecordAdapter;
import net.doyouhike.app.wildbird.util.TimeUtil;

import java.util.List;

/**
 * 功能：我的记录列表
 *
 * @author：曾江 日期：16-4-12.
 */
public class OtherPage extends PageBase<BirdRecordTotalItem> {

    public OtherPage(Context context,String userId) {
        super();
        mRequest = new GetUserRecordReq();
        adapter = new UserRecordAdapter(context, items);

        String tenDayAgo = TimeUtil.getStringFromTime(TimeUtil.getDateBefore(null, 0), "yyyy-MM-dd");
        getRequestParam().setDate(tenDayAgo);
        getRequestParam().setUser_id(userId);
        mRequest.setCount(0);
    }

    @Override
    protected void setEmptyMsg() {

    }

    @Override
    public void updateItem(List item, boolean isRefresh) {
        super.updateItem(item, isRefresh);
        sethasMore(!item.isEmpty());
    }

    @Override
    public BaseListGetParam getRequestParam(boolean isRefresh) {

        if (isRefresh) {
//            String today = TimeUtil.getStringFromTime(TimeUtil.getDateBefore(null, 0), "yyyy-MM-dd");
//            ((GetBirdRecordRequestParam) mRequest).setDate(today);
            getRequestParam().setDate(null);


        } else if (!getItems().isEmpty()) {

            String strDate = getItems().get(getItems().size() - 1).getTime();
            String date = strDate.replace(".", "-");

            getRequestParam().setDate(date);
        }


        return getRequestParam();
    }

    @Override
    public GetUserRecordReq getRequestParam() {
        return (GetUserRecordReq) mRequest;
    }
}
