package net.doyouhike.app.wildbird.ui.main.user.mine;

import android.content.Context;

import net.doyouhike.app.wildbird.biz.model.base.BaseListGetParam;
import net.doyouhike.app.wildbird.biz.model.bean.BirdRecordTotalItem;
import net.doyouhike.app.wildbird.biz.model.request.get.GetMyRecordReq;
import net.doyouhike.app.wildbird.ui.main.discovery.ranking.page.PageBase;
import net.doyouhike.app.wildbird.ui.main.user.UserRecordAdapter;
import net.doyouhike.app.wildbird.util.TimeUtil;

import java.util.List;

/**
 * 功能：我的记录列表
 *
 * @author：曾江 日期：16-4-12.
 */
public class MePage extends PageBase<BirdRecordTotalItem> {

    public MePage(Context context) {
        super();
        mRequest = new GetMyRecordReq();
        adapter = new UserRecordAdapter(context, items);

        String tenDayAgo = TimeUtil.getStringFromTime(TimeUtil.getDateBefore(null, 0), "yyyy-MM-dd");
        getRequestParam().setDate(tenDayAgo);
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
    public GetMyRecordReq getRequestParam() {
        return (GetMyRecordReq) mRequest;
    }
}
